package ui.utilities;

import backend.Block;
import backend.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Created by Adam Cattermole
 * 24/02/2016
 */
public class UiPointUpInverse extends UiBlock {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;

    public UiPointUpInverse() {
        super(WIDTH, HEIGHT);
    }

    public UiPointUpInverse(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
        block = new Point(Point.Orientation.UP);
    }
    
    public UiPointUpInverse(double x, double y, Block block) {
    	super(x, y, WIDTH, HEIGHT);
    	this.block = block;
    }

    @Override
    protected void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(STROKE_SIZE);
        gc.strokeLine(0, HEIGHT*.25, WIDTH, HEIGHT*.25);
        gc.bezierCurveTo(WIDTH*.25, HEIGHT*.25, WIDTH*.5, HEIGHT*.25, WIDTH*.5, HEIGHT*.5);
        gc.bezierCurveTo(WIDTH*.5, HEIGHT*.5, WIDTH*.5, HEIGHT*.75, WIDTH, HEIGHT*.75);
        gc.stroke();
    }
}