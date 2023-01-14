package thread;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class UniversalThread implements Runnable {
    // 消息
    protected String message = null;
    // 创建数据包套接字
    protected Socket socket;
    protected BufferedReader reader = null;
    protected PrintWriter writer = null;

    UniversalThread(Socket socket) {
        this.socket = socket;
    }
    /**
     * 设置消息
     * @param message:消息内容
     */
    public void setMessage(String message) {
        this.message = message;
    }
}

