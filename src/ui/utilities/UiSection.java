package ui.utilities;

import backend.Block;
import backend.Section;
import backend.Signal;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * Created by Adam Cattermole
 * 24/02/2016
 */

public class UiSection extends UiBlock {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

    public UiSection() {
        super(WIDTH, HEIGHT);
    }

    public UiSection(double x, double y) {
        super(x, y, WIDTH, HEIGHT, new Section());
    }
    
    public UiSection(double x, double y, Block block) {
    	super(x, y, WIDTH, HEIGHT, block);
    }

    @Override
    protected void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.beginPath();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(STROKE_SIZE);
        gc.strokeLine(0, HEIGHT*.5, WIDTH, HEIGHT*.5);
        gc.closePath();
        if (block != null) {
            gc.setLineWidth(0.5);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            Section section = (Section) block;
            Signal up = section.getSignalUp();
            Signal down = section.getSignalDown();
            if (up != null) {
                gc.strokeText("s"+up.getID(), WIDTH*.25, HEIGHT*0.75);
            }
            if (down != null) {
                gc.strokeText("s"+down.getID(), WIDTH*.75, HEIGHT*0.20);
            }
        }
    }
}
