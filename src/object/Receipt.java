package object;

public class Receipt {

    private String nameService;
    private float valueReading;
    private float rateValue;
    private float amountServices;

    public Receipt(String nameService, float valueReading, float rateValue, float amountServices) {
        this.nameService = nameService;
        this.valueReading = valueReading;
        this.rateValue = rateValue;
        this.amountServices = amountServices;
    }

    public String getNameService() {
        return nameService;
    }

    public void setNameService(String nameService) {
        this.nameService = nameService;
    }

    public float getValueReading() {
        return valueReading;
    }

    public void setValueReading(float valueReading) {
        this.valueReading = valueReading;
    }

    public float getRateValue() {
        return rateValue;
    }

    public void setRateValue(float rateValue) {
        this.rateValue = rateValue;
    }

    public float getAmountServices() {
        return amountServices;
    }

    public void setAmountServices(float amountServices) {
        this.amountServices = amountServices;
    }
}
