package com.maihao.client.service;

import com.maihao.common.Message;
import com.maihao.common.MsgType;
import com.maihao.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientService {

    private User user;

    private Socket socket;

    public void exitClient() {
        Message msgSent = new Message();
        msgSent.setMsgType(MsgType.TYPE_CLIENT_EXIT);
        msgSent.setSender(user.getId());

        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(msgSent);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToRoom(String content, String senderId) {

        Message msgSent = new Message();
        msgSent.setMsgType(MsgType.TYPE_SEND_TO_ROOM);
        msgSent.setSender(senderId);
        msgSent.setContent(content);
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(msgSent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkUser(String userId, String password) {

        user = new User();
        user.setId(userId);
        user.setPassword(password);

        try {

            // 发送用户数据
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 1234);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(user);

            // 获取返回消息
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Message msgReceived = (Message) in.readObject();

            // 判断返回数据类型
            if (msgReceived.getMsgType().equals(MsgType.TYPE_LOGIN_SUCCEED)) {

                ClientThread thread = new ClientThread(socket);
                thread.start();
                ThreadManager.addThread(userId + ".checkUser", thread);
                return true;

            } else if (msgReceived.getMsgType().equals(MsgType.TYPE_LOGIN_FAILED)) {

                // 登录失败则通讯通道就不需要了
                socket.close();
                return false;

            } else {

                System.out.println("客户端难以理解服务端发来的消息...");
                return false;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
