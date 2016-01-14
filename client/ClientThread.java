
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Scanner;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.Timer;

	public class ClientThread extends SwingWorker<Void, String >{
	private DataInputStream in;
	

	private Socket socket;
	private JTextArea info;
	private JTextArea names;
	private String chatName;
	private String myName;
	private String receiveStr;
	private DataOutputStream out;
	private TestClient game;
	private TreeSet<String> nameSet=new TreeSet<String>();
	private Client runner;
	private byte[] type= new byte[4];
	private int lastScore;
	private long now =	System.currentTimeMillis();
	private long pass =	System.currentTimeMillis(); 
	private long time =	System.currentTimeMillis();
	Timer startTimer = new Timer(1000,new starter());
	
	
	public ClientThread(DataInputStream in, String name, Socket socket, JTextArea a,JTextArea n, Client runner )
	{	myName=name;
		chatName=name;
		this.in=in;
		this.socket=socket;
		
		info=a;
		this.runner=runner;
		names=n;
		nameSet.add(name);
   		
   		
   		//time=(System.currentTimeMillis()+20000);
   		//startTimer.start();
   //	getGameReady((System.currentTimeMillis()-10000));
   		//playGame(System.currentTimeMillis());
   		try
   		{
   			out=new DataOutputStream(socket.getOutputStream());
   		}
   		catch(IOException e)
   		{
   		}
   		
   }

	protected Void doInBackground() throws Exception {
		try {
			 while(runner.getExit()==false)
		      {//updateNames();
		      
		      	in.read(type);
		      	byte[] size;
				 
				 int sizeInt;
				 byte[] read;
				System.out.println(type);
				int typInt=ByteHelper.bytesToInt(type);
				switch(typInt)
				{
				case 1:    // case 1 should not be needed for client side: it is from client to server only
							break;
				
				case 2:  //post("you have connected");
						  size= new byte[4];
						 in.read(size);
						 sizeInt=ByteHelper.bytesToInt(size);
						  read= new byte[sizeInt];
						 in.read(read);
						 String n="";//new String(read);
						 
						 for(int i=0; i<read.length;i++)
						 {
							 if((char)read[i]!= 0x1e)
							 {
								 n=n+(char)read[i];
							 }
							 else
								 n=n+"\n";
						 }
					postNames(n);
					//post(n);
					System.out.println(n);
						 	break;
				//don't really know what to do here
				
					
				case 3:  // Client sends 3 so client does not read in 3
						
					
					
				case 4:   
					//String n=new String(msg,5,8);
					byte[] timestamps= new byte[8];
					in.read(timestamps);
					time=ByteHelper.bytesToLong(timestamps);
					startTimer.start();
					break;
				
				
				case 6:
					System.out.println("ya got it");
				     size= new byte[4];
					 byte[] players= new byte[4];
				     in.read(players);
				    
				     in.read(size);
					 int num = ByteHelper.bytesToInt(players);
					 System.out.println("number of players: "+num);
					 int[] scores= new int[num];
					 String[] names=new String[num]; 
					 sizeInt=ByteHelper.bytesToInt(size);
					 System.out.println("Size is "+ sizeInt);
					 read= new byte[sizeInt];
					 in.read(read);
					 int index=0;
					 String str="";
					 String current="";
					 
					
					boolean convert=false;

					
					for(int i=0; i<read.length;i++)
					 {
						if((char)read[i]!= 0x1e)
						 {
							// str=str+(char)read[i];
							 current=current+(char)read[i];
						 }
						 else
						 {
							
							 //str=str+": ";
							 names[index]=current;
							 current=current+": ";
														 	 
							 byte[] nums=new byte[4];
							 	for(int j=0;j<4;j++)
								{
									i++;
									nums[j]=read[i];
									
								}
								i++;	 
							 int score=ByteHelper.bytesToInt(nums);
							 System.out.println("score is"+score );
						     str=str+"\n"+current + score;
							 scores[index]=score;
							 current="";
									 
							 index++;
							 
							 }
							 
						 }
					 
					
					post(str);
					int max=0;
					for(int i=1; i<num;i++)
					{
						if(scores[i]>scores[max])
							max=i;
					}
					
					post("The Winner is "+names[max]+" with "+scores[max]+" points");
					
					// */
					
				System.out.println(str);
					 	break;
					
				
				}
		
		      
		      }    
			
			}  
		catch(EOFException e){

			System.out.println("connection sucessfully closed Due to error");
		}
		catch (IOException e) {
			System.out.println("you gots errors bro");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
		
		//done();
		return null;
	} 

	
	
	public void getGameReady()
	{
	
		 now=	System.currentTimeMillis();
		//System.out.println(now);
		//System.out.println(time);
		if(now<time)
		{
			
			
			post("game will start in "+ ((time-now)/1000));
			
			
		}
		else
		{
			playGame(time);
			startTimer.stop();
		}
	}
	
	
	public void playGame(long seed)
	{
		
		game=new TestClient(seed);
		game.setVisible(true);
		runner.setVisible(false);
		long start = System.currentTimeMillis();
		long now =System.currentTimeMillis();
		long runtime = 30000L; 
//		while((now-start)<runtime)
//		{
//			now=System.currentTimeMillis();
//		}
//		
		Timer t= new Timer(30000,new ender());
		t.setRepeats(false);
		t.start();
	
//		
//		try {
//			sendScore(7);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		runner.setVisible(true);
		
	}

	
	
	
	public void sendScore()throws IOException
	{
		
		byte[] t= ByteHelper.intToBytes(5);
		
		
		
		byte[] nameBytes= myName.getBytes();
		byte[] size= ByteHelper.intToBytes(nameBytes.length);
		byte[] score=ByteHelper.intToBytes(game.getScore());
		byte[] send= new byte[t.length+nameBytes.length+size.length+score.length];
		for(int i=0;i<4;i++)
		{
			send[i]=t[i];
		}
		for(int i=0;i<4;i++)
		{
		send[i+4]=score[i];
		}
		for(int i=0;i<size.length;i++)
		{
			send[i+8]=size[i];
		}
		for(int i=0;i<nameBytes.length;i++)
		{
			send[i+12]=nameBytes[i];
		}
		out.write(send);
			
			
	}
	
	

	
	
	
	public void postNames(String s)
	{
	names.setText(s);
	}
	
	
	
	public void post(String s)
	{
		info.append("\n"+s);
	}
	
	
	/*
	public void updateNames()
	{
		Iterator<String> i= nameSet.iterator();
		String s="";
		String ap;
		while(i.hasNext())
		{	ap=i.next();
	//		if(ap.equals(myName))
		//	{	s=s+ap+"(me)\n";}
		//	else
				s= s+ap+"\n";
		}
		areaNames.setText(s);
	}
*/
	
	public String messageBuilder(int type, String content)
	{
			return "no";
	}
	
	@Override
	
	
	
	
	public void done(){
		try{
			in.close();
			out.close();
			socket.close();
			//System.exit(0);
		}
		catch(IOException e)
		{
			e.printStackTrace();}
	}

	public class starter implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			getGameReady();
		}
	}	
	
	
	
	
	public class ender implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			game.setVisible(false);
			runner.setVisible(true);
			try {
				sendScore();
			} catch (IOException ei) {
				// TODO Auto-generated catch block
				ei.printStackTrace();
			}
		}
	}	
	
	
	
}
	

