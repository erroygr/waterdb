package registratiocPackege;

import MSSQL.MSSQLConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import object.HomeClass;
import sample.AuthenticationController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static sample.AuthenticationController.stageAuthen;


public class RegistrationMenuController {
    public ComboBox comboBoxHome;
    public TextField fioUser;
    public TextField areaChart;
    public TextField valRegistr;
    public TextField loginUser;
    public TextField passwordUser;
    public TextField valColdWater;
    public TextField dateWater;
    public TextField valHotWater;

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    public void init() throws SQLException{
        fillComboBox();
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
            comboBoxHome.getItems().add(homeClass.toString());
        }
    }


    public void getRegstr(ActionEvent event) throws SQLException {
        Statement statement = connection.createStatement();


        if (comboBoxHome.getValue() != null) {
            String[] selectSQLitem=comboBoxHome.getSelectionModel().getSelectedItem().toString().split(",");
    String sqlInsertUser = "INSERT Home_apartment(id_homes,AreaApartments, FIOresident, kolRegisterd,loginn,passwrd) " +
            "VALUES(" + selectSQLitem[0] + ", " +
            "" + areaChart.getText() + ", " +
            "'" + fioUser.getText() + "', " +
            "" + valRegistr.getText() + ", " +
            "'" + loginUser.getText() + "', " +
            "'" + passwordUser.getText() + "')";

    int rows = statement.executeUpdate(sqlInsertUser);
    System.out.printf("Добавлена %d строка в Home_apartment", rows);

    String idUserApart = idUserHome(loginUser.getText());


    String sqlInsertDateWater = "INSERT Data_Water(id_apartaments,kolCOLD,kolHOT,Data_vnoski,statusPlatezha) " +
            "VALUES(" + idUserApart + ", " +
            "" + valColdWater.getText() + ", " +
            "" + valHotWater.getText() + ", " +
            "'" + dateWater.getText() + "', " +
            "'Оплачено')";

    int rowsDateWater = statement.executeUpdate(sqlInsertDateWater);
    System.out.printf("Добавлена %d строка в Data_Water", rowsDateWater);

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("ВНИМАНИЕ");
    alert.setHeaderText(null);
    alert.setContentText("Регистрация прошла успешно. Теперь Вы можете зайти в свой личный кабинет!");
    alert.showAndWait();

    getExit();

        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ВНИМАНИЕ");
            alert.setHeaderText(null);
            alert.setContentText("Регистрация не завершена! Проверьте введенные данные.");
            alert.showAndWait();
        }
    }


    private String idUserHome(String loginUser) throws SQLException {
        Statement statement = connection.createStatement();
        String idUser = "";
        String sqlSelectUserId = "SELECT id from Home_apartment " +
                "WHERE loginn='"+loginUser+"'";

        ResultSet resultIdUser = statement.executeQuery(sqlSelectUserId);

        while (resultIdUser.next()) {
            idUser=resultIdUser.getString(1);
        }

        System.out.println("Код юзера "+ idUser);
        return idUser;
    }





    public void getExit() {
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

        AuthenticationController.stageRegister.close();

    }

}
