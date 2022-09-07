package DouDiZhuClient.com.wowowo.util;

import DouDiZhuClient.com.wowowo.model.PokerLabel;

public class GameUtils {
    private GameUtils(){}
    public static void move(PokerLabel pokerLabel, int x, int y){
        pokerLabel.setLocation(x, y);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
