package im.core;

import java.util.HashMap;


import im.core.Connection;
import im.domain.DB;
import im.domain.User;

public class Connection_manager {
	public static HashMap<String, Connection> table = new HashMap<String, Connection>();
	public static HashMap<String, String> room_table = new HashMap<String, String>();
	public static void put(String account, Connection conn) {
		System.out.println("====账号" + account + "上线了");
		remove(account);
		table.put(account, conn);
		User u = DB.getByAccount(account);
	}
	public static void remove(String account) {
		if (table.containsKey(account)) {
//			System.out.println("====账号" + account + "下线了");
			table.remove(account);
		}
	}
	public static Connection get(String account) {
		if (table.containsKey(account)) {
			return table.get(account);
		}
		return null;
	}

	public static User get_player(String account) {
		return DB.getByAccount(account);
	}
	public static void remove_room(String account,String enemy) {
		room_table.remove(account);
		room_table.remove(enemy);
	}
	public static void put_enemy(String account, String enemy) {
		System.out.println("====账号" + account + "VS"+enemy);
		room_table.put(account, enemy);
		room_table.put(enemy,account);
	}
	public static String get_enemy(String account) {
		if (room_table.containsKey(account)) {
			return room_table.get(account);
		}
		return null;
	}
}
