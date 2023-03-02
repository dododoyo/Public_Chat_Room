import java.net.*;
import java.io.*;

public class ServerThread extends Thread {

	private Server server;
	private Socket socket;

	private ObjectInputStream input;
	private ObjectOutputStream output;

	private String username;
	Object message;

	public ServerThread(Server server, Socket socket) throws IOException, ClassNotFoundException {

		this.server = server;
		this.socket = socket;

		output = new ObjectOutputStream(this.socket.getOutputStream());
		output.flush();

		input = new ObjectInputStream(this.socket.getInputStream());

		username = (String) input.readObject();

		server.clients.put(username, output);
		server.outputStreams.put(socket, output);

		server.sendToAll("!" + server.clients.keySet());

		server.showMessage("\n" + username + " is online");
		// starting the thread
		start();
	}

	public void run() {

		try {
			// Thread will run until connections are present
			while (true) {
				try {
					message = input.readObject();
				} catch (Exception e) {
					// stop current thread
					// throws UnsupportedOperationException()
					stop();
				}

				if (message.toString().contains("@ALL@")) {
					server.sendToAll(message);
				}

				else {
					// Decoding the string for private reciever
					String messageToString = message.toString();

					String formattedMsg = "@" + username
							+ messageToString.substring((messageToString.indexOf(':')), messageToString.length());

					String userNameInMessage = messageToString.substring(1, messageToString.indexOf(':'));

					/*
					 * It then sends the message to the intended recipient using the
					 * "server" object's sendPrivately() method
					 */

					server.sendPrivately(userNameInMessage, formattedMsg);
				}
			}
		}

		catch (IOException e) {

			e.printStackTrace();
		}

		finally {
			try {
				server.removeClient(username);
				server.removeConnection(socket, username);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
