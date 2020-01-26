package com.shvms.chat;

import java.net.*;
import java.io.*;
import java.util.Base64;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TCPClient {
    private ServerAddress addr;

    public TCPClient(ServerAddress addr) {
        this.addr = addr;
    }

    public void startClient() throws IOException {
        Socket clientSocket = new Socket(addr.ip, addr.port);
        System.out.println("[CONNECTION ESTABLISHED] " + addr.ip);

        Thread input = new ClientChatInput(clientSocket);
        input.start();

        Thread output = new ClientChatOutput(clientSocket);
        output.start();
    }

    private static class ClientChatInput extends Thread {
        private static final Pattern pat = Pattern.compile("^data:image/([\\w]+);base64,([\\w+=/]+)$");

        private Socket clientSocket;
        private String ipAddr;
        private BufferedReader inFromServer;

        public ClientChatInput(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            this.ipAddr = clientSocket.getInetAddress().toString();
            this.inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String msg = inFromServer.readLine();
                    if (msg == null)
                        continue;
                    if (msg.compareToIgnoreCase("close") == 0) {
                        clientSocket.close();
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

    private static class ClientChatOutput extends Thread {
        private Socket clientSocket;
        private OutputStream outputStream;
        private BufferedReader inFromUser;

        public ClientChatOutput(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            this.outputStream = clientSocket.getOutputStream();
            this.inFromUser = new BufferedReader(new InputStreamReader(System.in));
        }

        public void start() {
            try {
                while (true) {
                    String msg = inFromUser.readLine();

                    if (msg.compareToIgnoreCase("CLOSE") == 0) {
                        close();
                        break;
                    }

                    if (msg.startsWith("@image/")) {
                        try {
                            ImageFile img = new ImageFile(msg.substring("@image/".length()));
                            msg = img.getBase64String();
                        } catch (Exception e) {
                            System.out.println("ERROR: Couldn't find the required image.");
                        }
                    }

                    outputStream.write((msg + "\n").getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void close() throws IOException {
            outputStream.write("CLOSE\n".getBytes());
            clientSocket.close();
            System.out.println("[CONNECTION CLOSED]");
            System.exit(0);
        }
    }
}
