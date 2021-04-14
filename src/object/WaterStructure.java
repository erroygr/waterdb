package object;

import java.util.Date;

public class WaterStructure {

    protected  String zhilec;
    protected  float valCold;
    protected  float valHot;
    protected  float sumOpl;
    protected  Date dataOpl;
    protected  String stageOpl;

    public WaterStructure(String zhilec, float valCold, float valHot,float sumOpl, Date dataOpl, String stageOpl) {
        this.zhilec = zhilec;
        this.valCold = valCold;
        this.valHot = valHot;
        this.sumOpl = sumOpl;
        this.dataOpl = dataOpl;
        this.stageOpl = stageOpl;
    }
    public WaterStructure(){

    }

    public String getZhilec() {
        return zhilec;
    }

    public float getValCold() {
        return valCold;
    }

    public float getValHot() {
        return valHot;
    }

    public Date getDataOpl() {
        return dataOpl;
    }

    public String getStageOpl() {
        return stageOpl;
    }

    public void setZhilec(String zhilec) {
        this.zhilec = zhilec;
    }

    public void setValCold(int valCold) {
        this.valCold = valCold;
    }

    public void setValHot(int valHot) {
        this.valHot = valHot;
    }

    public void setDataOpl(Date dataOpl) {
        this.dataOpl = dataOpl;
    }

    public void setStageOpl(String stageOpl) {
        this.stageOpl = stageOpl;
    }

    public float getSumOpl() {
        return sumOpl;
    }

    public void setSumOpl(float sumOpl) {
        this.sumOpl = sumOpl;
    }
}
