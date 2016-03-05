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

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

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
    @JsonIgnore protected Paint color = Color.BLACK;
    protected Block block;

    abstract void draw();
    
    public UiBlock(double width, double height) {
        super(width, height);
        draw();
    }

    public UiBlock(double x, double y, double width, double height) {
        super(width, height);
        setLayoutX(x);
        setLayoutY(y);
        draw();
    }
    
    protected void setBlock(Block block) {
    	this.block = block;
    	
        Tooltip t = new Tooltip(this.toString());
        Tooltip.install(this, t);
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
