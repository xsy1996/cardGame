package com.cardbattlegame.domain;

public class Card {
	private int id;//���ڲ�ѯͼƬ�����ݿ��ѯ
	private int type;//��Ƭ����
	private int gamefeature1;//���繥��
	private int gamefeature2;//����Ѫ��
	
	public Card(int id, int type, int gamefeature1, int gamefeature2) {
		super();
		this.id = id;
		this.type = type;
		this.gamefeature1 = gamefeature1;
		this.gamefeature2 = gamefeature2;
	}
	
}
