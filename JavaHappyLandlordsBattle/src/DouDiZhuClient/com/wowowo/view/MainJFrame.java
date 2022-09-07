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
    public MyPanel myPanel; // 图像
    public String usename; // 用户名
    public Socket socket; // 客户端连接
    public Player player; // 玩家对象
    public List<PokerLabel> pokerLabels = new ArrayList<>(); // 扑克标签列表
    public JLabel lordLabel1; // 抢地主
    public JLabel lordLabel2; // 不叫地主
    public JLabel timeLabel; // 定时器
    public CountThread countThread; // 计时器线程
    public SendThead sendThead; // 发送数据
    public ReceiveThread receiveThread; // 接收数据
    public boolean isLord;  // 是否是地主
    public JLabel megLabel; // 显示信息的标签
    public JLabel lordLabel; // 显示地主
    public List<PokerLabel> selectPoker = new ArrayList<>(); // 存放选中的扑克
    public JLabel chuPaiJLabel; // 出牌按钮
    public JLabel buChuJLabel; // 不出牌按钮
    public CountChuPaiThead countChuPaiThead; // 出牌线程
    public boolean isOut; // 是否打出牌
    public List<PokerLabel> showPokerLabel = new ArrayList<>(); // 存放打出去的牌
    public int prevPlayerId = -1; // 上一个玩家的id

    public MainJFrame(String usename, Socket socket) {
        this.usename = usename;
        this.socket = socket;
        this.setSize(1200, 700);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 显示图片到界面上
        this.myPanel = new MyPanel();
        this.myPanel.setBounds(0, 0, 1200, 700);
        this.add(this.myPanel);

        // 初始化窗口控件
        this.init();

        // 开启线程发送数据
        this.sendThead = new SendThead(this.usename, this.socket);
        this.sendThead.start();

        // 开启线程接收数据
        this.receiveThread = new ReceiveThread(socket, this);
        this.receiveThread.start();
    }

    // 初始化数据
    public void init() {

        // 创建消息框
        this.megLabel = new JLabel();

        // 出牌
        this.chuPaiJLabel = new JLabel();
        this.chuPaiJLabel.setBounds(330, 350, 110, 53);
        this.chuPaiJLabel.setIcon(new ImageIcon(".//images//bg//chupai.png"));
        this.chuPaiJLabel.addMouseListener(new MyMouse());
        this.chuPaiJLabel.setVisible(false);
        this.myPanel.add(this.chuPaiJLabel);

        // 不出牌
        this.buChuJLabel = new JLabel();
        this.buChuJLabel.setBounds(440, 350, 110, 53);
        this.buChuJLabel.setIcon(new ImageIcon(".//images//bg//buchupai.png"));
        this.buChuJLabel.addMouseListener(new MyMouse());
        this.buChuJLabel.setVisible(false);
        this.myPanel.add(this.buChuJLabel);

        // 计数器
        this.timeLabel = new JLabel();
        this.timeLabel.setBounds(550, 350, 50, 50);
        this.timeLabel.setFont(new Font("Dialog", 0, 30));
        this.timeLabel.setForeground(Color.RED);
        this.timeLabel.setVisible(false);
        this.myPanel.add(this.timeLabel);

    }

    // 显示所有玩家的信息以及牌
    public void showAllPlayerInfo(List<Player> players) {
        // 显示玩家名称


        // 显示玩家对应扑克牌信息
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(this.usename)) {
                this.player = players.get(i);
            }
        }

        // 当前玩家的扑克牌
        List<Poker> pokers = this.player.getPokers();
        for (int i = 0; i < pokers.size(); i++) {
            Poker poker = pokers.get(i);
            PokerLabel pokerLabel = new PokerLabel(poker.getId(), poker.getName(), poker.getNum());
            pokerLabel.turnUp(); // 获取牌
            // 绘制组件
            this.myPanel.add(pokerLabel);

            // 为后面扑克牌的排序等做基础
            this.pokerLabels.add(pokerLabel);

            // 动态显示
            this.myPanel.setComponentZOrder(pokerLabel, 0);
            // 通过定位显示到界面
            GameUtils.move(pokerLabel, 300 + 30 * i, 450);

        }

        // 排序扑克牌
        Collections.sort(this.pokerLabels);

        // 显示排序后的牌
        for (int i = 0; i < this.pokerLabels.size(); i++) {
            PokerLabel pokerLabel = this.pokerLabels.get(i);
            this.myPanel.setComponentZOrder(pokerLabel, 0);
            GameUtils.move(pokerLabel, 300 + 30 * i, 450);
        }

        if (this.player.getId() == 0) {
            this.getLord();
        }

    }

    // 抢地主
    public void getLord() {
        // 抢地主
        this.lordLabel1 = new JLabel();
        this.lordLabel1.setBounds(330, 400, 104, 46);
        this.lordLabel1.setIcon(new ImageIcon(".//images//bg//jiaodizhu.png"));
        this.lordLabel1.addMouseListener(new MyMouse());
        this.myPanel.add(this.lordLabel1);

        // 不抢地主
        this.lordLabel2 = new JLabel();
        this.lordLabel2.setBounds(440, 400, 104, 46);
        this.lordLabel2.setIcon(new ImageIcon(".//images//bg//bujiao.png"));
        this.lordLabel2.addMouseListener(new MyMouse());
        this.myPanel.add(this.lordLabel2);

        this.timeLabel.setVisible(true);

        this.setVisible(true);

        // 重绘
        this.repaint();

        // 启动计时器线程
        this.countThread = new CountThread(10, this);
        this.countThread.start();
    }

    // 显示出牌按钮和倒计时
    public void showChuPai() {

        // 显示出牌按钮
        this.chuPaiJLabel.setVisible(true);

        // 显示不出牌按钮
        this.buChuJLabel.setVisible(true);

        // 显示倒计时按钮
        this.timeLabel.setVisible(true);

        // 重绘
        this.repaint();

        // 启动出牌倒计时线程
        this.countChuPaiThead = new CountChuPaiThead(30, this);
        this.countChuPaiThead.start();
    }

    // 显示地主牌
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

    //游戏结束
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


    // 显示打出去的牌
    public void showOutPokers(int playerid, List<Poker> pokers) {

        // 从组件删除打出去的牌
        for (int i = 0; i < this.showPokerLabel.size(); i++) {
            this.myPanel.remove(this.showPokerLabel.get(i));
        }

        // 清空打出去的牌
        this.showPokerLabel.clear();

        // 有不出的消息就隐藏
        this.megLabel.setVisible(false);

        // 显示到屏幕上
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

    // 将选中的牌移除
    public void RemovePokerToPokerLabel() {
        // 将选中的牌从原有的扑克牌移除
        this.pokerLabels.removeAll(this.selectPoker);

        // 从组件删除
        for (int i = 0; i < this.selectPoker.size(); i++) {
            this.myPanel.remove(this.selectPoker.get(i));
        }

        // 重新排序牌
        for (int i = 0; i < this.pokerLabels.size(); i++) {
            PokerLabel pokerLabel = this.pokerLabels.get(i);
            this.myPanel.setComponentZOrder(pokerLabel, 0);
            GameUtils.move(pokerLabel, 300 + 30 * i, 450);
        }

        // 选中的牌全部清空
        this.selectPoker.clear();

        this.repaint();
    }

    // 显示不抢标签或者是不出牌
    public void showMeg(int typeid, int playerid) {
        this.megLabel.setVisible(true);
        this.megLabel.setBounds(500, 300, 129, 77);

        // 不抢标签
        if (typeid == 1) {
            this.megLabel.setIcon(new ImageIcon(".//images//bg//buqiang.png"));
        }

        // 不出牌标签
        if (typeid == 3) {
            this.megLabel.setIcon(new ImageIcon(".//images//bg//buchu.png"));
        }

        if (playerid == this.player.getId()) {
            this.megLabel.setLocation(400, 300);
        } else if (playerid + 1 == this.player.getId() || playerid - 2 == this.player.getId()) //上家  //  2 0    0 1   1 2
        {
            this.megLabel.setLocation(300, 100);
        } else { // 下家
            this.megLabel.setLocation(800, 100);
        }

        this.myPanel.add(this.megLabel);
        this.repaint();
    }

    // 选中牌子
    public void SelectPokerClick() {
        for (int i = 0; i < this.pokerLabels.size(); i++) {
            this.pokerLabels.get(i).addMouseListener(new SelectPokerEvent());
        }
    }

    // 显示地主图标
    public void showLord(int playerid) {
        this.lordLabel = new JLabel();
        this.lordLabel.setIcon(new ImageIcon(".//images//bg//dizhu.png"));
        this.lordLabel.setSize(60, 89);

        // 当前地主是自己的
        if (playerid == this.player.getId()) {
            this.lordLabel.setLocation(200, 450);
        } else if (playerid + 1 == this.player.getId() || playerid - 2 == this.player.getId()) {
            // 当前地主是上一家
            this.lordLabel.setLocation(200, 100);
        } else {
            // 当前地主是下一家
            this.lordLabel.setLocation(950, 100);
        }

        this.myPanel.add(this.lordLabel);
        this.repaint();
    }

    class MyMouse implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            // 当单击了抢地主的按钮时
            if (e.getSource().equals(lordLabel1)) {
                // 设置倒计时结束
                countThread.setIsRun(false);

                // 设置是否抢了地主
                isLord = true;

                // 设置不显示抢地主和不抢地主标签和计时器标签
                lordLabel1.setVisible(false);
                lordLabel2.setVisible(false);
                timeLabel.setVisible(false);
            }

            // 当单击了不抢地主的按钮时
            if (e.getSource().equals(lordLabel2)) {
                countThread.setIsRun(false);
                isLord = false;
                lordLabel1.setVisible(false);
                lordLabel2.setVisible(false);
                timeLabel.setVisible(false);
            }

            // 当单击了出牌的按钮时
            if (e.getSource().equals(chuPaiJLabel)) {
                PokerType pokerType = PokerRule.checkPokerType(selectPoker);
                // 判断是否符合牌型
                if (!pokerType.equals(PokerType.p_error)) {

                    // 判断是否是自己出牌，或者是比上一家的牌型还大
                    if (prevPlayerId == -1 || prevPlayerId == player.getId()
                            || PokerRule.isBigger(showPokerLabel, selectPoker)) {
                        // 停止倒计时
                        countChuPaiThead.setRun(false);

                        // 隐藏出牌和不出牌以及倒计时按钮
                        chuPaiJLabel.setVisible(false);
                        buChuJLabel.setVisible(false);
                        timeLabel.setVisible(false);

                        // 是否打出牌
                        isOut = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "出牌有误");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "请按规则出牌");
                }

            }

            // 当单击了不出牌的按钮时
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

    // 显示选中牌子的鼠标单击事件
    class SelectPokerEvent implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

            PokerLabel pokerLabel = (PokerLabel) e.getSource();
            // 当牌子选中时，移动回原位置（将选中的扑克移除扑克列表中）
            if (pokerLabel.isSelected()) {
                pokerLabel.setLocation(pokerLabel.getX(), pokerLabel.getY() + 30);
                selectPoker.remove(pokerLabel);
                pokerLabel.setSelected(false);
            }
            // 当牌子未选中时，移动到上面（将选中的扑克添加到扑克列表中）
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
