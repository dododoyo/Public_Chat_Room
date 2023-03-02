# Chat Application

This is a simple chat application that allows users to connect to a server and chat with each other.


## Getting Started

- Download or clone the project repository to your local machine.
- Open a command prompt or terminal window and navigate to the root directory of the project.
- Run the Server.java script to start the server.
- Once the server is running, run the ClientGUI2.java  to start the client application not the ClientGUI.
- Enter the user name and port number for the server in the corresponding text fields and click the "Connect" button.
- If the connection is successful, a new window will open where you can send and receive messages.
  
### NOTE
- Make sure that the server is running before starting the client application.

## Prerequisites

- Java Development Kit (JDK) installed on your computer
- Basic understanding of Java programming language
- Knowledge of socket programming and networking concepts
- IDE for Java programming (such as Eclipse or IntelliJ IDEA)
- Familiarity with GUI programming in Java (JavaFX or Swing) if you want to modify the GUI of the application.

## Project Limitations

- The application is currently limited to text-based communication only.
- The application has poor Graphical User Interface.

## Future Improvements


- Implement support for multimedia messages, such as images and videos.
- Improve the user interface to be more intuitive and user-friendly.



## Classes and Functions

### ChatServer

The `ChatServer` class is responsible for managing client connections and forwarding messages between clients. It has the following functions:

- Initializes the server with the given host and port.
- `start()` : Starts the server, accepting incoming client connections.
- `broadcast(message, sender)` : Sends the given message to all connected clients except for the sender.
- `remove_client(self, client)` : Removes the given client from the list of connected clients.

### ChatClient

The `ChatClient` class is responsible for connecting to the server and sending and receiving messages. It has the following functions:

-  Initializes the client with the given host, port, and username.
- `connect()` : Connects the client to the server.
- `send_message(message)` : Sends the given message to the server.
- `receive_message()` : Receives a message from the server.

### ChatGUI

The `ChatGUI` class is responsible for displaying the user interface and handling user input. It has the following functions:

- Initializes the GUI.
- `connect_to_server()` : Connects the user to the server.
- `send_message()` : Sends a message to the server.
- `display_message(message)` : Displays a message in the chat area.

### ChatWindow

The `ChatWindow` class is responsible for displaying the chat window and handling user input. It has the following functions:

- Initializes the chat window with the given username, host, and port.
- `send_message()`: Sends a message to the server.
- `display_message(message)`: Displays a message in the chat area.

### Main

The `Main` class is responsible for starting the application. It has the following function:

- `main()`: Creates a `ChatGUI` object and starts the main loop of the application.
  
## Built With

* [**Java**](https://www.java.com/en/) 

## Author

*  [**Dolphin Mulugeta**](https://github.com/Dooyo)
*  [**Fares Christian**](https://github.com/PHARCHR)

## Acknowledgments

* To Almighty God
* To Our Instructor Hailemelekot D.
* Addis Ababa Institute Of Technology

