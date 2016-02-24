package ui.utilities;

import javafx.scene.image.Image;

/**
 * Created by Adam Cattermole
 * 24/02/2016
 */
public class PointForwardU extends Block {

    private static Image image = new Image("ui/resources/point_forward_u.png");

    public PointForwardU() {
        super(image);
    }

    public PointForwardU(double x, double y) {
        super(x, y, image);
    }
}