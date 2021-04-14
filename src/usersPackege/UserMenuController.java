package usersPackege;

import MSSQL.MSSQLConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import object.RateAndCost;
import object.Receipt;
import object.UserHome;
import object.WaterStructure;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import sample.AuthenticationController;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static sample.AuthenticationController.stageAuthen;

public class UserMenuController {
    public Label labelNameUser;

    public TableColumn zhilec;
    public TableColumn coldWater;
    public TableColumn hotWater;
    public TableColumn dataOpl;
    public TableColumn statOpl;
    public TableColumn sumOpl;
    public TableView tableHistoryWater;
    public AreaChart<String, Float> AreaChartXY;

    public CategoryAxis xAxisX;
    public NumberAxis yAxisY;
    public Label labelRecomendation;


    public TextField insertCold;
    public TextField insertHot;
    public TextField insertDate;
    public TableView tableReceipt;

    public TableColumn nameService;
    public TableColumn valueReading;
    public TableColumn rateValue;
    public TableColumn amountServices;

    public Tab tabEx;
    public Tab WaterConsumption;
    public Tab paymentHistory;
    public Tab payment;
    public ComboBox comboBoxMonth;

    private Connection connection;
    private ResultSet resultSet;
    private UserHome userHome;
    private ArrayList<WaterStructure> waterStructureArrayList = new ArrayList<>();
    private ObservableList<WaterStructure> waterStructureObservableList;
    private RateAndCost rateAndCost;
    private ArrayList<Receipt> receipts = new ArrayList<>();

    public void setRateAndCost(RateAndCost rateAndCost) {
        this.rateAndCost = rateAndCost;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    private ImageView createTabImageView(String iconName) {
        return new ImageView(new Image(getClass().getResource(iconName).toExternalForm(),
                16, 16, false, true));
    }

    public void initialize() {
        zhilec.setCellValueFactory(new PropertyValueFactory<String, String>("zhilec"));
        coldWater.setCellValueFactory(new PropertyValueFactory<String, String>("valCold"));
        hotWater.setCellValueFactory(new PropertyValueFactory<String, String>("valHot"));
        sumOpl.setCellValueFactory(new PropertyValueFactory<String, String>("sumOpl"));
        dataOpl.setCellValueFactory(new PropertyValueFactory<Date, Date>("dataOpl"));
        statOpl.setCellValueFactory(new PropertyValueFactory<String, String>("stageOpl"));

        nameService.setCellValueFactory(new PropertyValueFactory<String, String>("nameService"));
        valueReading.setCellValueFactory(new PropertyValueFactory<String, String>("valueReading"));
        rateValue.setCellValueFactory(new PropertyValueFactory<String, String>("rateValue"));
        amountServices.setCellValueFactory(new PropertyValueFactory<String, String>("amountServices"));

        tableReceipt.setVisible(false);

        paymentHistory.setGraphic(createTabImageView("home.png"));
        WaterConsumption.setGraphic(createTabImageView("analys2.png"));
        payment.setGraphic(createTabImageView("Windows_Table.png"));
        tabEx.setGraphic(createTabImageView("exit1.png"));

    }

    public void init() throws SQLException {
        System.out.println("В ЖИЛЬЦЕ: " + rateAndCost.getRateConsumptionCold() + " " + rateAndCost.getRateConsumptionHot() + " " +
                rateAndCost.getPriceColdWater() + " " + rateAndCost.getPriceHotWaterEnergy() + " " +
                rateAndCost.getPriceWaterDisposal());

        userHome = new UserHome(resultSet.getString(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4),
                resultSet.getString(5),
                resultSet.getString(6),
                resultSet.getString(7),
                resultSet.getString(8)
        );

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ВНИМАНИЕ");
        alert.setHeaderText(null);
        alert.setContentText("Здравствуйте, " + userHome.getFIOresident());
        alert.showAndWait();
        labelNameUser.setText("Вход выполнен: " + userHome.getFIOresident() +
                ".\nКоличество прописанных людей в квартире: " + userHome.getKolRegisterd() +
                ".\nАдрес: " + userHome.getTown() + ", ул." + userHome.getStreet() + ", д." + userHome.getNumberHouse());

        waterStructureArrayList = sqlQue();
        waterStructureObservableList = FXCollections.observableList(waterStructureArrayList);
        tableHistoryWater.setItems(waterStructureObservableList);
        tableHistoryWater.refresh();

        graphicsWater();

    }

    private void graphicsWater() {
        ArrayList<Date> dateArrayListComboBox = new ArrayList<>();

        for (WaterStructure waterStructure : waterStructureArrayList) {
            dateArrayListComboBox.add(waterStructure.getDataOpl());
        }
        ObservableList dateArrayList = FXCollections.observableArrayList(dateArrayListComboBox);
        comboBoxMonth.setItems(dateArrayList);

        xAxisX.setLabel("Дата платежа");
        yAxisY.setLabel("Количество кубов");

        XYChart.Series<String, Float> seriesColdWater = new XYChart.Series<>();
        XYChart.Series<String, Float> seriesHOTWater = new XYChart.Series<>();
        XYChart.Series<String, Float> seriesRateColdWater = new XYChart.Series<>();
        XYChart.Series<String, Float> seriesRateHOTWater = new XYChart.Series<>();


        seriesColdWater.setName("Расход холодной воды, в кубометрах");
        seriesHOTWater.setName("Расход горячей воды, в кубометрах");
        seriesRateColdWater.setName("Норма потребления холодной воды на " + userHome.getKolRegisterd() + " чел., в кубометрах");
        seriesRateHOTWater.setName("Норма потребления горячей воды на " + userHome.getKolRegisterd() + " чел., в кубометрах");

        if (userHome.getTown().equals("Воронеж")) {

            for (int x = 0; x < waterStructureArrayList.size(); x++) {

                seriesColdWater.getData().add(new XYChart.Data<>(waterStructureArrayList.get(x).getDataOpl().toString(), waterStructureArrayList.get(x).getValCold()));
                seriesHOTWater.getData().add(new XYChart.Data<>(waterStructureArrayList.get(x).getDataOpl().toString(), waterStructureArrayList.get(x).getValHot()));
                seriesRateColdWater.getData().add(new XYChart.Data<>(waterStructureArrayList.get(x).getDataOpl().toString(),
                        Float.parseFloat(userHome.getKolRegisterd()) * rateAndCost.getRateConsumptionCold()));
                seriesRateHOTWater.getData().add(new XYChart.Data<>(waterStructureArrayList.get(x).getDataOpl().toString(),
                        Float.parseFloat(userHome.getKolRegisterd()) * rateAndCost.getRateConsumptionHot()));
            }
        } else {
            for (int x = 0; x < waterStructureArrayList.size(); x++) {

                seriesColdWater.getData().add(new XYChart.Data<>(waterStructureArrayList.get(x).getDataOpl().toString(), waterStructureArrayList.get(x).getValCold()));
                seriesHOTWater.getData().add(new XYChart.Data<>(waterStructureArrayList.get(x).getDataOpl().toString(), waterStructureArrayList.get(x).getValHot()));
                seriesRateColdWater.getData().add(new XYChart.Data<>(waterStructureArrayList.get(x).getDataOpl().toString(),
                        Float.parseFloat(userHome.getKolRegisterd()) * rateAndCost.getRateConsumptionColdMSC()));
                seriesRateHOTWater.getData().add(new XYChart.Data<>(waterStructureArrayList.get(x).getDataOpl().toString(),
                        Float.parseFloat(userHome.getKolRegisterd()) * rateAndCost.getRateConsumptionHotMSC()));
            }
        }
        AreaChartXY.getData().addAll(seriesColdWater, seriesHOTWater, seriesRateColdWater, seriesRateHOTWater);

        labelRecomendation.setText(recomendationWatersPotreblenie());
    }

    private String recomendationWatersPotreblenie() {
        String result = null;
        WaterStructure waterStructureRecomend = waterStructureArrayList.get(waterStructureArrayList.size() - 1);
        float potrblCold = waterStructureRecomend.getValCold();
        float potrblHot = waterStructureRecomend.getValHot();
        float nornCold = 0;
        float normHot = 0;
        if (userHome.getTown().equals("Воронеж")) {
            nornCold = rateAndCost.getRateConsumptionCold() * Float.parseFloat(userHome.getKolRegisterd());
            normHot = rateAndCost.getRateConsumptionHot() * Float.parseFloat(userHome.getKolRegisterd());
        } else {
            nornCold = rateAndCost.getRateConsumptionColdMSC() * Float.parseFloat(userHome.getKolRegisterd());
            normHot = rateAndCost.getRateConsumptionHotMSC() * Float.parseFloat(userHome.getKolRegisterd());
        }

        System.out.println(nornCold + "Холод ");
        System.out.println(normHot + "Горчая ");

        if ((potrblCold < nornCold || potrblCold == nornCold) && (potrblHot <= normHot || potrblHot == normHot)) {
            result = "По последним данным, потребление холодной и горчей воды в вашей квартире находится в норме!\n" +
                    "Продолжайте в том же духе!\n" +
                    "Советы по водосбережению:\n" +
                    "*При выборе смесителей отдайте предпочтение рычаговым.\n" +
                    "Они быстрее смешивают воду, чём смесители с двумя кранами, а значит,\n" +
                    "меньше уходит воды «впустую», когда Вы подбираете оптимальную температуру воды.\n" +
                    "*На время, когда Вы чистите зубы, выключайте воду.\n" +
                    "Чтобы ополоснуть рот достаточно стакана с водой.";
        }
        if ((potrblCold > nornCold) || (potrblHot > normHot)) {
            result = "По последним данным, потребление одной из видов вод находится в норме, а другой - нет.\n" +
                    "Пожалуйста проверьте краны, не протикают ли трубы.\n" +
                    "Если все находится в норме, то снизьте потребление.\n" +
                    "Возможные причины:\n" +
                    "Из сливного бачка в унитаз может постоянно течь вода.\n" +
                    "Из-за подобных протечек теряются десятки литров воды ежедневно.\n" +
                    "Замените все протекающие краны.\n" +
                    "Неисправный кран за сутки может «накапать» 30–200 литров воды!\n" +
                    "Старайтесь плотно закрывать кран.";
        }
        if ((potrblCold > nornCold) && (potrblHot > normHot)) {
            result = "О ужас, за последний месяц потребление воды в вашей квартире очень большое!\n" +
                    "Проверьте краны/трубы или обратитесь к сотуднику УК\n" +
                    "Возможные причины:\n" +
                    "*Используйте посудомоечную и стиральную машину только при полной загрузке.\n" +
                    "*Не размораживайте продукты под струёй воды из-под крана.\n" +
                    "Лучше всего заранее переложить продукты из морозилки в холодильник.\n" +
                    "*Принимая душ, Вы в 5-7 раз снижаете потребление воды по сравнению с тем, когда Вы принимаете ванну.\n" +
                    "Воды тратится меньше, если использовать в душе экономичный рассеиватель с меньшим диаметром отверстий.";
        }
        return result;
    }

    private ArrayList sqlQue() throws SQLException {
        Statement statement = connection.createStatement();
        String sqlAuthentication = "SELECT Data_Water.id as [№], " +
                "Home_apartment.FIOresident as [Жилец], " +
                "Data_Water.kolCOLD as [Количество холодной воды], " +
                "Data_Water.kolHOT as [Количество горячей воды], " +
                "CAST(Data_Water.Data_vnoski AS DATE) as [Дата оплаты], " +
                "Data_Water.statusPlatezha as [Статус оплаты]" +
                " FROM Data_Water" +
                " INNER JOIN Home_apartment ON(Data_Water.id_apartaments=Home_apartment.id)" +
                " WHERE Data_Water.id_apartaments=" + userHome.getId() + "" +
                " ORDER BY Data_Water.Data_vnoski";

        ResultSet resultSelectWater = statement.executeQuery(sqlAuthentication);

        ArrayList<WaterStructure> waterStructures = new ArrayList<>();
        while (resultSelectWater.next()) {
            WaterStructure waterStructure = new WaterStructure();
            waterStructure.setZhilec(resultSelectWater.getString(2).replaceAll("([^,] .)[^ ]+", "$1.").replaceAll("(\\.) (.)[^, ]+", "$1$2."));
            waterStructure.setValCold(resultSelectWater.getInt(3));
            waterStructure.setValHot(resultSelectWater.getInt(4));
            waterStructure.setDataOpl(resultSelectWater.getDate(5));
            waterStructure.setStageOpl(resultSelectWater.getString(6));
            waterStructure.setSumOpl(sumOpl(waterStructure));

            System.out.println(resultSelectWater.getString(1) +
                    " " + resultSelectWater.getString(2) +
                    " " + resultSelectWater.getString(3) +
                    " " + resultSelectWater.getString(4) +
                    " " + resultSelectWater.getString(5) +
                    " " + resultSelectWater.getString(6));
            waterStructures.add(waterStructure);
            System.out.println("ПО ФУНКЦИИ К ОПЛАТЕ: " + sumOpl(waterStructure));
        }
        return waterStructures;
    }

    private float sumOpl(WaterStructure waterStructure) throws SQLException {
        float SS = 0;
        String typeHomeName = whyTypeHome(userHome.getId());

        switch (typeHomeName) {

            case "Многоквартирный": {
                System.out.println("зашли в кейс многоквартиры");
                if (userHome.getTown().equals("Воронеж")) {
                    float odn = whyDifferenceODN(userHome.getId(), insertDate.getText());
                    float areaApart = Float.parseFloat(userHome.getAreaApartments());
                    float areaHouse = whyAreaHouse(userHome.getId());

                    float SummOplCold = waterStructure.getValCold() * rateAndCost.getPriceColdWater();

                    float SummOplHOT = (float) ((waterStructure.getValHot() * rateAndCost.getPriceColdWater()) +
                            (waterStructure.getValHot() * rateAndCost.getPowerConsumption() * rateAndCost.getPriceHotWaterEnergy()));
                    float SummOplVodootvod = (waterStructure.getValCold() + waterStructure.getValHot()) * rateAndCost.getPriceWaterDisposal();

                    float SumODN = ((odn / areaHouse) * areaApart * rateAndCost.getPriceColdWater()) +
                            ((odn / areaHouse) * areaApart * rateAndCost.getPriceColdWater() +
                                    (waterStructure.getValHot() * rateAndCost.getPowerConsumption() * rateAndCost.getPriceHotWaterEnergy()));
                    SS = SummOplCold + SummOplHOT + SummOplVodootvod + SumODN;
                } else {
                    float odn = whyDifferenceODN(userHome.getId(), insertDate.getText());
                    float areaApart = Float.parseFloat(userHome.getAreaApartments());
                    float areaHouse = whyAreaHouse(userHome.getId());

                    float SummOplCold = waterStructure.getValCold() * rateAndCost.getPriceColdWaterMSC();

                    float SummOplHOT = (float) ((waterStructure.getValHot() * rateAndCost.getPriceColdWaterMSC()) +
                            (waterStructure.getValHot() * rateAndCost.getPowerConsumption() * rateAndCost.getPriceHotWaterEnergyMSC()));
                    float SummOplVodootvod = (waterStructure.getValCold() + waterStructure.getValHot()) * rateAndCost.getPriceWaterDisposalMSC();

                    float SumODN = ((odn / areaHouse) * areaApart * rateAndCost.getPriceColdWaterMSC()) +
                            ((odn / areaHouse) * areaApart * rateAndCost.getPriceColdWaterMSC() +
                                    (waterStructure.getValHot() * rateAndCost.getPowerConsumption() * rateAndCost.getPriceHotWaterEnergyMSC()));
                    SS = SummOplCold + SummOplHOT + SummOplVodootvod + SumODN;
                }
            }
            break;
            case "Коттедж": {
                System.out.printf("зашли в кейс Коттедж");
                if (userHome.getTown().equals("Воронеж")) {
                    float SummOplCold = waterStructure.getValCold() * rateAndCost.getPriceColdWater();

                    float SummOplHOT = (float) ((waterStructure.getValHot() * rateAndCost.getPriceColdWater()) +
                            (waterStructure.getValHot() * rateAndCost.getPowerConsumption() * rateAndCost.getPriceHotWaterEnergy()));
                    float SummOplVodootvod = (waterStructure.getValCold() + waterStructure.getValHot()) * rateAndCost.getPriceWaterDisposal();

                    SS = SummOplCold + SummOplHOT + SummOplVodootvod;
                } else {

                    float SummOplCold = waterStructure.getValCold() * rateAndCost.getPriceColdWaterMSC();

                    float SummOplHOT = (float) ((waterStructure.getValHot() * rateAndCost.getPriceColdWaterMSC()) +
                            (waterStructure.getValHot() * rateAndCost.getPowerConsumption() * rateAndCost.getPriceHotWaterEnergyMSC()));
                    float SummOplVodootvod = (waterStructure.getValCold() + waterStructure.getValHot()) * rateAndCost.getPriceWaterDisposalMSC();

                    SS = SummOplCold + SummOplHOT + SummOplVodootvod;
                }
            }
            break;
            default:
                System.out.println("неудача(");
                break;
        }

        return SS;

    }

    public String whyTypeHome(String userId) throws SQLException {
        String typeHomeName = "";
        String sqlTypeHomeApart = "SELECT * from Home_apartment " +
                "INNER JOIN Homes ON  (Home_apartment.id_homes=Homes.id) " +
                "INNER JOIN TypeHome ON (Homes.id_typeHomes=TypeHome.id) " +
                "WHERE Home_apartment.id=" + userId + "";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sqlTypeHomeApart);
        while (result.next()) {
            typeHomeName = result.getString(16);
        }
        System.out.println(typeHomeName + " Тип дома из функции");
        return typeHomeName;

    }

    public float whyDifferenceODN(String userId, String dateODN) throws SQLException {
        System.out.println("в функции одн");
        float odnHouse = 0;
        if (dateODN.equals("")) {
            odnHouse = 0;
        } else {
            String[] seODN = dateODN.split("-");
            int val = Integer.parseInt(seODN[1]);
            System.out.println("val= " + val);
            val--;
            System.out.println("val--= " + val);
            seODN[1] = String.valueOf(val);
            System.out.println("seODN[1]= " + seODN[1]);

            String sqlDifferenceODN = "SELECT GeneralHouseIndications.ODN FROM GeneralHouseIndications " +
                    "INNER JOIN Homes ON (GeneralHouseIndications.id_house=Homes.id) " +
                    "INNER JOIN Home_apartment ON (Homes.id=Home_apartment.id_homes) " +
                    "WHERE Home_apartment.id=" + userId + " AND GeneralHouseIndications.Data_indications='" + seODN[0] + "-" + seODN[1] + "-" + seODN[2] + "'";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sqlDifferenceODN);
            while (result.next()) {
                odnHouse = result.getFloat(1);
            }
            System.out.println(odnHouse + " одн");
        }
        return odnHouse;
    }

    public float whyAreaHouse(String userId) throws SQLException {
        System.out.println("в функции whyAreaHouse");
        float areaHouse = 0;
        String sqlDifferenceODN = "SELECT Homes.totalAreaApartments FROM Homes " +
                "INNER JOIN Home_apartment ON (Home_apartment.id_homes=Homes.id) " +
                "WHERE Home_apartment.id=" + userId + "";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sqlDifferenceODN);
        while (result.next()) {
            areaHouse = result.getFloat(1);
        }
        System.out.println(areaHouse + " areaHouse");
        return areaHouse;
    }


    public void goPayPau(ActionEvent event) throws SQLException {


        String typeHomeName = whyTypeHome(userHome.getId());
        Statement statementINSERT = connection.createStatement();
        String sqlInsertDataWater = "INSERT INTO Data_Water ([id_apartaments], [kolCOLD],[kolHOT],[Data_vnoski],[statusPlatezha]) " +
                "VALUES (" + userHome.getId() + "," + Float.parseFloat(insertCold.getText()) + "," +
                "" + Float.parseFloat(insertHot.getText()) + ",'" + insertDate.getText() + "','Оплачено');";

        int rows = statementINSERT.executeUpdate(sqlInsertDataWater);
        System.out.printf("Добавлена %d строка в таблицу Data_Water", rows);
        // float odn = 0;
        // System.out.printf("одн за предыдущую дату ", +odn);
        switch (typeHomeName) {
            case "Многоквартирный": {
                if (userHome.getTown().equals("Воронеж")) {
                    System.out.printf("зашли в кейс многоквартиры");
                    float odn = whyDifferenceODN(userHome.getId(), insertDate.getText());
                    float areaApart = Float.parseFloat(userHome.getAreaApartments());
                    float areaHouse = whyAreaHouse(userHome.getId());
                    float SummOplCold = Float.parseFloat(insertCold.getText()) * rateAndCost.getPriceColdWater();

                    float SummOplHOT = (float) ((Float.parseFloat(insertHot.getText()) * rateAndCost.getPriceColdWater()) +
                            (Float.parseFloat(insertHot.getText()) * rateAndCost.getPowerConsumption() * rateAndCost.getPriceHotWaterEnergy()));

                    float SummOplVodootvod = (Float.parseFloat(insertCold.getText()) + Float.parseFloat(insertHot.getText())) * rateAndCost.getPriceWaterDisposal();

                    float SumODN = ((odn / areaHouse) * areaApart * rateAndCost.getPriceColdWater()) +
                            ((odn / areaHouse) * areaApart * rateAndCost.getPriceColdWater());
                    float SS = SummOplCold + SummOplHOT + SummOplVodootvod + SumODN;

                    Receipt receiptColdWater = new Receipt("Вода холодная",
                            Float.parseFloat(insertCold.getText()),
                            rateAndCost.getPriceColdWater(),
                            SummOplCold);
                    Receipt receiptHotWater = new Receipt("Вода горячая",
                            Float.parseFloat(insertHot.getText()),
                            rateAndCost.getPowerConsumption() * rateAndCost.getPriceHotWaterEnergy(),
                            SummOplHOT);
                    Receipt receiptDrain = new Receipt("Водоотведение",
                            (Float.parseFloat(insertCold.getText()) + Float.parseFloat(insertHot.getText())),
                            rateAndCost.getPriceWaterDisposal(),
                            SummOplVodootvod);
                    Receipt receiptOdn = new Receipt("ОДН",
                            odn,
                            0,
                            SumODN);

                    Receipt receiptSum = new Receipt("Итог",
                            0,
                            0,
                            SS);

                    //ArrayList<Receipt> receipts = new ArrayList<>();
                    receipts.add(receiptColdWater);
                    receipts.add(receiptHotWater);
                    receipts.add(receiptDrain);
                    receipts.add(receiptOdn);
                    receipts.add(receiptSum);

                    ObservableList<Receipt> receipts1 = FXCollections.observableList(receipts);
                    tableReceipt.setItems(receipts1);
                } else {
                    float odn = whyDifferenceODN(userHome.getId(), insertDate.getText());
                    float areaApart = Float.parseFloat(userHome.getAreaApartments());
                    float areaHouse = whyAreaHouse(userHome.getId());
                    float SummOplCold = Float.parseFloat(insertCold.getText()) * rateAndCost.getPriceColdWaterMSC();

                    float SummOplHOT = (float) ((Float.parseFloat(insertHot.getText()) * rateAndCost.getPriceColdWaterMSC()) +
                            (Float.parseFloat(insertHot.getText()) * rateAndCost.getPowerConsumption() * rateAndCost.getPriceHotWaterEnergyMSC()));

                    float SummOplVodootvod = (Float.parseFloat(insertCold.getText()) + Float.parseFloat(insertHot.getText())) * rateAndCost.getPriceWaterDisposalMSC();

                    float SumODN = ((odn / areaHouse) * areaApart * rateAndCost.getPriceColdWaterMSC()) +
                            ((odn / areaHouse) * areaApart * rateAndCost.getPriceColdWaterMSC());
                    float SS = SummOplCold + SummOplHOT + SummOplVodootvod + SumODN;

                    Receipt receiptColdWater = new Receipt("Вода холодная",
                            Float.parseFloat(insertCold.getText()),
                            rateAndCost.getPriceColdWaterMSC(),
                            SummOplCold);
                    Receipt receiptHotWater = new Receipt("Вода горячая",
                            Float.parseFloat(insertHot.getText()),
                            rateAndCost.getPowerConsumption() * rateAndCost.getPriceHotWaterEnergyMSC(),
                            SummOplHOT);
                    Receipt receiptDrain = new Receipt("Водоотведение",
                            (Float.parseFloat(insertCold.getText()) + Float.parseFloat(insertHot.getText())),
                            rateAndCost.getPriceWaterDisposalMSC(),
                            SummOplVodootvod);
                    Receipt receiptOdn = new Receipt("ОДН",
                            odn,
                            0,
                            SumODN);

                    Receipt receiptSum = new Receipt("Итог",
                            0,
                            0,
                            SS);

                    //ArrayList<Receipt> receipts = new ArrayList<>();
                    receipts.add(receiptColdWater);
                    receipts.add(receiptHotWater);
                    receipts.add(receiptDrain);
                    receipts.add(receiptOdn);
                    receipts.add(receiptSum);

                    ObservableList<Receipt> receipts1 = FXCollections.observableList(receipts);
                    tableReceipt.setItems(receipts1);
                }
            }
            break;
            case "Коттедж": {
                System.out.printf("зашли в кейс Коттедж");
                if (userHome.getTown().equals("Воронеж")) {
                    float SummOplCold = Float.parseFloat(insertCold.getText()) * rateAndCost.getPriceColdWater();

                    float SummOplHOT = (float) ((Float.parseFloat(insertHot.getText()) * rateAndCost.getPriceColdWater()) +
                            (Float.parseFloat(insertHot.getText()) * rateAndCost.getPowerConsumption() * rateAndCost.getPriceHotWaterEnergy()));
                    float SummOplVodootvod = (Float.parseFloat(insertCold.getText()) + Float.parseFloat(insertHot.getText())) * rateAndCost.getPriceWaterDisposal();

                    float SS = SummOplCold + SummOplHOT + SummOplVodootvod;

                    Receipt receiptColdWater = new Receipt("Вода холодная",
                            Float.parseFloat(insertCold.getText()),
                            rateAndCost.getPriceColdWater(),
                            SummOplCold);
                    Receipt receiptHotWater = new Receipt("Вода горячая",
                            Float.parseFloat(insertHot.getText()),
                            rateAndCost.getPowerConsumption() * rateAndCost.getPriceHotWaterEnergy(),
                            SummOplHOT);
                    Receipt receiptDrain = new Receipt("Водоотведение",
                            (Float.parseFloat(insertCold.getText()) + Float.parseFloat(insertHot.getText())),
                            rateAndCost.getPriceWaterDisposal(),
                            SummOplVodootvod);
                    Receipt receiptSum = new Receipt("Итог",
                            0,
                            0,
                            SS);

                    //ArrayList<Receipt> receipts = new ArrayList<>();
                    receipts.add(receiptColdWater);
                    receipts.add(receiptHotWater);
                    receipts.add(receiptDrain);
                    receipts.add(receiptSum);

                    ObservableList<Receipt> receipts1 = FXCollections.observableList(receipts);
                    tableReceipt.setItems(receipts1);
                } else {

                    float SummOplCold = Float.parseFloat(insertCold.getText()) * rateAndCost.getPriceColdWaterMSC();

                    float SummOplHOT = (float) ((Float.parseFloat(insertHot.getText()) * rateAndCost.getPriceColdWaterMSC()) +
                            (Float.parseFloat(insertHot.getText()) * rateAndCost.getPowerConsumption() * rateAndCost.getPriceHotWaterEnergyMSC()));
                    float SummOplVodootvod = (Float.parseFloat(insertCold.getText()) + Float.parseFloat(insertHot.getText())) * rateAndCost.getPriceWaterDisposalMSC();

                    float SS = SummOplCold + SummOplHOT + SummOplVodootvod;

                    Receipt receiptColdWater = new Receipt("Вода холодная",
                            Float.parseFloat(insertCold.getText()),
                            rateAndCost.getPriceColdWaterMSC(),
                            SummOplCold);
                    Receipt receiptHotWater = new Receipt("Вода горячая",
                            Float.parseFloat(insertHot.getText()),
                            rateAndCost.getPowerConsumption() * rateAndCost.getPriceHotWaterEnergyMSC(),
                            SummOplHOT);
                    Receipt receiptDrain = new Receipt("Водоотведение",
                            (Float.parseFloat(insertCold.getText()) + Float.parseFloat(insertHot.getText())),
                            rateAndCost.getPriceWaterDisposalMSC(),
                            SummOplVodootvod);
                    Receipt receiptSum = new Receipt("Итог",
                            0,
                            0,
                            SS);

                    //ArrayList<Receipt> receipts = new ArrayList<>();
                    receipts.add(receiptColdWater);
                    receipts.add(receiptHotWater);
                    receipts.add(receiptDrain);
                    receipts.add(receiptSum);

                    ObservableList<Receipt> receipts1 = FXCollections.observableList(receipts);
                    tableReceipt.setItems(receipts1);
                }
            }
            break;
            default:
                System.out.println("ХУЙНЯ ДАВАЙ ПО НОВОЙ");
                break;
        }
        tableReceipt.setVisible(true);

    }

    public void goRefresh12(ActionEvent event) throws SQLException {

        insertHot.setText("");
        insertCold.setText("");
        insertDate.setText("");
        tableReceipt.refresh();
        tableReceipt.setVisible(false);
        waterStructureArrayList.clear();
        waterStructureObservableList.clear();
        tableHistoryWater.refresh();
        waterStructureArrayList = sqlQue();
        waterStructureObservableList = FXCollections.observableList(waterStructureArrayList);
        tableHistoryWater.setItems(waterStructureObservableList);
        receipts.clear();
        tableHistoryWater.refresh();
        AreaChartXY.getData().clear();
        graphicsWater();
    }

    public void getClose(Event event) {

        try {

            MSSQLConnection.getConnection();
            Parent root = FXMLLoader.load(getClass().getResource("/sample/authentication.fxml"));
            stageAuthen = new Stage();
            stageAuthen.setTitle("Вход в систему");
            stageAuthen.setScene(new Scene(root, 450, 250));
            stageAuthen.show();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }


        AuthenticationController.stageUser.close();
    }

    public void exportVan(ActionEvent event) throws IOException {

        // получаем файл в формате xlsx
        String filePath = "zhelHistoryPlatezh.xls";
        // получаем экземпляр XSSFWorkbook для обработки xlsx файла
        FileInputStream file = new FileInputStream(new File(filePath));

        HSSFWorkbook workbook = new HSSFWorkbook(file);
        // выбираем первый лист для обработки
        // нумерация начинается из 0
        HSSFSheet sheetAt = workbook.getSheetAt(0);

        int rowNum = 3;

        for (WaterStructure waterStructure : waterStructureArrayList) {
            createSheetHeader(sheetAt, ++rowNum, waterStructure);
        }


        HSSFRow rZhilec = sheetAt.getRow(1);
        HSSFRow rAdress = sheetAt.getRow(2);

        HSSFCell cZhilec = rZhilec.getCell(1);
        HSSFCell cAdress = rAdress.getCell(1);


        cZhilec.setCellValue(userHome.getFIOresident());
        cAdress.setCellValue(userHome.getTown() + ", ул." + userHome.getStreet() + ", д." + userHome.getNumberHouse());

        try (FileOutputStream out = new FileOutputStream(new File("resultOtchet\\zhelHistoryPlatezh" + userHome.getFIOresident() + ".xls"))) {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Excel sozdan!");
    }


    private static void createSheetHeader(HSSFSheet sheetAt, int rowNum, WaterStructure waterStructureArrayList) {
        Row row = sheetAt.getRow(rowNum);
        row.getCell(0).setCellValue(waterStructureArrayList.getValCold());
        row.getCell(1).setCellValue(waterStructureArrayList.getValHot());
        row.getCell(2).setCellValue(waterStructureArrayList.getSumOpl());
        row.getCell(3).setCellValue(waterStructureArrayList.getDataOpl());
        row.getCell(4).setCellValue(waterStructureArrayList.getStageOpl());
    }

    public void reportMonths(ActionEvent event) throws IOException, ParseException, SQLException {
        if (comboBoxMonth.getSelectionModel().getSelectedItem() != null) {
            String dateMonths = comboBoxMonth.getSelectionModel().getSelectedItem().toString();
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            Date dateMonth = date.parse(dateMonths);
            // получаем файл в формате xlsx
            String filePath = "zhelRashodMonth.xls";

            System.out.println(dateMonth);

            // получаем экземпляр XSSFWorkbook для обработки xlsx файла
            FileInputStream file = new FileInputStream(new File(filePath));

            HSSFWorkbook workbook = new HSSFWorkbook(file);
            // выбираем первый лист для обработки
            // нумерация начинается из 0
            HSSFSheet sheetAt = workbook.getSheetAt(0);


            HSSFRow rZhilec = sheetAt.getRow(1);
            HSSFRow rDate = sheetAt.getRow(2);

            HSSFCell cZhilec = rZhilec.getCell(1);
            HSSFCell cDate = rDate.getCell(1);


            cZhilec.setCellValue(userHome.getFIOresident());
            cDate.setCellValue(dateMonths);

            HSSFRow rColdWater = sheetAt.getRow(3);
            HSSFRow rHotWater = sheetAt.getRow(4);

            HSSFCell cColdWater = rColdWater.getCell(1);
            HSSFCell cHotWater = rHotWater.getCell(1);

            cColdWater.setCellValue(whyWaterCold(dateMonths));
            cHotWater.setCellValue(whyWaterHot(dateMonths));

            HSSFRow rRateCold = sheetAt.getRow(5);
            HSSFRow rRateHot = sheetAt.getRow(6);

            HSSFCell cRateCold = rRateCold.getCell(1);
            HSSFCell cRateHot = rRateHot.getCell(1);

            cRateCold.setCellValue(Float.parseFloat(userHome.getKolRegisterd()) * rateAndCost.getRateConsumptionCold());
            cRateHot.setCellValue(Float.parseFloat(userHome.getKolRegisterd()) * rateAndCost.getRateConsumptionHot());

            try (FileOutputStream out = new FileOutputStream(new File("resultOtchet\\zhelRashodMonth" + userHome.getFIOresident() + ".xls"))) {
                workbook.write(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Excel sozdan!");
        } else {
            System.out.println("ОШИБКА");
        }
    }

    public float whyWaterCold(String date) throws SQLException {
        float watercold = 0;
        String sqlTypeHomeApart = "SELECT kolCOLD" +
                " FROM Data_Water" +
                " WHERE Data_vnoski='" + date + "' AND id_apartaments=" + userHome.getId() + "";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sqlTypeHomeApart);
        while (result.next()) {
            watercold = result.getFloat(1);
        }
        return watercold;
    }

    public float whyWaterHot(String date) throws SQLException {
        float waterHot = 0;
        String sqlTypeHomeApart = "SELECT kolHOT" +
                " FROM Data_Water" +
                " WHERE Data_vnoski='" + date + "' AND id_apartaments=" + userHome.getId() + "";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sqlTypeHomeApart);
        while (result.next()) {
            waterHot = result.getFloat(1);
        }
        return waterHot;
    }

    public void reportKvitansiya(ActionEvent event) throws IOException {

        // получаем файл в формате xlsx
        String filePath = "zhelKvitanciya.xls";

        // получаем экземпляр XSSFWorkbook для обработки xlsx файла
        FileInputStream file = new FileInputStream(new File(filePath));

        HSSFWorkbook workbook = new HSSFWorkbook(file);
        // выбираем первый лист для обработки
        // нумерация начинается из 0
        HSSFSheet sheetAt = workbook.getSheetAt(0);

        HSSFRow rZhilec = sheetAt.getRow(10);
        HSSFRow rDate = sheetAt.getRow(11);

        HSSFCell cZhilec = rZhilec.getCell(1);
        HSSFCell cDate = rDate.getCell(1);

        cZhilec.setCellValue(userHome.getFIOresident());
        cDate.setCellValue(insertDate.getText());

        int rowNum = 2;

        for (Receipt receipt : receipts) {
            Row row = sheetAt.getRow(rowNum);
            row.getCell(0).setCellValue(receipt.getNameService());
            row.getCell(1).setCellValue(receipt.getValueReading());
            row.getCell(2).setCellValue(receipt.getRateValue());
            row.getCell(3).setCellValue(receipt.getAmountServices());
            rowNum++;
        }

        try (FileOutputStream out = new FileOutputStream(new File("resultOtchet\\zhelKvitanciya" + userHome.getFIOresident() + ".xls"))) {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Excel sozdan!");
    }
}






