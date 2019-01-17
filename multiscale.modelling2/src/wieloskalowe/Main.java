package wieloskalowe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("View.fxml"));
        primaryStage.setTitle("mm");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1050, 700));
        primaryStage.show();
    }


}