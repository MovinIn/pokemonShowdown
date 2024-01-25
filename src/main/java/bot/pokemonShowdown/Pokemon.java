package bot.pokemonShowdown;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONObject;

public class Pokemon implements Cloneable {
	private int[] baseStats;
	private int[] currStats;
	private String id;
	private int level;
	private static JSONObject pokedex;
// this is basically cookies so we don't have to lookup the same pokemon twice
	private static HashMap<String,Pokemon> pokemonCache;
	private Move[] moves;
	
	static {
		pokemonCache=new HashMap<String,Pokemon>();
		
		File f = new File("pokedex.json");
		InputStream is;
		try {
			is = new FileInputStream("pokedex.json");
			Scanner s = new Scanner(is).useDelimiter("\\A");
			String jsonTxt = s.hasNext() ? s.next() : "";
			pokedex = new JSONObject(jsonTxt);
		} 
		catch (FileNotFoundException e) {
			System.out.println("Failed to init pokedex.");
		}
	}
	
	public static Pokemon createPokemon(String name, int level) {
		String id = name.replace("-", "").toLowerCase();
		if(pokemonCache.containsKey(id))
			try {
				return (Pokemon)(pokemonCache.get(id).clone());
			} catch(CloneNotSupportedException e) {
				System.out.println("Pokemon::createPokemon() - CloneNotSupportedException");
			}
		return new Pokemon(id,level);
	}
	
	private Pokemon() {
		moves=new Move[4];
		baseStats=new int[5];
		currStats=new int[5];
	}
	
	public static Pokemon createPokemon(JSONObject pokemonData) {
		return new Pokemon(pokemonData);
	}
	
	private Pokemon(JSONObject pokemonData) {
		this();
		String details = pokemonData.getString("details").replace(" ", "").toLowerCase();
		id = details.split(",")[0].replace("-", "");
		level = Integer.parseInt(details.split(",")[1].substring(1));
		String[] health = pokemonData.getString("condition").split("/");
		int currHp = Integer.parseInt(health[0]);
		//TODO: Finish stats
	}
	
	private Pokemon(String id, int level) {
		this();
		this.id=id;
		this.level=level;
		JSONObject stats = pokedex.getJSONObject(id).getJSONObject("baseStats");
		String[] names = JSONObject.getNames(stats);
		for(int i=0; i<names.length; i++) {
			baseStats[i] = stats.getInt(names[i]);
		}
		pokemonCache.put(id, this);
// In reality, enemy pokemon can have varying currStats 
// based on evs and nature (not sent by server).
// We need a better way of representing these various stats (like a range of possible values).
		currStats=baseStats.clone();
	}
	
	public int getBaseStat(Stat s) {
		if(s==null||s.toInt()==-1) throw new IllegalArgumentException("passed through null stat value");
		return baseStats[s.toInt()];
	}
	public int getCurrStat(Stat s) {
		if(s==null||s.toInt()==-1) throw new IllegalArgumentException("passed through null stat value");
		return currStats[s.toInt()];
	}
	
	public void setCurrStat(Stat s,int value) {
		if(s==null||s.toInt()==-1) throw new IllegalArgumentException("passed through null stat value");
		currStats[s.toInt()]=value;
	}
	
	public int[] getBaseStats() {
		return baseStats;
	}
	
	public int[] getCurrStats() {
		return currStats;
	}
	
	@Override
	public String toString() {
		return id+", L"+level+"\nBase Stats:"+Arrays.toString(baseStats)+"\n"+
				"CurrStats: "+Arrays.toString(currStats)+"\n"+
				"Moves: "+Arrays.toString(moves)+"\n";
	}
}
