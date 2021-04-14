package object;

public class RateAndCost {
    private float rateConsumptionCold;
    private float rateConsumptionHot;
    private float PriceColdWater;
    private float PriceHotWaterEnergy;
    private float PriceWaterDisposal;
    private float PowerConsumption;
    private float rateConsumptionColdMSC;
    private float rateConsumptionHotMSC;
    private float PriceColdWaterMSC;
    private float PriceHotWaterEnergyMSC;
    private float PriceWaterDisposalMSC;


    public RateAndCost(float rateConsumptionCold, float rateConsumptionHot, float priceColdWater,
                       float priceHotWaterEnergy, float priceWaterDisposal, float powerConsumption,
                       float rateConsumptionColdMSC, float rateConsumptionHotMSC, float priceColdWaterMSC,
                       float priceHotWaterEnergyMSC, float priceWaterDisposalMSC) {
        this.rateConsumptionCold = rateConsumptionCold;
        this.rateConsumptionHot = rateConsumptionHot;
        this.PriceColdWater = priceColdWater;
        this.PriceHotWaterEnergy = priceHotWaterEnergy;
        this.PriceWaterDisposal = priceWaterDisposal;
        this.PowerConsumption = powerConsumption;
        this.rateConsumptionColdMSC = rateConsumptionColdMSC;
        this.rateConsumptionHotMSC = rateConsumptionHotMSC;
        this.PriceColdWaterMSC = priceColdWaterMSC;
        this.PriceHotWaterEnergyMSC = priceHotWaterEnergyMSC;
        this.PriceWaterDisposalMSC = priceWaterDisposalMSC;
    }

    public float getRateConsumptionColdMSC() {
        return rateConsumptionColdMSC;
    }

    public void setRateConsumptionColdMSC(float rateConsumptionColdMSC) {
        this.rateConsumptionColdMSC = rateConsumptionColdMSC;
    }

    public float getRateConsumptionHotMSC() {
        return rateConsumptionHotMSC;
    }

    public void setRateConsumptionHotMSC(float rateConsumptionHotMSC) {
        this.rateConsumptionHotMSC = rateConsumptionHotMSC;
    }

    public float getPriceColdWaterMSC() {
        return PriceColdWaterMSC;
    }

    public void setPriceColdWaterMSC(float priceColdWaterMSC) {
        PriceColdWaterMSC = priceColdWaterMSC;
    }

    public float getPriceHotWaterEnergyMSC() {
        return PriceHotWaterEnergyMSC;
    }

    public void setPriceHotWaterEnergyMSC(float priceHotWaterEnergyMSC) {
        PriceHotWaterEnergyMSC = priceHotWaterEnergyMSC;
    }

    public float getPriceWaterDisposalMSC() {
        return PriceWaterDisposalMSC;
    }

    public void setPriceWaterDisposalMSC(float priceWaterDisposalMSC) {
        PriceWaterDisposalMSC = priceWaterDisposalMSC;
    }

    public float getRateConsumptionCold() {
        return rateConsumptionCold;
    }

    public float getRateConsumptionHot() {
        return rateConsumptionHot;
    }

    public float getPriceColdWater() {
        return PriceColdWater;
    }

    public float getPriceHotWaterEnergy() {
        return PriceHotWaterEnergy;
    }

    public float getPriceWaterDisposal() {
        return PriceWaterDisposal;
    }

    public void setRateConsumptionCold(float rateConsumptionCold) {
        this.rateConsumptionCold = rateConsumptionCold;
    }

    public void setRateConsumptionHot(float rateConsumptionHot) {
        this.rateConsumptionHot = rateConsumptionHot;
    }

    public void setPriceColdWater(float priceColdWater) {
        PriceColdWater = priceColdWater;
    }

    public void setPriceHotWaterEnergy(float priceHotWaterEnergy) {
        PriceHotWaterEnergy = priceHotWaterEnergy;
    }

    public void setPriceWaterDisposal(float priceWaterDisposal) {
        PriceWaterDisposal = priceWaterDisposal;
    }

    public float getPowerConsumption() {
        return PowerConsumption;
    }

    public void setPowerConsumption(float powerConsumption) {
        PowerConsumption = powerConsumption;
    }
}
