package emploerPackege;

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
import object.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import sample.AuthenticationController;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;


import static sample.AuthenticationController.stageAuthen;

public class EmplMenuController {
    public TableColumn zhilec;
    public TableColumn coldWater;
    public TableColumn hotWater;
    public TableColumn sumOpl;
    public TableColumn dataOpl;
    public TableColumn statOpl;
    public TableColumn kolProp;
    public TableColumn town;
    public TableColumn streets;
    public TableColumn numberHouse;

    public TableView tableHistoryWater;
    public ComboBox comboBoxFilters;
    public ComboBox comboBoxValueFilter;
    public TextField dateFirst;
    public TextField dateLast;
    public ComboBox comboBoxArea;
    public AreaChart<String,Float>  areaChartAnalys;
    public CategoryAxis xAxisX;
    public NumberAxis yAxisY;
    public CheckBox onlyHotWaterCheck;
    public CheckBox onlyColdWaterCheck;
    public TextArea textAreaRecomedation;

    public TextField textNormaPotrebleniyaCold;
    public TextField textNormaPotrebleniyaHot;
    public TextField textPriceColdWater;
    public TextField textPriceHotWaterRomponentEnerge;
    public TextField textPriceVodootvod;
    public TextField textPowerConsumption;
    public TextField textNormaPotrebleniyaColdMSC;
    public TextField textNormaPotrebleniyaHotMSC;
    public TextField textPriceColdWaterMSC;
    public TextField textPriceHotWaterRomponentEnergeMSC;
    public TextField textPriceVodootvodMSC;
    public ComboBox comboBoxDate;
    public ComboBox comboBoxHouseOdn;
    public TextField dateOdn;
    public TextField coldOdn;
    public TextField hotOdn;

    public Tab tab1;
    public Tab tab2;
    public Tab tab3;
    public Tab tabExit;


    private  ObservableList<String> nameColumn = FXCollections.observableArrayList("Жилец", "Статус Оплаты", "Город",
                                                                                            "Улица","Количество прописанных");

    private String filterSQL="";
    private String valueFilterSQL="";

    private String typeHome="";
    private int kolRegestr;
    private ArrayList<WaterStructureEmpl> waterStructureArrayList =new ArrayList<>();
    private ObservableList<WaterStructureEmpl> waterStructureObservableList;

    private ObservableList<WaterStructureEmpl> waterStructureObservableListFILTER;

    private ArrayList<WaterAreaChartHouseApartament> waterAreaChartHouseApartaments=new ArrayList<>();
    private ArrayList<WaterAreaHouseRateEmpl> waterAreaHouseRateEmpls=new ArrayList<>();

    private ArrayList<HomeClass> homesSQL = new ArrayList<>();

    private ArrayList<String> arrZhilec =new ArrayList<>();
    private ArrayList<String> arrStatus = new ArrayList<>();
    private ArrayList<String> arrTown =new ArrayList<>();
    private ArrayList<String> arrStreets = new ArrayList<>();
    private ArrayList<String> arrKolRegisterd =new ArrayList<>();

    private ArrayList<String> arrDates =new ArrayList<>();

    private RateAndCost rateAndCost;
    private Connection connection;
    private String townUser="";

    private float normCold=0;
    private float normHot=0;

    private float potrblColdApartaments;
    private float potrblHotApartaments;
    private float potrblColdHouseRateEmpl = 0;
    private float potrblHotHouseRateEmpl = 0;
    private float differenceReadings = 0;


    public void setRateAndCost(RateAndCost rateAndCost) {
        this.rateAndCost = rateAndCost;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    private ImageView createTabImageView(String iconName) {
        return new ImageView(new Image(getClass().getResource(iconName).toExternalForm(),
                16, 16, false, true));
    }

    public void initialize() {
        zhilec.setCellValueFactory(new PropertyValueFactory<String,String>("zhilec"));
        kolProp.setCellValueFactory(new PropertyValueFactory<String,String>("kolRegisterd"));
        town.setCellValueFactory(new PropertyValueFactory<String,String>("town"));
        streets.setCellValueFactory(new PropertyValueFactory<String,String>("streets"));
        numberHouse.setCellValueFactory(new PropertyValueFactory<String,String>("numberHouse"));
        coldWater.setCellValueFactory(new PropertyValueFactory<String,String>("valCold"));
        hotWater.setCellValueFactory(new PropertyValueFactory<String,String>("valHot"));
        sumOpl.setCellValueFactory(new PropertyValueFactory<String,String>("sumOpl"));
        dataOpl.setCellValueFactory(new PropertyValueFactory<String,String>("dataOpl"));
        statOpl.setCellValueFactory(new PropertyValueFactory<String,String>("stageOpl"));

        tab1.setGraphic(createTabImageView("table.png"));
        tab2.setGraphic(createTabImageView("analys.png"));
        tab3.setGraphic(createTabImageView("time.png"));
        tabExit.setGraphic(createTabImageView("exxIT.png"));
    }

    public void init() throws SQLException{
        tableHistoryWater.refresh();
        String sqlAuthentication = "SELECT Home_apartment.FIOresident as [Жилец], " +
                "Home_apartment.kolRegisterd as [Количество прописанных], " +
                "Homes.Gorod as [Город], " +
                "Homes.Streets as [Улица], " +
                "Homes.NumberHome as [Номер дома], " +
                "Data_Water.kolCOLD as [Количество холодной воды], " +
                "Data_Water.kolHOT as [Количество горячей воды], " +
                "CAST(Data_Water.Data_vnoski AS DATE) as [Дата оплаты], " +
                "Data_Water.statusPlatezha as [Статус оплаты], " +
                "TypeHome.nameType " +
                "FROM Data_Water " +
                "INNER JOIN Home_apartment ON(Data_Water.id_apartaments=Home_apartment.id) " +
                "INNER JOIN Homes ON(Home_apartment.id_homes=Homes.id) " +
                "INNER JOIN TypeHome ON (Homes.id_typeHomes=TypeHome.id) " +
                "ORDER BY Data_Water.Data_vnoski";
        waterStructureArrayList = sqlFullData(sqlAuthentication);
        waterStructureObservableList= FXCollections.observableList(waterStructureArrayList);
        tableHistoryWater.setItems(waterStructureObservableList);
        tableHistoryWater.refresh();

        textNormaPotrebleniyaCold.setText(String.valueOf(rateAndCost.getRateConsumptionCold()));
        textNormaPotrebleniyaHot.setText(String.valueOf(rateAndCost.getRateConsumptionHot()));
        textPriceColdWater.setText(String.valueOf(rateAndCost.getPriceColdWater()));
        textPriceHotWaterRomponentEnerge.setText(String.valueOf(rateAndCost.getPriceHotWaterEnergy()));
        textPriceVodootvod.setText(String.valueOf(rateAndCost.getPriceWaterDisposal()));
        textPowerConsumption.setText(String.valueOf(rateAndCost.getPowerConsumption()));
        textNormaPotrebleniyaColdMSC.setText(String.valueOf(rateAndCost.getRateConsumptionColdMSC()));
        textNormaPotrebleniyaHotMSC.setText(String.valueOf(rateAndCost.getRateConsumptionHotMSC()));
        textPriceColdWaterMSC.setText(String.valueOf(rateAndCost.getPriceColdWaterMSC()));
        textPriceHotWaterRomponentEnergeMSC.setText(String.valueOf(rateAndCost.getPriceHotWaterEnergyMSC()));
        textPriceVodootvodMSC.setText(String.valueOf(rateAndCost.getPriceWaterDisposalMSC()));
    }

    public void fillComboBox() throws SQLException {
        String sqlComboBoxHome = "SELECT Homes.id, " +
                "Homes.Gorod, " +
                "Homes.Streets, " +
                "Homes.NumberHome, " +
                "TypeHome.nameType FROM Homes " +
                "INNER JOIN TypeHome ON (TypeHome.id=Homes.id_typeHomes);";


        Statement statementT = connection.createStatement();
        ResultSet resultHomes = statementT.executeQuery(sqlComboBoxHome);

        while (resultHomes.next()) {
            HomeClass homeClass=new HomeClass();
            System.out.println(resultHomes.getString(1) +
                    " " + resultHomes.getString(2) +
                    " " + resultHomes.getString(3)+
                    " " + resultHomes.getString(4)+
                    " " + resultHomes.getString(5));

            homeClass.setIdHome(resultHomes.getInt(1));
            homeClass.setTown(resultHomes.getString(2));
            homeClass.setStreet(resultHomes.getString(3));
            homeClass.setNumberHouse(resultHomes.getInt(4));
            homeClass.setNameType(resultHomes.getString(5));
            homesSQL.add(homeClass);
            comboBoxArea.getItems().add(homeClass.toString());
            comboBoxHouseOdn.getItems().add(homeClass.toString());

        }
    }

    public ArrayList deleteRepeat(ArrayList<String> array){
        return (ArrayList) array.stream().distinct().collect(Collectors.toList());
    }

    private ArrayList sqlFullData(String sqlAuthentication) throws SQLException {

        Statement statement = connection.createStatement();
        ResultSet resultSelectWater = statement.executeQuery(sqlAuthentication);
        ArrayList<WaterStructureEmpl> waterStructures =new ArrayList<>();
        while (resultSelectWater.next()) {
            WaterStructureEmpl waterStructureEmpl=new WaterStructureEmpl();
            String tipeHouse;
            waterStructureEmpl.setZhilec(resultSelectWater.getString(1));
            waterStructureEmpl.setKolRegisterd(resultSelectWater.getInt(2));
            waterStructureEmpl.setTown(resultSelectWater.getString(3));
            waterStructureEmpl.setStreets(resultSelectWater.getString(4));
            waterStructureEmpl.setNumberHouse(resultSelectWater.getString(5));
            waterStructureEmpl.setValCold(resultSelectWater.getInt(6));
            waterStructureEmpl.setValHot(resultSelectWater.getInt(7));
            waterStructureEmpl.setDataOpl(resultSelectWater.getDate(8));
            waterStructureEmpl.setStageOpl(resultSelectWater.getString(9));
            tipeHouse=resultSelectWater.getString(10);
            waterStructureEmpl.setSumOpl(sumOpl(waterStructureEmpl,tipeHouse));


            System.out.println(resultSelectWater.getString(1) +
                    " " + resultSelectWater.getString(2) +
                    " " + resultSelectWater.getString(3) +
                    " " + resultSelectWater.getString(4) +
                    " " + resultSelectWater.getString(5) +
                    " " + resultSelectWater.getString(6) +
                    " " + resultSelectWater.getString(7) +
                    " " + resultSelectWater.getString(8) +
                    " " + resultSelectWater.getString(9));
            waterStructures.add(waterStructureEmpl);
            System.out.println("ПО ФУНКЦИИ К ОПЛАТЕ: "+sumOpl(waterStructureEmpl,tipeHouse));
            System.out.println(waterStructureEmpl.getKolRegisterd()+" КОЛ ПРОПИСАННЫХ");
        }
        for (WaterStructureEmpl waterStructure : waterStructures) {
            arrZhilec.add(String.valueOf(waterStructure.getZhilec()));
            arrStatus.add(String.valueOf(waterStructure.getStageOpl()));
            arrKolRegisterd.add(String.valueOf(waterStructure.getKolRegisterd()));
            arrStreets.add(String.valueOf(waterStructure.getStreets()));
            arrTown.add(String.valueOf(waterStructure.getTown()));
        }

        Map<String, List<String>> valueNameColumn = new HashMap<>();
        valueNameColumn.put(nameColumn.get(0),deleteRepeat(arrZhilec));
        valueNameColumn.put(nameColumn.get(1),deleteRepeat(arrStatus));
        valueNameColumn.put(nameColumn.get(2),deleteRepeat(arrTown));
        valueNameColumn.put(nameColumn.get(3),deleteRepeat(arrStreets));
        valueNameColumn.put(nameColumn.get(4),deleteRepeat(arrKolRegisterd));

        for (Map.Entry<String, List<String>> pair : valueNameColumn.entrySet()) {
            String key = pair.getKey();
            List<String> value = pair.getValue();
            System.out.println(key + ":" + value);
        }

        comboBoxFilters.setItems(nameColumn);

        comboBoxFilters.valueProperty().addListener((obs, oldValue, newValue) ->{
            ObservableList<String> list = FXCollections.observableArrayList(valueNameColumn.get(newValue));
            if(list !=null){
                comboBoxValueFilter.setDisable(false);
                comboBoxValueFilter.setItems(list);
            }else {
                comboBoxValueFilter.getItems().clear();
                comboBoxValueFilter.setDisable(true);
            }
        } );

        return waterStructures;
    }


    private float sumOpl(WaterStructureEmpl waterStructure, String tipeHouse) {
        float SS = 0;

        switch (tipeHouse) {
            case "Многоквартирный": {
                if(waterStructure.getTown().equals("Воронеж")) {
                    float SummOplCold = waterStructure.getValCold() * rateAndCost.getPriceColdWater();
                    float SummOplHOT = (float) ((waterStructure.getValHot() * rateAndCost.getPriceColdWater()) +
                            (waterStructure.getValHot() * rateAndCost.getPowerConsumption() * rateAndCost.getPriceHotWaterEnergy()));
                    float SummOplVodootvod = (waterStructure.getValCold() + waterStructure.getValHot()) * rateAndCost.getPriceWaterDisposal();
                    SS = SummOplCold + SummOplHOT + SummOplVodootvod;
                } else{
                    float SummOplCold = waterStructure.getValCold() * rateAndCost.getPriceColdWaterMSC();
                    float SummOplHOT = (float) ((waterStructure.getValHot() * rateAndCost.getPriceColdWaterMSC()) +
                            (waterStructure.getValHot() * rateAndCost.getPowerConsumption() * rateAndCost.getPriceHotWaterEnergyMSC()));
                    float SummOplVodootvod = (waterStructure.getValCold() + waterStructure.getValHot()) * rateAndCost.getPriceWaterDisposalMSC();
                    SS = SummOplCold + SummOplHOT + SummOplVodootvod+1;
                }
            }
            break;
            case "Коттедж": {
                if(waterStructure.getTown().equals("Воронеж")) {
                    float SummOplCold = waterStructure.getValCold() * rateAndCost.getPriceColdWater();
                    float SummOplHOT = (float) ((waterStructure.getValHot() * rateAndCost.getPriceColdWater()) +
                            (waterStructure.getValHot() * rateAndCost.getPowerConsumption() * rateAndCost.getPriceHotWaterEnergy()));
                    float SummOplVodootvod = (waterStructure.getValCold() + waterStructure.getValHot()) * rateAndCost.getPriceWaterDisposal();
                    SS = SummOplCold + SummOplHOT + SummOplVodootvod;
                } else{
                    float SummOplCold = waterStructure.getValCold() * rateAndCost.getPriceColdWaterMSC();
                    float SummOplHOT = (float) ((waterStructure.getValHot() * rateAndCost.getPriceColdWaterMSC()) +
                            (waterStructure.getValHot() * rateAndCost.getPowerConsumption() * rateAndCost.getPriceHotWaterEnergyMSC()));
                    float SummOplVodootvod = (waterStructure.getValCold() + waterStructure.getValHot()) * rateAndCost.getPriceWaterDisposalMSC();
                    SS = SummOplCold + SummOplHOT + SummOplVodootvod;
                }
            }break;
            default:  System.out.println("ДАВАЙ ПО НОВОЙ");
                break;
        }
        return SS;
    }

    public void sampleFilter(ActionEvent event) throws SQLException {
               filterSQL= (String) comboBoxFilters.getValue();
        valueFilterSQL = (String) comboBoxValueFilter.getValue();
        String sqlFilters="";
        if((dateFirst.getText() == null || Objects.requireNonNull(dateFirst.getText()).trim().isEmpty()) &&
                (dateLast.getText()==null || Objects.requireNonNull(dateLast.getText()).trim().isEmpty())){
            switch (filterSQL) {
                case "Жилец":
                    sqlFilters = "SELECT Home_apartment.FIOresident as [Жилец], " +
                            "Home_apartment.kolRegisterd as [Количество прописанных], " +
                            "Homes.Gorod as [Город], " +
                            "Homes.Streets as [Улица], " +
                            "Homes.NumberHome as [Номер дома], " +
                            "Data_Water.kolCOLD as [Количество холодной воды], " +
                            "Data_Water.kolHOT as [Количество горячей воды], " +
                            "CAST(Data_Water.Data_vnoski AS DATE) as [Дата оплаты], " +
                            "Data_Water.statusPlatezha as [Статус оплаты], " +
                            "TypeHome.nameType " +
                            "FROM Data_Water " +
                            "INNER JOIN Home_apartment ON(Data_Water.id_apartaments=Home_apartment.id) " +
                            "INNER JOIN Homes ON(Home_apartment.id_homes=Homes.id) " +
                            "INNER JOIN TypeHome ON (Homes.id_typeHomes=TypeHome.id) " +
                            "WHERE Home_apartment.FIOresident ='" + valueFilterSQL + "'" +
                            "ORDER BY Data_Water.Data_vnoski";
                    break;
                case "Статус Оплаты":
                    sqlFilters = "SELECT Home_apartment.FIOresident as [Жилец], " +
                            "Home_apartment.kolRegisterd as [Количество прописанных], " +
                            "Homes.Gorod as [Город], " +
                            "Homes.Streets as [Улица], " +
                            "Homes.NumberHome as [Номер дома], " +
                            "Data_Water.kolCOLD as [Количество холодной воды], " +
                            "Data_Water.kolHOT as [Количество горячей воды], " +
                            "CAST(Data_Water.Data_vnoski AS DATE) as [Дата оплаты], " +
                            "Data_Water.statusPlatezha as [Статус оплаты], " +
                            "TypeHome.nameType " +
                            "FROM Data_Water " +
                            "INNER JOIN Home_apartment ON(Data_Water.id_apartaments=Home_apartment.id) " +
                            "INNER JOIN Homes ON(Home_apartment.id_homes=Homes.id) " +
                            "INNER JOIN TypeHome ON (Homes.id_typeHomes=TypeHome.id) " +
                            "WHERE Data_Water.statusPlatezha ='" + valueFilterSQL + "'" +
                            "ORDER BY Data_Water.Data_vnoski";
                    break;
                case "Город":
                    sqlFilters = "SELECT Home_apartment.FIOresident as [Жилец], " +
                            "Home_apartment.kolRegisterd as [Количество прописанных], " +
                            "Homes.Gorod as [Город], " +
                            "Homes.Streets as [Улица], " +
                            "Homes.NumberHome as [Номер дома], " +
                            "Data_Water.kolCOLD as [Количество холодной воды], " +
                            "Data_Water.kolHOT as [Количество горячей воды], " +
                            "CAST(Data_Water.Data_vnoski AS DATE) as [Дата оплаты], " +
                            "Data_Water.statusPlatezha as [Статус оплаты], " +
                            "TypeHome.nameType " +
                            "FROM Data_Water " +
                            "INNER JOIN Home_apartment ON(Data_Water.id_apartaments=Home_apartment.id) " +
                            "INNER JOIN Homes ON(Home_apartment.id_homes=Homes.id) " +
                            "INNER JOIN TypeHome ON (Homes.id_typeHomes=TypeHome.id) " +
                            "WHERE Homes.Gorod ='" + valueFilterSQL + "'" +
                            "ORDER BY Data_Water.Data_vnoski";
                    break;
                case "Улица":
                    sqlFilters = "SELECT Home_apartment.FIOresident as [Жилец], " +
                            "Home_apartment.kolRegisterd as [Количество прописанных], " +
                            "Homes.Gorod as [Город], " +
                            "Homes.Streets as [Улица], " +
                            "Homes.NumberHome as [Номер дома], " +
                            "Data_Water.kolCOLD as [Количество холодной воды], " +
                            "Data_Water.kolHOT as [Количество горячей воды], " +
                            "CAST(Data_Water.Data_vnoski AS DATE) as [Дата оплаты], " +
                            "Data_Water.statusPlatezha as [Статус оплаты], " +
                            "TypeHome.nameType " +
                            "FROM Data_Water " +
                            "INNER JOIN Home_apartment ON(Data_Water.id_apartaments=Home_apartment.id) " +
                            "INNER JOIN Homes ON(Home_apartment.id_homes=Homes.id) " +
                            "INNER JOIN TypeHome ON (Homes.id_typeHomes=TypeHome.id) " +
                            "WHERE Homes.Streets ='" + valueFilterSQL + "'" +
                            "ORDER BY Data_Water.Data_vnoski";

                    break;
                default:
                    sqlFilters = "SELECT Home_apartment.FIOresident as [Жилец], " +
                            "Home_apartment.kolRegisterd as [Количество прописанных], " +
                            "Homes.Gorod as [Город], " +
                            "Homes.Streets as [Улица], " +
                            "Homes.NumberHome as [Номер дома], " +
                            "Data_Water.kolCOLD as [Количество холодной воды], " +
                            "Data_Water.kolHOT as [Количество горячей воды], " +
                            "CAST(Data_Water.Data_vnoski AS DATE) as [Дата оплаты], " +
                            "Data_Water.statusPlatezha as [Статус оплаты], " +
                            "TypeHome.nameType " +
                            "FROM Data_Water " +
                            "INNER JOIN Home_apartment ON(Data_Water.id_apartaments=Home_apartment.id) " +
                            "INNER JOIN Homes ON(Home_apartment.id_homes=Homes.id) " +
                            "INNER JOIN TypeHome ON (Homes.id_typeHomes=TypeHome.id) " +
                            "WHERE Home_apartment.kolRegisterd =" + Integer.parseInt(valueFilterSQL) + "" +
                            "ORDER BY Data_Water.Data_vnoski";
                    break;
            }

        } else {

            switch (filterSQL) {
                case "Жилец":
                    sqlFilters = "SELECT Home_apartment.FIOresident as [Жилец], " +
                            "Home_apartment.kolRegisterd as [Количество прописанных], " +
                            "Homes.Gorod as [Город], " +
                            "Homes.Streets as [Улица], " +
                            "Homes.NumberHome as [Номер дома], " +
                            "Data_Water.kolCOLD as [Количество холодной воды], " +
                            "Data_Water.kolHOT as [Количество горячей воды], " +
                            "CAST(Data_Water.Data_vnoski AS DATE) as [Дата оплаты], " +
                            "Data_Water.statusPlatezha as [Статус оплаты], " +
                            "TypeHome.nameType " +
                            "FROM Data_Water " +
                            "INNER JOIN Home_apartment ON(Data_Water.id_apartaments=Home_apartment.id) " +
                            "INNER JOIN Homes ON(Home_apartment.id_homes=Homes.id) " +
                            "INNER JOIN TypeHome ON (Homes.id_typeHomes=TypeHome.id) " +
                            "WHERE Home_apartment.FIOresident ='" + valueFilterSQL + "' AND " +
                            "Data_Water.Data_vnoski BETWEEN'" + dateFirst.getText() + "' AND '" + dateLast.getText() + "' " +
                            "ORDER BY Data_Water.Data_vnoski";
                    break;
                case "Статус Оплаты":
                    sqlFilters = "SELECT Home_apartment.FIOresident as [Жилец], " +
                            "Home_apartment.kolRegisterd as [Количество прописанных], " +
                            "Homes.Gorod as [Город], " +
                            "Homes.Streets as [Улица], " +
                            "Homes.NumberHome as [Номер дома], " +
                            "Data_Water.kolCOLD as [Количество холодной воды], " +
                            "Data_Water.kolHOT as [Количество горячей воды], " +
                            "CAST(Data_Water.Data_vnoski AS DATE) as [Дата оплаты], " +
                            "Data_Water.statusPlatezha as [Статус оплаты], " +
                            "TypeHome.nameType " +
                            "FROM Data_Water " +
                            "INNER JOIN Home_apartment ON(Data_Water.id_apartaments=Home_apartment.id) " +
                            "INNER JOIN Homes ON(Home_apartment.id_homes=Homes.id) " +
                            "INNER JOIN TypeHome ON (Homes.id_typeHomes=TypeHome.id) " +
                            "WHERE Data_Water.statusPlatezha ='" + valueFilterSQL + "' AND " +
                            "Data_Water.Data_vnoski BETWEEN'" + dateFirst.getText() + "' AND '" + dateLast.getText() + "' " +
                            "ORDER BY Data_Water.Data_vnoski";
                    break;
                case "Город":
                    sqlFilters = "SELECT Home_apartment.FIOresident as [Жилец], " +
                            "Home_apartment.kolRegisterd as [Количество прописанных], " +
                            "Homes.Gorod as [Город], " +
                            "Homes.Streets as [Улица], " +
                            "Homes.NumberHome as [Номер дома], " +
                            "Data_Water.kolCOLD as [Количество холодной воды], " +
                            "Data_Water.kolHOT as [Количество горячей воды], " +
                            "CAST(Data_Water.Data_vnoski AS DATE) as [Дата оплаты], " +
                            "Data_Water.statusPlatezha as [Статус оплаты], " +
                            "TypeHome.nameType " +
                            "FROM Data_Water " +
                            "INNER JOIN Home_apartment ON(Data_Water.id_apartaments=Home_apartment.id) " +
                            "INNER JOIN Homes ON(Home_apartment.id_homes=Homes.id) " +
                            "INNER JOIN TypeHome ON (Homes.id_typeHomes=TypeHome.id) " +
                            "WHERE Homes.Gorod ='" + valueFilterSQL + "' AND " +
                            "Data_Water.Data_vnoski BETWEEN'" + dateFirst.getText() + "' AND '" + dateLast.getText() + "' " +
                            "ORDER BY Data_Water.Data_vnoski";
                    break;
                case "Улица":
                    sqlFilters = "SELECT Home_apartment.FIOresident as [Жилец], " +
                            "Home_apartment.kolRegisterd as [Количество прописанных], " +
                            "Homes.Gorod as [Город], " +
                            "Homes.Streets as [Улица], " +
                            "Homes.NumberHome as [Номер дома], " +
                            "Data_Water.kolCOLD as [Количество холодной воды], " +
                            "Data_Water.kolHOT as [Количество горячей воды], " +
                            "CAST(Data_Water.Data_vnoski AS DATE) as [Дата оплаты], " +
                            "Data_Water.statusPlatezha as [Статус оплаты], " +
                            "TypeHome.nameType " +
                            "FROM Data_Water " +
                            "INNER JOIN Home_apartment ON(Data_Water.id_apartaments=Home_apartment.id) " +
                            "INNER JOIN Homes ON(Home_apartment.id_homes=Homes.id) " +
                            "INNER JOIN TypeHome ON (Homes.id_typeHomes=TypeHome.id) " +
                            "WHERE Homes.Streets ='" + valueFilterSQL + "' AND " +
                            "Data_Water.Data_vnoski BETWEEN'" + dateFirst.getText() + "' AND '" + dateLast.getText() + "' " +
                            "ORDER BY Data_Water.Data_vnoski";

                    break;
                case "Количество прописанных":
                    sqlFilters = "SELECT Home_apartment.FIOresident as [Жилец], " +
                            "Home_apartment.kolRegisterd as [Количество прописанных], " +
                            "Homes.Gorod as [Город], " +
                            "Homes.Streets as [Улица], " +
                            "Homes.NumberHome as [Номер дома], " +
                            "Data_Water.kolCOLD as [Количество холодной воды], " +
                            "Data_Water.kolHOT as [Количество горячей воды], " +
                            "CAST(Data_Water.Data_vnoski AS DATE) as [Дата оплаты], " +
                            "Data_Water.statusPlatezha as [Статус оплаты], " +
                            "TypeHome.nameType " +
                            "FROM Data_Water " +
                            "INNER JOIN Home_apartment ON(Data_Water.id_apartaments=Home_apartment.id) " +
                            "INNER JOIN Homes ON(Home_apartment.id_homes=Homes.id) " +
                            "INNER JOIN TypeHome ON (Homes.id_typeHomes=TypeHome.id) " +
                            "WHERE Home_apartment.kolRegisterd =" + Integer.parseInt(valueFilterSQL) + "' AND " +
                            "Data_Water.Data_vnoski BETWEEN'" + dateFirst.getText() + "' AND '" + dateLast.getText() + "' " +
                            "ORDER BY Data_Water.Data_vnoski";
                    break;


                default:
                    sqlFilters = "SELECT Home_apartment.FIOresident as [Жилец], " +
                            "Home_apartment.kolRegisterd as [Количество прописанных], " +
                            "Homes.Gorod as [Город], " +
                            "Homes.Streets as [Улица], " +
                            "Homes.NumberHome as [Номер дома], " +
                            "Data_Water.kolCOLD as [Количество холодной воды], " +
                            "Data_Water.kolHOT as [Количество горячей воды], " +
                            "CAST(Data_Water.Data_vnoski AS DATE) as [Дата оплаты], " +
                            "Data_Water.statusPlatezha as [Статус оплаты], " +
                            "TypeHome.nameType " +
                            "FROM Data_Water " +
                            "INNER JOIN Home_apartment ON(Data_Water.id_apartaments=Home_apartment.id) " +
                            "INNER JOIN Homes ON(Home_apartment.id_homes=Homes.id) " +
                            "INNER JOIN TypeHome ON (Homes.id_typeHomes=TypeHome.id) " +
                            "WHERE Data_Water.Data_vnoski BETWEEN'" + dateFirst.getText() + "' AND '" + dateLast.getText() + "'" +
                            "ORDER BY Data_Water.Data_vnoski";
            }
        }

        tableHistoryWater.refresh();
        waterStructureArrayList.clear();
        waterStructureArrayList = sqlFullData(sqlFilters);
        waterStructureObservableListFILTER= FXCollections.observableList(waterStructureArrayList);
        tableHistoryWater.setItems(waterStructureObservableListFILTER);
        tableHistoryWater.refresh();



    }

    public void clearFilter(ActionEvent event) throws SQLException {
        dateFirst.setText("");
        dateLast.setText("");
        comboBoxFilters.setValue(null);
        comboBoxValueFilter.setValue(null);
        init();
    }

    public ArrayList<WaterAreaChartHouseApartament> areaWaterFull(String selectSQLitem) throws SQLException {
        String sqlAreaWaterFull="SELECT SUM(Data_Water.kolCOLD), " +
                "SUM(Data_Water.kolHOT), " +
                "CAST(Data_Water.Data_vnoski AS DATE), " +
                "SUM(Home_apartment.kolRegisterd), " +
                "SUM(Home_apartment.AreaApartments), " +
                "TypeHome.nameType " +
                "FROM Data_Water INNER JOIN Home_apartment ON(Data_Water.id_apartaments=Home_apartment.id) " +
                "INNER JOIN Homes ON (Home_apartment.id_homes=Homes.id) " +
                "INNER JOIN TypeHome ON (Homes.id_typeHomes=TypeHome.id) " +
                "WHERE Home_apartment.id_homes="+selectSQLitem+"" +
                "Group BY Data_Water.Data_vnoski, TypeHome.nameType";

        ArrayList<WaterAreaChartHouseApartament> wrHouseApartArray= new ArrayList<>();
        Statement statementT = connection.createStatement();
        ResultSet resultHomes = statementT.executeQuery(sqlAreaWaterFull);

        while (resultHomes.next()) {
            WaterAreaChartHouseApartament waterAreaChartHouseApartament=new WaterAreaChartHouseApartament();
            waterAreaChartHouseApartament.setSumColdWaterApartament(resultHomes.getFloat(1));
            waterAreaChartHouseApartament.setSumHotWaterApartament(resultHomes.getFloat(2));
            waterAreaChartHouseApartament.setDataWaterApartament(resultHomes.getDate(3));
            waterAreaChartHouseApartament.setCountRegrestingHouse(resultHomes.getInt(4));
            waterAreaChartHouseApartament.setAreaApartamentsSum(resultHomes.getFloat(5));
            waterAreaChartHouseApartament.setTypeHome(resultHomes.getString(6));
            arrDates.add(resultHomes.getString(3));

            System.out.println(resultHomes.getString(1) +
                    " " + resultHomes.getString(2) +
                    " " + resultHomes.getString(3)+
                    " " + resultHomes.getString(4)+
                    " " + resultHomes.getString(5)+
                    " " + resultHomes.getString(6));
            typeHome=resultHomes.getString(6);
            wrHouseApartArray.add(waterAreaChartHouseApartament);

        }
        return wrHouseApartArray;
    }

    public ArrayList<WaterAreaHouseRateEmpl> waterGeneralHouseIndications(String selectSQLitem) throws SQLException {
        String sqlHouseCounter="SELECT CAST(GeneralHouseIndications.Data_indications AS DATE), " +
                "GeneralHouseIndications.valueColdWater, " +
                "GeneralHouseIndications.valueHotWater " +
                "FROM GeneralHouseIndications " +
                "WHERE GeneralHouseIndications.id_house="+selectSQLitem+"";

        ArrayList<WaterAreaHouseRateEmpl> wrHouseApartArray= new ArrayList<>();
        Statement statementT = connection.createStatement();

        ResultSet resultSetHouseCounter=statementT.executeQuery(sqlHouseCounter);

        while (resultSetHouseCounter.next()){
            WaterAreaHouseRateEmpl waterAreaHouseRateEmpl= new WaterAreaHouseRateEmpl();
            waterAreaHouseRateEmpl.setDataWaterApartament(resultSetHouseCounter.getDate(1));
            waterAreaHouseRateEmpl.setGeneralHouseIndicationsColdWater(resultSetHouseCounter.getFloat(2));
            waterAreaHouseRateEmpl.setGeneralHouseIndicationsHotWater(resultSetHouseCounter.getFloat(3));
            wrHouseApartArray.add(waterAreaHouseRateEmpl);
        }
        return wrHouseApartArray;
    }


    public void updateChart(ActionEvent event) throws SQLException {
        comboBoxDate.setValue(null);
        ObservableList<String> namearrDates;
        arrDates.clear();
        comboBoxDate.getItems().clear();
        onlyColdWaterCheck.setSelected(false);
        onlyHotWaterCheck.setSelected(false);
        waterAreaHouseRateEmpls.clear();
        waterAreaChartHouseApartaments.clear();
        areaChartAnalys.getData().clear();

        xAxisX.setLabel("Дата платежа");
        yAxisY.setLabel("Количество кубов");
        XYChart.Series<String,Float> seriesSumColdWater =new XYChart.Series<>();
        XYChart.Series<String,Float> seriesRateColdWater = new XYChart.Series<>();
        seriesSumColdWater.setName("");
        seriesRateColdWater.setName("");


        String[] selectSQLitem=comboBoxArea.getSelectionModel().getSelectedItem().toString().split(",");
        System.out.println(selectSQLitem[0]+"_"+selectSQLitem[1]+"_"+selectSQLitem[2]+"_"+selectSQLitem[3]+"_"+selectSQLitem[4]);



        waterAreaChartHouseApartaments=areaWaterFull(selectSQLitem[0]);

        kolRegestr=howManyRegrestingHouse(selectSQLitem[0]);

        deleteRepeat(arrDates);
         namearrDates = FXCollections.observableArrayList(arrDates);
        comboBoxDate.setItems(namearrDates);
        waterAreaHouseRateEmpls=waterGeneralHouseIndications(selectSQLitem[0]);

        for (WaterAreaChartHouseApartament waterAreaChartHouseApartament : waterAreaChartHouseApartaments) {
            seriesSumColdWater.getData().add(new XYChart.Data<>(waterAreaChartHouseApartament.getDataWaterApartament().toString(),
                    waterAreaChartHouseApartament.getSumColdWaterApartament() + waterAreaChartHouseApartament.getSumHotWaterApartament()));
        }
        for (WaterAreaHouseRateEmpl waterAreaHouseRateEmpl : waterAreaHouseRateEmpls) {
            seriesRateColdWater.getData().add(new XYChart.Data<>(waterAreaHouseRateEmpl.getDataWaterApartament().toString(),
                    waterAreaHouseRateEmpl.getGeneralHouseIndicationsColdWater() + waterAreaHouseRateEmpl.getGeneralHouseIndicationsHotWater()));
        }
        if(typeHome.equals("Многоквартирный")) {
            seriesSumColdWater.setName("Общеквартирный расход воды, в кубометрах");
            seriesRateColdWater.setName("Расход воды по общедомовому счетчику, в кубометрах");
            areaChartAnalys.getData().addAll(seriesSumColdWater,seriesRateColdWater);
        }else{
            seriesSumColdWater.setName("Расход воды в коттеджном доме, в кубометрах");
            areaChartAnalys.getData().addAll(seriesSumColdWater);
        }

        System.out.println("Количечтво прописанных в функции updateChart "+kolRegestr);
        if(typeHome.equals("Многоквартирный")) {
            WaterAreaChartHouseApartament waterAreaChartHouseApartament = waterAreaChartHouseApartaments.get(waterAreaChartHouseApartaments.size() - 1);
            WaterAreaHouseRateEmpl waterAreaHouseRateEmpl = waterAreaHouseRateEmpls.get(waterAreaHouseRateEmpls.size() - 1);

            textAreaRecomedation.setText(recomendationWatersPotreblenie(kolRegestr, typeHome, waterAreaChartHouseApartament, waterAreaHouseRateEmpl));
        }else{
            WaterAreaChartHouseApartament waterAreaChartHouseApartament = waterAreaChartHouseApartaments.get(waterAreaChartHouseApartaments.size() - 1);
           // WaterAreaHouseRateEmpl waterAreaHouseRateEmpl = waterAreaHouseRateEmpls.get(waterAreaHouseRateEmpls.size() - 1);

            textAreaRecomedation.setText(recomendationWatersPotreblenie(kolRegestr, typeHome, waterAreaChartHouseApartament, null));
        }
    }

    public void onlyColdAnalys(ActionEvent event) throws SQLException {
        onlyHotWaterCheck.setSelected(false);

        waterAreaChartHouseApartaments.clear();
        areaChartAnalys.getData().clear();
        xAxisX.setLabel("Дата платежа");
        yAxisY.setLabel("Количество кубов");
        XYChart.Series<String,Float> seriesSumColdWater =new XYChart.Series<>();
        XYChart.Series<String,Float> seriesRateColdWater = new XYChart.Series<>();
        seriesSumColdWater.setName("");
        seriesRateColdWater.setName("");
        System.out.println(typeHome+" Тип ДОМА ФУНКЦИЯ ОНЛИКОЛД");
        String[] selectSQLitem=comboBoxArea.getSelectionModel().getSelectedItem().toString().split(",");
        System.out.println(selectSQLitem[0]+"_"+selectSQLitem[1]+"_"+selectSQLitem[2]+"_"+selectSQLitem[3]);
        townUser=selectSQLitem[1];
        String sqlAreaWaterFull="SELECT SUM(Data_Water.kolCOLD)," +
                "SUM(Data_Water.kolHOT), " +
                "CAST(Data_Water.Data_vnoski AS DATE), " +
                "SUM(Home_apartment.kolRegisterd), " +
                "SUM(Home_apartment.AreaApartments) FROM Data_Water INNER JOIN Home_apartment ON(Data_Water.id_apartaments=Home_apartment.id) " +
                "WHERE Home_apartment.id_homes="+selectSQLitem[0]+"" +
                "Group BY Data_Water.Data_vnoski";

        Statement statementT = connection.createStatement();
        ResultSet resultHomes = statementT.executeQuery(sqlAreaWaterFull);

        int kolRegisters=0;
        while (resultHomes.next()) {
            WaterAreaChartHouseApartament waterAreaChartHouseApartament=new WaterAreaChartHouseApartament();
            waterAreaChartHouseApartament.setSumColdWaterApartament(resultHomes.getFloat(1));
            waterAreaChartHouseApartament.setSumHotWaterApartament(resultHomes.getFloat(2));
            waterAreaChartHouseApartament.setDataWaterApartament(resultHomes.getDate(3));
            waterAreaChartHouseApartament.setCountRegrestingHouse(resultHomes.getInt(4));
            waterAreaChartHouseApartament.setAreaApartamentsSum(resultHomes.getFloat(5));

            kolRegisters=resultHomes.getInt(4);
            waterAreaChartHouseApartaments.add(waterAreaChartHouseApartament);
        }

        if(townUser.equals("Воронеж")) {
            if(typeHome.equals("Многоквартирный")) {
            for (WaterAreaChartHouseApartament waterAreaChartHouseApartament : waterAreaChartHouseApartaments) {
                seriesSumColdWater.getData().add(new XYChart.Data<>(waterAreaChartHouseApartament.getDataWaterApartament().toString(),
                        waterAreaChartHouseApartament.getSumColdWaterApartament()));
                seriesRateColdWater.getData().add(new XYChart.Data<>(waterAreaChartHouseApartament.getDataWaterApartament().toString(),
                        kolRegisters * rateAndCost.getRateConsumptionCold()));

            }
                seriesSumColdWater.setName("Общеквартирный расход холодной воды, в кубометрах");
                seriesRateColdWater.setName("Расход холодной воды по нормам потребления для "+kolRegisters+" чел., в кубометрах");
            } else{
                for (WaterAreaChartHouseApartament waterAreaChartHouseApartament : waterAreaChartHouseApartaments) {
                    seriesSumColdWater.getData().add(new XYChart.Data<>(waterAreaChartHouseApartament.getDataWaterApartament().toString(),
                            waterAreaChartHouseApartament.getSumColdWaterApartament()));
                    seriesRateColdWater.getData().add(new XYChart.Data<>(waterAreaChartHouseApartament.getDataWaterApartament().toString(),
                            kolRegisters * rateAndCost.getRateConsumptionCold()));

                }
                seriesSumColdWater.setName("Расход холодной воды, в кубометрах");
                seriesRateColdWater.setName("Расход холодной воды по нормам потребления для "+kolRegisters+" чел., в кубометрах");
            }
        }else{
            if(typeHome.equals("Многоквартирный")) {
                for (WaterAreaChartHouseApartament waterAreaChartHouseApartament : waterAreaChartHouseApartaments) {
                    seriesSumColdWater.getData().add(new XYChart.Data<>(waterAreaChartHouseApartament.getDataWaterApartament().toString(),
                            waterAreaChartHouseApartament.getSumColdWaterApartament()));
                    seriesRateColdWater.getData().add(new XYChart.Data<>(waterAreaChartHouseApartament.getDataWaterApartament().toString(),
                            kolRegisters * rateAndCost.getRateConsumptionColdMSC()));
                }
                seriesSumColdWater.setName("Общеквартирный расход холодной воды, в кубометрах");
                seriesRateColdWater.setName("Расход холодной воды по нормам потребления для "+kolRegisters+" чел., в кубометрах");
            }else{
                for (WaterAreaChartHouseApartament waterAreaChartHouseApartament : waterAreaChartHouseApartaments) {
                    seriesSumColdWater.getData().add(new XYChart.Data<>(waterAreaChartHouseApartament.getDataWaterApartament().toString(),
                            waterAreaChartHouseApartament.getSumColdWaterApartament()));
                    seriesRateColdWater.getData().add(new XYChart.Data<>(waterAreaChartHouseApartament.getDataWaterApartament().toString(),
                            kolRegisters * rateAndCost.getRateConsumptionColdMSC()));
                }
                seriesSumColdWater.setName("Расход холодной воды, в кубометрах");
                seriesRateColdWater.setName("Расход холодной воды по нормам потребления для "+kolRegisters+" чел., в кубометрах");
            }
        }

        areaChartAnalys.getData().addAll(seriesSumColdWater,seriesRateColdWater);
    }

    public void onlyHotAnalys(ActionEvent event)  throws SQLException {

        onlyColdWaterCheck.setSelected(false);
        waterAreaChartHouseApartaments.clear();
        areaChartAnalys.getData().clear();
        xAxisX.setLabel("Дата платежа");
        yAxisY.setLabel("Количество кубов");
        XYChart.Series<String,Float> seriesSumColdWater =new XYChart.Series<>();
        XYChart.Series<String,Float> seriesRateColdWater = new XYChart.Series<>();
        seriesSumColdWater.setName("");
        seriesRateColdWater.setName("");
        System.out.println(typeHome+" Тип ДОМА ФУНКЦИЯ ОНЛИГОРЯЧ");
        String[] selectSQLitem=comboBoxArea.getSelectionModel().getSelectedItem().toString().split(",");

        System.out.println(selectSQLitem[0]+"_"+selectSQLitem[1]+"_"+selectSQLitem[2]+"_"+selectSQLitem[3]);
        townUser=selectSQLitem[1];
        String sqlAreaWaterFull="SELECT SUM(Data_Water.kolCOLD)," +
                "SUM(Data_Water.kolHOT), " +
                "CAST(Data_Water.Data_vnoski AS DATE), " +
                "SUM(Home_apartment.kolRegisterd), " +
                "SUM(Home_apartment.AreaApartments) FROM Data_Water INNER JOIN Home_apartment ON(Data_Water.id_apartaments=Home_apartment.id) " +
                "WHERE Home_apartment.id_homes="+selectSQLitem[0]+"" +
                "Group BY Data_Water.Data_vnoski";

        Statement statementT = connection.createStatement();
        ResultSet resultHomes = statementT.executeQuery(sqlAreaWaterFull);
        int kolRegisters=0;
        while (resultHomes.next()) {
            WaterAreaChartHouseApartament waterAreaChartHouseApartament=new WaterAreaChartHouseApartament();
            waterAreaChartHouseApartament.setSumColdWaterApartament(resultHomes.getFloat(1));
            waterAreaChartHouseApartament.setSumHotWaterApartament(resultHomes.getFloat(2));
            waterAreaChartHouseApartament.setDataWaterApartament(resultHomes.getDate(3));
            waterAreaChartHouseApartament.setCountRegrestingHouse(resultHomes.getInt(4));
            waterAreaChartHouseApartament.setAreaApartamentsSum(resultHomes.getFloat(5));

            kolRegisters=resultHomes.getInt(4);
            waterAreaChartHouseApartaments.add(waterAreaChartHouseApartament);
        }

        if(townUser.equals("Воронеж")) {
            if(typeHome.equals("Многоквартирный")) {
                for (WaterAreaChartHouseApartament waterAreaChartHouseApartament : waterAreaChartHouseApartaments) {
                    seriesSumColdWater.getData().add(new XYChart.Data<>(waterAreaChartHouseApartament.getDataWaterApartament().toString(),
                            waterAreaChartHouseApartament.getSumHotWaterApartament()));
                    seriesRateColdWater.getData().add(new XYChart.Data<>(waterAreaChartHouseApartament.getDataWaterApartament().toString(),
                            kolRegisters * rateAndCost.getRateConsumptionHot()));
                }
                seriesSumColdWater.setName("Общеквартирный расход горячей воды, в кубометрах");
                seriesRateColdWater.setName("Расход горячей воды по нормам потребления для "+kolRegisters+" чел., в кубометрах");

            }else{
                for (WaterAreaChartHouseApartament waterAreaChartHouseApartament : waterAreaChartHouseApartaments) {
                    seriesSumColdWater.getData().add(new XYChart.Data<>(waterAreaChartHouseApartament.getDataWaterApartament().toString(),
                            waterAreaChartHouseApartament.getSumHotWaterApartament()));
                    seriesRateColdWater.getData().add(new XYChart.Data<>(waterAreaChartHouseApartament.getDataWaterApartament().toString(),
                            kolRegisters * rateAndCost.getRateConsumptionHot()));
                }
                seriesSumColdWater.setName("Расход горячей воды, в кубометрах");
                seriesRateColdWater.setName("Расход горячей воды по нормам потребления для "+kolRegisters+" чел., в кубометрах");
            }
        }else{
            if(typeHome.equals("Многоквартирный")) {
                for (WaterAreaChartHouseApartament waterAreaChartHouseApartament : waterAreaChartHouseApartaments) {
                    seriesSumColdWater.getData().add(new XYChart.Data<>(waterAreaChartHouseApartament.getDataWaterApartament().toString(),
                            waterAreaChartHouseApartament.getSumHotWaterApartament()));
                    seriesRateColdWater.getData().add(new XYChart.Data<>(waterAreaChartHouseApartament.getDataWaterApartament().toString(),
                            kolRegisters * rateAndCost.getRateConsumptionHotMSC()));
                }
                seriesSumColdWater.setName("Общеквартирный расход горячей воды, в кубометрах");
                seriesRateColdWater.setName("Расход горячей воды по нормам потребления для "+kolRegisters+" чел., в кубометрах");

            } else{
                for (WaterAreaChartHouseApartament waterAreaChartHouseApartament : waterAreaChartHouseApartaments) {
                    seriesSumColdWater.getData().add(new XYChart.Data<>(waterAreaChartHouseApartament.getDataWaterApartament().toString(),
                            waterAreaChartHouseApartament.getSumHotWaterApartament()));
                    seriesRateColdWater.getData().add(new XYChart.Data<>(waterAreaChartHouseApartament.getDataWaterApartament().toString(),
                            kolRegisters * rateAndCost.getRateConsumptionHotMSC()));
                }
                seriesSumColdWater.setName("Расход горячей воды, в кубометрах");
                seriesRateColdWater.setName("Расход горячей воды по нормам потребления для "+kolRegisters+" чел., в кубометрах");
            }

        }

        areaChartAnalys.getData().addAll(seriesSumColdWater,seriesRateColdWater);
    }

    private String recomendationWatersPotreblenie(int kolRegrestr, String typeHome, WaterAreaChartHouseApartament waterAreaChartHouseApartament,
                                                  WaterAreaHouseRateEmpl waterAreaHouseRateEmpl){
        String result = null;

        float differenceReadingsCold = 0;
        float differenceReadingsHot = 0;

        Date dateAnal =waterAreaChartHouseApartament.getDataWaterApartament();

        if(typeHome.equals("Многоквартирный")) {

             potrblColdApartaments = waterAreaChartHouseApartament.getSumColdWaterApartament();
             potrblHotApartaments = waterAreaChartHouseApartament.getSumHotWaterApartament();

             potrblColdHouseRateEmpl = waterAreaHouseRateEmpl.getGeneralHouseIndicationsColdWater();
             potrblHotHouseRateEmpl = waterAreaHouseRateEmpl.getGeneralHouseIndicationsHotWater();

            differenceReadingsCold = potrblColdHouseRateEmpl - potrblColdApartaments;
            differenceReadingsHot = potrblHotHouseRateEmpl - potrblHotApartaments;
            differenceReadings = differenceReadingsCold + differenceReadingsHot;
        } else {
            potrblColdApartaments = waterAreaChartHouseApartament.getSumColdWaterApartament();
            potrblHotApartaments = waterAreaChartHouseApartament.getSumHotWaterApartament();
        }


        System.out.println("float potrblColdApartaments="+potrblColdApartaments +
                "\nfloat potrblHotApartaments= "+potrblHotApartaments +
                "\nfloat potrblColdHouseRateEmpl = " +potrblColdHouseRateEmpl+
                "\nfloat potrblHotHouseRateEmpl = "+potrblHotHouseRateEmpl +
                "\nfloat differenceReadingsCold = " +differenceReadingsCold+
                "\nfloat differenceReadingsHot = "+differenceReadingsHot +
                "\nfloat differenceReadings = " +differenceReadings);

        if(townUser.equals("Воронеж")) {
             normCold = rateAndCost.getRateConsumptionCold() * kolRegrestr;
             normHot = rateAndCost.getRateConsumptionHot() * kolRegrestr;
        }else{
             normCold = rateAndCost.getRateConsumptionColdMSC() * kolRegrestr;
             normHot = rateAndCost.getRateConsumptionHotMSC() * kolRegrestr;
        }

        System.out.println(normCold+"Холод норма ");
        System.out.println(normHot+"Горчая норма ");
        System.out.println(typeHome+"ТИП ДОМА ");
        if(typeHome.equals("Многоквартирный")) {
            if ((potrblColdApartaments == potrblColdHouseRateEmpl) && (potrblHotApartaments == potrblHotHouseRateEmpl)) {
                result = "По данным от "+dateAnal+":\n" +
                        "Общедомовое потребление сходится с показаниями квартир\n" +
                        "Сумма куб. х/в по данным квартир - "+potrblColdApartaments +"\n"+
                        "Сумма куб. г/в по данным квартир - "+potrblHotApartaments +"\n"+
                        "Общедомовое потребление - " +(potrblColdHouseRateEmpl+potrblHotHouseRateEmpl)+"\n"+
                        "Разница показаний: " + differenceReadings;
            }
            if ((potrblColdApartaments < potrblColdHouseRateEmpl) || (potrblHotApartaments < potrblHotHouseRateEmpl)) {
                result = "По данным от "+dateAnal+":\n" +
                        "Сумма куб. х/в по данным квартир - "+potrblColdApartaments +"\n"+
                        "Сумма куб. г/в по данным квартир - "+potrblHotApartaments +"\n"+
                        "Общедомовое потребление - " +(potrblColdHouseRateEmpl+potrblHotHouseRateEmpl) +"\n"+
                        "Общедомое потребление больше,чем показания квартир\n" +
                        "Разница показаний: х/в - " + differenceReadingsCold + ",\n" +
                        "г/в - " + differenceReadingsHot + ", общая - " + differenceReadings+"\n"+
                        "Возможные причины:\n" +
                        "Потери воды приходятся из-за неисправной сантехники.\n" +
                        "Механическое вмешательство в прибор учета (это магниты и стержни).\n" +
                        "Погрешности приборов учета не прошедшие своевременно поверку.\n" +
                        "Утечки на трубопроводах в подвальных помещениях!\n";;
            }
            if ((potrblColdApartaments > potrblColdHouseRateEmpl) && (potrblHotApartaments > potrblHotHouseRateEmpl)) {
                result = "По данным от "+dateAnal+":\n" +
                        "Сумма куб. х/в по данным квартир - "+potrblColdApartaments +"\n"+
                        "Сумма куб. г/в по данным квартир - "+potrblHotApartaments +"\n"+
                        "Необходимо внести общедомовые данные за текущую дату!";

            }
        }else{
            if ((potrblColdApartaments == normCold) && (potrblHotApartaments == normHot)) {
                result = "По данным от "+dateAnal+":\n" +
                        "Норма потребления х/в - " +normCold+"\n" +
                        "Норма потребления г/в - " +normHot+"\n" +
                        "Фактическое потребление х/в - " +potrblColdApartaments+"\n" +
                        "Фактическое потребление г/в - " +potrblHotApartaments+"\n" +
                        "Потребление воды находится в пределах нормы";
            }
            if ((potrblColdApartaments < normCold) || (potrblHotApartaments < normHot)) {
                result = "По данным от "+dateAnal+":\n" +
                        "Норма потребления х/в - " +normCold+"\n" +
                        "Норма потребления г/в - " +normHot+"\n" +
                        "Фактическое потребление х/в - " +potrblColdApartaments+"\n" +
                        "Фактическое потребление г/в - " +potrblHotApartaments+"\n" +
                        "Потребление воды находится ниже нормы";

            }
            if ((potrblColdApartaments > normCold) && (potrblHotApartaments > normHot)) {
                result = "По данным от "+dateAnal+":\n" +
                        "Потребление воды находится в выше нормы\n" +
                        "Норма потребления х/в - " +normCold+"\n" +
                        "Норма потребления г/в - " +normHot+"\n" +
                        "Фактическое потребление х/в - " +potrblColdApartaments+"\n" +
                        "Фактическое потребление г/в - " +potrblHotApartaments+"\n" +
                        "Разница: х/в - " + (potrblColdApartaments-normCold) + ",\n" +
                        "г/в - " + (potrblHotApartaments-normHot) + ", общая - " + ((potrblColdApartaments-normCold)+(potrblHotApartaments-normHot));

            }
        }
        return result;
    }

    public void changingSettings(ActionEvent event) throws SQLException {

        String sqlUpdate="UPDATE rateANDcost " +
                "SET NormaPotrebleniyaCold = "+textNormaPotrebleniyaCold.getText()+", " +
                "NormaPotrebleniyaHot = "+textNormaPotrebleniyaHot.getText()+", " +
                "PriceColdWater = "+textPriceColdWater.getText()+", " +
                "PriceHotWaterRomponentEnerge = "+textPriceHotWaterRomponentEnerge.getText()+", " +
                "PriceVodootvod = "+textPriceVodootvod.getText()+", " +
                "PowerConsumption = "+textPowerConsumption.getText()+", "+
                "NormaPotrebleniyaColdMSC = "+textNormaPotrebleniyaColdMSC.getText()+", " +
                "NormaPotrebleniyaHotMSC = "+textNormaPotrebleniyaHotMSC.getText()+", " +
                "PriceColdWaterMSC = "+textPriceColdWaterMSC.getText()+", " +
                "PriceHotWaterMSC = "+textPriceHotWaterRomponentEnergeMSC.getText()+", " +
                "PriceVodootvodMCS = "+textPriceVodootvodMSC.getText()+" ";

        Statement statement = connection.createStatement();
        int rows = statement.executeUpdate(sqlUpdate);
        System.out.printf("Изменена %d строка", rows);

        if(rows==1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Успешное обновление");
            alert.setHeaderText(null);
            alert.setContentText("Введенные данные успешно сохранились в базе данных!");
            alert.showAndWait();

            rateAndCost.setRateConsumptionCold(Float.parseFloat(textNormaPotrebleniyaCold.getText()));
            rateAndCost.setRateConsumptionHot(Float.parseFloat(textNormaPotrebleniyaHot.getText()));
            rateAndCost.setPriceColdWater(Float.parseFloat(textPriceColdWater.getText()));
            rateAndCost.setPriceHotWaterEnergy(Float.parseFloat(textPriceHotWaterRomponentEnerge.getText()));
            rateAndCost.setPriceWaterDisposal(Float.parseFloat(textPriceVodootvod.getText()));
            rateAndCost.setPowerConsumption(Float.parseFloat(textPowerConsumption.getText()));
            rateAndCost.setRateConsumptionColdMSC(Float.parseFloat(textNormaPotrebleniyaColdMSC.getText()));
            rateAndCost.setRateConsumptionHotMSC(Float.parseFloat(textNormaPotrebleniyaHotMSC.getText()));
            rateAndCost.setPriceColdWaterMSC(Float.parseFloat(textPriceColdWaterMSC.getText()));
            rateAndCost.setPriceHotWaterEnergyMSC(Float.parseFloat(textPriceHotWaterRomponentEnergeMSC.getText()));
            rateAndCost.setPriceWaterDisposalMSC(Float.parseFloat(textPriceVodootvodMSC.getText()));
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Что-то пошло не так");
            alert.showAndWait();
        }

        refreshProgram();
    }

    public void refreshProgram() throws SQLException {
        init();
        onlyColdWaterCheck.setSelected(false);
        onlyHotWaterCheck.setSelected(false);
        waterAreaHouseRateEmpls.clear();
        waterAreaChartHouseApartaments.clear();
        areaChartAnalys.getData().clear();
        textAreaRecomedation.clear();
        comboBoxDate.getItems().clear();
        comboBoxHouseOdn.getItems().clear();
    }

    public void getClose(Event event) {
        try {
            MSSQLConnection.getConnection();
            Parent root = FXMLLoader.load(getClass().getResource("/sample/authentication.fxml"));
            stageAuthen  = new Stage();
            stageAuthen.setTitle("Вход в систему");
            stageAuthen.setScene(new Scene(root, 450, 250));
            stageAuthen.show();
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        AuthenticationController.stageEmpl.close();

    }

    public void getDateAnalys(ActionEvent event) {

        WaterAreaChartHouseApartament waterAreaChartHouseApartamentDate= new WaterAreaChartHouseApartament();
        WaterAreaHouseRateEmpl waterAreaHouseRateEmplDate = new WaterAreaHouseRateEmpl();

        int iHouseDate = 0;
        int iHouseRate = 0;
        System.out.println("Дата комбобокса"+ comboBoxDate.getValue());
        String dataComboBox = (String) comboBoxDate.getValue();
        System.out.println("Дата в переменной"+ dataComboBox);
        for(int i=0; i<waterAreaChartHouseApartaments.size();i++){
            System.out.println("Дата из списка "+ waterAreaChartHouseApartaments.get(i).getDataWaterApartament());
            if(dataComboBox.equals(String.valueOf(waterAreaChartHouseApartaments.get(i).getDataWaterApartament()))){
                iHouseDate=i;
                 waterAreaChartHouseApartamentDate=waterAreaChartHouseApartaments.get(i);
                System.out.println("НУжная Дата из списка: "+ waterAreaChartHouseApartaments.get(i).getDataWaterApartament());
            }
        }
        for(int i=0; i<waterAreaHouseRateEmpls.size();i++){
            System.out.println("Дата ОБЩЕДОМОВЫХ из списка "+ waterAreaHouseRateEmpls.get(i).getDataWaterApartament());
            if(dataComboBox.equals(String.valueOf(waterAreaHouseRateEmpls.get(i).getDataWaterApartament()))){
                iHouseRate=i;
                System.out.println("НУжная Дата ОБЩЕДОМОВЫХ: "+ waterAreaHouseRateEmpls.get(i).getDataWaterApartament());
                waterAreaHouseRateEmplDate=waterAreaHouseRateEmpls.get(i);
            }
        }

        System.out.println("Количечтво прописанных в функции getDateAnalys !!"+kolRegestr);
        System.out.println("iHouseDate !!"+iHouseDate);
        System.out.println("iHouseRate !!"+iHouseRate);
        System.out.println("Дата из листа квартир  !!"+ waterAreaChartHouseApartamentDate.getDataWaterApartament());
        System.out.println("Дата ОБЩЕДОМОВЫХ квартир  !!"+ waterAreaHouseRateEmplDate.getDataWaterApartament());

        textAreaRecomedation.setText(recomendationWatersPotreblenie(kolRegestr,typeHome,
                waterAreaChartHouseApartamentDate, waterAreaHouseRateEmplDate));

    }

    public int howManyRegrestingHouse(String idHouse) throws SQLException {
        int kolRegresting=0;
        Statement statement = connection.createStatement();
        String sqlRegresting="SELECT SUM(kolRegisterd) " +
                "FROM Home_apartment " +
                "WHERE Home_apartment.id_homes="+idHouse +
                " GROUP BY Home_apartment.id_homes";

        ResultSet resultkolRegresting = statement.executeQuery(sqlRegresting);

        while (resultkolRegresting.next()) {
            kolRegresting=resultkolRegresting.getInt(1);
        }

        System.out.println("Количество в функции howManyRegrestingHouse "+ kolRegresting);
        return kolRegresting;
    }

    public void addOdn(ActionEvent event) throws SQLException {
        String[] selectSQLHouse=comboBoxHouseOdn.getSelectionModel().getSelectedItem().toString().split(",");
        System.out.println("addODN fun: "+selectSQLHouse[0]+"_"+selectSQLHouse[1]+"_"+selectSQLHouse[2]+"_"+selectSQLHouse[3]+"_"+selectSQLHouse[4]);

        String idHouse=selectSQLHouse[0];
        String dateOdnTxt = dateOdn.getText();
        String coldOdnTxt = coldOdn.getText();
        String hotOdnTxt = hotOdn.getText();
        float ODN;
        if(selectSQLHouse[4].equals("Коттедж")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Внимание!");
            alert.setHeaderText(null);
            alert.setContentText("Выбранный дом - Коттедж. Данные не будут добавлены в базу данных!");
            alert.showAndWait();
        }else {

            ODN=getODN(coldOdnTxt, hotOdnTxt, dateOdnTxt);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Внимание!");
            alert.setHeaderText(null);
            alert.setContentText("ODN= "+ODN);
            alert.showAndWait();

            Statement statementINSERT = connection.createStatement();
            String sqlInsertDataWater = "INSERT INTO GeneralHouseIndications ([id_house],[Data_indications]," +
                    "[valueColdWater],[valueHotWater],[ODN]) " +
                    "VALUES (" + idHouse + ",'" + dateOdnTxt + "'," +
                    "" + coldOdnTxt + "," + hotOdnTxt + ","+ODN+");";

            int rows = statementINSERT.executeUpdate(sqlInsertDataWater);
            System.out.printf("Добавлена %d строка в таблицу GeneralHouseIndications", rows);


        }

        dateOdn.setText("");
        coldOdn.setText("");
        hotOdn.setText("");
        refreshProgram();
    }

    private float getODN(String coldOdnTxt, String hotOdnTxt, String dateOdnTxt){

        WaterAreaChartHouseApartament waterAreaChartHouseApartamentDate= new WaterAreaChartHouseApartament();

        for(int i=0; i<waterAreaChartHouseApartaments.size();i++){
            System.out.println("getODN Дата из списка "+ waterAreaChartHouseApartaments.get(i).getDataWaterApartament());
            if(dateOdnTxt.equals(String.valueOf(waterAreaChartHouseApartaments.get(i).getDataWaterApartament()))){
                waterAreaChartHouseApartamentDate=waterAreaChartHouseApartaments.get(i);
                System.out.println("getODN НУжная Дата из списка: "+ waterAreaChartHouseApartaments.get(i).getDataWaterApartament());
            }
        }

        System.out.println("getODN Дата из листа квартир  !!"+ waterAreaChartHouseApartamentDate.getDataWaterApartament());

        float potrblColdApartaments;
        float potrblHotApartaments;
        float potrblColdHouseRateEmpl = 0;
        float potrblHotHouseRateEmpl = 0;
        float differenceReadingsCold = 0;
        float differenceReadingsHot = 0;
        float differenceReadings = 0;

            potrblColdApartaments = waterAreaChartHouseApartamentDate.getSumColdWaterApartament();
            potrblHotApartaments = waterAreaChartHouseApartamentDate.getSumHotWaterApartament();

            potrblColdHouseRateEmpl = Float.parseFloat(coldOdnTxt);
            potrblHotHouseRateEmpl = Float.parseFloat(hotOdnTxt);

            differenceReadingsCold = potrblColdHouseRateEmpl - potrblColdApartaments;
            differenceReadingsHot = potrblHotHouseRateEmpl - potrblHotApartaments;
            differenceReadings = differenceReadingsCold + differenceReadingsHot;


        System.out.println("float potrblColdApartaments="+potrblColdApartaments +
                "\nfloat potrblHotApartaments= "+potrblHotApartaments +
                "\nfloat potrblColdHouseRateEmpl = " +potrblColdHouseRateEmpl+
                "\nfloat potrblHotHouseRateEmpl = "+potrblHotHouseRateEmpl +
                "\nfloat differenceReadingsCold = " +differenceReadingsCold+
                "\nfloat differenceReadingsHot = "+differenceReadingsHot +
                "\nfloat differenceReadings = " +differenceReadings);

               System.out.println("Сумма куб. х/в по данным квартир - "+potrblColdApartaments +"\n"+
                                  "Сумма куб. г/в по данным квартир - "+potrblHotApartaments +"\n"+
                                  "Общедомовое потребление - " +(potrblColdHouseRateEmpl+potrblHotHouseRateEmpl)+"\n"+
                                  "Разница показаний: " + differenceReadings);

        return differenceReadings;
    }

    public void reportExcelRashod(ActionEvent event) throws IOException {
        // получаем файл в формате xlsx
        String filePath = "emplRashodPoZhel.xls";
        // получаем экземпляр XSSFWorkbook для обработки xlsx файла
        FileInputStream file = new FileInputStream(new File(filePath));

        HSSFWorkbook workbook = new HSSFWorkbook(file);
        // выбираем первый лист для обработки
        // нумерация начинается из 0
        HSSFSheet sheetAt = workbook.getSheetAt(0);

        int rowNum = 1;

        for (WaterStructureEmpl waterStructureEmpl : waterStructureArrayList) {
            createSheetHeader(sheetAt, ++rowNum, waterStructureEmpl);
        }
        try (FileOutputStream out = new FileOutputStream(new File("resultOtchet\\emplRashodPoZhel"  + ".xls"))) {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Excel sozdan!");

    }


    private static void createSheetHeader(HSSFSheet sheetAt, int rowNum, WaterStructureEmpl waterStructureArrayList) {
        Row row = sheetAt.getRow(rowNum);
        row.getCell(0).setCellValue(waterStructureArrayList.getZhilec());
        row.getCell(1).setCellValue(waterStructureArrayList.getKolRegisterd());
        row.getCell(2).setCellValue(waterStructureArrayList.getTown());
        row.getCell(3).setCellValue(waterStructureArrayList.getStreets());
        row.getCell(4).setCellValue(waterStructureArrayList.getNumberHouse());
        row.getCell(5).setCellValue(waterStructureArrayList.getValCold());
        row.getCell(6).setCellValue(waterStructureArrayList.getValHot());
        row.getCell(7).setCellValue(waterStructureArrayList.getSumOpl());
        row.getCell(8).setCellValue(waterStructureArrayList.getDataOpl());
        row.getCell(9).setCellValue(waterStructureArrayList.getStageOpl());
    }

    public void reportRateCost(ActionEvent event) throws IOException {

        System.out.println(rateAndCost.getRateConsumptionCold());
        System.out.println(rateAndCost.getRateConsumptionHot());
        System.out.println(rateAndCost.getRateConsumptionColdMSC());
        System.out.println(rateAndCost.getRateConsumptionHotMSC());


        // получаем файл в формате xlsx
        String filePath = "emplRateCost.xls";
        // получаем экземпляр XSSFWorkbook для обработки xlsx файла
        FileInputStream file = new FileInputStream(new File(filePath));

        HSSFWorkbook workbook = new HSSFWorkbook(file);
        // выбираем первый лист для обработки
        // нумерация начинается из 0
        HSSFSheet sheetAt = workbook.getSheetAt(0);

        HSSFRow rowRateConsumptionCold = sheetAt.getRow(3);
        HSSFRow rowRateConsumptionHot = sheetAt.getRow(4);
        HSSFRow rowPowerConsumption = sheetAt.getRow(7);
        HSSFRow rowRateConsumptionColdMSC = sheetAt.getRow(5);
        HSSFRow rowRateConsumptionHotMSC = sheetAt.getRow(6);

        HSSFCell cellRateConsumptionCold = rowRateConsumptionCold.getCell(4);
        HSSFCell cellRateConsumptionHot = rowRateConsumptionHot.getCell(4);
        HSSFCell cellPowerConsumption = rowPowerConsumption.getCell(4);
        HSSFCell cellRateConsumptionColdMSC = rowRateConsumptionColdMSC.getCell(4);
        HSSFCell cellRateConsumptionHotMSC = rowRateConsumptionHotMSC.getCell(4);

        HSSFRow rowPriceColdWater = sheetAt.getRow(9);
        HSSFRow rowPriceHotWaterEnergy = sheetAt.getRow(10);
        HSSFRow rowPriceWaterDisposal = sheetAt.getRow(11);
        HSSFRow rowPriceColdWaterMSC = sheetAt.getRow(12);
        HSSFRow rowPriceHotWaterEnergyMSC = sheetAt.getRow(13);
        HSSFRow rowPriceWaterDisposalMSC = sheetAt.getRow(14);

        HSSFCell cellPriceColdWater = rowPriceColdWater.getCell(4);
        HSSFCell cellPriceHotWaterEnergy = rowPriceHotWaterEnergy.getCell(4);
        HSSFCell cellPriceWaterDisposal = rowPriceWaterDisposal.getCell(4);
        HSSFCell cellPriceColdWaterMSC = rowPriceColdWaterMSC.getCell(4);
        HSSFCell cellPriceHotWaterEnergyMSC = rowPriceHotWaterEnergyMSC.getCell(4);
        HSSFCell cellPriceWaterDisposalMSC = rowPriceWaterDisposalMSC.getCell(4);

        cellRateConsumptionCold.setCellValue(rateAndCost.getRateConsumptionCold());
        cellRateConsumptionHot.setCellValue(rateAndCost.getRateConsumptionHot());
        cellPowerConsumption.setCellValue(rateAndCost.getPowerConsumption());
        cellRateConsumptionColdMSC.setCellValue(rateAndCost.getRateConsumptionColdMSC());
        cellRateConsumptionHotMSC.setCellValue(rateAndCost.getRateConsumptionHotMSC());

        cellPriceColdWater.setCellValue(rateAndCost.getPriceColdWater());
        cellPriceHotWaterEnergy.setCellValue(rateAndCost.getPriceHotWaterEnergy());
        cellPriceWaterDisposal.setCellValue(rateAndCost.getPriceWaterDisposal());
        cellPriceColdWaterMSC.setCellValue(rateAndCost.getPriceColdWaterMSC());
        cellPriceHotWaterEnergyMSC.setCellValue(rateAndCost.getPriceHotWaterEnergyMSC());
        cellPriceWaterDisposalMSC.setCellValue(rateAndCost.getPriceWaterDisposalMSC());



        try (FileOutputStream out = new FileOutputStream(new File("resultOtchet\\emplRateCost"  + ".xls"))) {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Excel sozdan!");
    }

    public void emplObsh(ActionEvent event) throws IOException {

        String[] selectSQLitem=comboBoxArea.getSelectionModel().getSelectedItem().toString().split(",");

        if (selectSQLitem[4].equals("Многоквартирный")) {
            // получаем файл в формате xlsx
            String filePath = "eplObshedomMonthMNOG.xls";
            // получаем экземпляр XSSFWorkbook для обработки xlsx файла
            FileInputStream file = new FileInputStream(new File(filePath));

            HSSFWorkbook workbook = new HSSFWorkbook(file);
            // выбираем первый лист для обработки
            // нумерация начинается из 0
            HSSFSheet sheetAt = workbook.getSheetAt(0);

            HSSFRow rowHouse = sheetAt.getRow(1);
            HSSFRow rowDate = sheetAt.getRow(2);
            HSSFRow rowKVARTIR = sheetAt.getRow(3);
            HSSFRow rowRASHouse = sheetAt.getRow(4);
            HSSFRow rowRaznica = sheetAt.getRow(5);
            HSSFRow rowODN = sheetAt.getRow(6);

            HSSFCell cellHouse = rowHouse.getCell(1);
            HSSFCell cellDate = rowDate.getCell(1);
            HSSFCell cellKVARTIR = rowKVARTIR.getCell(1);
            HSSFCell cellRASHouse = rowRASHouse.getCell(1);
            HSSFCell cellRaznica = rowRaznica.getCell(1);
            HSSFCell cellODN = rowODN.getCell(1);

            cellHouse.setCellValue(comboBoxArea.getSelectionModel().getSelectedItem().toString());
            cellDate.setCellValue(comboBoxDate.getSelectionModel().getSelectedItem().toString());
            cellKVARTIR.setCellValue((potrblColdApartaments + potrblHotApartaments));
            cellRASHouse.setCellValue((potrblColdHouseRateEmpl + potrblHotHouseRateEmpl));
            cellRaznica.setCellValue(differenceReadings);
            cellODN.setCellValue(differenceReadings);

            try (FileOutputStream out = new FileOutputStream(new File("resultOtchet\\eplObshedomMonthMNOG" + ".xls"))) {
                workbook.write(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            // получаем файл в формате xlsx
            String filePath = "eplObshedomMonthKOTTE.xls";
            // получаем экземпляр XSSFWorkbook для обработки xlsx файла
            FileInputStream file = new FileInputStream(new File(filePath));

            HSSFWorkbook workbook = new HSSFWorkbook(file);
            // выбираем первый лист для обработки
            // нумерация начинается из 0
            HSSFSheet sheetAt = workbook.getSheetAt(0);

            HSSFRow rowHouse = sheetAt.getRow(1);
            HSSFRow rowDate = sheetAt.getRow(2);
            HSSFRow rowKVARTIR = sheetAt.getRow(3);
            HSSFRow rowRASHouse = sheetAt.getRow(4);
            HSSFRow rowRaznica = sheetAt.getRow(5);

            HSSFCell cellHouse = rowHouse.getCell(1);
            HSSFCell cellDate = rowDate.getCell(1);
            HSSFCell cellKVARTIR = rowKVARTIR.getCell(1);
            HSSFCell cellRASHouse = rowRASHouse.getCell(1);
            HSSFCell cellRaznica = rowRaznica.getCell(1);

            cellHouse.setCellValue(comboBoxArea.getSelectionModel().getSelectedItem().toString());
            cellDate.setCellValue(comboBoxDate.getSelectionModel().getSelectedItem().toString());
            cellKVARTIR.setCellValue((potrblColdApartaments + potrblHotApartaments));
            cellRASHouse.setCellValue((normCold + normHot));
            if ((potrblColdApartaments == normCold) && (potrblHotApartaments == normHot)) {
                cellRaznica.setCellValue("Потребление воды находится в пределах нормы");
            }
            if ((potrblColdApartaments < normCold) || (potrblHotApartaments < normHot)) {
                cellRaznica.setCellValue("Потребление воды находится ниже нормы");
            }
            if ((potrblColdApartaments > normCold) && (potrblHotApartaments > normHot)) {
                cellRaznica.setCellValue(((potrblColdApartaments-normCold)+(potrblHotApartaments-normHot)));
            }
            try (FileOutputStream out = new FileOutputStream(new File("resultOtchet\\eplObshedomMonthKOTTE" + ".xls"))) {
                workbook.write(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Excel sozdan!");
    }
}
