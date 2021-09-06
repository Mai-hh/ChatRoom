package com.maihao.common.client.service;

import com.maihao.common.Message;
import com.maihao.common.MsgType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread extends Thread {
    // 每个线程都能通过Socket保持和服务端的通讯
    private Socket socket;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        boolean loop = true;
        while (loop) {
//            System.out.println("等待数据...");
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                // 服务端没有发送对象回来，则阻塞在readObject()
                Message msgReceived = (Message) in.readObject();

                if (msgReceived.getMsgType().equals(MsgType.TYPE_SEND_TO_ROOM)) {
                    System.out.println(msgReceived.getSender() + "说: " + msgReceived.getContent());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.run();
    }

    // 获得一个线程，就能获得对应的socket
    public Socket getSocket() {
        return socket;
    }
}
