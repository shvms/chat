package com.shvms.chat;

import java.io.*;
import java.net.*;

public class ChatOutput extends Thread {
    private Socket connectionSocket;
    private OutputStream outputStream;
    private BufferedReader inFromUser;

    private static String CLOSE = "CLOSE";

    public ChatOutput(Socket connectionSocket) throws IOException {
        super();
        System.out.println("[CHATOUTPUT THREAD STARTS]");
        this.connectionSocket = connectionSocket;

        this.outputStream = connectionSocket.getOutputStream();
        this.inFromUser = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        String reply;
        try {
            while (true) {
                System.out.println("chat o/p");
                reply = inFromUser.readLine();
                System.out.println("Reply: " + reply);

                if (reply.compareToIgnoreCase(CLOSE) == 0) {
                    close();
                    break;
                }

                outputStream.write((reply + "\n").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() throws IOException {
        outputStream.write((CLOSE + "\n").getBytes());
        connectionSocket.close();
        System.out.println("[CONNECTION CLOSED]");
    }
}
