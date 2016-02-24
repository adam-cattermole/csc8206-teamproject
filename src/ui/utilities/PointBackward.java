package ui.utilities;

import javafx.scene.image.Image;

/**
 * Created by Adam Cattermole
 * 24/02/2016
 */

public class PointBackward extends Block {

    private static Image image = new Image("ui/resources/point_backward.png");

    public PointBackward() {
        super(image);
    }

    public PointBackward(double x, double y) {
        super(x, y, image);
    }
}
