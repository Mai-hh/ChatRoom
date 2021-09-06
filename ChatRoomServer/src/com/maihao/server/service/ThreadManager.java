package com.maihao.server.service;

import com.maihao.common.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ThreadManager {

    private static HashMap<String, ServerThread> threadHashMap = new HashMap<>();

    public static void addThread(String threadName, ServerThread thread) {
        threadHashMap.put(threadName, thread);
    }

    public static ServerThread getThread(String threadName) {
        return threadHashMap.get(threadName);
    }

    public static void deleteThread(String threadName) {
        threadHashMap.remove(threadName);
    }

    public static void sendToAll(Message message) {
        System.out.println(message.getSender() + "群发了: " + message.getContent());
        Set<String> set = threadHashMap.keySet();
        for (String threadName : set) {
            ServerThread thread = threadHashMap.get(threadName);
            try {
                if (!message.getSender().equals(thread.getUserId())) {
                    // 内容发送者的消息已经在本地显示，不需要经由服务端发出
                    ObjectOutputStream out = new ObjectOutputStream(thread.getSocket().getOutputStream());
                    out.writeObject(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
