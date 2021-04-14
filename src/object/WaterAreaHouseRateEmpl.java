package object;

import java.util.Date;

public class WaterAreaHouseRateEmpl {
    private Date dataWaterApartament;
    private float GeneralHouseIndicationsColdWater;
    private float GeneralHouseIndicationsHotWater;

    private  WaterAreaHouseRateEmpl waterAreaHouseRateEmpl;

    public WaterAreaHouseRateEmpl(Date dataWaterApartament, float generalHouseIndicationsColdWater, float generalHouseIndicationsHotWater) {
        this.dataWaterApartament = dataWaterApartament;
        GeneralHouseIndicationsColdWater = generalHouseIndicationsColdWater;
        GeneralHouseIndicationsHotWater = generalHouseIndicationsHotWater;
    }

    public WaterAreaHouseRateEmpl(){

    }

    public WaterAreaHouseRateEmpl getWaterAreaHouseRateEmpl() {
        return waterAreaHouseRateEmpl;
    }

    public void setWaterAreaHouseRateEmpl(WaterAreaHouseRateEmpl waterAreaHouseRateEmpl) {
        this.waterAreaHouseRateEmpl = waterAreaHouseRateEmpl;
    }

    public Date getDataWaterApartament() {
        return dataWaterApartament;
    }

    public void setDataWaterApartament(Date dataWaterApartament) {
        this.dataWaterApartament = dataWaterApartament;
    }

    public float getGeneralHouseIndicationsColdWater() {
        return GeneralHouseIndicationsColdWater;
    }

    public void setGeneralHouseIndicationsColdWater(float generalHouseIndicationsColdWater) {
        GeneralHouseIndicationsColdWater = generalHouseIndicationsColdWater;
    }

    public float getGeneralHouseIndicationsHotWater() {
        return GeneralHouseIndicationsHotWater;
    }

    public void setGeneralHouseIndicationsHotWater(float generalHouseIndicationsHotWater) {
        GeneralHouseIndicationsHotWater = generalHouseIndicationsHotWater;
    }
}
