package backend;

public class Signal
{
	enum Setting
	{
		CLEAR, STOP
	}
	
	private int id = 0;
	private Setting setting = Setting.STOP;
	
	public Signal() {}
	
	public Signal(int id)
	{
		this.id = id;
		Identifiers.setMaxSignalID(id);
	}
	
	public int getID()
	{
		//lazily get ID
		if (id == 0)
		{
			this.id = Identifiers.getSignalID();
		}
		
		return id; 
	}
	
	public void setSetting(Setting setting)
	{
		this.setting = setting;
	}
	
	public Setting getSetting()
	{
		return setting;
	}
	
	public String toString()
	{
		return "s" + getID() + "-" + setting.toString();
	}
}
