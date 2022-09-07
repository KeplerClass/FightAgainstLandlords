package DouDiZhuClient.com.wowowo.model;

import javax.swing.*;

// 扑克牌标签
public class PokerLabel extends JLabel implements Comparable<PokerLabel> {
    private int id;
    private String name;
    private int num;
    private boolean isOut;
    private boolean isUp;
    private boolean isSelected; // 是否选中牌子

    public PokerLabel(){
        this.setSize(105, 150);
    }

    public PokerLabel(int id, String name, int num){
        this();
        this.id = id;
        this.name = name;
        this.num = num;
    }

    public PokerLabel(int id, String name, int num, boolean isOut, boolean isUp){
        this(id, name, num);
        this.isOut = isOut;
        this.isUp = isUp;

        if (this.isUp){
            this.turnUp();
        }else {
            this.turnDown();
        }
    }

    public void turnUp(){
        this.setIcon(new ImageIcon(".//images//poker//" + this.id + ".jpg"));
    }

    public void turnDown(){
        this.setIcon(new ImageIcon(".//images//poker//down.jpg"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "PokerLabel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", num=" + num +
                ", isOut=" + isOut +
                ", isUp=" + isUp +
                '}';
    }

    @Override
    public int compareTo(PokerLabel o) {
        if (this.num > o.getNum()){
            return 1;
        }else if (this.num < o.getNum()){
            return -1;
        }else {
            return 0;
        }
    }
}
