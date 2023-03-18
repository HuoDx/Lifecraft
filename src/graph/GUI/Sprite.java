package graph.GUI;

import java.awt.*;

public class Sprite extends GUI{
	Color color;
	Point size;
	public Sprite(Point newPosition,Color col,Point size) {
		super(newPosition);
		this.color = col;
		this.size = size;
	}
	public Sprite(int newX,int newY,Color col,Point size) {
		super(newX,newY);
		this.color = col;
		this.size = size;
	}
	public void drawSelf(Graphics2D g2d)
	{
		g2d.setColor(this.color);
		g2d.fillRect(position.x, position.y,this.size.x,this.size.y);
		System.out.println("Drawing an rect at" + position + " : " + this.size);
	}
}
