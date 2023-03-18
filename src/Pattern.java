import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Pattern {
	private char[][] thisPattern;
	private Point size;
	int MAP_SIZE;
	Pattern(File fp,int MAP_SIZE)
	{
		this.MAP_SIZE = MAP_SIZE;
		size = new Point(0,0);
		loadFrom(fp);
	}
	public Point getSize()
	{
		return this.size;
	}
	public void stampThis(Point position,BasicObject[][] map,int[][] items)
	{
		if(position.x + size.x >= MAP_SIZE)return ;
		if(position.y + size.y >= MAP_SIZE)return ;
		char fill = '2';
		//Point absoluteTarget = new Point(position.x+size.x,position.y+size.y);
		for(int i = 0;i < size.x;i++)
			for(int j = 0;j < size.y;j++)
			{
				
				if('1' <= thisPattern[i][j] && thisPattern[i][j] <= '9')
				{
					map[position.x + i][position.y + j].setID((thisPattern[i][j] - '0'));
					if(map[position.x + i][position.y + j].isWalkable())
						fill = thisPattern[i][j];
				}
				else if('A' <= thisPattern[i][j] && thisPattern[i][j] <= 'Z')
				{
					map[position.x + i][position.y + j].setID(fill - '0');
					items[position.x + i][position.y + j] = (thisPattern[i][j] - 'A');
				}
			}
	}
	public void stampThis(Point position,BasicGolly[][] bg)
	{
		if(position.x + size.y >= MAP_SIZE)return ;
		if(position.y + size.x >= MAP_SIZE)return ;
		//Point absoluteTarget = new Point(position.x+size.x,position.y+size.y);
		for(int i = 0;i < size.y;i++)
			for(int j = 0;j < size.x;j++)
			{
				System.out.println(size);
				if('0' <= thisPattern[i][j] && thisPattern[i][j] <= '1')
				{
					bg[position.x + i][position.y + j].setAlive(thisPattern[i][j] == '1');
				}
				if('A' <= thisPattern[i][j] && thisPattern[i][j] <= 'B')
				{
					bg[position.x + i][position.y + j].setAlive(thisPattern[i][j] == 'B');
					bg[position.x + i][position.y + j].setHarmful(false);
				}
			}
	}
	private void loadFrom(File fp) {
		// TODO Auto-generated method stub
		try {
			Scanner fileReader = new Scanner(fp);
			int len;
			size.x = fileReader.nextInt();
			size.y = fileReader.nextInt();

			thisPattern = new char[size.y][size.x]; 
			Point pos = new Point(0,0);
			String buffer;
			char ch = 0;
			fileReader.nextLine();
			while(fileReader.hasNextLine())
			{
				buffer = fileReader.nextLine();
				len = buffer.length();
				for(int i = 0;i < len;i++)
				{
					if(buffer.charAt(i) != ' ')ch = buffer.charAt(i);
					else
					{
						pos.x %= size.x;
						thisPattern[pos.y][pos.x] = ch;
						pos.x++;
					}
				}
				pos.y++;pos.y %= size.y;
			}
			for(int i = 0;i < size.y;i++)
			{
				for(int j = 0;j < size.x;j++)
				{
					System.out.print(thisPattern[i][j] + " ");
				}
				System.out.print("\n");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			try {
				FileWriter fw = new FileWriter(fp);
				fw.write("0 0\n");
			} catch (IOException e1) {
			}
		}
	}
}
