import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.IntNode;

/**
 * Top level abstract class for Blocks, components of a network
 * Allows for abstraction of components in networks
 * @author chris_curry
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="blockType", visible=true)
@JsonSubTypes({

        @JsonSubTypes.Type(value=Section.class, name="Section"),
        @JsonSubTypes.Type(value=Point.class, name="Point")
})
@JsonSerialize(using=Block.Serializer.class)
@JsonDeserialize(using=Block.Deserializer.class)
public abstract class Block
{
	protected final int id;
	protected Block up;
	protected Block down;
	
	protected Set<Block> neighbours;
	//protected Direction direction = Direction.UP;
	
	protected Block(int id)
	{
		this(null, null, id);
	}
	
	protected Block(Block up, Block down, int id)
	{
		this.up = up;
		this.down = down;
		this.id = id;
	}
	
	protected int getID() {
		return id;
	}
	
	protected Block getUp() {
		return up;
	}
	
	protected Block getDown() {
		return down;
	}
	
	/*protected void setDirection(Direction direction)
	{
		this.direction = direction;
	}
	
	protected Direction getDirection()
	{
		return direction;
	}*/
	
	protected void setUp(Block up)
	{
		setUp(up, true);
	}
	
	protected void setDown(Block down)
	{
		setDown(down, true);
	}
	
	protected void setUp(Block up, boolean reverse)
	{
		if (this.up == null)
		{
			System.out.println("Add up " + up + " to " + this);
			
			this.up = up;
			if (reverse)
			{
				up.setDown(this, false);
			}
		}
	}
	
	protected void setDown(Block down, boolean reverse)
	{	
		if (this.down == null)
		{
			System.out.println("Add down " + up + " to " + this);
			
			this.down = down;
			if (reverse)
			{
				down.setUp(this, false);
			}
		}
	}
	
	protected void deleteBlock(Block block)
	{	
		if (down == block)
		{
			down = null;
		}
		
		if (up == block)
		{
			up = null;
		}
	}
	
	protected Set<Block> getNeighbours()
	{
		if (neighbours == null)
		{
			neighbours = new HashSet<Block>(3);
		}
		
		//TODO don't clear and add elements every time, just add elements that were not added yet
		neighbours.clear();
		
		if (up != null)
		{
			neighbours.add(up);
		}
		
		if (down != null)
		{
			neighbours.add(down);
		}
		
		return neighbours;
	}
	
	public String toString()
	{
		return String.valueOf(id);
	}
	
	public int hashCode()
	{
	    final int prime = 31;
	    int result = 1;
	    result = (int) (prime * result + id);
	    result = (int) (prime * result + ((up == null) ? 0 : up.getID()));
	    result = (int) (prime * result + ((down == null) ? 0 : down.getID()));
	    return result;
	}
	
	/**
	 * Custom JSON serializer for Jackson
	 */
	public static class Serializer extends JsonSerializer<Block>
	{
		public void serialize(Block block, JsonGenerator json, SerializerProvider provider)
		throws IOException, JsonProcessingException
		{
			//firstly serialize fields common to all Blocks
			json.writeNumberField("id", block.id);
			//json.writeObjectField("direction", block.direction);
			
			if (block.up != null)
			{
				json.writeObjectFieldStart("up");
				json.writeStringField("blockType", block.up.getClass().getName());
				json.writeNumberField("id", block.up.getID());
				json.writeEndObject();
			} else {
				json.writeObjectField("up", null);
			}
			
			if (block.down != null)
			{
				json.writeObjectFieldStart("down");
				json.writeStringField("blockType", block.down.getClass().getName());
				json.writeNumberField("id", block.down.getID());
				json.writeEndObject();
			} else {
				json.writeObjectField("down", null);
			}
			
			//serialize fields specific to Point if given block is a Point
			if (block instanceof Point) {
				Point p = (Point) block;
				
				if (p.getSideline() != null)
				{
					json.writeObjectFieldStart("sideline");
					json.writeStringField("blockType", p.getSideline().getClass().getName());
					json.writeNumberField("id", p.getSideline().getID());
					json.writeEndObject();
				} else {
					json.writeObjectField("sideline", null);
				}
				
				json.writeObjectField("orientation", p.getOrientation());
				json.writeObjectField("setting", p.getSetting());
			}
			
			//serialize fields specific to Section if given block is a Section
			if (block instanceof Section) {
				Section s = (Section) block;
				
				json.writeObjectField("signalUp", s.getSignalUp());
				json.writeObjectField("signalDown", s.getSignalDown());
			}
		}
		
		public void serializeWithType(Block block, JsonGenerator json, SerializerProvider provider, TypeSerializer typeSerializer) 
		throws IOException, JsonProcessingException
		{
			typeSerializer.writeTypePrefixForObject(block, json);
			serialize(block, json, provider);
			typeSerializer.writeTypeSuffixForObject(block, json);
		}
	}
	
	public static class Deserializer extends JsonDeserializer<Block>
	{
		public Block deserialize(JsonParser parser, DeserializationContext context)
		throws IOException, JsonProcessingException
		{
			JsonNode node = parser.getCodec().readTree(parser);
			
			if (!(node.hasNonNull("blockType") || node.hasNonNull("id")))
			{
				return null;
			}
			
			//create a block
			Block block = BlockFactory.getBlock(
					node.get("blockType").asText(),
					(Integer) ((IntNode) node.get("id")).numberValue());
			
			// Extract the up block
			if (node.hasNonNull("up"))
			{
				JsonNode up = node.get("up");
				Block upBlock = BlockFactory.getBlock(
						up.get("blockType").asText(),
						(Integer) ((IntNode) up.get("id")).numberValue());
				
				block.setUp(upBlock, upBlock instanceof Section);
			}
			
			// Extract the down block
			if (node.hasNonNull("down"))
			{
				JsonNode down = node.get("down");
				Block downBlock = BlockFactory.getBlock(
						down.get("blockType").asText(),
						(Integer) ((IntNode) down.get("id")).numberValue());
				
				block.setDown(downBlock, downBlock instanceof Section);
			}

			if (block instanceof Section)
			{
				Signal signalUp = null;
				Signal signalDown = null;
				
				if (node.hasNonNull("signalUp"))
				{
					JsonNode signalNode = node.get("signalUp");
					signalUp = new Signal((Integer) ((IntNode) signalNode.get("id")).numberValue());
					signalUp.setDirection(Direction.valueOf(signalNode.get("direction").asText()));
				}
				
				if (node.hasNonNull("signalDown"))
				{
					JsonNode signalNode = node.get("signalDown");
					signalDown = new Signal((Integer) ((IntNode) signalNode.get("id")).numberValue());
					signalDown.setDirection(Direction.valueOf(signalNode.get("direction").asText()));
				}
				
				((Section) block).setSignalUp(signalUp).setSignalDown(signalDown);
			} else if (block instanceof Point) {
				Point p = (Point) block;
				
				// Extract the sideline block
				if (node.hasNonNull("sideline"))
				{
					JsonNode sideline = node.get("sideline");
					Block sidelineBlock = BlockFactory.getBlock(
							sideline.get("blockType").asText(),
							(Integer) ((IntNode) sideline.get("id")).numberValue());
					p.setSideline(sidelineBlock);
				}

				//extract orientation
				p.setOrientation(Point.Orientation.valueOf(node.get("orientation").asText()));
				
				//extract setting
				p.setSetting(Point.Setting.valueOf(node.get("setting").asText()));
			}

			return block;
		}
	}
}
