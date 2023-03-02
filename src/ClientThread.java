import java.net.*;
import java.io.*;
import javax.swing.*;
import java.util.*;

public class ClientThread implements Runnable {
	// Globals
	Socket clientSocket;

	// uses clients output to send messages

	// and input stream that comes from server to recieve messages
	public ObjectInputStream in;

	// used to store the array of connected clients that is
	// sent to the JList

	String[] currentUsers;

	// Constructor getting the socket
	public ClientThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {

		try {
			in = new ObjectInputStream(clientSocket.getInputStream());
			while (true) {
				recieveMessages();
			}
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, ioe.getStackTrace());
		} catch (ClassNotFoundException cnfe) {
			JOptionPane.showMessageDialog(null, cnfe.getStackTrace());
		} catch (Exception E) {
			JOptionPane.showMessageDialog(null, E.getStackTrace());
		}

	}

	public void recieveMessages() throws IOException, ClassNotFoundException {
		/*
		 * If the message starts with a !, it means that it contains a list of currently
		 * online users.
		 * The method extracts the usernames from the message, sorts them and updates
		 * the user list
		 * in the GUI using the SwingUtilities.invokeLater() method.
		 * 
		 * If the message starts with @ALL@|, it means that it is a broadcast message
		 * from the
		 * server to all clients.The method extracts the actual message content and
		 * appends it to
		 * the chat window using SwingUtilities.invokeLater().
		 * 
		 * If the message starts with @, it means
		 * that it is a private message sent to this client. The method extracts the
		 * message
		 * content and appends it to the chat window using SwingUtilities.invokeLater().
		 */

		if (!in.equals(null)) {
			// Convert read object into string
			String message = (String) in.readObject();

			if (message.startsWith("!")) {
				String temp1 = message.substring(1);
				temp1 = temp1.replace("[", "");
				temp1 = temp1.replace("]", "");

				currentUsers = temp1.split(", ");
				Arrays.sort(currentUsers);

				try {

					SwingUtilities.invokeLater(
							new Runnable() {
								public void run() {
									Client.userOnlineList.setListData(currentUsers);
								}
							});
				}

				catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Unable to set Online list data");
				}
			}

			else if (message.startsWith("@ALL@|")) {
				final String temp2 = message.substring(6);

				SwingUtilities.invokeLater(
						new Runnable() {
							public void run() {
								Client.chatArea.append("\n" + temp2);
							}
						});
			}

			else if (message.startsWith("@")) {
				final String temp3 = message.substring(1);

				SwingUtilities.invokeLater(
						new Runnable() {
							public void run() {
								Client.chatArea.append("\n" + temp3);
							}
						});
			}

		}
	}

	public void SEND(final String str) throws IOException {
		/*
		 * This is a method for sending a message to the server. It takes a String str
		 * as
		 * input and sends it to the server.
		 * 
		 * If the String starts with "@", it is considered
		 * a private message and is displayed in the client's chat window with the
		 * username prefix.
		 * 
		 * If the String does not start with "@", it is considered a message to be sent
		 * to all users
		 * and is displayed in the chat window with "@ALL@|" prefix followed by the
		 * username. The message
		 * is then sent to the server using the output object of the Client class
		 */

		String writeStr;
		if (str.startsWith("@")) {
			writeStr = str;
		}

		else
			writeStr = "@ALL@|" + Client.userName + ": " + str;

		Client.output.writeObject(writeStr);
		Client.output.flush();

	}
}
