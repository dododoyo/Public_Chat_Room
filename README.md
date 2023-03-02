# Public Chatroom

This is a simple chat application that allows users to connect to a server and chat with each other.

---
## Getting Started
---

- Download or clone the project repository to your local machine.
- Open a command prompt or terminal window and navigate to the root directory of the project.
- Run the Server.java script to start the server with the desired port number.
- Once the server is running, it will show a message saying `Waiting for clients on port ----`
- Enter the user name and port number for the server in the corresponding text fields and click the "Log In" button.
- To create another client you should just run client.java again with different user name.
  

### NOTE

- Make sure that the server is running before starting the client application.
- Make sure that the port number of the server and your entry are the same.
- If the connection is successful, a new window will open where you can send and receive messages.
  
---
## Classes And Methods
---

## *Client*
`Client` is responsible for connecting to the server building the main chat window, and handling window events.The `Client` class also contains global variables for the client thread, the user name, and the socket connection to the server 
`BuildMainWindow` : is responsible for setting up the main chat window after the user logs in. 

- `ConfigureMainWindow()` sets up the various Swing components that make up the main chat window. 
  
- `setMainWindowActions() ` this method is responsible for handling user interactions with the chat client's main window and responding to them accordingly.by setting up several event listeners for a chat client's main window.
  
- `returnSelectedUser`: This method is called when a user triple-clicks on a username in the online user list. It retrieves the selected username, adds the "@" symbol and a colon to the message typing area and sets the focus to the message typing area. This is a convenient way to mention a user in a message by automatically adding their username to the message to send them a private message.
  
- `sentTheMessage`: sends the message entered by the user to the server using the sendMessage() method of the clientThread object. It first checks if the message field messageTypingArea is not empty. If it is not empty, it sends the message using the sendMessage() method and then clears the message field. then it focus back to the message field using the requestFocus() method.
  
- `waitUntilConnection`: This method sets the "Send" button to be disabled and the main window to be disabled, so that the user cannot interact with them until a connection to the chat server has been established.

- `Connect(int the_port)` establishes a connection with the chat server, sends the user's username, and starts a thread to handle incoming messages from the server. it creates a socket to connect to a chat server and starts a new `ClientThread` to handle incoming messages from the server. It also sends the user's username to the server using an ObjectOutputStream. The method takes an integer the_port parameter which specifies the port number to connect to.

- `BuildLogInWindow`: Sets the title of the window to "Log In",calls a method to configure the Log In window components,such as setting their size and position font and styling.

- `ConfigureLogInWindow` sets up the graphical user interface (GUI) for the login window
  
- `LogInWindow_Action` : defines two action listeners for the login window. The first listener is added to the "logInEnter" button and the second is added to the "logInUsernameBox" text field. Both listeners call the "actionsOnLogin()" method when an action (i.e. button press or text input) is performed.

- `actionsOnLogin` : This method is called when the user clicks the login button on the login window. It retrieves the entered username and port number from the corresponding text fields. If the port number is numeric and the username is not empty, it calls the `Connect()` method to connect to the chat server using the entered port number and sends the username to the server. If the connection is successful, the login window is closed, the main window is enabled, and the message typing area is focused for the user to start typing messages. If the connection is not successful, an error message is displayed, and the port number text field is cleared. If any other exceptions occur, they are printed to the console.

- `isNumeric(Stirng str)` : takes a string as input and returns a boolean value indicating whether the string represents a valid integer within the range of valid port numbers (1-65535).

## *ClientThread*
  `ClientThread` class is responsible for handling incoming messages from the server and updating the client's user interface accordingly. It also provides a method for sending messages to the server.

- `ClientThread(Socket clientSocket)` This is the constructor for the ClientThread class. It receives a Socket object representing the client's connection to the server and initializes the clientSocket member variable.

- `run()`: This method is called when the thread is started. It creates an ObjectInputStream to read messages from the server and enters a loop that calls recieveMessages() repeatedly.

- `recieveMessages()`: This method reads messages from the server and updates the client's user interface accordingly. The method starts by reading an object from the input stream using in.readObject(). Depending on the content of the message, the method updates the user list, appends a message to the chat area, or ignores the message.

- `sendMessage(final String str)`: This method sends a message to the server. If the message starts with "@" (indicating a private message), the message is sent as-is. Otherwise, the message is prefixed with "@ALL@|" to indicate that it is a message intended for all connected clients. The message is then written to the output stream using Client.output.writeObject() and flushed using Client.output.flush().
  
  <br>

## *Server*
`Server` class creates a GUI that displays messages and waits for clients to connect. When a client connects, the server creates a new thread to handle the connection. The server can send messages to all connected clients or send private messages to specific clients.

- `Server`: A constructor method for the Server class that initializes various instance variables and creates a server GUI.

- `main`: The main method of the program that prompts the user for a port number and creates an instance of the Server class.

- `waitingForClients`: A method that sets up the server to continuously listen for incoming client connections and creates a new thread to handle each connection.

- `showMessage`: A method that displays a message on the server GUI indicating that a client has connected or disconnected.

- `sendToAll`: A method that sends a message to all the clients currently connected to the server.

- `sendPrivately`: A method that sends a private message to a specific client identified by their username.

- `removeClient`: A method that removes a client from the client hash table responsible for sending a private message to a specific client, identified by their username.

- `removeConnection`: A method that removes a connection from the outputStreams hash table and closes the socket.

- `isNumeric`: A helper method that checks whether a given string is a valid integer.

<br>

## *ServerThread*

`ServerThread` class: extends the Thread class and is responsible for handling the communication with a single client.
`ServerThread(server, socket)` - Constructor to initialize the instance variables and add the client to the list of clients connected to the server.

- `server` - reference to the server object

- `socket` - reference to the client socket object

- `input` - object input stream to read incoming messages from the 
`output` - object output stream to send messages to the client

- `username` - username of the client

- `message` - the incoming message received from the client

- `run()` - Overriden method that handles the communication between server and client. The method receives incoming messages from the client and decides if it's a private or public message. If it is a public message, the message is sent to all connected clients. If it is a private message, the message is sent to the intended recipient. The method also removes the client from the server if the client disconnects.

<br>

---
## Prerequisites
---

- Java Development Kit (JDK) installed on your computer
- Basic understanding of Java programming language
- Knowledge of socket programming and networking concepts
- Preffered to have IDE for Java programming (such as VSCode or IntelliJ IDEA)
- Familiarity with GUI programming in Java (JavaSwing) if you want to modify the GUI of the application.

<br>

---
## Project Limitations
---

- The application is currently limited to text-based communication only.
- The application has poor Graphical User Interface.
- Doesn't have a database to hold data.
- Lacks several key features, such as user authentication and encryption.
  
<br>

---
## Future Improvements
---

- [ ] Implement support for multimedia messages, such as images and videos.
- [ ] Improve the user interface to be more intuitive and user-friendly.
- [ ] Add a database.
- [ ] Add user authentication and encryption.
  
<br>

---
## Built With
---

* [**Java**](https://www.java.com/en/) 

<br>

---
## Author
---

*  [**Dolphin Mulugeta**](https://github.com/Dooyo)

<br>

---
## Acknowledgments
---

* To Almighty God
* To Our Instructor Hailemelekot D.
* Addis Ababa Institute Of Technology

