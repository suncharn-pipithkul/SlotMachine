/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slotmachine;

import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Suncharn Pipithkul
 */
public class Reel extends Label
{
    public ArrayList<Image> reel;
    ImageView imgView;
    
    Reel()
    {
        imgView = new ImageView();
        reel = new ArrayList<>();
        reel.add(new Image(getClass().getResourceAsStream("/Images/bell.png")));
        reel.add(new Image(getClass().getResourceAsStream("/Images/cherry.png")));
        reel.add(new Image(getClass().getResourceAsStream("/Images/lemon.png")));
        reel.add(new Image(getClass().getResourceAsStream("/Images/plum.png")));
        reel.add(new Image(getClass().getResourceAsStream("/Images/redseven.png")));
        reel.add(new Image(getClass().getResourceAsStream("/Images/watermelon.png")));
        imgView.setFitHeight(150);
        imgView.setFitWidth(150);
        setGraphic(imgView);
        setStyle("-fx-border-width: 10;" +
                 "-fx-border-color:rgba(0,0,0,0.1);" +
                 "-fx-background-color:white;" +
                 "-fx-padding:30px;" +
                 "-fx-pref-width:60px;" +
                 "-fx-pref-height:60px;" +
                 "-fx-alignment:center;");
    }
    
    public Image get(int index)
    {
        return reel.get(index);
    }
    
    public final void setImage(int index) 
    {
        imgView.setImage(reel.get(index));
    }
}
