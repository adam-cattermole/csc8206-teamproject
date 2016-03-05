package ui.utilities;

import backend.Block;
import backend.Point;
import javafx.scene.canvas.GraphicsContext;
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
        super(x, y, WIDTH, HEIGHT, new Point(Point.Orientation.UP));
    }
    
    public UiPointUpInverse(double x, double y, Block block) {
    	super(x, y, WIDTH, HEIGHT, block);
    }

    @Override
    /**
     * Initially draw straight line,
     * first bezier curve draws from quarter to midpoint
     * second from midpoint to three quarters
     * line connects to end*/
    protected void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.beginPath();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(STROKE_SIZE);
        gc.strokeLine(0, HEIGHT*.25, WIDTH, HEIGHT*.25);
        gc.bezierCurveTo(WIDTH*.25, HEIGHT*.25, WIDTH*.5, HEIGHT*.25, WIDTH*.5, HEIGHT*.5);
        gc.bezierCurveTo(WIDTH*.5, HEIGHT*.5, WIDTH*.5, HEIGHT*.75, WIDTH*.75, HEIGHT*.75);
        gc.lineTo(WIDTH, HEIGHT*.75);
        gc.stroke();
        gc.closePath();
    }
}