package com.cardbattlegame.domain;

public class Card {
	private int id;//用于查询图片和数据库查询
	private int type;//卡片类型
	private int gamefeature1;//比如攻击
	private int gamefeature2;//比如血量
	
	public Card(int id, int type, int gamefeature1, int gamefeature2) {
		super();
		this.id = id;
		this.type = type;
		this.gamefeature1 = gamefeature1;
		this.gamefeature2 = gamefeature2;
	}
	
}
