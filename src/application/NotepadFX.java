package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class NotepadFX extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/fxml/Main.fxml"));
            
            Scene scene = new Scene(root);

            scene.getStylesheets().add(getClass().getClassLoader().getResource("resources/css/cupertino-dark.css").toExternalForm());
            
            primaryStage.setTitle("NotepadFX");
            primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResource("resources/images/notepadfx_icon.png").toExternalForm()));
            primaryStage.setScene(scene);
            
            primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.toString());

            alert.showAndWait();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}