package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReadThread implements Runnable {

    private BufferedReader reader;
    private Socket socket;
    private ChatClient chatClient;


    public ReadThread(Socket socket, ChatClient chatClient) {
        this.socket = socket;
        this.chatClient = chatClient;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                String response = reader.readLine();
                System.out.println("/n" + response);

                if (chatClient.getUserName() != null) System.out.println("client name: " + chatClient.getUserName());

            } catch (IOException e) {
                System.out.println("error reading from server " + e.getMessage());
                break;
            }
        }

    }
}
