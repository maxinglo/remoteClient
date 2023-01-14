package thread;

import view.Client;
import view.PlayerListComponent;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveThread extends UniversalThread implements Runnable {
    // 父客户端
    private Client client;

    public ReceiveThread(Socket socket, Client client) {
        super(socket);
        this.client = client;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            if (Thread.currentThread().isInterrupted()) { // 若被中断，退出循环
                break;
            }
            try {
                // 读取一行内容
                message = reader.readLine();
                if (message == null || "".equals(message)) {
                    continue;
                }
                String[] msg = spliter(message);
                // 客户端显示消息
                if (msg[0].equals("!SyncPlayerList")) {
                    client.playerList.SyncPlayers(msg);
                } else if(msg[0].equals("!RemovePlayerInList")){
                    client.playerList.RemovePlayer(msg);
                }else  {
                    client.getJTextArea().append(message + "\n\n");
                    // 自动移动最新消息位置
                    client.getJTextArea().setSelectionStart(client.getJTextArea().getText().length());
                    if (SystemTray.isSupported()) {
                        if (!message.equals("Exit") && !client.IsFocused) {
                            try {
                                if (msg.length == 2 && !msg[0].equals(client.getName())) {
                                    displayTray(msg[0], msg[1]);
                                }
                            } catch (AWTException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        System.err.println("System tray not supported!");
                    }
                }
            } catch (IOException e) {
                client.closeConnection();
                e.printStackTrace();
            }
        }
    }

    public void displayTray(String name, String msg) throws AWTException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);
        trayIcon.displayMessage(name, msg, TrayIcon.MessageType.NONE);
    }

    public String[] spliter(String msg) {
        String[] strings = msg.split(":");
        return strings;
    }
}

