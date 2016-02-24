package ui.utilities;

import javafx.scene.image.Image;

/**
 * Created by Adam Cattermole
 * 24/02/2016
 */

public class Section extends Block {

    private static Image image = new Image("ui/resources/section.png");

    public Section() {
        super(image);
    }

    public Section(double x, double y) {
        super(x, y, image);
    }
}
