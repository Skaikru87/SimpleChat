package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {

    private int port;
    private Set<String> userNames = new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();

    public ChatServer(int port) {
        this.port = port;
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Start server on port: " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");
                UserThread user = new UserThread(socket, this);
                userThreads.add(user);
                Thread userThread = new Thread(user);
                userThread.start();
            }
        } catch (IOException e) {
            System.out.println("server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("correct syntax: java ChatServer <port-number>");
            System.exit(0);
        }

        int port = Integer.parseInt(args[0]);
        ChatServer chatServer = new ChatServer(port);
        chatServer.execute();

    }

    void addUserName(String userName) {
        userNames.add(userName);
    }

    void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("The user " + userName + " quitted");
        }
    }

    public boolean hasUsers() {
        return !this.userNames.isEmpty();
    }

    public Set<String> getUserNames() {
        return userNames;
    }

    public void broadcast(String serverMessage, UserThread userThread) {
        for (UserThread user : userThreads) {
            if (user != userThread){
                user.sendMessage(serverMessage);
            }
        }
    }
}
