package bot.pokemonShowdown;

import org.json.JSONObject;

public abstract class StatCalculator {
	private static JSONObject pokedex;
	private static JSONObject moves;
	public static int calcHP(int base, int iv, int ev, int level) {
		return (int)(Math.floor(0.01*(2*base+iv+Math.floor(0.25*ev))*level)+level+10);
	}
	public static int calcStats(int base,int iv, int ev, int level, double nature) {
		return (int)(Math.floor(0.01*(2*base+iv+Math.floor(0.25*ev))*level+5)*nature);
	}
	
	
	
}
