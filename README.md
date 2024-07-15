Universidad Galileo

🧑‍💻 Mariano Tahay

<center>

# Sockets TCP and UDP

</center>

![Networking](https://images.pexels.com/photos/159304/network-cable-ethernet-computer-159304.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1)

## Build

`javac Server.java`

`javac Client.java`

## Using - TCP

### Server

<u>Format</u>: **port** [0000] **protocol** [TCP]

Example:

`java Server port 3001 protocol TCP`

### Client

<u>Format</u>: **protocol** [TCP] **server** [0.0.0.0] **port** [0000]

Example:

`java Client protocol TCP server localhost port 3001`

## Using - UDP 

### Server

<u>Format</u>: **port** [0000] **protocol** [TCP]

Example:

`java Server port 3001 protocol TCP`

### Client

<u>Format</u>: **protocol** [TCP] **server** [0.0.0.0] **port** [0000]

Example:

`java Client protocol TCP server localhost port 3001`

## Documentation

### **TCP** - Connection

Creating the TCP server:

- [ServerSocket (Java Plarform SE 8)](https://docs.oracle.com/javase/8/docs/api/java/net/ServerSocket.html)

Create a connection between the Server and the Client:

- [Socket (Java Plarform SE 8)](https://docs.oracle.com/javase/8/docs/api/java/net/Socket.html)

### **UDP** - Connection

Create the "connection" between the Server and the Client:

- [DatagramSocket (Java Platform SE 8)](https://docs.oracle.com/javase/8/docs/api/java/net/DatagramSocket.html)

Sending and receiving data for both, the Servern and the Client:

- [DaragramPacket (Java Platform SE 8)](https://docs.oracle.com/javase/8/docs/api/java/net/DatagramPacket.html)