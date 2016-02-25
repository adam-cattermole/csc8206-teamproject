package ui.utilities;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Created by Adam Cattermole
 * 24/02/2016
 */

public abstract class Block extends ImageView {

    boolean selected = false;
    
    public Block(Image image) {
        super(image);
        addBlockListeners();
    }

    public Block(double x, double y, Image image) {
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
                event.consume();
            }
        });
    }

}
