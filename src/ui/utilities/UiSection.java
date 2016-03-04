package ui.utilities;

import backend.Block;
import backend.Section;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
        super(x, y, WIDTH, HEIGHT);
        setBlock(new Section());
    }
    
    public UiSection(double x, double y, Block block) {
    	super(x, y, WIDTH, HEIGHT);
    	setBlock(block);
    }

    @Override
    protected void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.beginPath();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(STROKE_SIZE);
        gc.strokeLine(0, HEIGHT/2, WIDTH, HEIGHT/2);
        gc.closePath();
    }
}
