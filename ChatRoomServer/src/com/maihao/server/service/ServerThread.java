package com.maihao.server.service;

import com.maihao.common.Message;
import com.maihao.common.MsgType;

import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerThread extends Thread {

    private Socket socket;

    private String userId;

    public ServerThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(Thread.currentThread().getName() + "线程连接客户端: " + userId);
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Message msgReceived = (Message) in.readObject();

                if (msgReceived.getMsgType().equals(MsgType.TYPE_SEND_TO_ROOM)) {
                    // 遍历所有线程，将msg中的内容转发到所有socket中
                    ThreadManager.sendToAll(msgReceived);
                } else if (msgReceived.getMsgType().equals(MsgType.TYPE_CLIENT_EXIT)) {
                    // 安全退出线程
                    System.out.println(msgReceived.getSender() + "客户端退出");
                    ThreadManager.deleteThread(msgReceived.getSender());
                    socket.close();
                    break;
                } else {
                    System.out.println("服务端难以理解接收的消息...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
