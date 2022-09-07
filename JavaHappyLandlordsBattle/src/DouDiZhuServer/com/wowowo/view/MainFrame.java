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

    public List<Player> players = new ArrayList<>(); // �������Ϣ����б�

    public List<Poker> pokers = new ArrayList<>(); // ������������б�

    public List<Poker> lordPokers = new ArrayList<>(); // ��ŵ���

    private int index = 0; // ����idֵ

    private int step = 0;

    public MainFrame() {
        // ������
        this.createpoker();
        try {
            // ��ȡ�ͻ��˵ȴ�����
            ServerSocket serverSocket = new ServerSocket(8000);
            while (true) {
                Socket socket = serverSocket.accept();

                // �����̻߳�ȡ�ͻ�����Ϣ
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
                    // ��ʼ��Ϊ0ʱ����û�
                    String meg = dataInputStream.readUTF();
                    if (step == 0) {
                        // ��ȡ�ͻ�����Ϣ�������������Ҷ���
                        Player player = new Player(index++, meg);
                        player.setSocket(this.socket);
                        players.add(player);
                        System.out.println(meg + "������");
                        System.out.println("��ǰ��������Ϊ��" + players.size());

                        if (players.size() == 3) {
                            fapai();
                            step = 1;
                        }
                    }else if (step == 1){
                        // ��������������
                        JSONObject jsonObject = JSON.parseObject(meg);
                        Integer typeid = jsonObject.getInteger("typeid");
                        Integer playerid = jsonObject.getInteger("playerid");
                        String content = jsonObject.getString("content");

                        // �����͵�idΪ2ʱ��Ϊ������
                        Message message;
                        if (typeid == 2){
                            // �������Ʒ��ͻؿͻ���
                            message = new Message(typeid, playerid, content, lordPokers);
                            // �����ݷ��ͻ�Ⱥ��Ϣ
                            meg = JSON.toJSONString(message);
                            step = 2;
                        }
                        sendMessage(meg);
                    }

                    // ���ͳ��ƺͲ������Լ���Ϸ����
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

    // �����˿���
    public void createpoker() {
        Poker dawang = new Poker(0, "����", 17);
        Poker xiaowang = new Poker(1, "С��", 16);
        this.pokers.add(dawang);
        this.pokers.add(xiaowang);

        String[] names = new String[]{"2", "A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3"};
        String[] colors = new String[]{"����", "����", "÷��", "˫��"};
        int index = 2;
        int num = 15;
        for (String name : names) {
            for (String color : colors) {
                Poker poker = new Poker(index++, color + name, num);
                this.pokers.add(poker);
            }
            num--;
        }

        // ������
        Collections.shuffle(this.pokers);
    }

    // ����
    public void fapai() {
        for (int i = 0; i < this.pokers.size(); i++) {
            // ʣ�µ����Ÿ������������ũ��
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

        // �����Ϣ���͵��ͻ���
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
