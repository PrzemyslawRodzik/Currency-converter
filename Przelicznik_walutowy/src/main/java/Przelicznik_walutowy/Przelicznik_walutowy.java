package Przelicznik_walutowy;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Pszemek
 */
public class Przelicznik_walutowy extends Application {




    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root =  FXMLLoader.load(this.getClass().getResource("/MainF.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setMinHeight(589);
        primaryStage.setMaxHeight(589);
        primaryStage.setMinWidth(1294);
        primaryStage.setMaxWidth(1294);



        primaryStage.show();

    }
}
