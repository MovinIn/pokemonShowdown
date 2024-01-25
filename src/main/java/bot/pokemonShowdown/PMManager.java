package bot.pokemonShowdown;

public class PMManager {
	public static void processPM(String pm) {
		if(pm.contains("MovinIn")&&pm.contains("/challenge")) {
			System.out.println("Challenging MovinIn:");
			App.socket.sendMessage("|/utm null");
			App.socket.sendMessage("|/accept movinin");
		}
	}
}
