package sample;

import MSSQL.MSSQLConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception{

        MSSQLConnection.getConnection();
        Parent root = FXMLLoader.load(getClass().getResource("authentication.fxml"));
        primaryStage.setTitle("Вход в систему");
        primaryStage.setScene(new Scene(root, 450, 250));
        primaryStage.show();
        stage = primaryStage;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
