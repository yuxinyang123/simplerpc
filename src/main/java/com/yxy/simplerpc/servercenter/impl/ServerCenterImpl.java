package com.yxy.simplerpc.servercenter.impl;

import com.yxy.simplerpc.servercenter.ServerCenter;
import com.yxy.simplerpc.util.RedisUtil;
import com.yxy.simplerpc.util.SocketUtil;
import lombok.extern.log4j.Log4j;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Log4j
public class ServerCenterImpl implements ServerCenter {

    private InetSocketAddress clientAddress;
    private InetSocketAddress registryAddress;
    private InetSocketAddress logoutAddress;

    private ServerSocket clientSocket;
    private ServerSocket registrySocket;
    private ServerSocket logoutSocket;

    private boolean clientPort, registryPort, logoutPort;

    private Jedis jedis = RedisUtil.getJedis();

    public ServerCenterImpl(InetSocketAddress clientAddress, InetSocketAddress registryAddress, InetSocketAddress logoutAddress) {
        this.clientAddress = clientAddress;
        this.registryAddress = registryAddress;
        this.logoutAddress = logoutAddress;
    }


    @Override
    public void start() {
        new Thread(() -> {
            listenClient(clientAddress);
        }).start();

        new Thread(() -> {
            listenRegistry(registryAddress);
        }).start();

        new Thread(() -> {
            listenLogout(logoutAddress);
        }).start();

        log.info("ServerCenter start");
    }

    @Override
    public void stop() {
        SocketUtil.stopServerSocket(clientSocket);
        clientPort = false;
        SocketUtil.stopServerSocket(registrySocket);
        registryPort = false;
        SocketUtil.stopServerSocket(logoutSocket);
        logoutPort = false;
    }

    @Override
    public void registry(InetSocketAddress registryAddress, Class classInterface, Class classImpl) {

    }

    @Override
    public void remove(InetSocketAddress registryAddress, Class classInterface, Class classImpl) {

    }

    @Override
    public void listenClient(InetSocketAddress clientAddress) {
        this.clientAddress = clientAddress;
        this.clientPort = true;
        clientSocket = SocketUtil.startServerSocket(clientAddress);
        if (Objects.isNull(clientSocket)) {
            return;
        }
        while (true) {
            Socket socket = SocketUtil.runServerSocket(clientSocket);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            ) {
                String interfaceName = br.readLine();
                List<String> addressLists = jedis.lrange(interfaceName, 0, jedis.llen(interfaceName));
                bw.write(selectAddress(addressLists));
                bw.newLine();
                bw.flush();
                log.info("a client has came");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String selectAddress(List<String> lists) {
        int len = lists.size();
        Random random = new Random();
        return lists.get(random.nextInt(len));
    }

    @Override
    public void listenRegistry(InetSocketAddress registryAddress) {
        this.registryAddress = registryAddress;
        this.registryPort = true;
        registrySocket = SocketUtil.startServerSocket(registryAddress);
        if (Objects.isNull(registrySocket)) {
            return;
        }
        while (true) {
            Socket socket = SocketUtil.runServerSocket(registrySocket);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String interfaceName = br.readLine();
                String registryHost = br.readLine();
                jedis.rpush(interfaceName, registryHost);
                log.info("a server has came");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void listenLogout(InetSocketAddress logoutAddress) {
        this.logoutAddress = logoutAddress;
        this.logoutPort = true;
        ServerSocket serverSocket = SocketUtil.startServerSocket(logoutAddress);

    }

    @Override
    public void isRunning() {

    }
}
