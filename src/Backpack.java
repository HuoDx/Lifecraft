import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

public class Backpack implements Serializable{
	private Item[] deck;
	private int SIZE;
	public int picked;
	public transient final int MAX_BACKPACK_SIZE = 8;
	public void Pick(int index)
	{
		picked = index;
	}
	Backpack(int SIZE)
	{
		this.SIZE = SIZE;
		deck = new Item[MAX_BACKPACK_SIZE];
		for(int i = 0;i < MAX_BACKPACK_SIZE;i++)
			deck[i] = new Item(SIZE);
		picked = 0;
	}
	public Item ItemAt(int index)
	{
		if(index < 0 || index >= MAX_BACKPACK_SIZE)
			return null;
		return deck[index];
	}
	public void AutoInsert(int ID)
	{
		int len = deck.length;
		for(int i = 0;i < len;i++)
			if(deck[i].getID() == 0)
			{
				deck[i].setID(ID);
				break;
			}
	}
	public void loadAllItems()
	{
		for(int i = 0;i < deck.length;i++)
			deck[i].loadItem();
	}
	public void SetItem(int ID,int index)
	{
		if(index < 0 || index >= MAX_BACKPACK_SIZE)
			return ;
		deck[index].setID(ID);
	}
	public void DeleteItem(int index)
	{
		SetItem(0,index);
	}
	public void DrawDeck(Graphics2D g2d, Lifecraft obs)
	{
		int len = deck.length;
		Point pos = new Point(obs.getWidth() - SIZE+2,obs.getHeight() - 8*(SIZE+2));
		g2d.setColor(new Color(175,175,175,169));
		g2d.fillRect(pos.x-1, pos.y-1, SIZE, 8*(SIZE+2));
		for(int i = 0;i < len;i++)
		{
			deck[i].drawThis(g2d, pos, obs);
			if(picked == i)
			{
				g2d.setColor(new Color(15,255,15));
				g2d.drawRect(pos.x-2, pos.y-2, SIZE-2, SIZE-2);
			}
			pos.y += (SIZE+1);
		}
	}
}
