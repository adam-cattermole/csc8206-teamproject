package ui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private Group grid;
    @FXML private GridPane palette;

    @FXML private ImageView section;
    @FXML private ImageView pointForward;
    @FXML private ImageView pointBackward;

    @FXML private ScrollPane scrollPane;

    public final int GRID_HEIGHT = 50;
    public final int GRID_WIDTH = 50;
    public final int CELL_SIZE = 30;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Rectangle[][] rectangles = new Rectangle[GRID_WIDTH][GRID_HEIGHT];
        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_HEIGHT; j++) {
                Rectangle r1 = new Rectangle(i*CELL_SIZE, j*CELL_SIZE, CELL_SIZE, CELL_SIZE);
                r1.setStroke(Color.DARKBLUE);
                r1.setStrokeType(StrokeType.INSIDE);
                r1.setStrokeWidth(0.5);
                r1.setFill(Color.LIGHTGRAY);
                rectangles[i][j] = r1;
                grid.getChildren().add(r1);
                addGridListener(r1);
            }
        }
        System.out.println(grid.getChildren().size());
        section.setId("section");
        pointForward.setId("pointForward");
        pointBackward.setId("pointBackward");
        palette.setPrefSize(60, 60);
        addPaletteListener(section);
        addPaletteListener(pointForward);
        addPaletteListener(pointBackward);
        scrollPane.setMaxSize(750, 750);
    }

    private void addGridListener(final Rectangle target) {
        target.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                System.out.println("onDragOver");

                /* accept it only if it is  not dragged from the same node
                 * and if it has a string data */
                if (event.getGestureSource() != target &&
                        event.getDragboard().hasString()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }

                event.consume();
            }
        });

        target.setOnDragEntered(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture entered the target */
                System.out.println("onDragEntered");
                /* show to the user that it is an actual gesture target */
                if (event.getGestureSource() != target &&
                        event.getDragboard().hasString()) {
                    target.setFill(Color.GREEN);
                }

                event.consume();
            }
        });

        target.setOnDragExited(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away, remove the graphical cues */
                target.setFill(Color.LIGHTGRAY);

                event.consume();
            }
        });

        target.setOnDragDropped(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                System.out.println("onDragDropped");
                /* if there is a string data on dragboard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    /* Do something with this imageview */
                    Image img = new Image(db.getString());
                    ImageView imgView = new ImageView(img);
                    imgView.setX(target.getX());
                    imgView.setY(target.getY());
                    grid.getChildren().add(imgView);

                    success = true;
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();
            }
        });
    }

    private void addPaletteListener(final ImageView source) {
        source.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                /* drag was detected, start drag-and-drop gesture*/
                System.out.println("onDragDetected");

                /* allow any transfer mode */
                Dragboard db = source.startDragAndDrop(TransferMode.ANY);
                /* put a string on dragboard */
                ClipboardContent content = new ClipboardContent();
                String url;
                switch(source.getId()) {
                    case "section":
                        url = "ui/resources/section.png";
                        break;
                    case "pointForward":
                        url = "ui/resources/point_forward.png";
                        break;
                    case "pointBackward":
                        url = "ui/resources/point_backward.png";
                        break;
                    default:
                        url = "";
                }
                System.out.println(url);
                content.putString(url);
                db.setContent(content);

                event.consume();
            }
        });

        source.setOnDragDone(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture ended */
                System.out.println("onDragDone");
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.MOVE) {
//                    source.setText("");
                }

                event.consume();
            }
        });
    }
}
