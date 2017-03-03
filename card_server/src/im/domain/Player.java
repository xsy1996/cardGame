package im.domain;

import java.util.HashMap;

public class Player {
	public int HP;
	public int MP;
	public int my_name;
	public int ene_name;
	public int add_buff=0;
	public int dec_buff=0;
	
	public HashMap<Integer,Integer> card_stack;
	public void Player(){
		HP=100;
		MP=10;
	}
}
