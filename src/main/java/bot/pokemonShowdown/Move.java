package bot.pokemonShowdown;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONObject;

public class Move implements Cloneable {
	private String id,category;
	private Type type;
	private int pow;
	private double acc;
	private boolean unknown;
// this is basically cookies so we don't have to lookup the same move twice
	private static HashMap<String,Move> moveCache;
	private static JSONObject movedex;

	static {
		moveCache=new HashMap<String,Move>();

		File f = new File("movedex.json");
		InputStream is;
		try {
			is = new FileInputStream("movedex.json");
			Scanner s = new Scanner(is).useDelimiter("\\A");
			String jsonTxt = s.hasNext() ? s.next() : "";
			movedex = new JSONObject(jsonTxt);
		} 
		catch (FileNotFoundException e) {
			System.out.println("Failed to init pokedex.");
		}
	}

	private Move() {
		unknown=true;
	}
	private Move(String id) {
		unknown=false;
		this.id=id;
		JSONObject o = movedex.getJSONObject(id);
		type = Type.toType(o.getString("type"));
		Object accObj=o.get("accuracy");
		if(accObj instanceof Boolean)
			acc=1.0;
		else if(accObj instanceof Integer)
			acc=(Integer)accObj/100.0;
		else {
			System.out.println("Err: Acc is not bool or int?");
			System.out.println(o.toString());
		}
		pow=o.getInt("basePower");
		category=o.getString("category");
		//TODO: V2: Add pp, priority, flags, drain, target, secondary (chance, boosts).
	}
	public static Move createMove(String id) {
		if(moveCache.containsKey(id))
			try {
				return (Move)(moveCache.get(id).clone());
			} catch(CloneNotSupportedException e) {
				System.out.println("Move::createMove() - CloneNotSupportedException");
			}
		return new Move(id);
	}
	@Override
	public String toString() {
		return "{id: "+id+",pow: "+pow+",acc: "+acc+"}";
	}
}
