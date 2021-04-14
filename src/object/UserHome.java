package object;

import javafx.beans.property.SimpleStringProperty;

public class UserHome {
    private SimpleStringProperty id = new SimpleStringProperty("");
    private SimpleStringProperty id_homes = new SimpleStringProperty("");
    private SimpleStringProperty AreaApartments = new SimpleStringProperty("");
    private SimpleStringProperty FIOresident = new SimpleStringProperty("");
    private SimpleStringProperty kolRegisterd = new SimpleStringProperty("");
    private SimpleStringProperty town = new SimpleStringProperty("");
    private SimpleStringProperty street = new SimpleStringProperty("");
    private SimpleStringProperty numberHouse = new SimpleStringProperty("");

    public String getTown() {
        return town.get();
    }

    public SimpleStringProperty townProperty() {
        return town;
    }

    public void setTown(String town) {
        this.town.set(town);
    }

    public String getStreet() {
        return street.get();
    }

    public SimpleStringProperty streetProperty() {
        return street;
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public String getNumberHouse() {
        return numberHouse.get();
    }

    public SimpleStringProperty numberHouseProperty() {
        return numberHouse;
    }

    public void setNumberHouse(String numberHouse) {
        this.numberHouse.set(numberHouse);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getId_homes() {
        return id_homes.get();
    }

    public SimpleStringProperty id_homesProperty() {
        return id_homes;
    }

    public void setId_homes(String id_homes) {
        this.id_homes.set(id_homes);
    }

    public String getAreaApartments() {
        return AreaApartments.get();
    }

    public SimpleStringProperty areaApartmentsProperty() {
        return AreaApartments;
    }

    public void setAreaApartments(String areaApartments) {
        this.AreaApartments.set(areaApartments);
    }

    public String getFIOresident() {
        return FIOresident.get();
    }

    public SimpleStringProperty FIOresidentProperty() {
        return FIOresident;
    }

    public void setFIOresident(String FIOresident) {
        this.FIOresident.set(FIOresident);
    }

    public String getKolRegisterd() {
        return kolRegisterd.get();
    }

    public SimpleStringProperty kolRegisterdProperty() {
        return kolRegisterd;
    }

    public void setKolRegisterd(String kolRegisterd) {
        this.kolRegisterd.set(kolRegisterd);
    }

    public UserHome(String id, String id_homes, String AreaApartments, String FIOresident, String kolRegisterd, String town,
                    String street, String numberHouse) {
        this.id = new SimpleStringProperty(id);
        this.id_homes =new SimpleStringProperty(id_homes);
        this.AreaApartments=new SimpleStringProperty(AreaApartments);
        this.FIOresident=new SimpleStringProperty(FIOresident);
        this.kolRegisterd=new SimpleStringProperty(kolRegisterd);
        this.town = new SimpleStringProperty(town);
        this.street = new SimpleStringProperty(street);
        this.numberHouse = new SimpleStringProperty(numberHouse);
    }




}
