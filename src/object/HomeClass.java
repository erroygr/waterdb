package object;

public class HomeClass {
    private int idHome;
    private String town;
    private String street;
    private int numberHouse;
    private String nameType;

    public HomeClass(){

    }
    public int getIdHome() {
        return idHome;
    }

    public void setIdHome(int idHome) {
        this.idHome = idHome;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumberHouse() {
        return numberHouse;
    }

    public void setNumberHouse(int numberHouse) {
        this.numberHouse = numberHouse;
    }

    public String toString(){
        return getIdHome()+","+getTown()+","+getStreet()+","+getNumberHouse()+","+getNameType();
    }
}
