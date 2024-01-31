package bot.pokemonShowdown;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONArray;
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
	private static final String[] STAT_NAMES = {"hp","def","spa","spd","atk","spe"};
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
		baseStats=new int[6];
		currStats=new int[6];
	}
	
	public static Pokemon createPokemon(JSONObject pokemonData) {
		return new Pokemon(pokemonData);
	}
	
	private Pokemon(JSONObject pokemonData) {
		//This is an initialization, so basestats = currstats at turn 1.
		this();
		String[] details = pokemonData.getString("details").replace(" ", "").toLowerCase().split(",");
		id = details[0].replace("-", "");
		if(details[1].charAt(0)=='l')
			level = Integer.parseInt(details[1].substring(1));
		else
			level=100;
		String[] health = pokemonData.getString("condition").split("/");
		currStats[0] = Integer.parseInt(health[0]); //hp
		JSONObject stats = pokemonData.getJSONObject("stats");
		String[] statNames = JSONObject.getNames(stats);
		for(int i=1; i<currStats.length; i++) { //i=1 skips hp
			currStats[i]=stats.getInt(statNames[i-1]);
		}
		setBaseStats();
		//Moves
		JSONArray moveJSON = pokemonData.getJSONArray("moves");
		for(int i=0; i<moveJSON.length(); i++) {
			moves[i]=Move.createMove(moveJSON.getString(i));
		}
		System.out.println("Final Initialized Pokemon: "+toString());
		//TODO: V2: Add baseAbility, ability, item
	}
	
	public void updatePokemon(JSONObject pokemonData) {
		
	}
	
	private Pokemon(String id, int level) {
		this();
		this.id=id;
		this.level=level;
		setBaseStats();
// In reality, enemy pokemon can have varying currStats 
// based on evs and nature (not sent by server).
// We need a better way of representing these various stats (like a range of possible values).
		currStats=baseStats.clone();
	}
	
	private void setBaseStats() {
		JSONObject stats = pokedex.getJSONObject(id).getJSONObject("baseStats");
		for(int i=0; i<STAT_NAMES.length; i++) {
			baseStats[i] = stats.getInt(STAT_NAMES[i]);
		}
		pokemonCache.put(id, this);
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
		return "\n"+id+", L"+level+"\nBase Stats:"+Arrays.toString(baseStats)+"\n"+
				"CurrStats: "+Arrays.toString(currStats)+"\n"+
				"Moves: "+Arrays.toString(moves)+"\n";
	}
}
