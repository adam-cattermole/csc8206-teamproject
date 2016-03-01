package ui.utilities;

import backend.Block;
import backend.Point;
import javafx.scene.image.Image;

/**
 * @author Adam Cattermole
 * 24/02/2016
 */

public class UiPointDown extends UiBlock {
    private static Image image = new Image("ui/resources/point_down.png");

    public UiPointDown() {
        super(image);
    }

    public UiPointDown(double x, double y) {
        super(x, y, image);
        block = new Point(Point.Orientation.DOWN);
    }
    
    public UiPointDown(double x, double y, Block block) {
    	super(x, y, image);
    	this.block = block;
    }
}
