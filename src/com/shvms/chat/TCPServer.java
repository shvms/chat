package com.shvms.chat;

import java.io.IOException;
import java.net.*;

public class TCPServer {
    private ServerAddress addr;

    public TCPServer(ServerAddress addr) {
        this.addr = addr;
    }

    public void startServer() throws IOException {
        ServerSocket welcomeSocket = new ServerSocket(addr.port);

        System.out.println("[WAITING FOR CONNECTION]");

        Socket connectionSocket = welcomeSocket.accept();
        System.out.println("[CONNECTION ESTABLISHED] " + connectionSocket.getInetAddress());

        Thread chatInput = new ChatInput(connectionSocket);
        chatInput.start();

        Thread chatOutput = new ChatOutput(connectionSocket);
        chatOutput.start();
    }
}
