package chat.network;


import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPConnection {  //реализация TCP соединения

    private final Socket socket;  //установка соединения
    private final Thread rxThread;  //поток, который слушает вход.сообщение
    private final TCPConnectionListener eventListener;
    private final BufferedReader in;  //поток ввода для чтения строк
    private final BufferedWriter out;  //поток вывода для чтения строк

    //сокет созд-ся внутри
    public TCPConnection(TCPConnectionListener eventListener, String ipAddr, int port) throws IOException {
        this(eventListener, new Socket(ipAddr,port));
    }

    //снаружи уже есть соедин-е
     public TCPConnection(TCPConnectionListener eventListener,Socket socket) throws IOException{  //конструктор соединения. принимает объект сокета и создает с ним соединение,метод генерирует исключения
        this.eventListener = eventListener;
        this.socket=socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));// простой поток ввода и создали экз-р, которые работает со строчками
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
        //поток, кот слушает вход.соединение
         rxThread= new Thread(new Runnable() {  //создание анонимного класса runnalbe
             @Override //переопределение метода
             public void run() {  //слушаем вход соединения
                 try {
                     eventListener.onConnectionReady(TCPConnection.this);
                     while (!rxThread.isInterrupted()) {  //получение строк. пока поток непрерван
                         eventListener.onReceiveString(TCPConnection.this, in.readLine()); //передаем получ-ю строку
                     }
                 }
                 catch (IOException e) {
                     eventListener.onException(TCPConnection.this, e);
                 }
                 finally {  //если ошибка-закрываем сокет. нужно сделать так, чтобы этот класс мог исп-ть и клиент и сервер
                     eventListener.onDisconnect(TCPConnection.this);
                         }
                 }
         });
         rxThread.start();
     }


   public synchronized void  sendString (String value){  //отправили строчку
       try {
           out.write(value + "\r\n" );  //с новой строки
           out.flush(); //сбрасывает в буфер и отправляет
       } catch (IOException e) {
           eventListener.onException(TCPConnection.this, e);
           disconnect();//если не можем передать строчку-обравыем соединение
       }
   }
//синх-я для безопасного обращения из разных потоков
    public synchronized void disconnect(){  //рвем соединение
      rxThread.isInterrupted();  // прерывание
        try {
            socket.close();  //закрыли сокет
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
        }
    }

    @Override
    public String toString (){//кто подключился
        return "TCPConnection"+socket.getInetAddress()+":"+socket.getPort();
    }
}
