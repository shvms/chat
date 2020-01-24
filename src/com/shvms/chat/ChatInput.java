package com.shvms.chat;

import java.io.*;
import java.net.*;

public class ChatInput extends Thread {
    private Socket connectionSocket;
    private String ipAddr;
    private BufferedReader inFromClient;

    public ChatInput(Socket connectionSocket) throws IOException {
        super();
        System.out.println("[CHATINPUT THREAD STARTS]");
        this.connectionSocket = connectionSocket;
        this.ipAddr = connectionSocket.getInetAddress().toString();
        this.inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
    }

    @Override
    public void run() {
        String msg;
        try {
            while (true) {
                System.out.println("chat i/p");
                msg = inFromClient.readLine();
                if (msg == null)    // first message
                    continue;

                if (msg.compareToIgnoreCase("close") == 0) {
                    connectionSocket.close();
                    System.out.println("[CONNECTION CLOSED]");
                    break;
                }
                System.out.printf("[%s] %s", ipAddr, msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
