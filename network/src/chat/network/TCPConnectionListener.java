package chat.network;
//интерфейс для того, чтобы и сервер и клиент могли обращаться к обработчику ошибок

public interface TCPConnectionListener {
    void onConnectionReady(TCPConnection topConnection);  //готовое соединение
    void onReceiveString(TCPConnection topConnection, String value);   //приняли строчку входящую
    void onDisconnect(TCPConnection topConnection);  //дсоединение пропало
    void onException (TCPConnection topConnection, Exception e);  //исключение

}
