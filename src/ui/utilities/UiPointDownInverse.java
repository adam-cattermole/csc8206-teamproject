package ui.utilities;

import javafx.scene.image.Image;

/**
 * Created by Adam Cattermole
 * 24/02/2016
 */
public class UiPointDownInverse extends UiBlock {

    private static Image image = new Image("ui/resources/point_down_inverse.png");

    public UiPointDownInverse() {
        super(image);
    }

    public UiPointDownInverse(double x, double y) {
        super(x, y, image);
    }
}
