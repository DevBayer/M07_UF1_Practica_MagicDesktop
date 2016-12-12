package app;

import app.api.ApiController;
import app.api.Card;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import javafx.scene.image.Image;
import org.json.JSONException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Main extends Application {
    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private Stage mainStage;
    private ArrayList<Card> cardList;

    @Override
    public void start(Stage primaryStage) throws Exception{

        final Task<ObservableList<String>> requestAPITask = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws InterruptedException {
                updateMessage("Conectando..");
                ObservableList<String> cardsLoad =
                        FXCollections.<String>observableArrayList();
                ArrayList<Card> cards = ApiController.GetAllCards(1, 100);
                cardList = cards;
                if(cards == null){
                    updateMessage("No se ha podido conectar con la API");
                    Thread.sleep(2000);
                    System.exit(0);
                }

                for (int i = 0; i < cards.size(); i++) {
                    Thread.sleep(25);
                    updateProgress(i + 1, cards.size());
                    Card card = cards.get(i);
                    //cardsLoad.add(card.getName());
                    cardsLoad.add(""+i);
                    updateMessage(card.getName());
                }
                Thread.sleep(400);

                return cardsLoad;
            }
        };

        showSplash(
                primaryStage,
                requestAPITask,
                () -> showMainStage(requestAPITask.valueProperty())
        );
        new Thread(requestAPITask).start();
    }


    private void showMainStage(
            ReadOnlyObjectProperty<ObservableList<String>> cards
    ) {
        mainStage = new Stage(StageStyle.DECORATED);
        mainStage.setTitle("MagicDesktop");

        AnchorPane grid = new AnchorPane();
        HBox hb = new HBox();
        hb.setPadding(new Insets(0, 10, 10, 10));
        hb.setSpacing(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        ImageView cardImage = new ImageView();
        cardImage.setFitWidth(200);
        cardImage.setFitHeight(300);

        Text cardName = new Text("@CardName");
        cardName.setId("cardName");
        cardName.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Text cardType = new Text("@CardType");
        cardType.setId("cardType");
        cardType.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        grid.getChildren().addAll(hb);


            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("cardlist.fxml"));
                Parent root = (Parent) loader.load();


                ImageView layout = (ImageView) root.lookup("#cardLayout");
                File cardback = new File("cardback.jpg");
                layout.setImage(new Image(new FileInputStream(cardback)));

                ListView cardsView = (ListView) root.lookup("#cardlist");
                cardsView.setCellFactory(param -> new ListCell<String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty) {
                            setText(null);
                            setGraphic(null);
                        }else{
                            Card card = cardList.get( Integer.parseInt(item) );
                            setText( card.getName() );

                            Image imagen = new Image(cardList.get( Integer.parseInt(item) ).getImageUrl(),50,80,true,false,true);
                            setGraphic( new ImageView(imagen) );

                            setHeight(80);

                        }
                    }
                });

                javafx.scene.control.Button btnRefresh = (javafx.scene.control.Button) root.lookup("#refresh");

                btnRefresh.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        String colors = "";
                        String rarities = "";

                        Group groupColors = (Group) root.lookup("#groupColors");

                        for (int i = 0; i < groupColors.getChildren().size(); i++) {
                            if(groupColors.getChildren().get(i).getClass().equals(CheckBox.class)) {
                                CheckBox checkb = (CheckBox) groupColors.getChildren().get(i);
                                if (checkb.isSelected()) {
                                    colors += checkb.getText()  + ",";
                                }
                            }
                        }

                        Group groupRarities = (Group) root.lookup("#groupRarities");

                        for (int i = 0; i < groupRarities.getChildren().size(); i++) {
                            if(groupRarities.getChildren().get(i).getClass().equals(CheckBox.class)){
                                CheckBox checkb = (CheckBox) groupRarities.getChildren().get(i);
                                if (checkb.isSelected()) {
                                    rarities += checkb.getText() + ",";
                                }
                            }
                        }
                        if(colors.length() > 0) {
                            colors = colors.substring(0, colors.length() - 1);
                        }
                        if(rarities.length() > 0) {
                            rarities = rarities.substring(0, rarities.length() - 1);
                        }
                        rarities = rarities.replace(" ", "%20");

                        ArrayList<Card> cards = ApiController.GetAllCards(1, 100, colors, rarities);
                        cardList = cards;
                        cardsView.getItems().removeAll();
                        for (int i = 0; i < cards.size(); i++) {
                            cardsView.getItems().add(cards.get(i));
                        }
                    }
                });

                Label _cardName = (Label) root.lookup("#cardName");
                _cardName.setText("");
                Label _cardType = (Label) root.lookup("#cardType");
                _cardType.setText("");
                Label _cardDescription = (Label) root.lookup("#cardDescription");
                _cardDescription.setText("");


                cardsView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        int item = Integer.parseInt(cardsView.getSelectionModel().getSelectedItem().toString());
                        Card card = cardList.get(item);
                        Label cardName = (Label) root.lookup("#cardName");
                        cardName.setText(card.getName());

                        Label cardDescription = (Label) root.lookup("#cardDescription");
                        if (card.getFlavor() == null) {
                            cardDescription.setText("Empty");
                        } else {
                            cardDescription.setText(card.getFlavor());
                        }

                        Label cardType = (Label) root.lookup("#cardType");
                        cardType.setText(card.getType());
                        File cardLayout;
                        try {
                            System.out.println(card.getColors().toString());
                            System.out.println(card.getColors().getString(0));
                            if (card.getColors().getString(0).equals("Blue")) {
                                cardLayout = new File("cardBlue.png");
                                cardName.setTextFill(Color.BLACK);
                            } else if (card.getColors().getString(0).equals("White")) {
                                cardLayout = new File("cardWhite.png");
                                cardName.setTextFill(Color.BLACK);
                            } else if (card.getColors().getString(0).equals("Green")) {
                                cardLayout = new File("cardGreen.png");
                                cardName.setTextFill(Color.BLACK);
                            } else if (card.getColors().getString(0).equals("Black")) {
                                cardLayout = new File("cardBlack.png");
                                cardName.setTextFill(Color.WHITE);
                            } else {
                                cardLayout = new File("cardSpecialRare.png");
                                cardName.setTextFill(Color.BLACK);
                            }
                            layout.setImage(new Image(new FileInputStream(cardLayout)));
                        }catch(FileNotFoundException e){

                        }catch(JSONException e){

                        }catch(NullPointerException e){
                            try {
                                cardLayout = new File("cardSpecialRare.png");
                                layout.setImage(new Image(new FileInputStream(cardLayout)));
                            }catch(FileNotFoundException d){ }
                        }

                        ImageView cardImage = (ImageView) root.lookup("#cardImage");

                        try {
                            File dir = new File("cache");
                            if(!dir.exists()) dir.mkdir();

                            File f = new File("cache/cache_"+card.getId()+".png");
                            if(!f.exists()) {
                                BufferedImage carta;
                                BufferedImage newCard = new BufferedImage(180, 140, BufferedImage.TYPE_INT_ARGB);
                                Graphics g = newCard.createGraphics();
                                carta = ImageIO.read(new URL(card.getImageUrl()));
                                newCard = ((BufferedImage) carta).getSubimage(25, 30, 177, 141);
                                g.drawImage(newCard, 0, 0, null);
                                ImageIO.write(newCard, "png", new File("cache/cache_" + card.getId() + ".png"));
                            }
                            cardImage.setImage(new Image(new FileInputStream(f)));
                        }catch(IOException e){
                            e.printStackTrace();
                        }

                    }
                });

                cardsView.itemsProperty().bind(cards);


                mainStage.setScene(new Scene(root, 800, 600));


                //mainStage.setScene(new Scene(grid));
                mainStage.show();
            }catch(IOException e){

            }
    }

    private void showSplash(
            final Stage initStage,
            Task<?> task,
            InitCompletionHandler initCompletionHandler
    ) {
        progressText.textProperty().bind(task.messageProperty());
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            } // todo add code to gracefully handle other task states.
        });

        Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - 400 / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - 200 / 2);
        initStage.initStyle(StageStyle.TRANSPARENT);
        initStage.setAlwaysOnTop(true);
        initStage.show();
    }

    public interface InitCompletionHandler {
        void complete();
    }

    @Override
    public void init() {
        ImageView splash = new ImageView(
                new Image(new File("logo.png").toURI().toString())
        );
        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(400 - 20);

        progressText = new Label("Conectando con la API...");
        progressText.setTextFill(Color.WHITE);
        splashLayout = new GridPane();
        splashLayout.getStylesheets().add("progress-bar.css");
        splashLayout.getChildren().addAll(splash, loadProgress, progressText);
        GridPane.setValignment(loadProgress, VPos.BOTTOM);
        GridPane.setValignment(progressText,  VPos.BOTTOM );
        GridPane.setHalignment(progressText, HPos.CENTER);
        progressText.setPadding(new Insets(0, 0, 3, 0));
        splashLayout.setStyle(
                "-fx-padding: 5; " +
                        "-fx-background-color: rgba(255, 255, 255, 0.0); " +
                        "-fx-border-width:0px; " +
                        "-fx-border-color: " +
                        "linear-gradient(" +
                        "to bottom, " +
                        "chocolate, " +
                        "derive(chocolate, 50%)" +
                        ");"
        );
        splashLayout.setEffect(new DropShadow());
    }

    public static void main(String[] args) {
        launch(args);
    }
}


