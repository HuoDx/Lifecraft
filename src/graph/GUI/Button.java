package graph.GUI;
import java.awt.*;
public class Button extends GUI{
	Sprite background;
	Label word;
	public Button(Point position,Color spriteColor,String word,Point size,Color fontColor,String font,int fontSize)
	{
		super(position);
		this.background = new Sprite(position,spriteColor,size);
		this.word = new Label(new Point(position.x,position.y + size.y/2),word,font,fontSize,fontColor);
	}
	public void drawSelf(Graphics2D g2d)
	{
		background.drawSelf(g2d);
		word.drawSelf(g2d);
	}
	public boolean checkClick(Point mousePosition)
	{
		if(mousePosition.x >= position.x && mousePosition.x <= position.x + this.background.size.x*1.7)
		{
			if(mousePosition.y >= position.y && mousePosition.y <= position.y + this.background.size.y)
			{
				return true;
			}
		}
		return false;
	}
}
