package thread;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SendThread extends UniversalThread implements Runnable {

    public SendThread(Socket socket) {
        super(socket);
        try {
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息的线程
     */
    @Override
    public void run() {
        while (true) {
            if (Thread.currentThread().isInterrupted()) { // 当前线程已被中断，退出循环
                break;
            }
            if (message != null && !"".equals(message)) { // 若消息不为空
                // 发送消息
                writer.println(message);
                writer.flush();
            }
            // 消息设为空
            message = null;
            try {
                // 线程休眠1s
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

