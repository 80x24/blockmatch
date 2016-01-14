# Block Match 
## A networked match three like game.

Block Match is a client-server match three like game written entirely
in Java. Any number of clients can connect to the server and play a
game simultaneously. The server then calculates the score and a winner
is determined at the end of the game.

Written using the Swing libraries for the GUI and Socket api for the server
Block Match includes a full multithreaded server for clients to connect
to. The clients send information to the server using a custom protocol
over TCP.

# How to Play
Simply compile all of the .java files in the server and client directories.
Then, to start the server, run ThreadedServer. The server will start on
port 25000 by default.

In order to connect using one of the clients, run ConnectGUI. From there
you can connect to a server by specifying an IP Address. 
