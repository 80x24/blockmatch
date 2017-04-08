import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

public class TestClient extends JFrame {
	private final int BOUNDS=50; 
	private final int XAXIS=5;
	private final int YAXIS=7; 
	private JPanel contentPane;
	private char[][] grid=new char[XAXIS][YAXIS];
	private boolean pressed=false;
	private String lastPressed = "100 100";
	private Random rando;
	JButton[][] squares = new JButton[XAXIS][YAXIS];
	HashMap<JButton , String> squareMap = new HashMap<JButton , String>();
	private Timer fallTimer;
	private Timer fallLineTimer;
	private int score=0;
	private JLabel scoreDisplay ;
	
	
	
	private ImageIcon green= new ImageIcon("greensquare.png");
	private ImageIcon greenCH= new ImageIcon("greensquareCH.png");
	private ImageIcon blue= new ImageIcon("bluesquare.png");
	private ImageIcon blueCH= new ImageIcon("bluesquareCH.png");
	private ImageIcon red= new ImageIcon("redsquare.png");
	private ImageIcon redCH= new ImageIcon("redsquareCH.png");
	private ImageIcon yellow= new ImageIcon("yellowsquare.png");
	private ImageIcon yellowCH= new ImageIcon("yellowsquareCH.png");
	private ImageIcon orange= new ImageIcon("orangesquare.png");
	private ImageIcon orangeCH= new ImageIcon("orangesquareCH.png");
	private ImageIcon black= new ImageIcon("blacksquare.png");
	
	
	
	/**
	 * Launch the application.
	 */
			
			
			
			
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestClient frame = new TestClient(112);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestClient(long seed) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		rando=new Random(seed);
		scoreDisplay=new JLabel();
		scoreDisplay.setBounds(BOUNDS*(XAXIS+1), BOUNDS*(YAXIS/2), 100, 50);
		contentPane.add(scoreDisplay);
		scoreDisplay.setText("Score:"+score);
		for( int i=0; i<XAXIS;i++)
		{
			for( int j=0; j<YAXIS;j++)
			{	
				JButton btn = new JButton();
				btn.addActionListener(new squareListener());
				btn.setBounds(i*(BOUNDS), j*(BOUNDS), BOUNDS, BOUNDS);
				contentPane.add(btn);
				squareMap.put(btn,i+" "+j );
				squares[i][j]=btn;
				// created the grid
			grid[i][j]=generate();
			}
			grid[0][0]='b';
			updateimages();
			Timer t= new Timer(7000,new lineAdder());
			t.setRepeats(true);
			t.start();
			Timer t2= new Timer(100,new checker());
			t2.setRepeats(true);
			t2.start();
			fallTimer= new Timer(500,new faller());
			fallTimer.setRepeats(false);
		//	fallLineTimer= new Timer(500,new faller2());
		//	fallLineTimer.setRepeats(false);
			
			//t3.start();
		}
		
	
		
		//JButton btn0_0 = new JButton("New button");
		//btn0_0.addActionListener(new squareListener());
		//btn0_0.setBounds(126, 59, 50, 50);
		//contentPane.add(btn0_0);
	}
	
	
	public void updateimages()
	{
		 String[] sa= lastPressed.split(" ");
			
			int lx= Integer.parseInt(sa[0]);
			int ly= Integer.parseInt(sa[1]);
		for( int i=0; i<XAXIS;i++)
		{
			for( int j=0; j<YAXIS;j++)
			{	JButton b = squares[i][j];
					
					if((lx==i&&ly==j))
					{
						if(grid[i][j]=='a')
							squares[i][j].setIcon(blueCH);
						if(grid[i][j]=='b')
							squares[i][j].setIcon(orangeCH);
						if(grid[i][j]=='c' )
							squares[i][j].setIcon(greenCH);
						if(grid[i][j]=='d')
							squares[i][j].setIcon(redCH);
						if(grid[i][j]=='e')
							squares[i][j].setIcon(yellowCH);
					}
					else
					{
						
						if(grid[i][j]=='a')
							squares[i][j].setIcon(blue);
						if(grid[i][j]=='b')
							squares[i][j].setIcon(orange);
						if(grid[i][j]=='c')
							squares[i][j].setIcon(green);
						if(grid[i][j]=='d')
							squares[i][j].setIcon(red);
						if(grid[i][j]=='e')
							squares[i][j].setIcon(yellow);
						if(grid[i][j]=='0')
							squares[i][j].setIcon(black);
					}
			
			
			}
		}
		
		scoreDisplay.setText("Score: "+score);
	}
	
	public void checkBoard()
	{
		char last;
		char now;
		char uplast;//upper case last
		char upnow;
		int chain=1;
	
		for( int i=0; i<XAXIS;i++)
		{
			chain=1;
			last='1';//there will be no char zero
			
			for( int j=0; j<YAXIS;j++)
			{	now=grid[i][j];
				uplast=Character.toUpperCase(last);
				upnow=Character.toUpperCase(now);
				if(upnow==uplast)
				{
					chain++;
					if(chain>2)
					{
						grid[i][j]=upnow;
						grid[i][j-1]=upnow;
						grid[i][j-2]=upnow;
		
					}
				}
				else
				{
					chain=1;
					last=Character.toLowerCase(now);
				}
		
			}
	
		}
		
		for( int i=0; i<YAXIS;i++)
		{
			chain=1;
			last='1';//there will be no char zero
			
			for( int j=0; j<XAXIS;j++)
			{	now=grid[j][i];
				uplast=Character.toUpperCase(last);
				upnow=Character.toUpperCase(now);
				if(upnow==uplast)
				{
					chain++;
					if(chain>2)
					{
						grid[j][i]=upnow;
						grid[j-1][i]=upnow;
						grid[j-2][i]=upnow;
					}
				}
				else
				{
					chain=1;
					last=Character.toLowerCase(now);
				}
		
			}
	
		}
		updateimages();
		remove();
	    
		fallTimer.start();
		//fall();
	}
	
	
	
	public void fall() //fall has the blocks fall one block at a time. 
	{
		boolean keepgoing=true;
		boolean noFall=false;
		char here;
		char above;
		while(keepgoing)
		{
			keepgoing=false;
			for(int i=0;i<XAXIS;i++)
			{
				//for(int j=1; j<YAXIS;j++)
				for(int j=YAXIS-1; j>0;j--)
				{
					above=grid[i][j-1];
					here=grid[i][j];
					if(here=='0' &&above!='0')
					{
						noFall=true;
						keepgoing=true;
						grid[i][j]=above;
						grid[i][j-1]='0';		
					}
					
				}
			}
		
			
		}
		
		
		
	}
	
	
	
	public void fall2()  // fall 2 has the blocks fall 1 at a time. 
	{
		boolean keepgoing=true;
		boolean noFall=false;
		char here;
		char above;
		
		keepgoing=false;
		for(int i=0;i<XAXIS;i++)
		{
			//for(int j=1; j<YAXIS;j++)
			for(int j=YAXIS-1; j>0;j--)
			{
				above=grid[i][j-1];
				here=grid[i][j];
				if(here=='0' &&above!='0')
				{
					noFall=true;
					keepgoing=true;
					grid[i][j]=above;
					grid[i][j-1]='0';		
				}
				
			}
		}
		updateimages();
	}
	
	
	
	public void remove()
	{

		int combo=1;
		int points=0;
	    for( int i=0; i<XAXIS;i++)
		{
			for( int j=0; j<YAXIS;j++)
			{
				if(Character.isUpperCase(grid[i][j]))
				{
					grid[i][j]='0';	
					points+=combo;
					combo++;
				}
			}
		}	
		score+=points;
	    updateimages();
	}
	
	public char generate()
	{ char v='0';
		int chance=rando.nextInt(5);
		switch(chance)
		{
		case 0:
			v='a';
			break;
		case 1:	
			v='b';	
			break;
		case 2:
			v='c';	
			break;
		case 3:	
			v='d';
			break;
		case 4:
			v='e';
			break;
	}
		return v;
	}
	
	
	
	public void newLine()
	{
		int j=0;
		for(int i=0; i<XAXIS; i++)
		{
			if(grid[i][j]=='0')
			{
				grid[i][j]=generate();
			}
			
		}
		//checkBoard();
			
	}
	
	public class lineAdder implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			newLine();
		}
	}
	
	public class checker implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			checkBoard();
		}
	}
	public class faller implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			fall2();
			//fall();
		}
	}

	public int getScore()
	{
		return score;
	}
	public class squareListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String s = squareMap.get((JButton)e.getSource());	
			String[] sa= s.split(" ");
			int x= Integer.parseInt(sa[0]);
			int y= Integer.parseInt(sa[1]);
			if(pressed==false)
			{
				
				if(grid[x][y]!='0')
				{
					lastPressed=s;
					pressed=true;
				}
			}
			else
			{
				
				
		   	    sa= lastPressed.split(" ");
				
				int x2= Integer.parseInt(sa[0]);
				int y2= Integer.parseInt(sa[1]);
				//System.out.println(lastPressed + s);
				
				int x3=x-x2;
				int y3=y-y2;
				if(( x3<=1 && x3>=-1 )&&( y3<=1 && y3>=-1 )&&((x3==0)||(y3==0)))
				{
					
					char temp;
					temp= grid[x][y];
					grid[x][y]=grid[x2][y2];
					grid[x2][y2]=temp;
					System.out.println(lastPressed +" " +s +grid[x][y]);
					updateimages();
				//	checkBoard();
					lastPressed = "100 100";
					pressed=false;
				}
				else
				{	 if(grid[x][y]!='0')
					{
						lastPressed=s;
					}
					else
					{
						lastPressed = "100 100";
						pressed=false;
					}
				}
			}
			
			updateimages();
			
		}
	}
}
	
