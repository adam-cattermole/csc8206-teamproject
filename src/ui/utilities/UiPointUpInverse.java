package ui.utilities;

import javafx.scene.image.Image;

/**
 * Created by Adam Cattermole
 * 24/02/2016
 */
public class UiPointUpInverse extends UiBlock {

    private static Image image = new Image("ui/resources/point_up_inverse.png");

    public UiPointUpInverse() {
        super(image);
    }

    public UiPointUpInverse(double x, double y) {
        super(x, y, image);
    }
}