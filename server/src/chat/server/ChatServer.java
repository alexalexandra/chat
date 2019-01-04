package chat.server;


import chat.network.TCPConnection;
import chat.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;

public class ChatServer implements TCPConnectionListener {

    public static void main(String[] args) {
     new ChatServer();
    }

    private  ChatServer()  {
       System.out.println("сервер запущен");
       //базовый класс. умеет слушать порт  и принимать вход. соедин-е
       try (ServerSocket serverSocket=new ServerSocket(8189)) {

            while (true){
               try {
                   new TCPConnection(this, serverSocket.accept()); // весим в методе accept, а он создает объект сокета
               }
               catch (IOException e){
                   System.out.println("TCPConnection"+e);
               }
            }
       } catch (IOException e) {
            throw new RuntimeException(e);
       }
    }

    @Override
    public void onConnectionReady(TCPConnection topConnection) {

    }

    @Override
    public void onReceiveString(TCPConnection topConnection, String value) {

    }

    @Override
    public void onDisconnect(TCPConnection topConnection) {

    }

    @Override
    public void onException(TCPConnection topConnection, Exception e) {

    }
}
