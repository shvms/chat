package com.shvms.chat;

import java.io.*;

/**
 * Example of command line
 * application -h
 * application -s 1111                  // starts server at port 1111
 * application -c 12.45.23.101 1111     // starts client for given host address
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ServerAddress addr;

        // command parsing
        if (args.length < 1) {
            System.out.println("Wrong argument list. Use -h for help.");
            System.exit(1);
        }

        if (args[0].compareTo("-h") == 0) {
            System.out.println("Usage\n-h - help\n-s <port> - starts server at given port\n-c " +
                    "<host-ip> <host-port> - starts client for given host address");
        } else if (args[0].compareTo("-s") == 0 && args.length > 1) {
            try {
                addr = new ServerAddress(null, Integer.parseInt(args[1]));
                TCPServer server = new TCPServer(addr);
                server.startServer();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("ERROR: Port should be a number between 0-65535");
            }
        } else if (args[0].compareTo("-c") == 0 && args.length > 2) {
            try {
                addr = new ServerAddress(args[1], Integer.parseInt(args[2]));
                TCPClient client = new TCPClient(addr);
                client.startClient();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("ERROR: Port should be a number between 0-65535");
            }
        }
    }
}