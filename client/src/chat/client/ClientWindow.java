package chat.client;
//клиентское окно
import javax.swing.*;

public class ClientWindow extends JFrame {

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

    private ClientWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//закрыли на крестик
        setSize(WIDTH,HEIGHT);//размер окна
        setLocationRelativeTo(null);//по центру
        setAlwaysOnTop(true);//окно сверху
        setVisible(true);//видимое окно
    }

}
