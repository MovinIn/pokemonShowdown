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
	private boolean unknown;
// this is basically cookies so we don't have to lookup the same move twice
	private static HashMap<String,Move> moveCache;
	private static JSONObject movedex;

	static {
		moveCache=new HashMap<String,Move>();

		File f = new File("pokedex.json");
		InputStream is;
		try {
			is = new FileInputStream("moveDex.json");
			Scanner s = new Scanner(is).useDelimiter("\\A");
			String jsonTxt = s.hasNext() ? s.next() : "";
			System.out.println(jsonTxt);
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
		JSONObject o = movedex.getJSONObject(id);
		type = Type.toType(o.getString("type"));
		//TODO: Finish stats
	}
	public static Move createMove(String id) throws CloneNotSupportedException {
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
		return id;
	}
}
