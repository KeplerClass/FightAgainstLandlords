package DouDiZhuClient.com.wowowo.thread;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import DouDiZhuClient.com.wowowo.view.MainJFrame;
import DouDiZhuServer.com.wowowo.model.Player;
import DouDiZhuServer.com.wowowo.model.Poker;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.swing.*;

public class ReceiveThread extends Thread {

	private Socket socket;
	private MainJFrame mainJFrame;
	private int step = 0;
	private boolean isRun = true;

	public ReceiveThread(Socket socket, MainJFrame mainJFrame) {
		this.mainJFrame = mainJFrame;
		this.socket = socket;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DataInputStream dataInputStream = new DataInputStream(this.socket.getInputStream());
			while (true) {

				if (this.isRun == false){
					break;
				}

				String json = dataInputStream.readUTF();
				if (this.step == 0) {
					JSONArray plays = JSONArray.parseArray(json);
					List<Player> playerList = new ArrayList<>();

					for (int i = 0; i < plays.size(); i++) {
						JSONObject play = plays.getJSONObject(i);
						Integer id = play.getInteger("id");
						String name = play.getString("name");
						JSONArray pokers = play.getJSONArray("pokers");
						List<Poker> pokerList = new ArrayList<>();
						for (int j = 0; j < pokers.size(); j++) {
							JSONObject poker = pokers.getJSONObject(j);
							Integer pid = poker.getInteger("id");
							String pname = poker.getString("name");
							int num = poker.getInteger("num");
							Poker poker1 = new Poker(pid, pname, num);
							pokerList.add(poker1);
						}
						Player player = new Player(id, name, pokerList);
						playerList.add(player);
					}
					// 显示玩家扑克牌信息
					if (playerList.size() == 3) {
						this.mainJFrame.showAllPlayerInfo(playerList);
						this.step = 1;
					}
				}else if (this.step == 1){

					// 解析信息
					JSONObject jsonObject = JSON.parseObject(json);
					Integer typeid = jsonObject.getInteger("typeid");
					Integer playerid = jsonObject.getInteger("playerid");
					String content = jsonObject.getString("content");

					// 不抢地主
					if (typeid == 1){
						// 显示不抢地主的信息
						this.mainJFrame.showMeg(1, playerid);

						// 显示下一个玩家抢地主的标签
						if (playerid + 1 == this.mainJFrame.player.getId()){
							this.mainJFrame.getLord();
						}
					}

					// 抢地主
					if (typeid == 2){
						// 解析地主牌
						JSONArray pokersJSONObject = jsonObject.getJSONArray("pokers");
						List<Poker> pokers = new ArrayList<>();
						for (int i = 0; i < pokersJSONObject.size(); i++){
							JSONObject pokersJSON = pokersJSONObject.getJSONObject(i);
							Integer id = pokersJSON.getInteger("id");
							String name = pokersJSON.getString("name");
							Integer num = pokersJSON.getInteger("num");
							Poker poker = new Poker(id, name, num);
							pokers.add(poker);
						}

						// 将地主牌发给地主
						if (this.mainJFrame.player.getId() == playerid){
							this.mainJFrame.addLordPokers(pokers);

							// 显示出牌的按钮
							this.mainJFrame.showChuPai();
						}

						// 显示地主图标
						this.mainJFrame.showLord(playerid);

						// 当地主确定后不显示所有的不抢图标
						this.mainJFrame.megLabel.setVisible(false);

						// 所有玩家可以选择牌子，但不能出牌
						this.mainJFrame.SelectPokerClick();
					}

					// 不出牌
					if (typeid == 3){
						// 显示不出牌的标签
						this.mainJFrame.showMeg(3, playerid);

						// 轮到下一家出牌
						if (this.mainJFrame.player.getId() == playerid + 1 || this.mainJFrame.player.getId() == playerid - 2){
							this.mainJFrame.showChuPai();
						}
					}

					// 出牌
					if (typeid == 4){

						// 解析打出去的牌
						JSONArray outPokersJSON = jsonObject.getJSONArray("pokers");
						List<Poker> pokers = new ArrayList<>();
						for (int i = 0; i < outPokersJSON.size(); i++){
							JSONObject outPoker = outPokersJSON.getJSONObject(i);
							Integer id = outPoker.getInteger("id");
							String name = outPoker.getString("name");
							Integer num = outPoker.getInteger("num");
							Poker poker = new Poker(id, name, num);
							pokers.add(poker);
						}

						// 显示在客户端上
						this.mainJFrame.showOutPokers(playerid, pokers);

						// 轮到下一家出牌
						if (this.mainJFrame.player.getId() == playerid + 1 || this.mainJFrame.player.getId() == playerid - 2){
							this.mainJFrame.showChuPai();
						}

						// 记录上一个出牌的玩家
						this.mainJFrame.prevPlayerId = playerid;
					}

					// 玩家获胜
					if (typeid == 5){
						if (this.mainJFrame.player.getId() == playerid){
							JOptionPane.showMessageDialog(this.mainJFrame, "你赢了");
						}else {
							JOptionPane.showMessageDialog(this.mainJFrame, "你输了");
						}
						this.mainJFrame.gameOver();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
