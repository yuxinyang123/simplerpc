package com.yxy.simplerpc.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Objects;

public class ServerTask implements Runnable {

    private Socket socket;

    private Class classImpl;

    public ServerTask(Socket socket, Class classImpl) {
        this.socket = socket;
        this.classImpl = classImpl;
    }

    public void run() {


        try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {


            String serviceName = ois.readUTF();
            String methodName = ois.readUTF();
            Class<?>[] parameterTypes = (Class<?>[]) ois.readObject();
            Object[] parameters = (Object[]) ois.readObject();

            //FIXME 临时实现
            //Class serviceClass = ServerImpl.getServiceRegistry().get(serviceName);

            Class serviceClass = classImpl;

            Method method = serviceClass.getMethod(methodName, parameterTypes);
            Object result = method.invoke(serviceClass.newInstance(), parameters);

            oos.writeObject(result);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(socket)) {
                try {
                    socket.close();
                    socket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
