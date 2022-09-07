package DouDiZhuClient.com.wowowo.thread;

import java.io.DataOutputStream;
import java.net.Socket;

public class SendThead extends Thread {

	private String meg; // 用户消息
	public String getMeg() {
		return meg;
	}
	private boolean isRun = true; // 停止运行

	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean run) {
		isRun = run;
	}

	public void setMeg(String meg) {
		this.meg = meg;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	private Socket socket; // 获取客户端连接

	public SendThead(String meg, Socket socket) {
		this.meg = meg;
		this.socket = socket;
	}

	public SendThead(Socket socket) {
		this.socket = socket;
	}

	public SendThead() {

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			// 发送消息到服务端
			DataOutputStream dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
			while (true) {
				if (this.isRun == false){
					break;
				}
				if (this.meg != null) {
					dataOutputStream.writeUTF(this.meg);
					this.meg = null;
				}
				Thread.sleep(50);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
