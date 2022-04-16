/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slotmachine;

import java.util.Optional;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 *
 * @author Suncharn Pipithkul
 */
public class SlotMachine extends Application
{
    private Stage primaryStage;
    
    private Timeline timeline1;
    private Timeline timeline2;
    private Timeline timeline3;
    private AnimationTimer timer;
    private boolean isStop = true;
    
    private Random rand = new Random();
    private Integer times1 = 400, times2 = 400, times3 = 400;
    private Integer i1 = 0, i2 = 0, i3 = 0;

    private Label lbJackpot = new Label();
    private Label lbBank = new Label();
    private Label lbPocket = new Label();
    private double moneyInBank = 1000.00;
    private double moneyInPocket = 1000.00;
    
    private boolean isCheat = false;
    private boolean isWon = false;
    
    @Override
    public void start(Stage primaryStage)
    {
        this.primaryStage = primaryStage;

        // Load the sound from Audio directory
        Media mediaSong = new Media(getClass().getClassLoader()
                .getResource("Audio/DragonBallZChaLaHeadChaLa(Royalty_Free_Anime_Music).mp3").toString());
        Media mediaCoin = new Media(getClass().getClassLoader().getResource("Audio/coinDrop.mp3").toString());
        Media mediaWon = new Media(getClass().getClassLoader().getResource("Audio/won.mp3").toString());
        Media mediaLost = new Media(getClass().getClassLoader().getResource("Audio/lost.mp3").toString());
        Media mediaSpin = new Media(getClass().getClassLoader().getResource("Audio/spin(cut).mp3").toString());
        
        // Create Media player for each sound
        MediaPlayer playerCoin = new MediaPlayer(mediaCoin);
        MediaPlayer playerWon = new MediaPlayer(mediaWon);
        MediaPlayer playerLost = new MediaPlayer(mediaLost);
        MediaPlayer playerSpin = new MediaPlayer(mediaSpin);
        MediaPlayer playerSong = new MediaPlayer(mediaSong);
        
        // Set the volume for winning sound and the bg song
        playerWon.setVolume(0.8);
        playerSong.setVolume(0.5);
        playerSong.play(); // play the bg song
        
        // Set the sound to play INDEFINITE
        playerWon.setCycleCount(MediaPlayer.INDEFINITE);
        playerSong.setCycleCount(MediaPlayer.INDEFINITE);
        playerSpin.setCycleCount(MediaPlayer.INDEFINITE);
        
        // Set price money
        VBox vbBank = new VBox();
        vbBank.setAlignment(Pos.CENTER);
        StackPane stckBank = new StackPane();
        stckBank.getChildren().add(lbBank);
        vbBank.getChildren().addAll(lbJackpot, lbBank);
        lbJackpot.setText("Jackpot");
        lbJackpot.setFont(Font.font("Bernard MT Condensed", FontWeight.BLACK, FontPosture.REGULAR, 50));
        lbBank.setText("$ " + String.format("%.2f", moneyInBank));
        lbBank.setFont(Font.font("Bernard MT Condensed", FontWeight.BLACK, FontPosture.REGULAR, 100));
        
        // Set pocket money
        lbPocket.setText("Pocket:  $ " + String.format("%.2f", moneyInPocket));
        lbPocket.setFont(Font.font("Bernard MT Condensed", FontWeight.NORMAL, FontPosture.REGULAR, 50));
        
        // Set cheat button
        CheckBox cbCheat = new CheckBox("cheat");
        cbCheat.selectedProperty().addListener(new ChangeListener<Boolean>()
        {
            public void changed(ObservableValue<? extends Boolean> ov,
            Boolean old_val, Boolean new_val) 
            {
                isCheat = !isCheat;
            }
        });
        
        // Create 3 reels
        Reel reel1 = new Reel();
        Reel reel2 = new Reel();
        Reel reel3 = new Reel();
        
        // Get the initial random number to display initial reel images
        i1 = rand.nextInt(reel1.reel.size() - 1);
        i2 = rand.nextInt(reel2.reel.size() - 1);
        i3 = rand.nextInt(reel3.reel.size() - 1);

        // Set the image according to the random numbers
        reel1.setImage(i1);
        reel2.setImage(i2);
        reel3.setImage(i3);
        
        
        // Create a button to spin the 3 reels
        Button btnSpin = new Button("Spin");
        btnSpin.setPrefSize(100, 50);
        btnSpin.setOnAction(e ->
        {
            playerCoin.stop();
            playerWon.stop();
            playerSpin.play();
            isStop = false;
            cbCheat.setDisable(!isStop);
            btnSpin.setDisable(!isStop);
            
            // Randomize the spining times
            times1 = rand.nextInt((400 - 40) + 1 ) + 40;
            times2 = rand.nextInt((400 - 40) + 1) + 40;
            times3 = rand.nextInt((400 - 40) + 1) + 40;
            times2 += times1; // reel 2 spins more times than reel 1
            times3 += times2; // reel 3 spins more times than reel 2

            // Start the animation
            timeline1.play();
            timeline2.play();
            timeline3.play();

            // Start the animation of each frame
            timer.start();
        });
        
        
        
        // Create 3 different timeline for each reel
        timeline1 = new Timeline();
        timeline2 = new Timeline();
        timeline3 = new Timeline();
        timeline1.setCycleCount(times1);
        timeline2.setCycleCount(times2);
        timeline3.setCycleCount(times3);
        
        // Define behavior of each frame
        timer = new AnimationTimer()
        {
            @Override
            public void handle(long l)
            {
                // Count down the times. If it reaches 0, then stop the timeline
                if(times1 > 0)
                    times1--;
                else
                    timeline1.stop();
                
                if(times2 > 0)
                    times2--;
                else
                {
                    if(isCheat)
                    {
                        i2 = i1;
                        reel2.setImage(i2 % 6);
                    }
                    timeline2.stop();
                }
                
                if(times3 > 0)
                    times3--;
                else
                {
                    if(isCheat)
                    {
                        i3 = i2;
                        reel3.setImage(i3 % 6);
                    }
                    timeline3.stop();
                    isStop = true;
                    btnSpin.setDisable(!isStop);
                    cbCheat.setDisable(!isStop);
                    playerSpin.stop();
                    playerCoin.play();
                    
                    // Check for winning
                    if(i1 == i2 && i2 == i3)
                    {
                        if(moneyInBank > 0)
                        {
                            isStop = false;
                            btnSpin.setDisable(!isStop);
                            cbCheat.setDisable(!isStop);
                            playerWon.play();
                            moneyInPocket++;
                            moneyInBank--;
                            lbPocket.setText("Pocket:  $ " + String.format("%.2f", moneyInPocket));
                            lbBank.setText("$ " + String.format("%.2f", moneyInBank));
                        }
                        else
                        {
                            isStop = true;
                            btnSpin.setDisable(!isStop);
                            cbCheat.setDisable(!isStop);
                            playerWon.stop();
                            timer.stop();
                            moneyInBank = 1000.00;
                            lbBank.setText("$ " + String.format("%.2f", moneyInBank));
                        }
                    }
                    else
                    {
                        
                        timer.stop();
                        moneyInPocket--;
                        moneyInBank++;
                        lbPocket.setText("Pocket:  $ " + String.format("%.2f", moneyInPocket));
                        lbBank.setText("$ " + String.format("%.2f", moneyInBank));
                        
                        if(moneyInPocket <= 0)
                        {
                            Alert alLost = new Alert(Alert.AlertType.WARNING);
                            alLost.setTitle("Game Over");
                            alLost.setHeaderText(null);
                            alLost.setContentText("You are out of funds!");
                            playerSong.stop();
                            playerLost.play();
                            alLost.show();
                            btnSpin.setDisable(true);
                            cbCheat.setDisable(true);
                        }
                    }
                }
                
                
            }
        };
        
        Duration duration = Duration.millis(80);
        
        EventHandler onFinished1 = new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                if(i1 >= reel1.reel.size() - 1)
                    i1 = 0;
                else
                    i1++;
                reel1.setImage(i1 % 6);
            }
        };
        
        EventHandler onFinished2 = new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                if(i2 >= reel2.reel.size() - 1)
                    i2 = 0;
                else
                    i2++;
                reel2.setImage(i2 % 6);
            }
        };
        
        EventHandler onFinished3 = new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                if(i3 >= reel3.reel.size() - 1)
                    i3 = 0;
                else
                    i3++;
                reel3.setImage(i3 % 6);
            }
        };
        
        KeyFrame keyFrame1 = new KeyFrame(duration, onFinished1);
        KeyFrame keyFrame2 = new KeyFrame(duration, onFinished2);
        KeyFrame keyFrame3 = new KeyFrame(duration, onFinished3);

        timeline1.getKeyFrames().add(keyFrame1);
        timeline2.getKeyFrames().add(keyFrame2);
        timeline3.getKeyFrames().add(keyFrame3);
        
        // Create a Close button
        Button btnQuit = new Button("Quit");
        btnQuit.setPrefSize(100, 50);
        btnQuit.setOnAction(e -> { handleCloseEvent(); });
        
        // HBox for the reel
        HBox hbReels = new HBox(10);
        hbReels.getChildren().addAll(reel1, reel2, reel3);
        hbReels.setAlignment(Pos.CENTER);
        
        // HBox for Buttons
        HBox hbButtons = new HBox(60);
        hbButtons.setPadding(new Insets(20, 20, 20, 0));
        hbButtons.getChildren().addAll(lbPocket, btnSpin, btnQuit);
        hbButtons.setAlignment(Pos.CENTER);
        
        // VBox for bottom graphic
        VBox vbBottom = new VBox();
        vbBottom.setAlignment(Pos.CENTER);
        vbBottom.getChildren().addAll(hbButtons, cbCheat);

        // Root node
        BorderPane root = new BorderPane(hbReels);
        root.setBottom(vbBottom);
        root.setTop(vbBank);
        
        if(false)
        {
            hbReels.setStyle("-fx-border-color: red;");
            hbButtons.setStyle("-fx-border-color: green;");
        }
        
        // Set background
        BackgroundImage bi = new BackgroundImage(new Image(getClass().getResourceAsStream("/Images/backgroundImage.png")),
        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setBackground(new Background(bi));
        
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Slot Machine");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, e ->
                            {
                                e.consume(); // Consume the WINDOW_CLOSE_REQUEST event
                                handleCloseEvent();
                            });
        
        if(moneyInPocket <= 0)
        {
            btnSpin.setDisable(true);
            Alert alLost = new Alert(Alert.AlertType.WARNING);
            alLost.setTitle("Game Over");
            alLost.setHeaderText(null);
            alLost.setContentText("You are out of funds!");
            playerSong.stop();
            playerLost.play();
            alLost.show();
            btnSpin.setDisable(true);
            cbCheat.setDisable(true);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * A method that display an Alert asking user to confirm exiting request
     */
    public void handleCloseEvent()
    {
        // Alert for exiting the program
        Alert alClosing = new Alert(Alert.AlertType.CONFIRMATION);
        alClosing.setTitle("Are you sure?");
        alClosing.setHeaderText(null);
        alClosing.setContentText("Do you want to close the program?");

        Optional<ButtonType> result = alClosing.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK)
        {
            primaryStage.close();
        }
        else
        {
            alClosing.close();
        }
    }
}
