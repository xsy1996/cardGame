package com.cardbattlegame.domain;

import java.util.Vector;

import com.cardbattlegame.connect.MyConnection;

import android.app.Application;

public class Hero extends Application{
	public MyConnection getMyConn() {
		return myConn;
	}
	public void setMyConn(MyConnection myConn) {
		this.myConn = myConn;
	}
	public long getMyAccount() {
		return myAccount;
	}
	public void setMyAccount(long myAccount) {
		this.myAccount = myAccount;
	}
	public String getCardJson() {
		return cardJson;
	}
	public void setCardJson(String cardJson) {
		this.cardJson = cardJson;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public Vector<Card> getCards() {
		return cards;
	}
	public void setCards(Vector<Card> cards) {
		this.cards = cards;
	}
	private MyConnection myConn;// ������
	private long myAccount;// �û��ĵ�¼�˺�
	private String cardJson;// ���Ƶĵ�json��
	private String name="";//�û���
	private String level="";//�û��ȼ�
	private String rank="";//�û�����
	private Vector<Card> cards = new Vector<Card>();//�û�����
}
