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
        String msg;
        try {
            while (true) {
                msg = inFromUser.readLine();

                if (msg.compareToIgnoreCase(CLOSE) == 0) {
                    close();
                    break;
                }

                if (msg.startsWith("@image/")) {
                    try {
                        ImageFile img = new ImageFile(msg.substring("@image/".length()));
                        msg = img.getBase64String();
                    } catch (Exception e) {
                        System.out.println("ERROR: Couldn't find the required image.");
                        continue;
                    }
                }

                outputStream.write((msg + "\n").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() throws IOException {
        outputStream.write((CLOSE + "\n").getBytes());
        connectionSocket.close();
        System.out.println("[CONNECTION CLOSED]");
        System.exit(0);
    }
}
