package ui.utilities;

import javafx.scene.image.Image;

/**
 * Created by Adam Cattermole
 * 24/02/2016
 */

public class UiSection extends UiBlock {

    private static Image image = new Image("ui/resources/section.png");

    public UiSection() {
        super(image);
    }

    public UiSection(double x, double y) {
        super(x, y, image);
    }
}
