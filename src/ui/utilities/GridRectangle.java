/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.utilities;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 *
 * @author b0428165
 */
public class GridRectangle extends Rectangle 
{
    //boolean used = false;
    UiBlock block;
    
    private GridRectangle[][] rectangles;
    
    public GridRectangle(int x, int y, int width, int height, GridRectangles gridRectangles)
    {
       super(x,y,width,height);
       rectangles = gridRectangles.getRectangles();
       setUpStyle();
    }
    
    private void setUpStyle()
    {
        setStroke(Color.DARKBLUE);
        setStrokeType(StrokeType.INSIDE);
        setStrokeWidth(0.2);
        setFill(Color.LIGHTGRAY);        
    }
            
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
        return block != null;
    }
    
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
