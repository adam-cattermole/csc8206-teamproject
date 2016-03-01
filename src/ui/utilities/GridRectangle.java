/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.utilities;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import ui.Controller;

/**
 *
 * @author b0428165
 */
public class GridRectangle extends Rectangle 
{
    boolean used = false;
    UiBlock block;
    
    Group grid;
    GridRectangle[][] rectangles;
    private Controller controller;

    
    public GridRectangle(int x, int y, int width, int height, Controller controller)
    {
       super(x,y,width,height);
       
       this.controller = controller;
       grid = controller.getGrid();
       rectangles = controller.getRectangles();
       
       addGridListener();
       setUpStyle();
    }
    
    private void setUpStyle()
    {
        setStroke(Color.DARKBLUE);
        setStrokeType(StrokeType.INSIDE);
        setStrokeWidth(0.2);
        setFill(Color.LIGHTGRAY);        
    }
    
    private void addGridListener() 
    {
        setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                //System.out.println("onDragOver");

                /* accept it only if it is  not dragged from the same node
                 * and if it has a string data */
                if (event.getGestureSource() != this &&
                        event.getDragboard().hasString()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }

                event.consume();
            }
        });

        setOnDragEntered(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture entered the target */
                //System.out.println("onDragEntered");
                /* show to the user that it is an actual gesture target */
                if (event.getGestureSource() != this &&
                        event.getDragboard().hasString()) {
                    // Add colors to grid
                    highlight(event.getDragboard().getString(), Color.GREEN);
                }

                event.consume();
            }
        });

        setOnDragExited(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away, remove the graphical cues */
                highlight(event.getDragboard().getString(), Color.LIGHTGRAY);
                event.consume();
            }
        });
        
        setOnDragDropped(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                System.out.println("onDragDropped");
                /* if there is a string data on dragboard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    String type = db.getString();
                    UiBlock UiBlock;
                    
                    if (!isPlacementValid(type))
                    {
                        System.out.println("Invalid placement!");
                    }
                    else
                    {
                        switch(type) 
                        {
                        case "UiSection":
                            UiBlock = new UiSection(getX(), getY());
                            break;
                        case "UiPointUp":
                            UiBlock = new UiPointUp(getX(), getY());
                            break;
                        case "UiPointDown":
                            UiBlock = new UiPointDown(getX(), getY());
                            break;
                        case "UiPointUpInverse":
                            UiBlock = new UiPointUpInverse(getX(), getY());
                            break;
                        case "UiPointDownInverse":
                            UiBlock = new UiPointDownInverse(getX(), getY());
                            break;
                        default:
                            UiBlock = null;
                        }
                        if (UiBlock != null)
                        {
                            prepareForPlacement(type, UiBlock);
                            // Here we handle block with the backend
                        	controller.getUiNetwork().addUiBlock(UiBlock);
                        }
                        success = true;                        
                    }
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();
            }
        });
    }
            
    private void highlight(String blockType, Color col)
    {
        try
        {
            if (Color.GREEN == col)
            {
                if (!isPlacementValid(blockType))
                    col = Color.RED;
            }
            
            setFill(col);
            rectangles[((int) getX() / Controller.CELL_SIZE)+1][((int)getY() / Controller.CELL_SIZE)].setFill(col);
            if (!blockType.equals(UiSection.class.getSimpleName()))
            {
                rectangles[((int) getX() / Controller.CELL_SIZE)+1][((int)getY() / Controller.CELL_SIZE)+1].setFill(col);
                rectangles[((int) getX() / Controller.CELL_SIZE)][((int)getY() / Controller.CELL_SIZE)+1].setFill(col);
            }
        }
        catch(Exception e)
        {
            // Ignore exception
        }
    }
    
    private boolean isPlacementValid(String blockType)
    {
        boolean blockUsedFlag = false;
        if (used || rectangles[((int) getX() / Controller.CELL_SIZE)+1][((int)getY() / Controller.CELL_SIZE)].isUsed())
            blockUsedFlag = true;            
        if ((!blockType.equals(UiSection.class.getSimpleName())) && (!blockUsedFlag) && (rectangles[((int) getX() / Controller.CELL_SIZE)+1][((int)getY() / Controller.CELL_SIZE)+1].isUsed() || rectangles[((int) getX() / Controller.CELL_SIZE)][((int)getY() / Controller.CELL_SIZE)+1].isUsed()))
            blockUsedFlag = true;

        return !blockUsedFlag;
    }
    
    /*
    private void setUsed(boolean isUsed)
    {
        this.used = isUsed;
    }
    */
    private void setUiBlock(UiBlock b)
    {
        this.block = b;
    }
    
    public UiBlock getUiBlock()
    {
        return this.block;
    }
    
    public boolean isUsed()
    {
        if (block == null)
        {
            return false;
        }
        else
        {
            return true;
        }
        //return used;
    }
    
    protected void prepareForPlacement(String blockType, UiBlock block)
    {
        this.block = block;
        rectangles[((int) getX() / Controller.CELL_SIZE)+1][((int)getY() / Controller.CELL_SIZE)].setUiBlock(block);
        
        if (!blockType.equals(UiSection.class.getSimpleName()))
        {
            rectangles[((int) getX() / Controller.CELL_SIZE)+1][((int)getY() / Controller.CELL_SIZE)+1].setUiBlock(block);
            rectangles[((int) getX() / Controller.CELL_SIZE)][((int)getY() / Controller.CELL_SIZE)+1].setUiBlock(block);
        }
        
        /*
        used = true;
        rectangles[((int) getX() / Controller.CELL_SIZE)+1][((int)getY() / Controller.CELL_SIZE)].setUsed(true);
        if (!blockType.equals(UiSection.class.getSimpleName()))
        {
            rectangles[((int) getX() / Controller.CELL_SIZE)+1][((int)getY() / Controller.CELL_SIZE)+1].setUsed(true);
            rectangles[((int) getX() / Controller.CELL_SIZE)][((int)getY() / Controller.CELL_SIZE)+1].setUsed(true);
        }
        */
    }
    
    public void freeUpSpace(String blockType)
    {
        this.block = null;
        rectangles[((int) getX() / Controller.CELL_SIZE)+1][((int)getY() / Controller.CELL_SIZE)].setUiBlock(null);
        
        if (!blockType.equals(UiSection.class.getSimpleName()))
        {
            rectangles[((int) getX() / Controller.CELL_SIZE)+1][((int)getY() / Controller.CELL_SIZE)+1].setUiBlock(null);
            rectangles[((int) getX() / Controller.CELL_SIZE)][((int)getY() / Controller.CELL_SIZE)+1].setUiBlock(null);
        }
        
        /*
        used = false;
        System.out.println(blockType);
        rectangles[((int) getX() / Controller.CELL_SIZE)+1][((int)getY() / Controller.CELL_SIZE)].setUsed(false);
        
        if (!blockType.equals(UiSection.class.getSimpleName()))
        {
            rectangles[((int) getX() / Controller.CELL_SIZE)+1][((int)getY() / Controller.CELL_SIZE)+1].setUsed(false);
            rectangles[((int) getX() / Controller.CELL_SIZE)][((int)getY() / Controller.CELL_SIZE)+1].setUsed(false);
        }
        */
        
    }
}
