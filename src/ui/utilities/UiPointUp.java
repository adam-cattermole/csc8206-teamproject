package ui.utilities;

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
    }
}
