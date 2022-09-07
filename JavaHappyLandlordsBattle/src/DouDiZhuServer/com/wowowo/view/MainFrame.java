package DouDiZhuServer.com.wowowo.view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import DouDiZhuClient.com.wowowo.model.Message;
import DouDiZhuServer.com.wowowo.model.Player;
import DouDiZhuServer.com.wowowo.model.Poker;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class MainFrame {

    public List<Player> players = new ArrayList<>(); // 将玩家信息添加列表

    public List<Poker> pokers = new ArrayList<>(); // 将所有牌添加列表

    public List<Poker> lordPokers = new ArrayList<>(); // 存放底牌

    private int index = 0; // 自增id值

    private int step = 0;

    public MainFrame() {
        // 创建牌
        this.createpoker();
        try {
            // 获取客户端等待连接
            ServerSocket serverSocket = new ServerSocket(8000);
            while (true) {
                Socket socket = serverSocket.accept();

                // 开启线程获取客户端信息
                SendThead sendThead = new SendThead(socket);
                sendThead.start();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    class SendThead extends Thread {
        private Socket socket;

        public SendThead(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                DataInputStream dataInputStream = new DataInputStream(this.socket.getInputStream());
                while (true) {
                    // 初始化为0时添加用户
                    String meg = dataInputStream.readUTF();
                    if (step == 0) {
                        // 获取客户端信息，创建并添加玩家对象
                        Player player = new Player(index++, meg);
                        player.setSocket(this.socket);
                        players.add(player);
                        System.out.println(meg + "上线了");
                        System.out.println("当前上线人数为：" + players.size());

                        if (players.size() == 3) {
                            fapai();
                            step = 1;
                        }
                    }else if (step == 1){
                        // 解析发过来的牌
                        JSONObject jsonObject = JSON.parseObject(meg);
                        Integer typeid = jsonObject.getInteger("typeid");
                        Integer playerid = jsonObject.getInteger("playerid");
                        String content = jsonObject.getString("content");

                        // 当类型的id为2时，为抢地主
                        Message message;
                        if (typeid == 2){
                            // 将地主牌发送回客户端
                            message = new Message(typeid, playerid, content, lordPokers);
                            // 将数据发送回群消息
                            meg = JSON.toJSONString(message);
                            step = 2;
                        }
                        sendMessage(meg);
                    }

                    // 发送出牌和不出牌以及游戏结束
                    else if (step == 2){
                        sendMessage(meg);
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message){
        for (int i = 0; i < this.players.size(); i++){
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(this.players.get(i).getSocket().getOutputStream());
                dataOutputStream.writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 创建扑克牌
    public void createpoker() {
        Poker dawang = new Poker(0, "大王", 17);
        Poker xiaowang = new Poker(1, "小王", 16);
        this.pokers.add(dawang);
        this.pokers.add(xiaowang);

        String[] names = new String[]{"2", "A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3"};
        String[] colors = new String[]{"黑桃", "红桃", "梅花", "双块"};
        int index = 2;
        int num = 15;
        for (String name : names) {
            for (String color : colors) {
                Poker poker = new Poker(index++, color + name, num);
                this.pokers.add(poker);
            }
            num--;
        }

        // 打乱牌
        Collections.shuffle(this.pokers);
    }

    // 发牌
    public void fapai() {
        for (int i = 0; i < this.pokers.size(); i++) {
            // 剩下的三张给地主，其余给农民
            if (i >= 51) {
                this.lordPokers.add(this.pokers.get(i));
            } else if (i % 3 == 0) {
                this.players.get(0).getPokers().add(this.pokers.get(i));
            } else if (i % 3 == 1) {
                this.players.get(1).getPokers().add(this.pokers.get(i));
            } else {
                this.players.get(2).getPokers().add(this.pokers.get(i));
            }
        }

        // 玩家信息发送到客户端
        for (int i = 0; i < this.players.size(); i++) {
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(
                        this.players.get(i).getSocket().getOutputStream());
                String jsonString = JSON.toJSONString(this.players);
                dataOutputStream.writeUTF(jsonString);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
