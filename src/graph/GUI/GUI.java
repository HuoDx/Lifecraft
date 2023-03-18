package graph.GUI;
import java.awt.*;
public class GUI {
	public Point position;
	public GUI(Point newPosition)
	{
		setPosition(newPosition);
	}
	public GUI(int newX,int newY)
	{
		setPosition(newX,newY);
	}
	public void setPosition(Point newPosition)
	{
		this.position = newPosition;
	}
	public void setPosition(int newX,int newY)
	{
		this.position.x = newX;
		this.position.y = newY;
	}
	public Point getPosition()
	{
		return position;
	}
}
