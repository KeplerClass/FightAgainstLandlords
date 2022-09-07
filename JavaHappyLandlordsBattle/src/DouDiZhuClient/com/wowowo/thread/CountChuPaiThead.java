package DouDiZhuClient.com.wowowo.thread;

import DouDiZhuClient.com.wowowo.model.Message;
import DouDiZhuClient.com.wowowo.model.PokerLabel;
import DouDiZhuClient.com.wowowo.view.MainJFrame;
import DouDiZhuServer.com.wowowo.model.Poker;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class CountChuPaiThead extends Thread{
    private int time;
    private MainJFrame mainJFrame;
    private boolean isRun;

    public CountChuPaiThead(){}

    public CountChuPaiThead(int time, MainJFrame mainJFrame){
        this.time = time;
        this.mainJFrame = mainJFrame;
        this.isRun = true;
    }

    @Override
    public void run() {
        while(this.time >= 0 && this.isRun){
            this.mainJFrame.timeLabel.setText(String.valueOf(this.time));
            this.time--;

            try {
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        // 时间到了或者是不出牌的情况
        Message message = null;
        if (this.time == -1 || this.isRun == false && this.mainJFrame.isOut == false){
            message = new Message(3, this.mainJFrame.player.getId(), "不出", null);
            // 将出牌和不出牌的消息以及倒计时隐藏
            this.mainJFrame.timeLabel.setVisible(false);
            this.mainJFrame.chuPaiJLabel.setVisible(false);
            this.mainJFrame.buChuJLabel.setVisible(false);
        }

        // 出牌的情况
        if (this.mainJFrame.isOut == true && this.isRun == false){
            message = new Message(4, this.mainJFrame.player.getId(), "出牌", this.ChangePokerLabelToPoker(this.mainJFrame.selectPoker));
            // 将选中的牌移除并重新排序牌
            this.mainJFrame.RemovePokerToPokerLabel();
        }

        // 发送至服务端
        String meg = JSON.toJSONString(message);
        this.mainJFrame.sendThead.setMeg(meg);

        // 当扑克牌都没了表示赢了
        if (this.mainJFrame.pokerLabels.size() == 0){
            message = new Message(5, this.mainJFrame.player.getId(), "游戏结束", null);
            meg = JSON.toJSONString(message);
            try {
                Thread.sleep(100);
                this.mainJFrame.sendThead.setMeg(meg);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public List<Poker> ChangePokerLabelToPoker(List<PokerLabel> pokerLabels){
        List<Poker> pokers = new ArrayList<>();
        for (int i = 0; i < pokerLabels.size(); i++){
            PokerLabel pokerLabel = pokerLabels.get(i);
            Poker poker = new Poker(pokerLabel.getId(), pokerLabel.getName(), pokerLabel.getNum());
            pokers.add(poker);
        }
        return pokers;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public MainJFrame getMainJFrame() {
        return mainJFrame;
    }

    public void setMainJFrame(MainJFrame mainJFrame) {
        this.mainJFrame = mainJFrame;
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
    }
}
