package com.shvms.chat;

import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatInput extends Thread {
    private static final Pattern pat = Pattern.compile("^data:image/([\\w]+);base64,([\\w+=/]+)$");

    private Socket connectionSocket;
    private String ipAddr;
    private BufferedReader inFromClient;

    public ChatInput(Socket connectionSocket) throws IOException {
        super();

        this.connectionSocket = connectionSocket;
        this.ipAddr = connectionSocket.getInetAddress().toString();
        this.inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
    }

    @Override
    public void run() {
        String msg;
        try {
            while (true) {
                msg = inFromClient.readLine();
                if (msg == null)    // first message
                    continue;

                if (msg.compareToIgnoreCase("close") == 0) {
                    connectionSocket.close();
                    System.out.println("[CONNECTION CLOSED]");
                    System.exit(0);
                    break;
                }

                // is image?
                Matcher m = pat.matcher(msg);
                if (m.find()) {
                    String extension = m.group(1);
                    String base64String = m.group(2);
                    byte[] bytes = Base64.getDecoder().decode(base64String);

                    String fileName = "image"+ (new Random().nextInt(1000)) + "." + extension;

                    File imgFile = new File(fileName);
                    imgFile.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(imgFile);
                    fileOutputStream.write(bytes);
                    fileOutputStream.close();

                    System.out.printf("[%s] %s received.\n", ipAddr, fileName);
                    continue;
                }


                System.out.printf("[%s] %s\n", ipAddr, msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
