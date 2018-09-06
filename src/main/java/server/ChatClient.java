package server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {

    private String hostName;
    private int port;
    private String userName;

    public ChatClient(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void execute(){
        try{
            Socket socket = new Socket(hostName, port);
            System.out.println("Connected to the chatserver");

            Thread read = new Thread(new ReadThread(socket, this));
            read.start();

            Thread write = new Thread(new WriteThread(socket, this));
            write.start();

        }catch (UnknownHostException e){
            System.out.println("server not found: " + e.getMessage());
        }catch (IOException e){
            System.out.println("IOException: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        if(args.length < 2) return;

        String hostName = args[0];
        int port = Integer.parseInt(args[1]);

        ChatClient chatClient = new ChatClient(hostName, port);
        chatClient.execute();
    }
}
