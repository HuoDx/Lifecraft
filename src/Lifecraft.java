/**
 * @author Queue Huo
 * @version 0.8.1.17
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import graph.GUI.Button;
import graph.GUI.Label;
import graph.GUI.Sprite;
public class Lifecraft extends JPanel implements ActionListener,KeyListener,MouseListener
{
	/**
	 * 
	 */
	int status = 0;
	//0 is before start
	//1 is playing
	//2 is you died!!!!!!!!!!
	//3 is nether
	private static final long serialVersionUID = 1L;
	AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("Lifecraft.wav"));
	public int PlacedItems[][];
	public BasicObject[][] curMap,netMap;
	public BasicGolly[][] curLayer,nexLayer;
	Image[] TerrianImg = new Image[12];
	Image[] ItemImg = new Image[10];
	final int SIZE = 64;
	final int MAP_SIZE = 100;
	Player player;
	Pattern[] patt = new Pattern[3];
	Pattern[] golPatt = new Pattern[4];
	Clip[] bgm = new Clip[3];
	Sprite GUIBackground;
	Label Title,GameOver;
	Button Start,Restart,NewMap;
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		if(status == 0)
		{
			//renderMap(g2d);
			GUIBackground.drawSelf(g2d);
			Title.drawSelf(g2d);
			Start.drawSelf(g2d);
			NewMap.drawSelf(g2d);
		}
		else if(status == 1)
		{
			g2d.translate(player.cameraPosition.x, player.cameraPosition.y);
			g2d.setColor(Color.DARK_GRAY);
			g2d.fillRect(-1000, -1000, MAP_SIZE * SIZE * 2 + 1000, MAP_SIZE * SIZE * 2+ 1000);//bg
			renderMap(g2d);
			renderPlacedItems(g2d);
			renderGolly(g2d);
			player.renderSelf(g2d,this);	
			g2d.translate(-player.cameraPosition.x,-player.cameraPosition.y);
			if(player.NightvisionTime <= 0) {
				g2d.setColor(new Color(2,2,2,(int)((((double)player.getCurrentTime() / player.timeInADay) <= 1 ? ((double)player.getCurrentTime() / player.timeInADay) : 1)*200)));
				g2d.fillRect(-10, -10, MAP_SIZE * SIZE * 2, MAP_SIZE * SIZE * 2);//bg
			}
			else if(((double)player.getCurrentTime() / player.timeInADay) >= 0.6)
			{
				g2d.setColor(new Color(23,33,66,100));
				g2d.fillRect(-10, -10, MAP_SIZE * SIZE * 2, MAP_SIZE * SIZE * 2);//bg
			}
			//g2d.fillRect(-10, -10, MAP_SIZE * SIZE * 2, MAP_SIZE * SIZE * 2);//bg
			renderGUI(g2d);	
		}
		else if(status == 2)
		{
			GUIBackground.drawSelf(g2d);
			GameOver.drawSelf(g2d);
			NewMap.drawSelf(g2d);
		}
		if(status == 3)
		{
			g2d.translate(player.cameraPosition.x, player.cameraPosition.y);
			g2d.setColor(Color.DARK_GRAY);
			g2d.fillRect(-1000, -1000, MAP_SIZE * SIZE * 2 + 1000, MAP_SIZE * SIZE * 2+ 1000);//bg
			renderNetherMap(g2d);
			player.renderSelf(g2d,this);	
			g2d.translate(-player.cameraPosition.x,-player.cameraPosition.y);
			renderGUI(g2d);	
		}
		g2d.finalize();
		//System.out.println(player.cameraPosition.toString());
	}
	private void renderNetherMap(Graphics2D g2d) {
		// TODO Auto-generated method stub
		Point pos = new Point(0,0);
		Point s,t;
		s = new Point(-player.cameraPosition.x/4 - SIZE*49,(-player.cameraPosition.y)-128);
		if(s.x < 0)s.x = 0;
		if(s.y < 0)s.y = 0;
		t = new Point(-player.cameraPosition.x + SIZE*29,s.y + getHeight() + 2*SIZE);
		//for test
		//s = new Point(0,0);s
		//t = new Point(500,500);

		//if(t.x > MAP_SIZE*SIZE)t.x = MAP_SIZE*SIZE;
		//if(t.y > MAP_SIZE*SIZE)t.y = MAP_SIZE*SIZE;

		for(int i = 0;i < MAP_SIZE;i++)
		{
			for(int j = 0;j < MAP_SIZE;j++)
			{
				if(s.x > pos.x || s.y > pos.y || t.x < pos.x || t.y < pos.y)
					continue;
				/*switch(curMap[i][j].getID())
				{
					case 1: // Bush
						g2d.setColor(new Color(5,220,5));
						break;
					case 2: // grass
						g2d.setColor(new Color(6,255,6));
						break;
					case 3: // dirt
						g2d.setColor(new Color(80,65,37));
						break;
					case 4: // water
						g2d.setColor(new Color(45,45,245,230));
						break;
					case 5: // rock
						g2d.setColor(new Color(120,120,120));
						break;
				}*/
				//g2d.fillRect(pos.x, pos.y, SIZE, SIZE);
				g2d.drawImage(TerrianImg[netMap[i][j].getID()], pos.x, pos.y, SIZE, SIZE,this);
				pos.x += (SIZE + 2);
			}
			pos.y += (SIZE + 2);
			pos.x = 0;
		}
	}
	private void renderPlacedItems(Graphics2D g2d) {
		// TODO Auto-generated method stub
		Point pos = new Point(0,0);
		Point s,t;
		s = new Point(-player.cameraPosition.x/4 - SIZE*59,(-player.cameraPosition.y)-128);
		if(s.x < 0)s.x = 0;
		if(s.y < 0)s.y = 0;
		t = new Point(-player.cameraPosition.x + SIZE*29,s.y + getHeight() + 2*SIZE);
		for(int i = 0;i < MAP_SIZE;i++)
		{
			for(int j = 0;j < MAP_SIZE;j++)
			{
				if(s.x > pos.x || s.y > pos.y || t.x < pos.x || t.y < pos.y)
					continue;
				if(PlacedItems[i][j] != 0)
				{
					g2d.drawImage(ItemImg[PlacedItems[i][j]],pos.x,pos.y,SIZE,SIZE, this);
					//System.out.println(PlacedItems[i][j] + " Should be at: " + i + " , " + j + ", Actually at: " + pos.x/SIZE + " , " + pos.y/SIZE);
				}
				pos.x += (SIZE + 2);
			}
			pos.y += (SIZE + 2);
			pos.x = 0;
		}
	}
	private void renderGolly(Graphics2D g2d) {
		// TODO Auto-generated method stub
		Point pos = new Point(0,0);
		Point s,t;
		s = new Point(-player.cameraPosition.x/4 - SIZE*59,(-player.cameraPosition.y)-128);
		if(s.x < 0)s.x = 0;
		if(s.y < 0)s.y = 0;
		t = new Point(-player.cameraPosition.x + SIZE*29,s.y + getHeight() + 2*SIZE);
		for(int i = 0;i < MAP_SIZE;i++)
		{
			for(int j = 0;j < MAP_SIZE;j++)
			{
				if(s.x > pos.x || s.y > pos.y || t.x < pos.x || t.y < pos.y)
					continue;
				//System.out.print(curLayer[i][j].isAlive() ? "X" : " ");
				curLayer[i][j].drawItself(g2d,pos);
				pos.x += (SIZE + 2);
			}
			pos.y += (SIZE + 2);
			pos.x = 0;
			//System.out.print("\n");
		}
	}
	private void renderGUI(Graphics2D g2d)
	{
		int mod = -54;
		if(player.isUnderWater())
		{
			mod = 32;
			Image bubble = Toolkit.getDefaultToolkit().getImage("Bubble.png");
			//Image deck = Toolkit.getDefaultToolkit().getImage("Deck.png");
			int bubbleNumber = (player.getBreath()) / ((int)(player.maxBreath * 0.1));
			//g2d.drawImage(deck,0, 32 , bubbleNumber * 65 ,  64, this);
			for(int i = 0;i < bubbleNumber;i++)
			{
				//System.out.println("Rendering..." + i);
				g2d.drawImage(bubble,i * 65, 32 , 64 ,  64, this);
			}
		}
		Image blood = Toolkit.getDefaultToolkit().getImage("Blood.png");
		//Image deck = Toolkit.getDefaultToolkit().getImage("Deck.png");
		int bloodNumber = (player.getBlood()) / ((int)(player.maxBlood * 0.1));
		//g2d.drawImage(deck,0, getHeight() - 66 , (bloodNumber) * 65 ,  64, this);
		for(int i = 0;i < bloodNumber;i++)
		{
			//System.out.println("Rendering..." + i);
			g2d.drawImage(blood,i * 65, 65+mod , 64 ,  64, this);
		}
		player.backpack.DrawDeck(g2d, this);
	}
	private void renderMap(Graphics2D g2d) {
		// TODO Auto-generated method stub
		Point pos = new Point(0,0);
		Point s,t;
		s = new Point(-player.cameraPosition.x/4 - SIZE*49,(-player.cameraPosition.y)-128);
		if(s.x < 0)s.x = 0;
		if(s.y < 0)s.y = 0;
		t = new Point(-player.cameraPosition.x + SIZE*29,s.y + getHeight() + 2*SIZE);
		//for test
		//s = new Point(0,0);s
		//t = new Point(500,500);

		//if(t.x > MAP_SIZE*SIZE)t.x = MAP_SIZE*SIZE;
		//if(t.y > MAP_SIZE*SIZE)t.y = MAP_SIZE*SIZE;

		for(int i = 0;i < MAP_SIZE;i++)
		{
			for(int j = 0;j < MAP_SIZE;j++)
			{
				if(s.x > pos.x || s.y > pos.y || t.x < pos.x || t.y < pos.y)
					continue;
				/*switch(curMap[i][j].getID())
				{
					case 1: // Bush
						g2d.setColor(new Color(5,220,5));
						break;
					case 2: // grass
						g2d.setColor(new Color(6,255,6));
						break;
					case 3: // dirt
						g2d.setColor(new Color(80,65,37));
						break;
					case 4: // water
						g2d.setColor(new Color(45,45,245,230));
						break;
					case 5: // rock
						g2d.setColor(new Color(120,120,120));
						break;
				}*/
				//g2d.fillRect(pos.x, pos.y, SIZE, SIZE);
				g2d.drawImage(TerrianImg[curMap[i][j].getID()], pos.x, pos.y, SIZE, SIZE,this);
				pos.x += (SIZE + 2);
			}
			pos.y += (SIZE + 2);
			pos.x = 0;
		}
	}
	private int getNum(int i,int j)
	{
		if(i < 0 || j < 0 || i >= MAP_SIZE || j >= MAP_SIZE)
		{
			//System.out.println(i + " , " + j);
			return 0;
		}
		if(curLayer[i][j].isAlive() && !curLayer[i][j].isFrozen())
			return 1;
		return 0;
	}
	private void LoadFiles() throws Exception
	{
		File fp = new File("Terrian.LCmap");
		try {
			FileInputStream fi = new FileInputStream(fp);
			ObjectInputStream oi = new ObjectInputStream(fi);
			for(int i = 0;i < MAP_SIZE;i++)
				for(int j = 0;j < MAP_SIZE;j++)
					curMap[i][j] = (BasicObject)oi.readObject();
			//System.out.println(curMap[5][5]);
			fp = new File("Player.LCdata");
			fi = new FileInputStream(fp);
			oi = new ObjectInputStream(fi);
			player = (Player)oi.readObject();
			player.backpack.loadAllItems();
			fi.close();
			oi.close();
		}
		catch(FileNotFoundException fnfe) {//no map no player
			generateMap();
			generatePlayer();
		}
	}
	private void LoadNether() throws Exception
	{
		File fp = new File("Nether.LCmap");
		try {
			FileInputStream fi = new FileInputStream(fp);
			ObjectInputStream oi = new ObjectInputStream(fi);
			for(int i = 0;i < MAP_SIZE;i++)
				for(int j = 0;j < MAP_SIZE;j++)
					netMap[i][j] = (BasicObject)oi.readObject();
			//System.out.println(curMap[5][5]);
			fi.close();
			oi.close();
		}
		catch(FileNotFoundException fnfe) {//no map no player
			generateNet();
			saveNether();
		}
	}
	private void generateNet() {
		// TODO Auto-generated method stub
		Random rd = new Random(465879542);
		for(int i = 0;i < MAP_SIZE;i ++)
			for(int j = 0;j < MAP_SIZE;j ++)
				netMap[i][j] = new BasicObject(10);
		for(int i = 0;i < MAP_SIZE;i += rd.nextInt(6))
			for(int j = 0;j < MAP_SIZE;j += rd.nextInt(6))
				netMap[i][j].setID(11);
		
	}
	private void saveNether() throws Exception{
		// TODO Auto-generated method stub
		File fp = new File("Neither.LCmap");
		FileOutputStream fo = new FileOutputStream(fp);
		ObjectOutputStream oo = new ObjectOutputStream(fo);
		for(int i = 0;i < MAP_SIZE;i++)
			for(int j = 0;j < MAP_SIZE;j++)
				oo.writeObject(netMap[i][j]);
		oo.close();
		fo.close();
	}
	private void generatePlayer()throws Exception
	{
		player = new Player(SIZE,MAP_SIZE,new Point(15,7),new Point(0,0));
		savePlayer();
	}
	private void savePlayer() throws Exception{
		// TODO Auto-generated method stub
		File fp = new File("Player.LCdata");
		FileOutputStream fo = new FileOutputStream(fp);
		ObjectOutputStream oo = new ObjectOutputStream(fo);
		oo.writeObject(player);
		oo.close();
		fo.close();
	}
	private void saveGollys() throws Exception{
		// TODO Auto-generated method stub
		File fp = new File("Gollys.LCdata");
		FileOutputStream fo = new FileOutputStream(fp);
		ObjectOutputStream oo = new ObjectOutputStream(fo);
		for(int i = 0;i < MAP_SIZE;i++)
			for(int j = 0;j < MAP_SIZE;j++)
				oo.writeObject(curLayer[i][j]);
		oo.close();
		fo.close();
	}
	private void loadGollys() throws Exception
	{
		try {
			File fp = new File("Gollys.LCdata");
			FileInputStream fi = new FileInputStream(fp);
			ObjectInputStream oi = new ObjectInputStream(fi);
			for(int i = 0;i < MAP_SIZE;i++)
				for(int j = 0;j < MAP_SIZE;j++)
				{
					curLayer[i][j] = (BasicGolly)oi.readObject();
					nexLayer[i][j] = new BasicGolly(SIZE);
				}
			fi.close();
			oi.close();
		}
		catch(FileNotFoundException ex)
		{
			generateGollys();
			saveGollys();
		}
	}
	private void generateGollys()
	{
		for(int i = 0;i < MAP_SIZE;i++)//init golly maps
			for(int j = 0;j < MAP_SIZE;j++)
			{
				curLayer[i][j] = new BasicGolly(SIZE);
				nexLayer[i][j] = new BasicGolly(SIZE);
			}
	}
	private void generateMap()
	{
		Random rd = new Random();
		for(int i = 0;i < MAP_SIZE;i++)
			for(int j = 0;j < MAP_SIZE;j++)
				curMap[i][j] = new BasicObject(3);	
		for(int i = 0;i < MAP_SIZE;i += rd.nextInt(12))
			for(int j = 0;j < MAP_SIZE;j += rd.nextInt(12))
			{
				setCurMapID(i,j,4);
				setCurMapID(i-1,j,4);
				setCurMapID(i,j-1,4);
				setCurMapID(i+1,j,4);
				setCurMapID(i,j+1,4);
				setCurMapID(i+1,j+1,4);
				setCurMapID(i-1,j-1,4);
			}
		for(int i = 0;i < MAP_SIZE;i += rd.nextInt(5))
			for(int j = 0;j < MAP_SIZE;j += rd.nextInt(5))
				curMap[i][j].setID(5);	
		for(int i = 0;i < MAP_SIZE;i += rd.nextInt(4))
			for(int j = 0;j < MAP_SIZE;j += rd.nextInt(4)+3)
				curMap[i][j].setID(1);
		for(int i = 0;i < MAP_SIZE;i += rd.nextInt(6))
			for(int j = 0;j < MAP_SIZE;j += rd.nextInt(6))
				curMap[i][j].setID(2);	
		for(int i = 0;i < MAP_SIZE;i += patt[0].getSize().x+2)
			for(int j = 0;j < MAP_SIZE;j += patt[0].getSize().x+2)
				if(rd.nextInt(100) + 1 > 89)
					patt[0].stampThis(new Point(i,j),curMap, PlacedItems);
		for(int i = 0;i < MAP_SIZE;i += patt[1].getSize().x+2)
			for(int j = 0;j < MAP_SIZE;j += patt[1].getSize().x+2)
				if(rd.nextInt(100) + 1 > 92)
					patt[1].stampThis(new Point(i,j),curMap, PlacedItems);
		for(int i = 0;i < MAP_SIZE;i += patt[1].getSize().x+2)
			for(int j = 0;j < MAP_SIZE;j += patt[1].getSize().x+2)
				if(rd.nextInt(100) + 1 > 95)
					patt[2].stampThis(new Point(i,j),curMap, PlacedItems);
		saveMap();
	}
	private void saveMap()
	{
		File fp = new File("Terrian.LCmap");
		try {

			FileOutputStream fo = new FileOutputStream(fp);
			ObjectOutputStream oo = new ObjectOutputStream(fo);
			for(int i = 0;i < MAP_SIZE;i++)
				for(int j = 0;j < MAP_SIZE;j++)
				{
					oo.writeObject(curMap[i][j]);
					//System.out.print(curMap[i][j]);
				}
			//System.out.println("Successfully saved map!");
			fo.close();
			oo.close();
		}
		catch(Exception ex)
		{

		}
	}
	private int getItemType(int i,int j)
	{
		if(i < 0 || j < 0 || i >= MAP_SIZE || j >= MAP_SIZE)
			return 0;
		return PlacedItems[i][j];
	}
	public Lifecraft() throws Exception//constructor
	{
		this.addMouseListener(this);
		GUIBackground = new Sprite(new Point(0,0),Color.gray,new Point(2000,2000));
		Title = new Label(new Point(700,150),"L i f e c r a f t","Consolas",50,Color.green);
		GameOver = new Label(new Point(700,150),"Y o u     D i e d","Consolas",50,Color.red);
		Start = new Button(new Point(700,250),Color.BLUE,"     S t a r t",new Point(450,70),Color.white,"Consolas",45);
		NewMap = new Button(new Point(700,350),Color.BLUE,"    N e w M a p",new Point(450,70),Color.white,"Consolas",45);
		PlacedItems = new int[MAP_SIZE][MAP_SIZE];
		loadItems();
		patt[0] = new Pattern(new File("Bakery.LCPattern"),MAP_SIZE);
		patt[1] = new Pattern(new File("Wizard's_Workshop.LCPattern"),MAP_SIZE);
		patt[2] = new Pattern(new File("WarriorTemple.LCPattern"),MAP_SIZE);
		golPatt[0] = new Pattern(new File("Shuttle.LCPattern"),MAP_SIZE);
		golPatt[1] = new Pattern(new File("Flower.LCPattern"),MAP_SIZE);
		golPatt[2] = new Pattern(new File("Gen.LCPattern"),MAP_SIZE);
		golPatt[3] = new Pattern(new File("PufferTrain.LCPattern"),MAP_SIZE);
		curMap = new BasicObject[MAP_SIZE][MAP_SIZE];
		LoadFiles();
		addKeyListener(this);
		curLayer = new BasicGolly[MAP_SIZE][MAP_SIZE];
		nexLayer = new BasicGolly[MAP_SIZE][MAP_SIZE];
		loadGollys();

		curLayer[5][5].setAlive(true);curLayer[5][5].setHarmful(true);
		curLayer[6][6].setAlive(true);curLayer[5][5].setHarmful(true);
		curLayer[7][6].setAlive(true);curLayer[5][5].setHarmful(true);
		curLayer[7][5].setAlive(true);curLayer[5][5].setHarmful(true);
		curLayer[7][4].setAlive(true);curLayer[5][5].setHarmful(true);
		TerrianImg[1] = Toolkit.getDefaultToolkit().getImage("Bush.png");
		TerrianImg[2] = Toolkit.getDefaultToolkit().getImage("Grass.png");
		TerrianImg[3] = Toolkit.getDefaultToolkit().getImage("Dirt.png");
		TerrianImg[4] = Toolkit.getDefaultToolkit().getImage("Water.png");
		TerrianImg[5] = Toolkit.getDefaultToolkit().getImage("Rock.png");
		TerrianImg[6] = Toolkit.getDefaultToolkit().getImage("Grass_With_Stone.png");
		TerrianImg[7] = Toolkit.getDefaultToolkit().getImage("Wood_Wall.png");
		TerrianImg[8] = Toolkit.getDefaultToolkit().getImage("DeadLand.png");
		TerrianImg[9] = Toolkit.getDefaultToolkit().getImage("TempleStone.png");
		TerrianImg[10] = Toolkit.getDefaultToolkit().getImage("NetherRock.png");
		TerrianImg[11] = Toolkit.getDefaultToolkit().getImage("NetherPink.png");
		ItemImg[4] = Toolkit.getDefaultToolkit().getImage("Bed.png");
		ItemImg[1] = Toolkit.getDefaultToolkit().getImage("WaterPotion.png");
		ItemImg[2] = Toolkit.getDefaultToolkit().getImage("Nightvision.png");
		ItemImg[3] = Toolkit.getDefaultToolkit().getImage("Superball.png");
		ItemImg[5] = Toolkit.getDefaultToolkit().getImage("BirthdayCake.png");
		ItemImg[6] = Toolkit.getDefaultToolkit().getImage("StaffOfSkulls.png");
		ItemImg[7] = Toolkit.getDefaultToolkit().getImage("GhostTotem.png");
		ItemImg[8] = Toolkit.getDefaultToolkit().getImage("Teleport.png");
		Random rd = new Random(27871543);
		netMap = new BasicObject[MAP_SIZE][MAP_SIZE];
		LoadNether();
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {// this updates gollies' logic 
				// TODO Auto-generated method stub
				int tmp = 0;
				for(int i = 0;i < MAP_SIZE;i++)
					for(int j = 0;j < MAP_SIZE;j++)
					{
						if(curLayer[i][j].isFrozen())continue;//is frozen,no logic
						tmp = getNum(i-1,j-1) + getNum(i,j-1) + getNum(i-1,j) 
						+ getNum(i+1,j+1) + getNum(i,j+1) + getNum(i+1,j) 
						+ getNum(i-1,j+1) + getNum(i+1,j-1);
						if(tmp == 3)//birth
						{
							nexLayer[i][j].setAlive(true);
						}
						else if ((tmp == 2 || tmp == 3) && curLayer[i][j].isAlive())//survive
							nexLayer[i][j].setAlive(true);
						else nexLayer[i][j].setAlive(false);
					}
				for(int i = 0;i < MAP_SIZE;i++)
					for(int j = 0;j < MAP_SIZE;j++)
						curLayer[i][j].setAlive(nexLayer[i][j].isAlive());
				//System.out.println("Golly logic updated!");
				if(status == 1)
				{
					if(PlacedItems[player.position.y][player.position.x] == 8)
					{
						status = 3;//go to hell!!!!!!!!
					}
					for(int i = player.position.y - 10;i < player.position.y + 10;i++)
					{
						if(i < 0 || i >= MAP_SIZE)continue;
						for(int j = player.position.x - 10;j < player.position.x + 10;j++)
						{
							if(j < 0 || j >= MAP_SIZE)continue;
							if(getItemType(i+1,j+1) == 4 && getItemType(i+1,j-1) == 4 && getItemType(i-1,j) == 4)
							{
								System.out.println("Gened!");
								PlacedItems[i][j] = 8;//generate a portal
							}
							else if(PlacedItems[i][j] == 8)PlacedItems[i][j] = 0;
						}
					}
					if(curLayer[player.position.y][player.position.x].isAlive() && curLayer[player.position.y][player.position.x].isHarmful())
						player.harm(1);
					else if(curLayer[player.position.y][player.position.x].isAlive() && !curLayer[player.position.y][player.position.x].isHarmful())
						player.harm(-1);
					player.update();
				}
				
				if(player.getBlood() <= 0)status = 2;
				if(rd.nextInt(100) + 1 >= 27)
				{
					int t = rd.nextInt(3);
					Point pos = new Point(rd.nextInt(MAP_SIZE - golPatt[t].getSize().x),rd.nextInt(MAP_SIZE - golPatt[t].getSize().y));
					golPatt[t].stampThis(pos, curLayer);
				}
				try {
					savePlayer();
					saveGollys();
					savePlacedItems();
					saveNether();
				}catch(Exception exp) {}
				repaint();

			}
		};
		Timer gollyTimer = new Timer(1000,taskPerformer);
		gollyTimer.start();
		//curMap = new BasicObject[MAP_SIZE][MAP_SIZE];
		player.backpack.AutoInsert(4);
		//System.out.println(curLayer[5][5].isAlive());

		//patt[0].stampThis(new Point(60,27), curMap, PlacedItems);

		//patt[1].stampThis(new Point(20,25), curMap, PlacedItems);
		bgm[0] = AudioSystem.getClip();
		bgm[0].open(audioIn);
		bgm[0].start();
		bgm[0].loop(Clip.LOOP_CONTINUOUSLY);
	}
	private void loadItems() throws Exception{
		// TODO Auto-generated method stub
		try {
			File fp = new File("PlacedItems.LCdata");
			FileInputStream fi = new FileInputStream(fp);
			ObjectInputStream oi = new ObjectInputStream(fi);
			for(int i = 0;i < MAP_SIZE;i++)
				for(int j = 0;j < MAP_SIZE;j++)
				{
					PlacedItems[i][j] = (int)oi.readObject();
				}
			fi.close();
			oi.close();
		}
		catch(Exception ex)
		{
			generateItems();
			savePlacedItems();
		}
	}
	private void generateItems()
	{
		for(int i = 0;i < MAP_SIZE;i++)//init golly maps
			for(int j = 0;j < MAP_SIZE;j++)
			{
				PlacedItems[i][j] = 0;
			}
	}
	private void savePlacedItems() throws Exception{
		// TODO Auto-generated method stub
		File fp = new File("PlacedItems.LCdata");
		FileOutputStream fo = new FileOutputStream(fp);
		ObjectOutputStream oo = new ObjectOutputStream(fo);
		for(int i = 0;i < MAP_SIZE;i++)
			for(int j = 0;j < MAP_SIZE;j++)
				oo.writeObject(PlacedItems[i][j]);
		oo.close();
		fo.close();
	}
	private void setCurMapID(int i,int j,int id)
	{
		if(i < 0 || j < 0 || i >= MAP_SIZE || j >= MAP_SIZE)
			return ;
		curMap[i][j].setID(id);
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Lifecraft pannel = new Lifecraft();
		JFrame mainWindow = new JFrame("Lifecraft");//create window(frame)
		//create pannel
		//add window to pannel
		mainWindow.setBackground(Color.DARK_GRAY);
		mainWindow.setBounds(0,0,1280,720);
		mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH); //FullScreen
		mainWindow.setUndecorated(true);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
		//mainWindow.pack();
		Container c = mainWindow.getContentPane();
		c.add(pannel);
		Timer frameTimer = new Timer(10,pannel);


		pannel.setFocusable(true);
		pannel.requestFocusInWindow();
		mainWindow.setVisible(true);
		frameTimer.start();



	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//move Camera
		
		PointerInfo a = MouseInfo.getPointerInfo();
		Point mousePosition = a.getLocation();
		int SW  = getWidth(),SH = getHeight();
		/*
        if(mousePosition.x < SW/10)
        	{
        		if(player.cameraPosition.x <= 0)
        			player.cameraPosition.x += 10;
        	}
        if(mousePosition.x > (SW * 9)/10)
	    	{
	    		if(player.cameraPosition.x > -MAP_SIZE*SIZE + 1600)
	    			player.cameraPosition.x -= 10;
	    	}
        if(mousePosition.y > (SH * 9)/10)
	    	{
	    		if(player.cameraPosition.y > -MAP_SIZE*SIZE + 600)
	    			player.cameraPosition.y -= 10;
	    	}
        if(mousePosition.y < SH/10)
	    	{
	    		if(player.cameraPosition.y <= 0)
	    			player.cameraPosition.y += 10;
	    	}
		 */
		if(curMap[player.position.y][player.position.x].getID() == 4 && !player.isUnderWater())
			player.setInWater();
		else if(curMap[player.position.y][player.position.x].getID() != 4 && player.isUnderWater())
			player.setOutWater();

		repaint();
	}
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		if(status == 0 || status == 2)return ;
		char ch = e.getKeyChar();
		int id;
		//System.out.println(ch);
		if(ch == 'W' || ch == 'w' && player.position.y > 0)
		{
			if(curMap[player.position.y-1][player.position.x].isWalkable() || player.getGhostTime() > 0 || status == 3)
			{
				player.position.y--;
				player.cameraPosition.y+=SIZE;
			}
		}
		if(ch == 'S' || ch == 's' && player.position.y < MAP_SIZE-1)
		{
			if(curMap[player.position.y+1][player.position.x].isWalkable() || player.getGhostTime() > 0 || status == 3)
			{
				player.setMyFace(0);
				player.position.y++;
				player.cameraPosition.y-=SIZE;
			}
		}
		if(ch == 'A' || ch == 'a' && player.position.x > 0)
		{	
			if(curMap[player.position.y][player.position.x-1].isWalkable() || player.getGhostTime() > 0 || status == 3)
			{
				player.setMyFace(3);
				player.position.x--;
				player.cameraPosition.x+=SIZE;
			}
		}
		if(ch == 'D' || ch == 'd' && player.position.x < MAP_SIZE-1)
		{
			if(curMap[player.position.y][player.position.x+1].isWalkable() || player.getGhostTime() > 0 || status == 3)
			{
				player.setMyFace(1);
				player.position.x++;
				player.cameraPosition.x-=SIZE;
			}
		}
		else if(status != 3)
			switch(ch)
			{
			case 'e' : player.backpack.Pick((player.backpack.picked + 1)%player.backpack.MAX_BACKPACK_SIZE);break;
			case 'c' : 
				id = player.backpack.ItemAt(player.backpack.picked).getID();
				switch(id)//use this item
				{
				case 1: 
					player.maxBreath++;
					player.backpack.DeleteItem(player.backpack.picked);
					break;
				case 2: 
					player.NightvisionTime += 61;
					player.backpack.DeleteItem(player.backpack.picked);
					break;
					//3 is trash
				case 4: 
					Place(4);
					player.backpack.DeleteItem(player.backpack.picked);
					break;
				case 5: 
					player.harm(-2);
					player.backpack.DeleteItem(player.backpack.picked);
					break;
				case 6://Staff of Skulls
					System.out.println("Hey!!");
					if(player.position.x < 1 || player.position.y < 1)break;
						curMap[player.position.y-1][player.position.x].setID(8);//deadLand
					curLayer[player.position.y-1][player.position.x].setFrozen(true);
					
					try {
					saveGollys();
					saveMap();
					}catch(Exception exc) {}
					break;
				case 7:
					player.setGhostTime(player.getGhostTime() + 75);
					player.backpack.DeleteItem(player.backpack.picked);
					break;
				}
			case ' ':
				id = PlacedItems[player.position.y][player.position.x];
				switch(id)//use this item
				{
					case 4://bed
						if(((double)player.getCurrentTime() / player.timeInADay) >= 0.7)
						{
							player.setCurrentTime(player.timeInADay/2);
							player.setFace(-1);
							player.backpack.DeleteItem(id);
						}
						break;
					
				}
				break;
			case 'v': // pick
				id = PlacedItems[player.position.y][player.position.x];
				if(id != 0)
				{
					System.out.println(id);
					player.backpack.AutoInsert(id);
					PlacedItems[player.position.y][player.position.x] = 0;
				}
				break;
			}
		repaint();
	}
	private void Place(int i) {
		// TODO Auto-generated method stub
		if(player.position.x <= 1 || player.position.y <= 1)return ;
		PlacedItems[player.position.y-1][player.position.x] = i;
		//System.out.println("Now at: " + player.position.toString());
		player.backpack.DeleteItem(player.backpack.picked);
	}
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(status == 0 && Start.checkClick(e.getPoint()))
			status = 1;
		if(NewMap.checkClick(e.getPoint()))
		{
			generateItems();
			generateMap();
			saveMap();
			try {
				savePlacedItems();
				generateGollys();
				saveGollys();
				generatePlayer();
				savePlayer();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			status = 1;
			
			
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
