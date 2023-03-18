import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.beans.Transient;
import java.io.Serializable;

public class Player implements Serializable{
	public Point position,cameraPosition;
	private int breath,blood;
	private boolean underWater;
	private int time,face;
	private int myFace;
	private int ghostTime;
	public int NightvisionTime;
	public final int timeInADay = 60*24;
	int SIZE , MAP_SIZE;
	public Backpack backpack;
	private transient Image img;
	public int maxBreath = 11,maxBlood = 10;
	public int getCurrentTime()
	{
		return time;
	}
	public void setCurrentTime(int curTime)
	{
		this.time = curTime;
	}
	public void setMyFace(int dirc)
	{
		this.myFace = dirc;
	}
	public void setGhostTime(int seconds)
	{
		this.ghostTime = seconds;
	}
	public int getGhostTime()
	{
		return this.ghostTime;
	}
	public int getBreath()
	{
		return this.breath;
	}
	public void harm(int amount)
	{
		if(amount < 0)
		{
			if(blood - amount <= maxBlood)blood -= amount;
			else blood = maxBlood;
		}
		else
		{
			if(blood >= amount)blood -= amount;
			else blood = 0;
		}
	}
	public int getBlood()
	{
		return this.blood;
	}
	public boolean isUnderWater()
	{
		return underWater;
	}
	public void setInWater()
	{
		underWater = true;
	}
	public void setOutWater()
	{
		underWater = false;
	}
	public void setFace(int face)
	{
		this.face = face;
	}
	public void update()
	{
		//System.out.println("Updating, the breath is :" + breath);
		if(isUnderWater())
		{
			//System.out.println("I'm under water!");
			if(breath > 0)breath --;
			//System.out.println("Updating, the breath is :" + breath);
		}
		else
		{
			//System.out.println("Hohoho! I'm breathing!");
			if(breath+2 <= maxBreath)
				breath += 2;
		}
		if(breath <= 0)
		{
			harm(1);
		}
		time += face;
		if(time >= timeInADay)face = -1;
		else if(time <=  0)face = 1;
		if(NightvisionTime > 0)NightvisionTime -- ;
		if(this.ghostTime > 0)this.ghostTime--;
	}
	public void renderSelf(Graphics2D g2d,Lifecraft obs)
	{
		if(this.ghostTime <= 0)
		{
			img = Toolkit.getDefaultToolkit().getImage("PHat_front.png");
		}
		else 
		{
			img = Toolkit.getDefaultToolkit().getImage("PHat_Front_ghost.png");
		}
		//g2d.fillOval(32, 32, 32, 32);//TEST
		g2d.drawImage(img,this.position.x * (SIZE + 2), this.position.y* (SIZE + 2), SIZE, SIZE,obs);
	}
	Player(int SIZE,int MAP_SIZE,Point origPos,Point camPos)
	{
		this.SIZE = SIZE;
		this.MAP_SIZE = MAP_SIZE;
		this.position = origPos;
		this.breath = maxBreath;
		this.blood = maxBlood;
		this.cameraPosition = camPos;
		time = 0;face = 1;
		NightvisionTime = 0;
		backpack = new Backpack(SIZE);
		ghostTime = 0;
	}
}
