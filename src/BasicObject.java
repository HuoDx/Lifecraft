
public class BasicObject implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ID;
	private boolean walkable;
	public int getID()
	{
		return this.ID;
	}
	public void setID(int ID)
	{
		this.ID = ID;
		switch(ID)
		{
			case 1: walkable = false;
				break;
			case 5: walkable = false;
				break;
			case 6: walkable = false;
				break;
			case 7: walkable = false;
				break;
			case 9: walkable = false;
				break;
			default: walkable = true;
				break;
		}
	}
	public boolean isWalkable()
	{
		return walkable;
	}
	public BasicObject()
	{
		this.ID = 0;
		this.walkable = true;
	}
	public BasicObject(int id)
	{
		this.setID(id);
	}
	public String toString()
	{
		return ""+ID;
	}
}
