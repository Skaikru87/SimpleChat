package server;

import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class WriteThread implements Runnable {

    private PrintWriter writer;
    private Socket socket;
    private ChatClient chatClient;

    public WriteThread(Socket socket, ChatClient chatClient) {
        this.socket = socket;
        this.chatClient = chatClient;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        Console console = System.console();

        String userName = console.readLine("\nEnter your name: ");
        chatClient.setUserName(userName);
        writer.println(userName);

        String text;

        do {
            text = console.readLine("User:" + userName + "->");
            writer.println(text);
        } while (!text.equals("bye"));

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
