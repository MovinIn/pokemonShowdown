package bot.pokemonShowdown;

public class StatCalculator {
	public static int calcHP(int base, int iv, int ev, int level) {
		return (int)(Math.floor(0.01*(2*base+iv+Math.floor(0.25*ev))*level)+level+10);
	}
	public static int calcStats() {
		return 0;
	}
}
