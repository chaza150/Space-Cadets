import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class Client extends Frame implements Runnable, WindowListener, ActionListener {

	private static final long serialVersionUID = 1L;
	protected DataInputStream i;
	protected DataOutputStream o;
	protected TextArea conversation;
	protected TextField input;
	protected Thread listener;
	boolean running;
	
	public static void main(String args[]) throws IOException{
		if(args.length != 2)
			throw new RuntimeException("Syntax: Client <host> <port>");
		Socket s = new Socket(args[0], Integer.parseInt(args[1]));
		new Client("Chat " + args[0] + ":" + args[1], s.getInputStream(), s.getOutputStream());
	}
	
	public Client(String title, InputStream in, OutputStream out) {
		super(title);
		running = true;
		this.i = new DataInputStream(new BufferedInputStream(in));
		this.o = new DataOutputStream(new BufferedOutputStream(out));
		setLayout(new BorderLayout());
		add ("Center", conversation = new TextArea());
		conversation.setEditable(false);
		add("South", input = new TextField());
		input.addActionListener(this);
		addWindowListener(this);
		pack();
		setVisible(true);
		input.requestFocus();
		listener = new Thread(this);
		listener.start();
		
	}
	
	public void run() {
		try {
			while(running) {
				String line = i.readUTF();
				conversation.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			listener = null;
			input.setVisible(false);
			validate();
			try {
				o.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
			try {
				o.writeUTF(input.getText());
				o.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
				running = false;
			}
			input.setText("");	
	}

	public void windowActivated(WindowEvent arg0) {}
	
	public void windowClosed(WindowEvent arg0) {}
	@Override
	public void windowClosing(WindowEvent arg0) {
		if(listener != null)
			running=false;
		setVisible(false);
		try {
			o.writeUTF("Client from " + InetAddress.getLocalHost().getHostAddress() + " has exited the room.");
			o.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	public void windowDeactivated(WindowEvent arg0) {	}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}
	
}
