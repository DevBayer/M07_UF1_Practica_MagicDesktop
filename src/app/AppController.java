package app;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialize()");
        try {
            Thread.sleep(2);
        }catch(InterruptedException e){
            System.out.println(e.getMessage());
        }

        System.out.println("Fin Initialize()");
    }

}
