package rs.ac.uns.ftn.informatika.jpa.authentification;

public class Authentification {

    private String accessToken;

    public Authentification(String accessToken){
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
