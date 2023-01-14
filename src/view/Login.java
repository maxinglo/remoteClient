package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login extends JFrame {
    // 用户昵称
    public static String name;
    // 提示标签
    private JLabel nikeNameLabel;
    // 昵称输入框
    private JTextField nikeNameField;
    // 点击按钮
    private JButton button;
    private JLabel serverIpLabel;
    private JTextField serverIpField;

    public Login() {
        setTitle("聊天室");
        setBounds(500, 400, 400, 200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        nikeNameLabel = new JLabel();
        nikeNameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        nikeNameLabel.setText("请输入你的昵称：");
        nikeNameLabel.setBounds(30, 0, 150, 50);
        add(nikeNameLabel);

        nikeNameField = new JTextField();
        nikeNameField.setBounds(200, 10, 150, 30);
        add(nikeNameField);
        serverIpField = new JTextField();
        serverIpField.setBounds(200, 40, 150, 30);
        add(serverIpField);
        serverIpLabel = new JLabel();
        serverIpLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        serverIpLabel.setText("请输入服务器IP：");
        serverIpLabel.setBounds(30, 30, 150, 50);
        serverIpField.addFocusListener(new JTextFieldHintListener(serverIpField, "留空以使用本地服务器"));
        add(serverIpLabel);
        button = new JButton("加入");/**/
        button.addActionListener(e -> {
            // 对话窗体出现
            if (serverIpField.getText().equals("留空以使用本地服务器")) {
                new Client(nikeNameField.getText());
            } else {
                new Client(nikeNameField.getText(), serverIpField.getText());
            }
            // 本窗体消失
            setVisible(false);
        });
        button.setBounds(100, 100, 150, 30);
        nikeNameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    button.doClick();
                }
            }
        });
        add(button);

        setVisible(true);
    }

}

