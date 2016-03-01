package ui.utilities;

import backend.Block;
import backend.Point;
import javafx.scene.image.Image;

/**
 * Created by Adam Cattermole
 * 24/02/2016
 */

public class UiPointUp extends UiBlock {

    private static Image image = new Image("ui/resources/point_up.png");

    public UiPointUp() {
        super(image);
    }

    public UiPointUp(double x, double y) {
        super(x, y, image);
        block = new Point(Point.Orientation.UP);
    }
    
    public UiPointUp(double x, double y, Block block) {
    	super(x, y, image);
    	this.block = block;
    }
}
