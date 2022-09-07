package DouDiZhuClient.com.wowowo.thread;

import DouDiZhuClient.com.wowowo.model.Message;
import DouDiZhuClient.com.wowowo.view.MainJFrame;
import com.alibaba.fastjson.JSON;

// 计时器线程
public class CountThread extends Thread {
    private int i;
    private MainJFrame mainFrame;
    private boolean isRun = true;
    public CountThread(int i, MainJFrame mainFrame){
        this.i = i;
        this.mainFrame = mainFrame;
    }

    @Override
    public void run() {
        while (this.i >= 0 && this.isRun){
            this.mainFrame.timeLabel.setText(String.valueOf(this.i));
            this.i--;
            try {
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        Message message = null;
        // 当时间到了或者是不抢地主时
        if (this.i == -1 || this.isRun == false && this.mainFrame.isLord == false){
            message = new Message(1, this.mainFrame.player.getId(), "不抢", null);
            // 不显示抢地主和不抢地主的图标以及倒计时
            this.mainFrame.lordLabel1.setVisible(false);
            this.mainFrame.lordLabel2.setVisible(false);
            this.mainFrame.timeLabel.setVisible(false);
        }

        // 当抢地主了时
        if (this.isRun == false && this.mainFrame.isLord == true){
            message = new Message(2, this.mainFrame.player.getId(), "抢地主", null);
        }

        // 发送到服务端
        String meg = JSON.toJSONString(message);
        this.mainFrame.sendThead.setMeg(meg);
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public MainJFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(MainJFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void setIsRun(boolean isRun) {
        this.isRun = isRun;
    }

    public boolean getIsRun() {
        return isRun;
    }

    @Override
    public String toString() {
        return "CountThread{" +
                "i=" + i +
                ", mainFrame=" + mainFrame +
                '}';
    }
}
