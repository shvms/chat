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
        this.connectionSocket = connectionSocket;

        this.outputStream = connectionSocket.getOutputStream();
        this.inFromUser = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        String reply;
        try {
            while (true) {
                reply = inFromUser.readLine();

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
