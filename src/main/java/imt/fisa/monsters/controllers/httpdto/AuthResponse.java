package imt.fisa.monsters.controllers.httpdto;

public class AuthResponse {
    private String identifiant;

    public AuthResponse(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }
}
