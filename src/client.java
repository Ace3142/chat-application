import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class client extends JFrame {

	Socket socket;
	BufferedReader br;
	PrintWriter out;
	
	//components
	
	private JLabel heading = new JLabel("Client Area");
	private JTextArea massage=new JTextArea();
	private JTextField messageInput=new JTextField();
	private Font font=new Font("Roboto",Font.PLAIN,20);
	
	
	
	
	
	
	//Constructor-----
	public client()
	{
		try {
			
			 System.out.println("Sending request to server");
			socket=new Socket("192.168.36.90",8081);
			System.out.println("connection done.");
			
			br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out=new PrintWriter(socket.getOutputStream());
			
			createGUI();
			handleEvents();
			startReading();
			//startWriting();
		
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	private void handleEvents() {
		messageInput.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				System.out.println("key release"+e.getKeyCode());
				if(e.getKeyCode()==10)
				{
					String contentToSend=messageInput.getText();
					massage.append("me:"+contentToSend+"\n");
					out.println(contentToSend);
					out.flush();
					messageInput.setText(" ");
					//messageInput.requestFocus();
				}
			}
			
		});
	}
	
	private void createGUI()
	{
		this.setTitle("Client masssage[END]");
		this.setSize(600,800);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//code
		heading.setFont(font);
		massage.setFont(font);
		messageInput.setFont(font);
		//heading.setBackground(getBackground(BLACK));
		
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		
		heading.setIcon(new ImageIcon("CircleLogo.png"));
		 heading.setHorizontalTextPosition(SwingConstants.CENTER);
		 heading.setVerticalTextPosition(SwingConstants.BOTTOM);
		 massage.setEdittable(false);
		 messageInput.setHorizontalAlignment(SwingConstants.RIGHT);
		 
		 
		
		//layout
		
		this.setLayout(new BorderLayout());
		// componenet of frame 
		this.add(heading,BorderLayout.NORTH);
		JScrollPane jScrollPane=new JScrollPane(massage);
		
		this.add(jSrcrollPane,BorderLayout.CENTER);
		this.add(messageInput,BorderLayout.SOUTH);
		
		
		
		this.setVisible(true);
	}
	
	// reading------method-----
	private void startReading() {
		// TODO Auto-generated method stub
		//thread will read and give
				//reading code
				Runnable r1=()->{
					System.out.println("connection started");
					while(true)
					{
						try {
							String msg= br.readLine();
							if(msg.equals("exit"))
							{
								System.out.println("client ended the chat");
								JOptionPane.showMessageDialog(this,"Server terminated the chat");
								messageInput.setEnabled(false);
								socket.close();
								break;
							}
							massage.append("Server :" +msg+"\n");
							
						}catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				};
				new Thread (r1).start();
	}
//writing--------method-----
	public void startWriting()
	{
		//thread will take and give to client
		//writing coding
		Runnable r2=()->{
			System.out.println("Writer start.....");
			
			try {
				while(socket.isClosed())
				{
	                BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
					
					String content=br1.readLine();
					out.println(content);
					out.flush();
					if(content.equals("exit")) {
						socket.close();
						break;
					}
				}
			}catch(Exception e)
			{
				System.out.println("connection closed");
			}
			
		};
		
		
		new Thread (r2).start();
		
	}
	public static void main(String[] args) {
		System.out.println("this is client");
		new client();
	}

}
