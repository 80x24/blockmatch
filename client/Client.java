import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Client extends JFrame {

	private JPanel contentPane;

	private Socket socket;
	private String receiveMsg;

	private String name;
	 DataOutputStream out;
	private JTextArea info;
	private JTextArea txNames;
	private DataInputStream in;
	private boolean exit;

	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws  
	 */
	public Client(String n, String server, String p) throws Exception {
		name=n;
		exit=false;
		 int servPort=Integer.parseInt(p);
		 socket = new Socket(server, servPort);
		    System.out.println("Connected to server");

		    receiveMsg = "";
		   //selfMsg=new Message(name,1, "");
		   // othersMsg=new Message("",0, "");
		    in = new DataInputStream(socket.getInputStream());
		      out =
		          new DataOutputStream(socket.getOutputStream());

		    send(1,name);
		      
		  
		  
	     //implement read and write operations using ThreadRead  
	     //out.writeUTF(selfMsg.toString());  //hello
		 
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 479, 294);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 0, 342, 221);
		scrollPane.setEnabled(false);
		contentPane.add(scrollPane);
		
		info = new JTextArea();
		scrollPane.setViewportView(info);
		
		JPanel panel = new JPanel();
		panel.setBounds(356, 0, 107, 221);
		panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		contentPane.add(panel);
		panel.setLayout(null);
		
		txNames = new JTextArea();
		txNames.setBounds(0, 0, 107, 221);
		panel.add(txNames);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 224, 453, 32);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
	
	
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					send(9,name);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				exit=true;
//			
			}
		});
		exitButton.setBounds(354, 5, 89, 23);
		panel_1.add(exitButton);
		
		JButton startButton = new JButton("Ready");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
			try {
				send(3,"");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		});
		startButton.setBounds(10, 5, 89, 23);
		panel_1.add(startButton);
	}
	
	public void runMain()
	{
		ClientThread t= new ClientThread(in, name, socket, info,txNames,this);
		t.execute();
	}
	
	
	public boolean getExit()
	{
		return exit;
	}
		
	
	public void send(int type, String content)throws IOException
	{
		byte[] t= ByteHelper.intToBytes(type);
		
		
		if(content.equals(""))
			out.write(t);
		else
			{
			byte[] c= content.getBytes();
			byte[] s= ByteHelper.intToBytes(c.length);
			byte[] send= new byte[t.length+c.length+s.length];
			for(int i=0;i<4;i++)
			{
				send[i]=t[i];
			}
			for(int i=0;i<4;i++)
			{
				send[i+4]=s[i];
			}
			for(int i=0;i<c.length;i++)
			{
				send[i+8]=c[i];
			}
			out.write(send);
			
			}
	}
	
	
	

}
