package ui.utilities;

import javafx.scene.image.Image;

/**
 * Created by Adam Cattermole
 * 24/02/2016
 */
public class PointBackwardU extends Block {

    private static Image image = new Image("ui/resources/point_backward_u.png");

    public PointBackwardU() {
        super(image);
    }

    public PointBackwardU(double x, double y) {
        super(x, y, image);
    }
}
