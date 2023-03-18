import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.Serializable;

public class Item implements Serializable{
	private int ID;
	private int SIZE;
	private transient Image[] imgs = new Image[8];
	public int getID()
	{
		return ID;
	}
	public void setID(int id)
	{
		ID = id;
	}
	Item(int ID,int SIZE)
	{
		this.ID = ID;
		this.SIZE = SIZE;
		loadItem();
	}
	Item(int SIZE)
	{
		this.ID = 0;
		this.SIZE = SIZE;
		loadItem();
	}
	public void loadItem()
	{
		imgs = new Image[9];
		imgs[1] = Toolkit.getDefaultToolkit().getImage("WaterPotion.png");
		imgs[2] = Toolkit.getDefaultToolkit().getImage("Nightvision.png");
		imgs[3] = Toolkit.getDefaultToolkit().getImage("Superball.png");
		imgs[4] = Toolkit.getDefaultToolkit().getImage("Bed.png");
		imgs[5] = Toolkit.getDefaultToolkit().getImage("BirthdayCake.png");
		imgs[6] = Toolkit.getDefaultToolkit().getImage("StaffOfSkulls.png");
		imgs[7] = Toolkit.getDefaultToolkit().getImage("GhostTotem.png");
		imgs[8] = Toolkit.getDefaultToolkit().getImage("portal.png");
	}
	public void drawThis(Graphics2D g2d,Point position, Lifecraft obs)
	{
		g2d.drawImage(imgs[this.ID], position.x,position.y,SIZE,SIZE, obs);
	}
}
