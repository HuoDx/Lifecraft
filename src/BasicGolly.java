import java.awt.*;
public class BasicGolly implements java.io.Serializable{
	private boolean alive;
	private boolean harmful;
	private boolean frozen;
	private int SIZE;
	public boolean isAlive()
	{
		return alive;
	}
	public boolean isFrozen()
	{
		return frozen;
	}
	public void setFrozen(boolean tmp)
	{
		this.frozen = tmp;
	}
	public void setAlive(boolean status) {
		// TODO Auto-generated method stub
		this.alive = status;
	}
	public void drawItself(Graphics2D g2d,Point position)
	{
		if(this.isAlive())
		{
			//System.out.println("en," + position);
			if(isHarmful())
				g2d.setColor(new Color(15,15,15));	
			else 
				g2d.setColor(new Color(255,255,153));	
			g2d.fillRect(position.x, position.y, SIZE, SIZE);
		}
	}
	BasicGolly(int SIZE)
	{
		alive = false;
		frozen = false;
		this.SIZE = SIZE;
		harmful = true;
	}
	public boolean isHarmful() {
		// TODO Auto-generated method stub
		return harmful;
	}
	public void setHarmful(boolean val) {
		// TODO Auto-generated method stub
		this.harmful = val;
	}
}
