package object;

import java.util.Date;

public class WaterStructureEmpl {

    private  String zhilec;
    private int kolRegisterd;
    private String town;
    private String streets;
    private String numberHouse;
    private  float valCold;
    private  float valHot;
    private  float sumOpl;
    private Date dataOpl;
    private  String stageOpl;

    public WaterStructureEmpl(String zhilec, int kolRegisterd, String town,
                              String streets, String numberHouse, float valCold,
                              float valHot, float sumOpl,
                              Date dataOpl, String stageOpl) {
        this.zhilec = zhilec;
        this.kolRegisterd = kolRegisterd;
        this.town = town;
        this.streets = streets;
        this.numberHouse = numberHouse;
        this.valCold = valCold;
        this.valHot = valHot;
        this.sumOpl = sumOpl;
        this.dataOpl = dataOpl;
        this.stageOpl = stageOpl;
    }
   public WaterStructureEmpl(){

    }

    public String getZhilec() {
        return zhilec;
    }

    public void setZhilec(String zhilec) {
        this.zhilec = zhilec;
    }

    public int getKolRegisterd() {
        return kolRegisterd;
    }

    public void setKolRegisterd(int kolRegisterd) {
        this.kolRegisterd = kolRegisterd;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreets() {
        return streets;
    }

    public void setStreets(String streets) {
        this.streets = streets;
    }

    public String getNumberHouse() {
        return numberHouse;
    }

    public void setNumberHouse(String numberHouse) {
        this.numberHouse = numberHouse;
    }

    public float getValCold() {
        return valCold;
    }

    public void setValCold(float valCold) {
        this.valCold = valCold;
    }

    public float getValHot() {
        return valHot;
    }

    public void setValHot(float valHot) {
        this.valHot = valHot;
    }

    public float getSumOpl() {
        return sumOpl;
    }

    public void setSumOpl(float sumOpl) {
        this.sumOpl = sumOpl;
    }

    public Date getDataOpl() {
        return dataOpl;
    }

    public void setDataOpl(Date dataOpl) {
        this.dataOpl = dataOpl;
    }

    public String getStageOpl() {
        return stageOpl;
    }

    public void setStageOpl(String stageOpl) {
        this.stageOpl = stageOpl;
    }
}
