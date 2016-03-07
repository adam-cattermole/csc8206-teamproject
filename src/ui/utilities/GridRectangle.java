/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.utilities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * GridRectangle class is used to represent every rectangle on the grid. 
 * 
 * @author Justas Miknys
 */
public class GridRectangle extends Rectangle 
{
    UiBlock block;    
    private GridRectangle[][] rectangles;
    
    /**
     * Constructor of the GridRectangle class
     * @param x                 the x-coordinate of the rectangle position
     * @param y                 the y-coordinate of the rectangle position
     * @param width             specifies the width of rectangle
     * @param height            specifies the height of rectangle
     * @param gridRectangles    a reference to the GridRectangles class which holds all the rectangles
     */
    public GridRectangle(int x, int y, int width, int height, GridRectangles gridRectangles)
    {
       super(x,y,width,height);
       rectangles = gridRectangles.getRectangles();
       setUpStyle();
    }
    
    /**
     * Sets up the 'default' style for a rectangle
     */
    private void setUpStyle()
    {
        setStroke(Color.DARKBLUE);
        setStrokeType(StrokeType.INSIDE);
        setStrokeWidth(0.2);
        setFill(Color.LIGHTGRAY);        
    }
    
    /**
     * Used to highlight a rectangle background either in green or red color depending on the validity of placement
     * @param blockType String which specifies the type of block the rectangle holds
     * @param col       The color that will be used to fill the background
     */
    protected void highlight(String blockType, Color col)
    {
        try
        {
            
            if (Color.GREEN == col)
            {
                if (!isPlacementValid(blockType))
                    col = Color.RED;
            }
            
            
            setFill(col);
            rectangles[((int) getX() / GridRectangles.CELL_SIZE)+1][((int)getY() / GridRectangles.CELL_SIZE)].setFill(col);
            if (!blockType.equals(UiSection.class.getSimpleName()))
            {
                rectangles[((int) getX() / GridRectangles.CELL_SIZE)+1][((int)getY() / GridRectangles.CELL_SIZE)+1].setFill(col);
                rectangles[((int) getX() / GridRectangles.CELL_SIZE)][((int)getY() / GridRectangles.CELL_SIZE)+1].setFill(col);
            }
        }
        catch(Exception e)
        {
            // Ignore array out of bounds exception
        }
    }
    
    /**
     * Checks if the placement that user made is valid, enforces some of the valid network assumptions
     * @param blockType String which specifies the type of block the rectangle holds
     * @return true if the placement is allowed, false if it's not valid
     */
    protected boolean isPlacementValid(String blockType)
    {
        boolean blockUsedFlag = false;
        if (block != null || rectangles[((int) getX() / GridRectangles.CELL_SIZE)+1][((int)getY() / GridRectangles.CELL_SIZE)].isUsed())
            blockUsedFlag = true;            
        if ((!blockType.equals(UiSection.class.getSimpleName())) && (!blockUsedFlag) && (rectangles[((int) getX() / GridRectangles.CELL_SIZE)+1][((int)getY() / GridRectangles.CELL_SIZE)+1].isUsed() || rectangles[((int) getX() / GridRectangles.CELL_SIZE)][((int)getY() / GridRectangles.CELL_SIZE)+1].isUsed()))
            blockUsedFlag = true;
        
        // If point is placed, make sure there are no other points to left or right
        if (!blockType.equals(UiSection.class.getSimpleName()) && !blockUsedFlag)
        {
            blockUsedFlag = havePointNeighbours();
        }

        return !blockUsedFlag;
    }
    
    /**
     * Checks if a GridRectangle object has any neighbours which are of type Point
     * @return true if there are adjacent neighbours of type Point
     */
    private boolean havePointNeighbours()
    {
        boolean havePointNeighbour = false;
        UiBlock rightNeighbour1 = rectangles[((int) getX() / GridRectangles.CELL_SIZE)+2][((int)getY() / GridRectangles.CELL_SIZE)].getUiBlock();
        UiBlock rightNeighbour2 = rectangles[((int) getX() / GridRectangles.CELL_SIZE)+2][((int)getY() / GridRectangles.CELL_SIZE)+1].getUiBlock();
        
        UiBlock leftNeighbour1 = rectangles[((int) getX() / GridRectangles.CELL_SIZE)-1][((int)getY() / GridRectangles.CELL_SIZE)].getUiBlock();
        UiBlock leftNeighbour2 = rectangles[((int) getX() / GridRectangles.CELL_SIZE)-1][((int)getY() / GridRectangles.CELL_SIZE)+1].getUiBlock();
        
        if (rightNeighbour1 != null)
        {
            if (rightNeighbour1.getClass().getSimpleName().contains("Point"))
            {
                havePointNeighbour = true;
            }
        }
        if (rightNeighbour2 != null)
        {
            if (rightNeighbour2.getClass().getSimpleName().contains("Point"))
            {
                havePointNeighbour = true;
            }
        }
        
        if (leftNeighbour1 != null)
        {
            if (leftNeighbour1.getClass().getSimpleName().contains("Point"))
            {
                havePointNeighbour = true;
            }
        }
        if (leftNeighbour2 != null)
        {
            if (leftNeighbour2.getClass().getSimpleName().contains("Point"))
            {
                havePointNeighbour = true;
            }
        }
        return havePointNeighbour;
    }
    
    /**
     * Connect GridRectangle with a UiBlock object which is going to be displayed on this rectangle
     * @param b reference to the object which is displayed on a rectangle
     */
    private void setUiBlock(UiBlock b)
    {
        this.block = b;
    }
    
    /**
     * Used to get the block which is displayed on the rectangle
     * @return return the block which is displayed on the rectangle
     */
    public UiBlock getUiBlock()
    {
        return this.block;
    }
    
    /**
     * Checks if the rectangle is used to display a Block
     * @return true if a block is drawn on top of rectangle
     */
    public boolean isUsed()
    {
        return block != null;
    }
    
    /**
     * Method used to set the variables of all the rectangles which will be involved in a placement, and make sure all of them reference to the same object
     * @param blockType Type of the block that is going to be placed
     * @param block     Reference to the black which will be placed
     */
    protected void prepareForPlacement(String blockType, UiBlock block)
    {
        this.block = block;
        rectangles[((int) getX() / GridRectangles.CELL_SIZE)+1][((int)getY() / GridRectangles.CELL_SIZE)].setUiBlock(block);
        
        if (!blockType.equals(UiSection.class.getSimpleName()))
        {
            rectangles[((int) getX() / GridRectangles.CELL_SIZE)+1][((int)getY() / GridRectangles.CELL_SIZE)+1].setUiBlock(block);
            rectangles[((int) getX() / GridRectangles.CELL_SIZE)][((int)getY() / GridRectangles.CELL_SIZE)+1].setUiBlock(block);
        }
    }
    
    /**
     * Used when a block is deleted from the grid, to update all the rectangles so they don't hold references to deleted object
     * @param blockType Type of the block that is going to be deleted
     */
    public void freeUpSpace(String blockType)
    {
        this.block = null;
        rectangles[((int) getX() / GridRectangles.CELL_SIZE)+1][((int)getY() / GridRectangles.CELL_SIZE)].setUiBlock(null);
        
        if (!blockType.equals(UiSection.class.getSimpleName()))
        {
            rectangles[((int) getX() / GridRectangles.CELL_SIZE)+1][((int)getY() / GridRectangles.CELL_SIZE)+1].setUiBlock(null);
            rectangles[((int) getX() / GridRectangles.CELL_SIZE)][((int)getY() / GridRectangles.CELL_SIZE)+1].setUiBlock(null);
        }        
    }
}
