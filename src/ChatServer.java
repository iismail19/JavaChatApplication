import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
	
	static ArrayList<String> userNames = new ArrayList<String>();
	static ArrayList<PrintWriter> printWriters = new ArrayList<PrintWriter>();
	

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		System.out.println("Waiting for clients...");
		ServerSocket ss = new ServerSocket(5816);
		
		//To keep waiting for incoming clients connections
		while(true)
		{
			Socket soc = ss.accept();
			System.out.println("Connection established");
			//Create a multi-thread server
			//Each Thread handles a particular client Connection
			ConversationHandler handler = new ConversationHandler(soc);
			handler.start(); 
		}
		
	}
}

//Create a Thread Class
class ConversationHandler extends Thread{
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	String name;
	PrintWriter pw;
	static FileWriter fw;
	static BufferedWriter bw;
	
	public ConversationHandler(Socket socket) throws IOException{
		this.socket = socket;
		if(System.getProperty("os.name").toLowerCase().contains("win"))
		{
			fw = new FileWriter(System.getProperty("user.home") + "\\Desktop" +"\\ChatServer-Log.txt", true);//for windows computers
		}
		else
		{
			fw = new FileWriter(System.getProperty("user.home") + "/Desktop" +"/ChatServer-Log.txt", true);//true means append to existing file

		}
		bw = new BufferedWriter(fw);
		pw = new PrintWriter(bw, true); //true means auto flush
	}
	
	public void run(){
		try
		{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			int count = 0;
			while(true)
			{
				if(count > 0)
				{
					out.println("NAMEALREADYEXISTS");
				}
				else
				{
					out.println("NAMEREQUIRED");
				}
				
				name = in.readLine();
				
				if(name == null)
				{
					return;
				}
				
				if(!ChatServer.userNames.contains(name))
				{
					ChatServer.userNames.add(name);
					break;
				}
				count++;
			}
			
			out.println("NAMEACCEPTED"+name);
			ChatServer.printWriters.add(out);
			
			while(true)
			{
				String message = in.readLine();
				
				if(message == null)
				{
					return;
				}
				//this message writes to a file
				pw.println(name+ ": " + message);
				
				for(PrintWriter writer : ChatServer.printWriters)
				{
					writer.println(name + ": " + message);
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
}





