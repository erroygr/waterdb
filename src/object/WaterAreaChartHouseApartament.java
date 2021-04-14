package object;

import java.util.Date;

public class WaterAreaChartHouseApartament {
    private float sumColdWaterApartament;
    private float sumHotWaterApartament;
    private Date dataWaterApartament;
    private int countRegrestingHouse;
    private float areaApartamentsSum;
    private String typeHome;
    private WaterAreaChartHouseApartament waterAreaChartHouseApartament;

    public WaterAreaChartHouseApartament(float sumColdWaterApartament, float sumHotWaterApartament, Date dataWaterApartament, int countRegrestingHouse, float areaApartamentsSum, String typeHome) {
        this.sumColdWaterApartament = sumColdWaterApartament;
        this.sumHotWaterApartament = sumHotWaterApartament;
        this.dataWaterApartament = dataWaterApartament;
        this.countRegrestingHouse = countRegrestingHouse;
        this.areaApartamentsSum = areaApartamentsSum;
        this.typeHome = typeHome;
    }

    public WaterAreaChartHouseApartament(){

}

    public WaterAreaChartHouseApartament getWaterAreaChartHouseApartament() {
        return waterAreaChartHouseApartament;
    }

    public void setWaterAreaChartHouseApartament(WaterAreaChartHouseApartament waterAreaChartHouseApartament) {
        this.waterAreaChartHouseApartament = waterAreaChartHouseApartament;
    }

    public float getSumColdWaterApartament() {
        return sumColdWaterApartament;
    }

    public void setSumColdWaterApartament(float sumColdWaterApartament) {
        this.sumColdWaterApartament = sumColdWaterApartament;
    }

    public float getSumHotWaterApartament() {
        return sumHotWaterApartament;
    }

    public void setSumHotWaterApartament(float sumHotWaterApartament) {
        this.sumHotWaterApartament = sumHotWaterApartament;
    }

    public Date getDataWaterApartament() {
        return dataWaterApartament;
    }

    public void setDataWaterApartament(Date dataWaterApartament) {
        this.dataWaterApartament = dataWaterApartament;
    }

    public int getCountRegrestingHouse() {
        return countRegrestingHouse;
    }

    public void setCountRegrestingHouse(int countRegrestingHouse) {
        this.countRegrestingHouse = countRegrestingHouse;
    }

    public float getAreaApartamentsSum() {
        return areaApartamentsSum;
    }

    public void setAreaApartamentsSum(float areaApartamentsSum) {
        this.areaApartamentsSum = areaApartamentsSum;
    }

    public String getTypeHome() {
        return typeHome;
    }

    public void setTypeHome(String typeHome) {
        this.typeHome = typeHome;
    }
}
