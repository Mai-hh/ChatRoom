package com.maihao.common.client.service;

import java.util.HashMap;

public class ThreadManager {

    private static HashMap<String , ClientThread> threadHashMap = new HashMap<>();

    public static void addThread(String threadName, ClientThread clientThread) {
        threadHashMap.put(threadName, clientThread);
    }

    public static ClientThread getThread(String threadName) {
        return threadHashMap.get(threadName);
    }

}
