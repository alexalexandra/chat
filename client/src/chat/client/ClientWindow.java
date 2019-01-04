package chat.client;
//клиентское окно
import chat.network.TCPConnection;
import chat.network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {

    private static final String IP_ADDR="192.168.1.96";
    private static final int PORT=8189;
    private static final int WIDTH=600;
    private static final int HEIGHT=400;

    public static void main(String[] args) {
        //для обхода проблемы с многопоточность.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();// в потоке едт выполняется
            }
        });
    }

    private  final JTextArea log=new JTextArea();//поле,куда писать текст
    private final JTextField fieldNickname=new JTextField("Shura");//никнейм
    private final JTextField fieldInput = new JTextField();//поле для текста сообщения

    private TCPConnection connection;

    private ClientWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//закрыли на крестик
        setSize(WIDTH,HEIGHT);//размер окна
        setLocationRelativeTo(null);//по центру
        setAlwaysOnTop(true);//окно сверху
        log.setEditable(false);//запрет редактирование
        log.setLineWrap(true);//автомат. перенос слов
        add(log, BorderLayout.CENTER); //добавили поле в центр
        fieldInput.addActionListener(this);//добавили себя
        add(fieldInput,BorderLayout.SOUTH);//текст добавили ниже
        add(fieldNickname,BorderLayout.NORTH);//поле с ником выше
        setVisible(true);//видимое окно
        try {
            connection=new TCPConnection(this, IP_ADDR,PORT);
        } catch (IOException e) {
            printMsg("Исключение:"+ e);
        }

    }

    @Override//чтобы поймать нажатие клавиши
    public void actionPerformed(ActionEvent e) {
        String msg=fieldInput.getText();
        if (msg.equals("")) return; //обработка пустой строки
        fieldInput.setText(null); //очистили поле
        connection.sendString(fieldNickname.getText()+":"+msg);
    }


    @Override
    public void onConnectionReady(TCPConnection topConnection) {
        printMsg("Соединение установлено");
    }

    @Override
    public void onReceiveString(TCPConnection topConnection, String value) {
        printMsg(value);
    }

    @Override
    public void onDisconnect(TCPConnection topConnection) {
        printMsg("Соединение прервано");
    }

    @Override
    public void onException(TCPConnection topConnection, Exception e) {
        printMsg("Исключение:"+ e);
    }

    private synchronized void printMsg(String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg+"\n");
                log.setCaretPosition(log.getDocument().getLength());//автоскрол
            }
        });
    }
}
