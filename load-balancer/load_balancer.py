from flask import Flask, request, jsonify,Response
import requests

app = Flask(__name__)

# Liste backend instanci
#Provjera rada instanci: curl -X GET "http://localhost:8081/api/posts/allPaged/false?page=0&size=3&sort=createdAt,DESC"
instances = ["http://localhost:8081","http://localhost:8082"]
current_index = 0

def get_next_instance():
  """Round Robin Algoritam"""
  global current_index
  instance = instances[current_index]
  current_index = (current_index + 1) % len(instances)
  return instance

@app.route('/<path:path>', methods=['GET', 'POST', 'PUT', 'PATCH', 'DELETE', 'OPTIONS'])
def load_balancer(path):
  global current_index
  retries = len(instances)
  attempted_instances = set()
  try_count = 0

  for _ in range(retries):
    target_instance = get_next_instance()
    if target_instance in attempted_instances:
      continue
    attempted_instances.add(target_instance)

    url = f"{target_instance}/{path}"
    try_count += 1
    print(f"Routing request to {url} [Method: {request.method}] [TRY NUMBER {try_count}]\n")

    try:
      if request.method == 'GET':
        response = requests.get(url, params=request.args, headers=request.headers, stream=True)
      elif request.method == 'POST':
        response = requests.post(url, json=request.get_json(), headers=request.headers, stream=True)
      elif request.method == 'PUT':
        response = requests.put(url, json=request.get_json(), headers=request.headers, stream=True)
      elif request.method == 'PATCH':
        response = requests.patch(url, json=request.get_json(), headers=request.headers, stream=True)
      elif request.method == 'DELETE':
        response = requests.delete(url, headers=request.headers, stream=True)
      elif request.method == 'OPTIONS':
        response = requests.options(url, headers=request.headers, stream=True)

      # Kreiranje Flask Response sa svim originalnim zaglavljima
      flask_response = Response(
        response=response.raw,  # Prosleđivanje RAW tela
        status=response.status_code,
        headers=dict(response.headers)  # Kopiranje svih zaglavlja
      )
      print(f"Request to {url} succeeded with status {response.status_code}\n")
      return flask_response

    except requests.exceptions.RequestException as e:
      print(f"Instance {target_instance} failed: {e}\n")

  print("\nAll instances are unavailable.\n")
  return jsonify({"error": "All instances are unavailable.\n"}), 503

if __name__ == '__main__':
  print("Starting Load Balancer on port 8080...")
  app.run(port=8080)
