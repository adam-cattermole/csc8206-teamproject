package ui.utilities;

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
    }
}
