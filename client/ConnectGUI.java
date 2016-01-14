import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class ConnectGUI extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldName;
	private JTextField textFieldServer;
	private JTextField textFieldPort;
	private static ConnectGUI frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
		

			public void run() {
				try {
					frame = new ConnectGUI();
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
	public ConnectGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 322, 236);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(66, 44, 46, 14);
		contentPane.add(lblName);
		
		JLabel lblServer = new JLabel("Server:");
		lblServer.setBounds(66, 78, 46, 14);
		contentPane.add(lblServer);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(66, 115, 46, 14);
		contentPane.add(lblPort);
		
		textFieldName = new JTextField();
		textFieldName.setText("William");
		textFieldName.setBounds(122, 41, 86, 20);
		contentPane.add(textFieldName);
		textFieldName.setColumns(10);
		
		textFieldServer = new JTextField();
		textFieldServer.setText("localhost");
		textFieldServer.setText("10.90.115.217");
		textFieldServer.setBounds(122, 75, 86, 20);
		contentPane.add(textFieldServer);
		textFieldServer.setColumns(10);
		
		textFieldPort = new JTextField();
		textFieldPort.setText("27000");
		textFieldPort.setText("25000");
		textFieldPort.setBounds(122, 112, 86, 20);
		contentPane.add(textFieldPort);
		textFieldPort.setColumns(10);
		
		JButton btnJoin = new JButton("Join");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					joinChat();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnJoin.setBounds(97, 152, 89, 23);
		contentPane.add(btnJoin);
	}
	
	
	public void joinChat() throws Exception
	{
		frame.dispose();// close the first window
		Client chatWindow= new Client(textFieldName.getText(),
						textFieldServer.getText(),textFieldPort.getText());
		chatWindow.setVisible(true);
		chatWindow.runMain();
	}
}
