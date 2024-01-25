package bot.pokemonShowdown;

public enum Type {
	NORMAL,FIRE,WATER,ELECTRIC,GRASS,ICE,FIGHTING,POISON,GROUND,FLYING,PSYCHIC,BUG,ROCK,GHOST,DRAGON,DARK,STEEL,FAIRY;
	public static Type toType(String s) {
		for(Type t:Type.values()) {
			if(t.name().equalsIgnoreCase(s)) {
				return t;
			}
		}
		return null; 
	}
}
