package graph.GUI;

import java.awt.*;


public class Label extends GUI{
	String words;
	Font font;
	Color color;
	public Label(Point newPosition,String sign,String font,int fontsize,Color color) {
		super(newPosition);
		this.words = sign;
		this.font = new Font(font,fontsize,fontsize);
		this.color = color;
	}
	public void drawSelf(Graphics2D g2d)
	{
		g2d.setFont(this.font);
		g2d.setColor(this.color);
		g2d.drawString(words, position.x, position.y);
		//System.out.println("Drawing string");
	}
	
}
