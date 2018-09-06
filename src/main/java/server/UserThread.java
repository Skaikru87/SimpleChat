package server;

import java.io.*;
import java.net.Socket;

public class UserThread implements Runnable {


    private Socket socket;
    private ChatServer chatServer;
    private PrintWriter writer;

    public UserThread(Socket socket, ChatServer chatServer) {
        this.socket = socket;
        this.chatServer = chatServer;
    }

    @Override
    public void run() {

        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine();
            chatServer.addUserName(userName);

            String serverMessage = "New user connected: " + userName;
            chatServer.broadcast(serverMessage, this);

            String clientMessage;

            do {
                clientMessage = reader.readLine();
                serverMessage = "user: " + userName + "-->" + clientMessage;
                chatServer.broadcast(serverMessage, this);
            } while (!clientMessage.equals("bye"));
            chatServer.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " has quitted.";
            chatServer.broadcast(serverMessage, this);

        } catch (IOException e) {
            System.out.println("Error in user thread: " + e.getMessage());
            e.printStackTrace();
        }


    }

    private void printUsers() {
        if (chatServer.hasUsers()) {
            writer.println("Connected users: " + chatServer.getUserNames());
        } else {
            writer.println("No users connected");
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }
}
