package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
// https://gist.github.com/jewelsea/1588531
    private Stage primaryStage; // **Declare static Stage**

    private void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        setPrimaryStage(primaryStage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = (Parent) loader.load();
        root.setStyle("-fx-background-color: rgba(255, 255, 255, 0.0);");
        primaryStage.setTitle("Magic The Gathering - Desktop Edition");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setAlwaysOnTop(true);
        final Scene scene = new Scene(root, 610, 210);
        scene.setFill(null);
        primaryStage.setScene(scene);
        primaryStage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);

        loadMain();
    }

    public void loadMain() throws IOException, InterruptedException{
        Thread.sleep(3000);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root, 800, 600);
        Stage stage = new Stage();
        stage.setTitle("App");
        stage.setScene(scene);
        primaryStage.close();
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
