package com.yxy.simplerpc.client.impl;

import com.yxy.simplerpc.client.Client;
import com.yxy.simplerpc.util.SocketUtil;
import lombok.extern.log4j.Log4j;

import java.io.*;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

@Log4j
public class ClientImpl implements Client {

    private Socket socket = new Socket();

    private InetSocketAddress serverCenterAddress;

    private InetSocketAddress address;


    public ClientImpl( InetSocketAddress serverCenterAddress) {
        this.serverCenterAddress = serverCenterAddress;
//        this.address = address;

    }

    private InetSocketAddress getServerAddress(InetSocketAddress address, String interfaceName) {
        Socket centerSocket = SocketUtil.startSocket(address);
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(centerSocket.getOutputStream()));
             BufferedReader br = new BufferedReader(new InputStreamReader(centerSocket.getInputStream()))) {
            bw.write(interfaceName);
            bw.newLine();
            bw.flush();
            String[] serverAddress = br.readLine().split("[/:]");
            log.info(serverAddress[serverAddress.length-2]);
            log.info(serverAddress[serverAddress.length-1]);
            address = new InetSocketAddress(serverAddress[serverAddress.length - 2],
                    Integer.parseInt(serverAddress[serverAddress.length - 1]));
            return address;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> T getResult(Class<T> serviceInterface) throws IOException {
        socket.connect(getServerAddress(serverCenterAddress, serviceInterface.getName()));
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[]{serviceInterface}, (proxy, method, args) -> {
            try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());) {

                oos.writeUTF(serviceInterface.getName());
                oos.writeUTF(method.getName());
                oos.writeObject(method.getParameterTypes());
                oos.writeObject(args);

                return ois.readObject();

            } finally {
                if (Objects.nonNull(socket)) {
                    socket.close();
                    socket = null;
                }
            }
        });
    }

}
