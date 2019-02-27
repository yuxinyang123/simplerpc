package test;

import com.yxy.simplerpc.servercenter.ServerCenter;
import com.yxy.simplerpc.servercenter.impl.ServerCenterImpl;

import java.net.InetSocketAddress;

public class ServerCenterTest {
    public static void main(String[] args) {
        ServerCenter serverCenter = new ServerCenterImpl(new InetSocketAddress(7777), new InetSocketAddress(6666)
                , new InetSocketAddress(5555));
        serverCenter.start();
    }
}
