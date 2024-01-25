package bot.pokemonShowdown;

import java.util.HashMap;

import org.json.JSONObject;

public class BattleBot {
	String gameCode;
	JSONObject data;
	private static HashMap<String,BattleBot> bots;
	static {
		bots=new HashMap<String,BattleBot>();
	}
	
	private BattleBot(String gameCode) {
		this.gameCode=gameCode;
	}
	
	public static void createBattleBot(String gameCode) {
		if(bots.containsKey(gameCode)) return;
		BattleBot b = new BattleBot(gameCode);
		bots.put(gameCode, b);
	}
	
	public static BattleBot getBattleBot(String gameCode) {
		return bots.get(gameCode);
	}
	
	public String makeMove(Game g) {
		return "";
	}
}
