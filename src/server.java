import java.net.Socket;
import java.net.ServerSocket;

import java.io.*;
public class server {

	ServerSocket Server;
	Socket socket;
	BufferedReader br;
	PrintWriter out;
	//constructor
	public server() {
		
		
		try {
			Server= new ServerSocket(8081);
			System.out.println("server is ready to accept connection");
			System.out.println("waiting....");
			socket=Server.accept();
			br =new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out=new PrintWriter(socket.getOutputStream());
			
			startReading();
			startWriting();
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	public void startReading()
	{
		//thread will read and give
		//reading code
		Runnable r1=()->{
			System.out.println("connection started");
			try{ 
				while(true)
			    {
				
					String msg= br.readLine();
					if(msg.equals("exit"))
					{
						System.out.println("client ended the chat");
						socket.close();
						break;
					}
				    	System.out.println("You->");
				      	System.out.println("Client : "+msg);
					
				    }
		       }catch(Exception e)
				{
					e.printStackTrace();
					System.out.println("connextion closed");
				}
			
		};
		new Thread (r1).start();
	}

	public void startWriting()
	{
		//thread will take and give to client
		//writing coding
		Runnable r2=()->{
			System.out.println("Writer start.....");
			
			while(!socket.isClosed())
			{
				try {
					
				BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
				
				String content=br1.readLine();
				out.println(content);
				out.flush();
					
				}catch(Exception e)
				{
				  //	e.printStackTrace();
					System.out.println("connection closed");
				}
			}
			System.out.println("connection closed");
			
		};
		
		
		new Thread (r2).start();
		
	}
	
	public static void main(String[] args) {
		System.out.println("this is server..going to start server");
		new server();

	}

}
