package com.maihao.common.client.view;

import com.maihao.common.Message;
import com.maihao.common.MsgType;
import com.maihao.common.client.service.ClientService;
import com.maihao.common.client.util.Utility;

/**
 * 菜单
 */
public class View {

    private boolean loop = true;

    private String key;

    private ClientService clientService = new ClientService();

    public static void main(String[] args) {

        View view = new View();
        view.showMenu();
        System.out.println("客户端退出...");

    }

    private void showMenu() {

        while (loop) {

            System.out.println("====ChatRoom====");
            System.out.println("\t 1 登录");
            System.out.println("\t 0 退出系统");
            System.out.println("输入您的选择: ");
            key = Utility.scanner.next();

            switch (key) {
                case "1":
                    System.out.println("用户名: ");
                    String userId = Utility.scanner.next();
                    System.out.println("密码: ");
                    String password = Utility.scanner.next();
                    if (clientService.checkUser(userId, password)) {
                        System.out.println("====二级菜单（用户: " + userId + "）====");
                        while (loop) {
                            System.out.println("\t 1 群发聊天");
                            System.out.println("\t 0 退出系统");
                            key = Utility.scanner.next();
                            switch (key) {
                                case "0":
                                    clientService.exitClient();
                                    loop = false;
                                    break;
                                case "1":
                                    System.out.println("群发内容: ");
                                    String content = Utility.scanner.next();
                                    // 本地的信息直接显示
                                    System.out.println(userId + "说: " + content);
                                    clientService.sendToRoom(content, userId);
                                    break;
                                default:
                                    System.out.println("功能未完善...");
                            }
                        }
                    } else {
                        System.out.println("登陆失败...");
                    }

                    break;
                case "0":
                    loop = false;
                    break;
            }

        }

    }


}
