package ui.utilities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Adam Cattermole
 * 24/02/2016
 */

public abstract class Block extends ImageView {

    public Block(Image image) {
        super(image);
    }

    public Block(double x, double y, Image image) {
        super(image);
        setX(x);
        setY(y);
    }

}
