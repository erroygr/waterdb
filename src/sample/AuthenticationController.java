package sample;

import MSSQL.MSSQLConnection;
import emploerPackege.EmplMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import object.RateAndCost;
import registratiocPackege.RegistrationMenuController;
import usersPackege.UserMenuController;

import java.io.IOException;
import java.sql.*;

public class AuthenticationController {

    public TextField loginTextField;
    public PasswordField passwordText;
    public CheckBox checkEmpl;
    private EmplMenuController emplMenuController;
    private RateAndCost rateAndCost;
    public Connection connection;
    public static String role = "";
    ResultSet resultSet;
    public static Stage stageEmpl;
    public static Stage stageAuthen;
    public static Stage stageUser;
    public static Stage stageRegister;

    public void logInYourPersonalAccount(ActionEvent event) throws SQLException, IOException {
        int checkUser = 0;
        connection = MSSQLConnection.getConnectionDB();
        rateAndCost=MSSQLConnection.getRateAndCost();
        Statement statement = connection.createStatement();
        String sqlAuthentication;
        //String sqlRateAndCost = "SELECT *FROM rateANDcost ";
        if(checkEmpl.isSelected()){
            System.out.println("Режим сотрудник");
            sqlAuthentication ="SELECT * FROM [EmployeeUC] " +
                    "WHERE [loginn]='"+loginTextField.getText()+"' " +
                    "AND [passwrd]='"+passwordText.getText()+"' ";


        } else {
            System.out.println("Режим жилец");
            sqlAuthentication ="SELECT Home_apartment.id, Home_apartment.id_homes, " +
                    "Home_apartment.AreaApartments, Home_apartment.FIOresident, " +
                    "Home_apartment.kolRegisterd, " +
                    "Homes.Gorod, Homes.Streets, Homes.NumberHome FROM Home_apartment " +
                    "INNER JOIN Homes ON (Home_apartment.id_homes = Homes.id) " +
                    "WHERE [loginn]='"+loginTextField.getText()+"' " +
                    "AND [passwrd]='"+passwordText.getText()+"' ";


        }

        System.out.println(rateAndCost.getRateConsumptionCold()+" "+rateAndCost.getRateConsumptionHot()+" "+
                            rateAndCost.getPriceColdWater()+" "+ rateAndCost.getPriceHotWaterEnergy()+" "+
                            rateAndCost.getPriceWaterDisposal());

        resultSet = statement.executeQuery(sqlAuthentication);

        if (resultSet.next()){
            System.out.println("Зашли В систему");
            if(checkEmpl.isSelected()) {

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../emploerPackege/emplMenuController.fxml"));
                    Parent root = loader.load();
                    stageEmpl = new Stage();
                    stageEmpl.setScene(new Scene(root));
                    EmplMenuController emplMenuController = loader.getController();
                    emplMenuController.setConnection(connection);
                    emplMenuController.setRateAndCost(rateAndCost);
                    emplMenuController.init();
                    emplMenuController.fillComboBox();
                    stageEmpl.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                checkUser = 1;
            }else{
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../usersPackege/userMenuController.fxml"));
                    Parent root = loader.load();
                    stageUser = new Stage();
                    stageUser.setScene(new Scene(root));
                    UserMenuController userMenuController = loader.getController();
                    userMenuController.setConnection(connection);
                    userMenuController.setResultSet(resultSet);
                    userMenuController.setRateAndCost(rateAndCost);
                    userMenuController.init();
                    stageUser.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                checkUser = 2;
            }

                   role = resultSet.getString(1) +
                    " " + resultSet.getString(2) +
                    " " + resultSet.getString(3) +
                    " " + resultSet.getString(4);

            System.out.println(role);
            Main.stage.close();
            if(stageAuthen!=null) {
                stageAuthen.close();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ВНИМАНИЕ");
            alert.setHeaderText(null);
            alert.setContentText("Введенные данные не верны. Попробуйте еще раз.");
            alert.showAndWait();


            System.out.println("Аккаунт не найден");
        }

        System.out.println(checkUser);

    }


    public void goToRegistration(ActionEvent event) throws IOException, SQLException {
        connection = MSSQLConnection.getConnectionDB();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../registratiocPackege/registrMenuController.fxml"));
        Parent root = loader.load();
        stageRegister = new Stage();
        stageRegister.setScene(new Scene(root));
        RegistrationMenuController registrationMenuController = loader.getController();
        registrationMenuController.setConnection(connection);
        registrationMenuController.init();
        stageRegister.show();
        Main.stage.close();
        if(stageAuthen!=null) {
            stageAuthen.close();
        }
    }
}


