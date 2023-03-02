import javax.swing.*;
import javax.swing.border.*;

import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/*Client class contains methods to 
	connect to the server, 
	build the main chat window, 
	and handle window events. 
	The Client class also contains global variables 
	for the client thread, the user name, and the socket connection to the server 
*/
public class Client {

	private static ClientThread clientThread;
	private static int portNumber;
	private static String port_entered;

	public static String userName = "Anonymous";
	public static Socket SOCK;
	public static ObjectOutputStream output;

	// no need for input stream because client thread will read from server and send
	// it
	public static JFrame mainWindow = new JFrame();
	public static ImageIcon clientIcon;
	public static JPanel clientsGUI = new JPanel();

	public static JPanel topBar = new JPanel();
	public static JLabel top = new JLabel();

	public static JPanel userList = new JPanel();
	public static JList<String> userOnlineList = new JList<String>();
	public static JScrollPane listScroll = new JScrollPane();

	public static JPanel textCenter = new JPanel();
	public static JTextArea chatArea = new JTextArea();

	public static JPanel sendingArea = new JPanel();
	public static JButton sendButton = new JButton();
	public static JPanel messageTyped = new JPanel();
	public static JTextArea messageTypingArea = new JTextArea();

	public static JFrame logInWindow = new JFrame();
	public static JPanel logInWindowclientsGUI = new JPanel();
	public static ImageIcon logInIcon;

	public static JPanel portIntakeWindow = new JPanel();
	public static JLabel portPart = new JLabel("Enter Port Number: ");
	public static JTextField portNumberBox = new JTextField(20);

	public static JPanel UserNameTaker = new JPanel();
	public static JLabel logInEnterUsername = new JLabel("Enter Username: ");
	public static JTextField logInUsernameBox = new JTextField(20);

	public static JButton logInEnter = new JButton("Log In");

	public static void main(String[] args) throws UnknownHostException, IOException {
		Client.BuildMainWindow();
		Client.waitUntilConnection();
		Client.BuildLogInWindow();
	}

	public static void BuildMainWindow() {
		// Main window is the main window of our ClientclientsGUI
		mainWindow.setTitle("Joined ChatRoom As- " + userName);

		ConfigureMainWindow();

		setMainWindowActions();

		mainWindow.setVisible(true);
	}

	public static void ConfigureMainWindow() {

		/*
		 * This method sets up the various Swing components that
		 * make up the main chat window, such as the user list, chat
		 * display area, and message typing area.
		 */

		mainWindow.setContentPane(clientsGUI);
		mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainWindow.setMinimumSize(new Dimension(500, 500));
		mainWindow.pack();
		mainWindow.setLocationRelativeTo(null);

		clientIcon = new ImageIcon("bin/userNotLoggedIn.png");
		mainWindow.setIconImage(clientIcon.getImage());

		// setting top bar
		top.setText("AAiT Chat Room Offline");
		top.setBackground(new Color(0, 100, 139));
		top.setForeground(Color.BLACK);
		top.setFont(new Font("Arial", Font.BOLD, 14));
		top.setOpaque(true);
		top.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		// Set layout manager and spacing
		topBar.setLayout(new BorderLayout(5, 5));
		topBar.setBackground(Color.WHITE);
		// Add a titled border and set its color
		topBar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 2)));
		// Add component to the west side of the border layout
		topBar.add(top, BorderLayout.WEST);

		userOnlineList.setFont(new Font("Arial", Font.PLAIN, 14));
		userOnlineList.setBackground(new Color(255, 255, 204));
		userOnlineList.setSelectionBackground(new Color(204, 204, 204));
		userOnlineList.setSelectionForeground(Color.BLACK);
		userOnlineList.setVisibleRowCount(10);

		// setting the username list
		listScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		listScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		listScroll.setViewportView(userOnlineList);
		listScroll.setPreferredSize(new Dimension(100, 200));
		listScroll.setMinimumSize(new Dimension(100, 200));
		listScroll.setBorder(BorderFactory.createTitledBorder("Online Users"));
		listScroll.setBackground(new Color(238, 255, 238));

		// setting the sendButton Button
		sendButton.setText("SEND");
		sendButton.setPreferredSize(new Dimension(70, 50));
		sendButton.setMinimumSize(new Dimension(100, 30));
		sendButton.setFont(new Font("Arial", Font.BOLD, 9));
		sendButton.setBackground(new Color(0, 0, 139));
		sendButton.setForeground(Color.WHITE);
		sendButton.setFocusPainted(false);
		sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// setting the east
		userList.setLayout(new BorderLayout(5, 5));
		userList.add(listScroll, BorderLayout.CENTER);
		userList.setBackground(new Color(238, 255, 238));
		userList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// setting the chat display area
		chatArea.setText("");
		chatArea.setBorder(new LineBorder(Color.GREEN, 2));
		chatArea.setEditable(false);
		chatArea.setFont(new Font("Arial", Font.ITALIC, 17));
		chatArea.setBackground(new Color(210, 230, 255));
		chatArea.setForeground(new Color(51, 51, 51)); // Dark gray text color

		// setting the textarea to type chat
		messageTypingArea.setPreferredSize(new Dimension(400, 60));
		messageTypingArea.setEditable(true);
		messageTypingArea.setBorder(new LineBorder(Color.CYAN, 2));
		messageTypingArea.setLayout(new BorderLayout(5, 5));
		messageTypingArea.setFont(new Font("Arial", Font.PLAIN, 14));
		messageTypingArea.setBackground(new Color(255, 255, 255)); // White
		messageTypingArea.setForeground(new Color(51, 51, 51));

		messageTyped.setLayout(new BorderLayout(5, 5));
		messageTyped.add(new JScrollPane(messageTypingArea), BorderLayout.CENTER);

		sendingArea.setLayout(new BorderLayout(5, 5));
		sendingArea.add(messageTyped, BorderLayout.CENTER);
		sendingArea.add(sendButton, BorderLayout.EAST);

		textCenter.setLayout(new BorderLayout(5, 5));
		textCenter.add(new JScrollPane(chatArea), BorderLayout.CENTER);
		textCenter.add(sendingArea, BorderLayout.SOUTH);

		// setting everything in clientsGUI
		clientsGUI.setLayout(new BorderLayout(5, 5));
		clientsGUI.add(topBar, BorderLayout.NORTH);
		clientsGUI.add(userList, BorderLayout.EAST);
		clientsGUI.add(textCenter, BorderLayout.CENTER);

	}

	public static void setMainWindowActions() {
		/*
		 * This method handles various actions that can be
		 * performed within the main chat window, such as closing
		 * the window or sending a message.
		 */

		mainWindow.addWindowListener(
				new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						/*
						 * This code is implementing a method called windowClosing which is
						 * triggered when the user attempts to close the window.
						 * 
						 * Inside the method, it shows a confirmation dialog asking
						 * the user if they are sure they want to close the window.
						 * If the user selects "Yes", the method closes the output stream,
						 * input stream, and socket, and then exits the program. If the user selects
						 * "No", the method does nothing and the program continues running.
						 */
						int result = JOptionPane.showConfirmDialog(null, "Are you sure", "Confirm",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

						if (result == JOptionPane.YES_OPTION) {
							try {
								// close object output stream
								output.close();

								// close input stream coming from objects thread
								clientThread.in.close();

								// disconnect socket from server
								SOCK.close();
							}

				catch (IOException e1) {
								e1.printStackTrace();
							}
							// Stop the program and close the window
							System.exit(0);
						}

				else {
							// Do nothing and keep running
						}
					}
				});

		sendButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						/*
						 * This code adds an ActionListener to the "sendButton" JButton.
						 * When the button is clicked, the actionPerformed method is called
						 * which in turn calls the "sendTheMessage" method. If there is an
						 * IOException thrown by "sendTheMessage" method, it will be caught
						 * and printed to the console.
						 */
						try {
							sendTheMessage();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}

				});

		userOnlineList.addMouseListener(
				new MouseAdapter() {
					public void mouseClicked(MouseEvent e)
					/*
					 * When the user clicks on the component, the mouseClicked
					 * method of the MouseAdapter is called, which in turn calls
					 * the actionForUsersOnline method with the MouseEvent e as a parameter.
					 */
					{
						returnSelectedUser(e);
					}
				});

	}

	public static void returnSelectedUser(MouseEvent e) {
		if (e.getClickCount() == 3) {
			final String selectedUser = (String) userOnlineList.getSelectedValue();
			SwingUtilities.invokeLater(
					new Runnable() {
						public void run() {
							messageTypingArea.setText("@" + selectedUser + ": ");
							messageTypingArea.requestFocus();
						}
					});
		}
	}

	public static void sendTheMessage() throws IOException {
		/*
		 * This method sends the message entered by the user to the server
		 * using the sendMessage() method of the clientThread object. If the message
		 * field messageTypingArea is not empty, it sends the message and clears the
		 * field.
		 * 
		 * sendMessage() checks what type of message it is and sends it to the server using
		 * clients output
		 * 
		 * The requestFocus() method sets the focus back to the message field for
		 * the user to enter the next message. If there is an exception while sending
		 * the message, it will be thrown as an IOException.
		 */

		if (!messageTypingArea.getText().equals("")) {
			// calling send from clientThreadClass

			clientThread.sendMessage(messageTypingArea.getText());
			messageTypingArea.requestFocus();
			messageTypingArea.setText("");
		}
	}

	public static void waitUntilConnection() {
		/*
		 * This method sets the "Send" button to be disabled and
		 * the main window to be disabled, so that the user cannot interact with
		 * them until a connection to the chat server has been established.
		 */
		sendButton.setEnabled(false);
		mainWindow.setEnabled(false);
	}

	public static void Connect(int the_port) throws IOException {
		/*
		 * Connect(): This method creates a Socket to connect to
		 * the chat server and starts a new ClientThread to handle
		 * incoming messages from the server. It also sends the user's
		 * username to the server using an ObjectOutputStream.
		 */

		try {
			SOCK = new Socket(InetAddress.getLocalHost(), the_port);

			clientThread = new ClientThread(SOCK);

			// sending UserName
			output = new ObjectOutputStream(SOCK.getOutputStream());

			try {
				output.writeObject(userName);
				output.flush();
			}

			catch (IOException ioException) {
				JOptionPane.showMessageDialog(null, "Error - UserName not Sent!");
			}

			top.setText("AAiT Chat Room - Online");

			Thread eachClientsThread = new Thread(clientThread);
			eachClientsThread.start();

		} catch (Exception e) {
			throw new ConnectException();
		}

	}

	public static void BuildLogInWindow() {
		/*
		 * logInWindow.setTitle("Log In"); Sets the title of the window to "Log In".
		 * 
		 * ConfigureLogInWindow(); --> Calls a method to configure the Log In window
		 * components,
		 * such as setting their size and position font and styling.
		 * 
		 * LogInWindow_Action();: --> Calls a method to handle user actions on the Log
		 * In window components.
		 * 
		 * logInWindow.setVisible(true);: Makes the Log In window visible to the user.
		 */

		logInWindow.setTitle("Log In");
		ConfigureLogInWindow();
		LogInWindow_Action();
		logInWindow.setVisible(true);
	}

	public static void ConfigureLogInWindow() {
		// set up the main window
		logInWindow.setContentPane(logInWindowclientsGUI);
		logInWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		logInWindow.setMinimumSize(new Dimension(350, 150));
		logInIcon = new ImageIcon("bin/userNotLoggedIn.png");
		logInWindow.setIconImage(logInIcon.getImage());
		logInWindow.pack();
		logInWindow.setLocationRelativeTo(null);

		try {
			logInWindow.setLocationByPlatform(true);
			logInWindow.setMinimumSize(logInWindow.getSize());
		} catch (Throwable ignoreAndContinue) {
		}

		logInEnterUsername.setFont(new Font("Arial", Font.PLAIN, 15));
		logInEnterUsername.setHorizontalAlignment(JLabel.RIGHT);
		logInEnterUsername.setOpaque(true);
		logInEnterUsername.setBackground(new Color(220, 220, 220)); // Set a light gray background color

		portPart.setFont(new Font("Arial", Font.PLAIN, 15));
		portPart.setHorizontalAlignment(JLabel.RIGHT);
		portPart.setOpaque(true);
		portPart.setBackground(new Color(220, 220, 220)); // Set a light gray background color

		portIntakeWindow.setLayout(new GridLayout(1, 2, 2, 2));
		portIntakeWindow.add(portPart);
		portIntakeWindow.add(portNumberBox);
		portIntakeWindow.setOpaque(true);
		portIntakeWindow.setBackground(new Color(220, 220, 220)); // Set a light gray background color

		UserNameTaker.setLayout(new GridLayout(1, 2, 2, 2));
		UserNameTaker.add(logInEnterUsername);
		UserNameTaker.add(logInUsernameBox);
		UserNameTaker.setOpaque(true);
		UserNameTaker.setBackground(new Color(220, 220, 220)); // Set a light gray background color

		// Set up the login button with a colored background
		logInEnter.setFont(new Font("Arial", Font.PLAIN, 15));
		logInEnter.setForeground(Color.WHITE); // Set the text color to white
		logInEnter.setBackground(new Color(65, 105, 225)); // Set the background color to a dark blue
		logInEnter.setOpaque(true);
		logInEnter.setBorderPainted(false); // Remove the button border

		logInWindowclientsGUI.setLayout(new GridLayout(3, 1, 3, 3));
		logInWindowclientsGUI.add(UserNameTaker);
		logInWindowclientsGUI.add(portIntakeWindow);
		logInWindowclientsGUI.add(logInEnter);

	}

	public static void LogInWindow_Action() {
		logInEnter.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						actionsOnLogin();
					}

				});

		logInUsernameBox.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						actionsOnLogin();
					}
				});
	}

	public static void actionsOnLogin() {
		try {
			userName = logInUsernameBox.getText().trim();
			port_entered = portNumberBox.getText().trim();

			if (isNumeric(port_entered) && !userName.equals("")) {
				userName = logInUsernameBox.getText().trim();
				portNumber = Integer.parseInt(port_entered);

				Connect(portNumber);

				mainWindow.setTitle("Joined Chatroom As - " + userName);
				clientIcon = new ImageIcon("bin/UserLoggedIn.png");
				mainWindow.setIconImage(clientIcon.getImage());
				logInWindow.dispose();

				sendButton.setEnabled(true);
				messageTypingArea.setEnabled(true);
				messageTypingArea.setEditable(true);
				mainWindow.setEnabled(true);

				messageTypingArea.requestFocus();

			} else {
				JOptionPane.showMessageDialog(null, "Incorrect, Entry");
			}
		} catch (ConnectException ce) {
			userName = "";
			port_entered = "";
			JOptionPane.showMessageDialog(null, "Try Again with different values.");
			portNumberBox.setText("");
			// logInUsernameBox.setText("");

		} catch (Exception ee) {
			ee.printStackTrace();
		}

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