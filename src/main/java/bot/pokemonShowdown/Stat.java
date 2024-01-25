package bot.pokemonShowdown;

public enum Stat {
HP,ATK,DEF,SPA,SPD,SPE;
	public int toInt() {
		switch(this) {
			case HP: return 0;
			case ATK: return 1;
			case DEF: return 2;
			case SPA: return 3;
			case SPD: return 4;
			case SPE: return 5;
			default: return -1;
		}
	}
}
