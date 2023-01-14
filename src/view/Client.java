package view;

import thread.ReceiveThread;
import thread.SendThread;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.xml.bind.Marshaller;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends JFrame {
    // 创建套接字对象
    private Socket socket;
    // 用户昵称
    private String name;
    private JTextArea jTextArea;
    // 消息发送线程
    private SendThread sendThread = null;
    // 消息接收线程
    private ReceiveThread receiveThread = null;
    public Boolean IsFocused = true;
    private Thread send, receive;
    public PlayerListComponent playerList = new PlayerListComponent();
    private String serverIP="localhost";


    public Client(String name) {
        System.out.println(name);
        this.name = name;
        try {
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initView();
    }
    public Client(String name,String ip){
        System.out.println(name);
        this.name = name;
        this.serverIP = ip;
        try {
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initView();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JTextArea getJTextArea() {
        return jTextArea;
    }

    private void initView() {
        setLayout(new BorderLayout());
        setTitle("聊天室");
        setBounds(700, 200, 1000, 500);
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // 添加提示标签

        try {
            add(getLogComponent(), "Center");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            add(getPlayerListComponent(), "West");
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendThread = new SendThread(socket);
        receiveThread = new ReceiveThread(socket, this);
        send = new Thread(sendThread);
        receive = new Thread(receiveThread);
        // 启动线程
        send.start();
        receive.start();
        sendThread.setMessage("M}}J>#qzNiK+}rtZ)%5v:"+name);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 关闭与服务器的连接
                closeConnection();
                System.exit(0);
            }
        });
        setVisible(true);
        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                IsFocused = true;
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                IsFocused = false;
            }
        });
    }

    /**
     * 连接服务器，参与聊天
     *
     * @throws IOException
     */
    private void connect() throws IOException {
/*            InetAddress ip = InetAddress.getByName("150.158.214.157");
            System.out.println(ip);
            socket = new Socket(ip, 20200);*/

        InetAddress ip = InetAddress.getByName(serverIP);
        System.out.println(ip);
        socket = new Socket(ip, 20200);

/*        InetAddress ip = InetAddress.getLocalHost();
        System.out.println(ip);
        socket = new Socket(ip, 20200);*/
    }

    /**
     * 断开连接，退出聊天室
     */
    public synchronized void closeConnection() {
        sendThread.setMessage("Exit");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 中断两个线程
        send.interrupt();
        receive.interrupt();
        if (socket != null) {
            try {
                // 关闭套接字
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private JComponent getPlayerListComponent() throws Exception {

        JScrollPane jscrollpane = new JScrollPane(playerList, 22, 30);
        jscrollpane.setPreferredSize(new Dimension(260, 300));
        jscrollpane.setBorder(new TitledBorder(new EtchedBorder(), "Players"));
        return jscrollpane;
    }

    private JComponent getLogComponent() throws Exception {
        JPanel jpanel = new JPanel(new BorderLayout());
        jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        jTextArea.setFont(new Font("华文中宋 常规", Font.PLAIN, 12));
        jTextArea.setLineWrap(true);
        jTextArea.setPreferredSize(new Dimension(380, 10000));
        jTextArea.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent p_focusGained_1_) {
            }
        });
        final JScrollPane jscrollpane = new JScrollPane(jTextArea, 22, 30);
        jpanel.add(jscrollpane, "Center");
        jpanel.add(getChatBar(), "South");
        jpanel.setBorder(new TitledBorder(new EtchedBorder(), "Chat"));
        return jpanel;
    }

    private JComponent getChatBar() throws Exception {
        JPanel jPanel = new JPanel(new BorderLayout());
        JButton jButton = new JButton("发 送");
        JLabel jLabel = new JLabel("输入消息：");
        JTextField jTextField = new JTextField();
        jTextField.setFont(new Font("华文中宋 常规", Font.PLAIN, 14));
        jTextField.setBounds(70, 430, 230, 30);
        jTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // 按下回车键，发送消息
                if (e.getKeyCode() == 10) {
                    jButton.doClick();
                }
            }
        });
        jLabel.setFont(new Font("华文中宋 常规", Font.PLAIN, 12));
        jLabel.setBounds(5, 430, 70, 30);
        jPanel.add(jLabel, "West");
        jButton.setFont(new Font("华文中宋 常规", Font.PLAIN, 14));
        jButton.setBounds(310, 430, 70, 30);
        jButton.addActionListener(actionEvent -> {
            // 将输入框内容设为线程发送消息
            sendThread.setMessage(jTextField.getText().trim());
            // 输入框清空
            jTextField.setText("");
        });
        jPanel.add(jButton, "East");
        jPanel.add(jTextField, "Center");

        return jPanel;
    }

    private JComponent getStatsComponent() throws Exception {
        JPanel jpanel = new JPanel(new BorderLayout());
        jpanel.add(this.getPlayerListComponent(), "Center");
        jpanel.setBorder(new TitledBorder(new EtchedBorder(), "Stats"));
        return jpanel;
    }
}

