package bot.pokemonShowdown;

import java.util.Arrays;

public class MessageSanitizer {
	public static void processMessage(String message) {
		String prefix=message.substring(0,Math.min(100, message.length()));
		System.out.println("PROCESSING MESSAGE: "+prefix);
		Object[] objectArray = Game.games.keySet().toArray();
		String[] gameKeys = Arrays.copyOf(objectArray, objectArray.length, String[].class);
		for(String k:gameKeys) {
			if(prefix.contains(k)) {
				Game.games.get(k).recievedGameMessage(message);
				return;
			}
		}
		if(prefix.contains("challstr")) {
			System.out.println("inside");
    		String challStr = message.substring("|challstr|".length());
			User.buildLogin(challStr);
		}
		else if(prefix.contains("|init|battle")) {
    		String gameCode = prefix.split("\\|")[0].substring(1).replace("\\n", "");
    		System.out.println("Creating GAME with gameCode: "+gameCode);
    		Game g=new Game(gameCode);
    		g.recievedGameMessage(message);
		}
		else if(prefix.contains("|pm|")) {
			PMManager.processPM(message);
		}
	}
}
