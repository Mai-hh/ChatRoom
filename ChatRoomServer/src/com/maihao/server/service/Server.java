package com.maihao.server.service;

import com.maihao.common.Message;
import com.maihao.common.MsgType;
import com.maihao.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    private boolean loop = true;

    public static void main(String[] args) {
        new Server();
    }

    public Server() {

        System.out.println("监听端口: 1234");

        try {
            serverSocket = new ServerSocket(1234);

            while (loop) {
                Socket socket = serverSocket.accept();
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                User user = (User) in.readObject();
                System.out.println("ID:" + user.getId() + " Password: " + user.getPassword());

                Message msgSent = new Message();
                if (checkUser(user)) {
                    // 回复登录成功信息给客户端
                    msgSent.setMsgType(MsgType.TYPE_LOGIN_SUCCEED);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(msgSent);
                    // 登录成功则创建一个线程负责这个客户端与服务端的通信
                    ServerThread serverThread = new ServerThread(socket, user.getId());
                    serverThread.start();
                    ThreadManager.addThread(user.getId(), serverThread);

                } else {
                    // 回复登录失败信息给客户端,并且释放socket
                    msgSent.setMsgType(MsgType.TYPE_LOGIN_FAILED);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(msgSent);
                    socket.close();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert serverSocket != null;
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // todo: 从数据库中检索用户
    private boolean checkUser(User user) {

        // 按理来说应该对照数据库或集合中的信息，这里先偷个懒
        if (("test1".equals(user.getId()) && "111".equals(user.getPassword())) || ("test2".equals(user.getId()) && "222".equals(user.getPassword()))) {
            return true;
        }
        return false;
    }
}
