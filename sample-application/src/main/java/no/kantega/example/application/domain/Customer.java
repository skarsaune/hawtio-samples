package no.kantega.example.application.domain;

public class Customer {
    public Customer(String identifier, String givenName, String familyName) {
        super();
        this.identifier = identifier;
        this.givenName = givenName;
        this.familyName = familyName;
    }
    
    public Customer() {
        super();
    }
    public String getIdentifier() {
        return identifier;
    }
    public String getGivenName() {
        return givenName;
    }
    public String getFamilyName() {
        return familyName;
    }
    private String identifier;
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
    private String givenName;
    private String familyName;

}
