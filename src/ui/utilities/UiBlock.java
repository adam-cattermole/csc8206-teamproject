package ui.utilities;

import backend.Block;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;

/**
 * Created by Adam Cattermole
 * 24/02/2016
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="uiBlockType", visible=true)
@JsonSubTypes({

        @JsonSubTypes.Type(value=UiSection.class, name="UiSection"),
        @JsonSubTypes.Type(value=UiPointUp.class, name="UiPointUp"),
        @JsonSubTypes.Type(value=UiPointUpInverse.class, name="UiPointUpInverse"),
        @JsonSubTypes.Type(value=UiPointDown.class, name="UiPointDown"),
        @JsonSubTypes.Type(value=UiPointDownInverse.class, name="UiPointDownInverse"),
})
@JsonSerialize(using=UiBlock.Serializer.class)
@JsonDeserialize(using=UiBlock.Deserializer.class)
public abstract class UiBlock extends Canvas {

    @JsonIgnore protected static final int STROKE_SIZE = 2;
    @JsonIgnore private boolean selected = false;
    protected Block block;

    abstract void draw();
    
    public UiBlock(double width, double height) {
        super(width, height);
        addBlockListeners();
        draw();
    }

    public UiBlock(double x, double y, double width, double height) {
        this(width, height);
        setLayoutX(x);
        setLayoutY(y);
    }
    
    private void addBlockListeners()
    {
        setOnMouseClicked(new EventHandler <MouseEvent>() 
        {
            public void handle(MouseEvent event) 
            {
                System.out.println("onMouseClicked");
                
                if (selected)
                {
                    setEffect(null);
                    selected = false;
                }
                else
                {
                    int depth = 70; //Setting the uniform variable for the glow width and height 
                    DropShadow borderGlow= new DropShadow();
                    borderGlow.setOffsetY(0f);
                    borderGlow.setOffsetX(0f);
                    borderGlow.setColor(Color.RED);
                    borderGlow.setWidth(depth);
                    borderGlow.setHeight(depth);
                    setEffect(borderGlow); //Apply the effect
                    
                    selected = true;
                }
                
                event.consume();
            }
        });
    }
    
    public boolean isSelected()
    {
        return selected;
    }
    
    public String toString() {
    	return (getClass().getSimpleName() + "[" + getLayoutX() + "," + getLayoutY() + "]: " + block);
    }
    
	/**
	 * Custom JSON serializer for Jackson
	 */
	public static class Serializer extends JsonSerializer<UiBlock>
	{
		public void serialize(UiBlock uiBlock, JsonGenerator json, SerializerProvider provider)
		throws IOException, JsonProcessingException
		{
			json.writeNumberField("x", uiBlock.getLayoutX());
			json.writeNumberField("y", uiBlock.getLayoutY());
			json.writeObjectField("block", uiBlock.block);
		}
		
		public void serializeWithType(UiBlock block, JsonGenerator json, SerializerProvider provider, TypeSerializer typeSerializer) 
		throws IOException, JsonProcessingException
		{
			typeSerializer.writeTypePrefixForObject(block, json);
			serialize(block, json, provider);
			typeSerializer.writeTypeSuffixForObject(block, json);
		}
	}
	
	public static class Deserializer extends JsonDeserializer<UiBlock>
	{
		public UiBlock deserialize(JsonParser parser, DeserializationContext context)
		throws IOException, JsonProcessingException
		{
			ObjectMapper mapper = (ObjectMapper) parser.getCodec();
			JsonNode node = mapper.readTree(parser);
			
			//invalid node
			if (!(node.hasNonNull("uiBlockType") || node.hasNonNull("x") || node.hasNonNull("y") || node.hasNonNull("block")))
			{
				return null;
			}
			
			String uiBlockType = node.get("uiBlockType").asText();
			double x = node.get("x").asDouble();
			double y = node.get("y").asDouble();
			Block block = mapper.readValue(node.get("block").toString(), Block.class);
			
			return UiBlockFactory.getUiBlock(uiBlockType, block, x, y);
		}
	}
}
