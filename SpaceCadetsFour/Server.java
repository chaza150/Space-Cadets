import java.net.*;
import java.io.*;
import java.util.*;

public class Server {

	public Server(int port) throws IOException{
		ServerSocket server = new ServerSocket(port);
		while(true) {
			Socket client = server.accept();
			System.out.println("Acceptance from " + client.getInetAddress());
			ChatHandler c = new ChatHandler(client);
			c.start();
		}
	}
	
	public static void main(String[] args) {
		if(args.length != 1) 
			throw new RuntimeException("Syntax: Server <port>");
		try {
			new Server(Integer.parseInt(args[0]));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
