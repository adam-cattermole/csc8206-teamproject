package ui.utilities;

import javafx.scene.image.Image;

/**
 * Created by Adam Cattermole
 * 24/02/2016
 */

public class PointForward extends Block {

    private static Image image = new Image("ui/resources/point_forward.png");

    public PointForward() {
        super(image);
    }

    public PointForward(double x, double y) {
        super(x, y, image);
    }
}
