package com.shvms.chat;

import java.net.*;
import java.io.*;

public class TCPClient {
    private ServerAddress addr;

    public TCPClient(ServerAddress addr) {
        this.addr = addr;
    }

    public void startClient() throws IOException {
        Socket clientSocket = new Socket(addr.ip, addr.port);
        System.out.println("[CONNECTION ESTABLISHED] " + addr.ip);

        Thread output = new ClientChatOutput(clientSocket);
        output.start();

        Thread input = new ClientChatInput(clientSocket);
        input.start();
    }

    private static class ClientChatInput extends Thread {
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
                        break;
                    }

                    System.out.printf("[%s] %s", ipAddr, msg);
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
        }
    }
}
