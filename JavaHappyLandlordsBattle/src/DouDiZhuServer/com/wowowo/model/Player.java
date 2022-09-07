package DouDiZhuServer.com.wowowo.model;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

// �����
public class Player {
	
	private int id; // ���id
	private String name; // �������
	private List<Poker> pokers = new ArrayList<>(); // �˿����б�
	private Socket socket; // ��ҿͻ�������
	
	public Player() {
		
	}
	
	public Player(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Player(int id) {
		this.id = id;
	}
	
	public Player(int id, String name, List<Poker> pokers, Socket socket) {
		this(id, name, socket);
		this.pokers = pokers;
	}

	public Player(int id, String name, Socket socket){
		this(id, name);
		this.socket = socket;
	}

	public Player(int id, String name, List<Poker> pokers){
		this(id, name);
		this.pokers = pokers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Poker> getPokers() {
		return pokers;
	}

	public void setPokers(List<Poker> pokers) {
		this.pokers = pokers;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	@Override
	public String toString() {
		return "Player{" +
				"id=" + id +
				", name='" + name + '\'' +
				", pokers=" + pokers +
				", socket=" + socket +
				'}';
	}
}
