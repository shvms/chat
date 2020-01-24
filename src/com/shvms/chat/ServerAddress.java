package com.shvms.chat;

public class ServerAddress {
    String ip;
    Integer port;

    public ServerAddress(String ip, Integer port) throws PortException {
        this.ip = ip;
        if (port < 0 || port > 65535) throw new PortException("TCP port range is 0-65535");
        this.port = port;
    }

    private static class PortException extends Exception {
        public PortException(String s) {
            super(s);
        }
    }
}
