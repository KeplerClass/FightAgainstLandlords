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

        // ʱ�䵽�˻����ǲ����Ƶ����
        Message message = null;
        if (this.time == -1 || this.isRun == false && this.mainJFrame.isOut == false){
            message = new Message(3, this.mainJFrame.player.getId(), "����", null);
            // �����ƺͲ����Ƶ���Ϣ�Լ�����ʱ����
            this.mainJFrame.timeLabel.setVisible(false);
            this.mainJFrame.chuPaiJLabel.setVisible(false);
            this.mainJFrame.buChuJLabel.setVisible(false);
        }

        // ���Ƶ����
        if (this.mainJFrame.isOut == true && this.isRun == false){
            message = new Message(4, this.mainJFrame.player.getId(), "����", this.ChangePokerLabelToPoker(this.mainJFrame.selectPoker));
            // ��ѡ�е����Ƴ�������������
            this.mainJFrame.RemovePokerToPokerLabel();
        }

        // �����������
        String meg = JSON.toJSONString(message);
        this.mainJFrame.sendThead.setMeg(meg);

        // ���˿��ƶ�û�˱�ʾӮ��
        if (this.mainJFrame.pokerLabels.size() == 0){
            message = new Message(5, this.mainJFrame.player.getId(), "��Ϸ����", null);
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
