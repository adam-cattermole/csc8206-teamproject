package ui.utilities;

import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Created by Adam Cattermole
 * 24/02/2016
 */

public abstract class UiBlock extends ImageView {

    boolean selected = false;
    
    public UiBlock(Image image) {
        super(image);
        addBlockListeners();
    }

    public UiBlock(double x, double y, Image image) {
        this(image);
        setX(x);
        setY(y);
    }
    
    private void addBlockListeners()
    {
        setOnMouseClicked(new EventHandler <MouseEvent>() 
        {
            public void handle(MouseEvent event) 
            {
                System.out.println("onMouseClicked");
                
                if (selected)
                {
                    setEffect(null);
                    selected = false;
                }
                else
                {
                    int depth = 70; //Setting the uniform variable for the glow width and height 
                    DropShadow borderGlow= new DropShadow();
                    borderGlow.setOffsetY(0f);
                    borderGlow.setOffsetX(0f);
                    borderGlow.setColor(Color.RED);
                    borderGlow.setWidth(depth);
                    borderGlow.setHeight(depth);
                    setEffect(borderGlow); //Apply the effect
                    
                    selected = true;
                }
                
                event.consume();
            }
        });
    }
    public boolean isSelected()
    {
        return selected;
    }

}
