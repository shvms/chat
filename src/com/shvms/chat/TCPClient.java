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

        Thread input = new ChatInput(clientSocket);
        input.start();

        Thread output = new ChatOutput(clientSocket);
        output.start();
    }
}
