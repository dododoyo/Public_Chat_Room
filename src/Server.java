import java.net.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Server {

	JFrame serverGui;
	JTextArea displayWindow;
	ImageIcon serverIcon;

	private ServerSocket serverSocket;
	private Socket socket;

	// holds the output streams for all connected clients.
	public Hashtable<Socket, ObjectOutputStream> outputStreams;

	// mainly used to send secret chats
	public Hashtable<String, ObjectOutputStream> clients;

	// constructor that creates the server socket and sets up the server GUI
	public Server(int port) throws IOException {
		// create the server GUI
		serverGui = new JFrame("Server");
		serverGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverGui.setSize(600, 400);

		serverIcon = new ImageIcon("bin/server.png");
		serverGui.setIconImage(serverIcon.getImage());
		Container ServerWindow = serverGui.getContentPane();

		// create the text area for displaying messages
		displayWindow = new JTextArea();
		displayWindow.setEditable(false);
		displayWindow.setLineWrap(true);
		displayWindow.setWrapStyleWord(true);
		displayWindow.setFont(new Font("Arial", Font.PLAIN, 25));
		displayWindow.setForeground(Color.GREEN);
		displayWindow.setBackground(Color.DARK_GRAY);

		// add a scroll pane to the text area
		JScrollPane scrollPane = new JScrollPane(displayWindow);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// set the background color of the server GUI
		ServerWindow.setBackground(Color.DARK_GRAY);

		// add the scroll pane to the server GUI
		ServerWindow.add(scrollPane);

		// make the server GUI visible
		serverGui.setVisible(true);

		// create the hash tables for the output streams and clients
		outputStreams = new Hashtable<Socket, ObjectOutputStream>();
		clients = new Hashtable<String, ObjectOutputStream>();

		// create the server socket
		serverSocket = new ServerSocket(port);

		// display a message indicating that the server is waiting for clients
		showMessage("Waiting for clients at Port " + serverSocket.getLocalPort());
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		// Ask user for the desired port number
		String port_entered = "";

		do {
			port_entered = JOptionPane.showInputDialog("Please Enter Port Number.");
		} while (!isNumeric(port_entered));

		Server server = new Server(Integer.parseInt(port_entered));
		server.waitingForClients();

	}

	/*
	 * sets up the server to continuously listen for incoming client
	 * connections and create a new thread to handle each connection.
	 */
	public void waitingForClients() throws IOException, ClassNotFoundException {

		while (true) {
			socket = serverSocket.accept();
			new ServerThread(this, socket);
		}
	}

	// displaying message on Server when clients connect and disconnect
	public void showMessage(final String message) {

		SwingUtilities.invokeLater(
				new Runnable() {
					@Override
					public void run() {
						displayWindow.append(message);
					}
				});
	}

	public void sendToAll(Object data) throws IOException {

		for (Enumeration<ObjectOutputStream> e = getOutputStreams(); e.hasMoreElements();) {
			/*
			 * this synchronized block ensures that the method is thread-safe,
			 * so that multiple clients can be added or removed
			 * from the outputStreams Hashtable at the same time
			 * as messages are being sent. The block also ensures
			 * that the method won't remove a client's output stream
			 * from the outputStreams Hashtable while that client's
			 * message is being sent, avoiding a potential
			 * ConcurrentModificationException.
			 */
			synchronized (outputStreams) {
				ObjectOutputStream tempOutput = e.nextElement();
				tempOutput.writeObject(data);
				tempOutput.flush();
			}
		}
	}
	// Sending a message to all the available clients

	// To get Output Stream of the available clients from the hash table
	private Enumeration<ObjectOutputStream> getOutputStreams() {
		return outputStreams.elements();
	}

	// Sending private message to specific user
	public void sendPrivately(String username, String message) throws IOException {

		/*
		 * The method retrieves the ObjectOutputStream for
		 * the specified client from the clients Hashtable
		 */

		ObjectOutputStream tempOutput = clients.get(username);
		tempOutput.writeObject(message);
		tempOutput.flush();
		/* flushes it to ensure the message is sent immediately. */
	}

	// Removing the client from the client hash table
	// responsible for sending a private message to
	// a specific client, identified by their username.

	public void removeClient(String username) throws IOException {

		synchronized (clients) {
			clients.remove(username);
			sendToAll("!" + clients.keySet());
		}
		/*
		 * the synchronized keyword is used to
		 * synchronize on the clients object. This means that when one thread
		 * is executing this method,
		 * 
		 * '''no other thread can modify the clients object
		 * until the first thread is finished.'''
		 * 
		 * This ensures that the map is not being modified by multiple threads at the
		 * same time and helps to prevent race conditions and inconsistent data.
		 */
	}

	// Removing a connection from the outputStreams hash table and closing the
	// socket
	public void removeConnection(Socket socket, String username) throws IOException {

		synchronized (outputStreams) {
			outputStreams.remove(socket);
		}

		// Printing out the client along with the IP offline in the format of
		// ReetAwwsum(123, 12, 21, 21) is offline
		showMessage("\n" + username + " is offline");

	}

	public static boolean isNumeric(String str) {
		if (str == null || str.isEmpty()) {
			return false;
		}
		try {
			int port = Integer.parseInt(str);
			return (port >= 1 && port <= 65535);
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
}
