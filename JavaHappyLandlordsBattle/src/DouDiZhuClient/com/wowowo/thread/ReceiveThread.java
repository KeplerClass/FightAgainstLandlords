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
					// ??????????????????
					if (playerList.size() == 3) {
						this.mainJFrame.showAllPlayerInfo(playerList);
						this.step = 1;
					}
				}else if (this.step == 1){

					// ????????
					JSONObject jsonObject = JSON.parseObject(json);
					Integer typeid = jsonObject.getInteger("typeid");
					Integer playerid = jsonObject.getInteger("playerid");
					String content = jsonObject.getString("content");

					// ????????
					if (typeid == 1){
						// ??????????????????
						this.mainJFrame.showMeg(1, playerid);

						// ??????????????????????????
						if (playerid + 1 == this.mainJFrame.player.getId()){
							this.mainJFrame.getLord();
						}
					}

					// ??????
					if (typeid == 2){
						// ??????????
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

						// ????????????????
						if (this.mainJFrame.player.getId() == playerid){
							this.mainJFrame.addLordPokers(pokers);

							// ??????????????
							this.mainJFrame.showChuPai();
						}

						// ????????????
						this.mainJFrame.showLord(playerid);

						// ????????????????????????????????
						this.mainJFrame.megLabel.setVisible(false);

						// ????????????????????????????????
						this.mainJFrame.SelectPokerClick();
					}

					// ??????
					if (typeid == 3){
						// ????????????????
						this.mainJFrame.showMeg(3, playerid);

						// ??????????????
						if (this.mainJFrame.player.getId() == playerid + 1 || this.mainJFrame.player.getId() == playerid - 2){
							this.mainJFrame.showChuPai();
						}
					}

					// ????
					if (typeid == 4){

						// ??????????????
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

						// ??????????????
						this.mainJFrame.showOutPokers(playerid, pokers);

						// ??????????????
						if (this.mainJFrame.player.getId() == playerid + 1 || this.mainJFrame.player.getId() == playerid - 2){
							this.mainJFrame.showChuPai();
						}

						// ????????????????????
						this.mainJFrame.prevPlayerId = playerid;
					}

					// ????????
					if (typeid == 5){
						if (this.mainJFrame.player.getId() == playerid){
							JOptionPane.showMessageDialog(this.mainJFrame, "??????");
						}else {
							JOptionPane.showMessageDialog(this.mainJFrame, "??????");
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
