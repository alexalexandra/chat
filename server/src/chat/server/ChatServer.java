package chat.server;


import chat.network.TCPConnection;
import chat.network.TCPConnectionListener;
import sun.awt.SunHints;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConnectionListener {

    public static void main(String[] args) {
     new ChatServer();
    }
     //список соединений
    private final ArrayList<TCPConnection> connections = new ArrayList<>();

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
//обработка событий на сервере. синх-м,т.к.мб много потоков
    @Override
        public synchronized void onConnectionReady(TCPConnection topConnection) {
            connections.add(topConnection);//добавляем соединение
        //оповестили всех
            sendToAllConnections("Соединение установлено:"+ topConnection);
        }

    @Override
    public synchronized void onReceiveString(TCPConnection topConnection, String value) {
        //если приняли строчку
        sendToAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection topConnection) {
       connections.remove(topConnection); //если соедин-е отвалилось - удаляем
        sendToAllConnections("Соединение прервано:"+ topConnection);
    }

    @Override
    public synchronized void onException(TCPConnection topConnection, Exception e) {
        System.out.println("TCPConnection exception: "+ e); //исключение
    }

    private void sendToAllConnections(String value) {  //отправляет сообщение всем
        System.out.println(value);
        for (int i=0; i<connections.size();i++){
            connections.get(i).sendString(value);
        }
    }
}
