package com.yxy.simplerpc.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class SocketUtil {
    public static ServerSocket startServerSocket(InetSocketAddress address) {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(address);
            return serverSocket;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Socket runServerSocket(ServerSocket serverSocket) {
        try {
            return serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean stopServerSocket(ServerSocket serverSocket) {
        if (Objects.nonNull(serverSocket)) {
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static Socket startSocket(InetSocketAddress address) {
        Socket socket = new Socket();
        try {
            socket.connect(address);
            return socket;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
