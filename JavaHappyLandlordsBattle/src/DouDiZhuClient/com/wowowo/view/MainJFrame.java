package DouDiZhuClient.com.wowowo.view;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

import DouDiZhuClient.com.wowowo.model.PokerLabel;
import DouDiZhuClient.com.wowowo.thread.CountChuPaiThead;
import DouDiZhuClient.com.wowowo.thread.CountThread;
import DouDiZhuClient.com.wowowo.thread.ReceiveThread;
import DouDiZhuClient.com.wowowo.thread.SendThead;
import DouDiZhuClient.com.wowowo.util.GameUtils;
import DouDiZhuClient.com.wowowo.util.PokerRule;
import DouDiZhuClient.com.wowowo.util.PokerType;
import DouDiZhuServer.com.wowowo.model.Player;
import DouDiZhuServer.com.wowowo.model.Poker;

public class MainJFrame extends JFrame {
    public MyPanel myPanel; // ͼ��
    public String usename; // �û���
    public Socket socket; // �ͻ�������
    public Player player; // ��Ҷ���
    public List<PokerLabel> pokerLabels = new ArrayList<>(); // �˿˱�ǩ�б�
    public JLabel lordLabel1; // ������
    public JLabel lordLabel2; // ���е���
    public JLabel timeLabel; // ��ʱ��
    public CountThread countThread; // ��ʱ���߳�
    public SendThead sendThead; // ��������
    public ReceiveThread receiveThread; // ��������
    public boolean isLord;  // �Ƿ��ǵ���
    public JLabel megLabel; // ��ʾ��Ϣ�ı�ǩ
    public JLabel lordLabel; // ��ʾ����
    public List<PokerLabel> selectPoker = new ArrayList<>(); // ���ѡ�е��˿�
    public JLabel chuPaiJLabel; // ���ư�ť
    public JLabel buChuJLabel; // �����ư�ť
    public CountChuPaiThead countChuPaiThead; // �����߳�
    public boolean isOut; // �Ƿ�����
    public List<PokerLabel> showPokerLabel = new ArrayList<>(); // ��Ŵ��ȥ����
    public int prevPlayerId = -1; // ��һ����ҵ�id

    public MainJFrame(String usename, Socket socket) {
        this.usename = usename;
        this.socket = socket;
        this.setSize(1200, 700);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ��ʾͼƬ��������
        this.myPanel = new MyPanel();
        this.myPanel.setBounds(0, 0, 1200, 700);
        this.add(this.myPanel);

        // ��ʼ�����ڿؼ�
        this.init();

        // �����̷߳�������
        this.sendThead = new SendThead(this.usename, this.socket);
        this.sendThead.start();

        // �����߳̽�������
        this.receiveThread = new ReceiveThread(socket, this);
        this.receiveThread.start();
    }

    // ��ʼ������
    public void init() {

        // ������Ϣ��
        this.megLabel = new JLabel();

        // ����
        this.chuPaiJLabel = new JLabel();
        this.chuPaiJLabel.setBounds(330, 350, 110, 53);
        this.chuPaiJLabel.setIcon(new ImageIcon(".//images//bg//chupai.png"));
        this.chuPaiJLabel.addMouseListener(new MyMouse());
        this.chuPaiJLabel.setVisible(false);
        this.myPanel.add(this.chuPaiJLabel);

        // ������
        this.buChuJLabel = new JLabel();
        this.buChuJLabel.setBounds(440, 350, 110, 53);
        this.buChuJLabel.setIcon(new ImageIcon(".//images//bg//buchupai.png"));
        this.buChuJLabel.addMouseListener(new MyMouse());
        this.buChuJLabel.setVisible(false);
        this.myPanel.add(this.buChuJLabel);

        // ������
        this.timeLabel = new JLabel();
        this.timeLabel.setBounds(550, 350, 50, 50);
        this.timeLabel.setFont(new Font("Dialog", 0, 30));
        this.timeLabel.setForeground(Color.RED);
        this.timeLabel.setVisible(false);
        this.myPanel.add(this.timeLabel);

    }

    // ��ʾ������ҵ���Ϣ�Լ���
    public void showAllPlayerInfo(List<Player> players) {
        // ��ʾ�������


        // ��ʾ��Ҷ�Ӧ�˿�����Ϣ
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(this.usename)) {
                this.player = players.get(i);
            }
        }

        // ��ǰ��ҵ��˿���
        List<Poker> pokers = this.player.getPokers();
        for (int i = 0; i < pokers.size(); i++) {
            Poker poker = pokers.get(i);
            PokerLabel pokerLabel = new PokerLabel(poker.getId(), poker.getName(), poker.getNum());
            pokerLabel.turnUp(); // ��ȡ��
            // �������
            this.myPanel.add(pokerLabel);

            // Ϊ�����˿��Ƶ������������
            this.pokerLabels.add(pokerLabel);

            // ��̬��ʾ
            this.myPanel.setComponentZOrder(pokerLabel, 0);
            // ͨ����λ��ʾ������
            GameUtils.move(pokerLabel, 300 + 30 * i, 450);

        }

        // �����˿���
        Collections.sort(this.pokerLabels);

        // ��ʾ��������
        for (int i = 0; i < this.pokerLabels.size(); i++) {
            PokerLabel pokerLabel = this.pokerLabels.get(i);
            this.myPanel.setComponentZOrder(pokerLabel, 0);
            GameUtils.move(pokerLabel, 300 + 30 * i, 450);
        }

        if (this.player.getId() == 0) {
            this.getLord();
        }

    }

    // ������
    public void getLord() {
        // ������
        this.lordLabel1 = new JLabel();
        this.lordLabel1.setBounds(330, 400, 104, 46);
        this.lordLabel1.setIcon(new ImageIcon(".//images//bg//jiaodizhu.png"));
        this.lordLabel1.addMouseListener(new MyMouse());
        this.myPanel.add(this.lordLabel1);

        // ��������
        this.lordLabel2 = new JLabel();
        this.lordLabel2.setBounds(440, 400, 104, 46);
        this.lordLabel2.setIcon(new ImageIcon(".//images//bg//bujiao.png"));
        this.lordLabel2.addMouseListener(new MyMouse());
        this.myPanel.add(this.lordLabel2);

        this.timeLabel.setVisible(true);

        this.setVisible(true);

        // �ػ�
        this.repaint();

        // ������ʱ���߳�
        this.countThread = new CountThread(10, this);
        this.countThread.start();
    }

    // ��ʾ���ư�ť�͵���ʱ
    public void showChuPai() {

        // ��ʾ���ư�ť
        this.chuPaiJLabel.setVisible(true);

        // ��ʾ�����ư�ť
        this.buChuJLabel.setVisible(true);

        // ��ʾ����ʱ��ť
        this.timeLabel.setVisible(true);

        // �ػ�
        this.repaint();

        // �������Ƶ���ʱ�߳�
        this.countChuPaiThead = new CountChuPaiThead(30, this);
        this.countChuPaiThead.start();
    }

    // ��ʾ������
    public void addLordPokers(List<Poker> pokers) {
        for (int i = 0; i < pokers.size(); i++) {
            Poker poker = pokers.get(i);
            PokerLabel pokerLabel = new PokerLabel(poker.getId(), poker.getName(), poker.getNum());
            pokerLabel.turnUp();
            this.pokerLabels.add(pokerLabel);
        }

        Collections.sort(this.pokerLabels);
        for (int i = 0; i < this.pokerLabels.size(); i++) {
            this.myPanel.add(this.pokerLabels.get(i));
            this.myPanel.setComponentZOrder(pokerLabels.get(i), 0);
            GameUtils.move(this.pokerLabels.get(i), 300 + 30 * i, 450);
        }
        this.player.getPokers().addAll(pokers);

    }

    //��Ϸ����
    public void gameOver()
    {
        this.myPanel.removeAll();
        this.repaint();
        try {
            this.socket.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.countThread.setIsRun(false);
        this.countChuPaiThead.setRun(false);
        this.sendThead.setRun(false);
        this.receiveThread.setRun(false);
    }


    // ��ʾ���ȥ����
    public void showOutPokers(int playerid, List<Poker> pokers) {

        // �����ɾ�����ȥ����
        for (int i = 0; i < this.showPokerLabel.size(); i++) {
            this.myPanel.remove(this.showPokerLabel.get(i));
        }

        // ��մ��ȥ����
        this.showPokerLabel.clear();

        // �в�������Ϣ������
        this.megLabel.setVisible(false);

        // ��ʾ����Ļ��
        for (int i = 0; i < pokers.size(); i++) {
            Poker poker = pokers.get(i);
            PokerLabel pokerLabel = new PokerLabel(poker.getId(), poker.getName(), poker.getNum());
            pokerLabel.turnUp();
            if (playerid == this.player.getId()) {
                pokerLabel.setLocation(500 + 30 * i, 200);
            } else if (playerid + 1 == this.player.getId() || playerid - 2 == this.player.getId()) {
                pokerLabel.setLocation(200 + 30 * i, 100);
            } else
                pokerLabel.setLocation(700 + 30 * i, 100);
            this.myPanel.add(pokerLabel);
            this.myPanel.setComponentZOrder(pokerLabel, 0);
            this.showPokerLabel.add(pokerLabel);
        }

        this.repaint();
    }

    // ��ѡ�е����Ƴ�
    public void RemovePokerToPokerLabel() {
        // ��ѡ�е��ƴ�ԭ�е��˿����Ƴ�
        this.pokerLabels.removeAll(this.selectPoker);

        // �����ɾ��
        for (int i = 0; i < this.selectPoker.size(); i++) {
            this.myPanel.remove(this.selectPoker.get(i));
        }

        // ����������
        for (int i = 0; i < this.pokerLabels.size(); i++) {
            PokerLabel pokerLabel = this.pokerLabels.get(i);
            this.myPanel.setComponentZOrder(pokerLabel, 0);
            GameUtils.move(pokerLabel, 300 + 30 * i, 450);
        }

        // ѡ�е���ȫ�����
        this.selectPoker.clear();

        this.repaint();
    }

    // ��ʾ������ǩ�����ǲ�����
    public void showMeg(int typeid, int playerid) {
        this.megLabel.setVisible(true);
        this.megLabel.setBounds(500, 300, 129, 77);

        // ������ǩ
        if (typeid == 1) {
            this.megLabel.setIcon(new ImageIcon(".//images//bg//buqiang.png"));
        }

        // �����Ʊ�ǩ
        if (typeid == 3) {
            this.megLabel.setIcon(new ImageIcon(".//images//bg//buchu.png"));
        }

        if (playerid == this.player.getId()) {
            this.megLabel.setLocation(400, 300);
        } else if (playerid + 1 == this.player.getId() || playerid - 2 == this.player.getId()) //�ϼ�  //  2 0    0 1   1 2
        {
            this.megLabel.setLocation(300, 100);
        } else { // �¼�
            this.megLabel.setLocation(800, 100);
        }

        this.myPanel.add(this.megLabel);
        this.repaint();
    }

    // ѡ������
    public void SelectPokerClick() {
        for (int i = 0; i < this.pokerLabels.size(); i++) {
            this.pokerLabels.get(i).addMouseListener(new SelectPokerEvent());
        }
    }

    // ��ʾ����ͼ��
    public void showLord(int playerid) {
        this.lordLabel = new JLabel();
        this.lordLabel.setIcon(new ImageIcon(".//images//bg//dizhu.png"));
        this.lordLabel.setSize(60, 89);

        // ��ǰ�������Լ���
        if (playerid == this.player.getId()) {
            this.lordLabel.setLocation(200, 450);
        } else if (playerid + 1 == this.player.getId() || playerid - 2 == this.player.getId()) {
            // ��ǰ��������һ��
            this.lordLabel.setLocation(200, 100);
        } else {
            // ��ǰ��������һ��
            this.lordLabel.setLocation(950, 100);
        }

        this.myPanel.add(this.lordLabel);
        this.repaint();
    }

    class MyMouse implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            // ���������������İ�ťʱ
            if (e.getSource().equals(lordLabel1)) {
                // ���õ���ʱ����
                countThread.setIsRun(false);

                // �����Ƿ����˵���
                isLord = true;

                // ���ò���ʾ�������Ͳ���������ǩ�ͼ�ʱ����ǩ
                lordLabel1.setVisible(false);
                lordLabel2.setVisible(false);
                timeLabel.setVisible(false);
            }

            // �������˲��������İ�ťʱ
            if (e.getSource().equals(lordLabel2)) {
                countThread.setIsRun(false);
                isLord = false;
                lordLabel1.setVisible(false);
                lordLabel2.setVisible(false);
                timeLabel.setVisible(false);
            }

            // �������˳��Ƶİ�ťʱ
            if (e.getSource().equals(chuPaiJLabel)) {
                PokerType pokerType = PokerRule.checkPokerType(selectPoker);
                // �ж��Ƿ��������
                if (!pokerType.equals(PokerType.p_error)) {

                    // �ж��Ƿ����Լ����ƣ������Ǳ���һ�ҵ����ͻ���
                    if (prevPlayerId == -1 || prevPlayerId == player.getId()
                            || PokerRule.isBigger(showPokerLabel, selectPoker)) {
                        // ֹͣ����ʱ
                        countChuPaiThead.setRun(false);

                        // ���س��ƺͲ������Լ�����ʱ��ť
                        chuPaiJLabel.setVisible(false);
                        buChuJLabel.setVisible(false);
                        timeLabel.setVisible(false);

                        // �Ƿ�����
                        isOut = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "��������");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "�밴�������");
                }

            }

            // �������˲����Ƶİ�ťʱ
            if (e.getSource().equals(buChuJLabel)) {
                countChuPaiThead.setRun(false);
                chuPaiJLabel.setVisible(false);
                buChuJLabel.setVisible(false);
                timeLabel.setVisible(false);
                isOut = false;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    // ��ʾѡ�����ӵ���굥���¼�
    class SelectPokerEvent implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

            PokerLabel pokerLabel = (PokerLabel) e.getSource();
            // ������ѡ��ʱ���ƶ���ԭλ�ã���ѡ�е��˿��Ƴ��˿��б��У�
            if (pokerLabel.isSelected()) {
                pokerLabel.setLocation(pokerLabel.getX(), pokerLabel.getY() + 30);
                selectPoker.remove(pokerLabel);
                pokerLabel.setSelected(false);
            }
            // ������δѡ��ʱ���ƶ������棨��ѡ�е��˿���ӵ��˿��б��У�
            else {
                pokerLabel.setLocation(pokerLabel.getX(), pokerLabel.getY() - 30);
                selectPoker.add(pokerLabel);
                pokerLabel.setSelected(true);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
