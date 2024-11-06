package rs.ac.uns.ftn.informatika.jpa.aut;

public class Aut {

    private String accessToken;

    public Aut(String accessToken){
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
