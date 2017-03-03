package im.domain;

import java.util.HashMap;



public class DB {
	public static HashMap<String, User> map = new HashMap<String, User>();
	
	static {
		for (int i = 0; i < 50; i++) {
			User user = new User();
			user.account = "1000000" + i;
			user.password = "test";
			user.name = "Alice" + i;
			user.level = i;
			user.rank=i;
			map.put(user.account, user);
		}
		for (int i = 50; i < 100; i++) {
			User user = new User();
			user.account = "1000000" + i;
			user.password = "test";
			user.name = "Bob" + i;
			user.level = i;
			user.rank=i;
			map.put(user.account, user);
		}
	}
	public static User getByAccount(String account) {
		if (map.containsKey(account)) {
			return map.get(account);
		}
		return null;

	}
}
