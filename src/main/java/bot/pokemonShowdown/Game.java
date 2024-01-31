package bot.pokemonShowdown;

import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class Game {
	private Pokemon[] sideList, oppList;
	private BattleBot b;
	public static HashMap<String,Game> games;
	private Pokemon sideActive, oppActive;
	private String oppPlayer;
	static {
		games=new HashMap<String,Game>();
	}
	Game(String gameCode) {
		sideList=new Pokemon[6];
		oppList=new Pokemon[6];
		BattleBot.createBattleBot(gameCode);
		b=BattleBot.getBattleBot(gameCode);
		games.put(gameCode, this);
	}
	
	public void recievedGameMessage(String s) {
        String[] splitMsg = s.split("\\n");
        for(String p:splitMsg){
            String[] data = p.substring(1).split("\\|");
            System.out.println(Arrays.toString(data));
            for(int i=0; i<data.length; i++) {
            	if(data[i].equals("player")&&data.length>(i+2)) {
            		if(data[i+1].equals("p1")&&data[i+2].equals("Ghost445")) {
            			oppPlayer="p2";
            			System.out.println("OPPONENT is p2!");
            		}
            		else {
            			oppPlayer="p1";
            			System.out.println("OPPONENT is p1!");
            		}
            		i+=2;
            		continue; // Loop will enter at i+=3
            	}
            	if(data[i].equals("switch")&&data.length>(i+2)&&data[i+1].equals(oppPlayer+"a")) {
            		//Terrible code, should move to pokemon class somewhere.
        			String[] activePokemon=data[i+2].toLowerCase().replace(" ", "").split(",");
        			String name = activePokemon[0].replace("-", ""); //landorus-therian
        			int level = Integer.parseInt(activePokemon[1].substring(1)); //L86, remove L
        			int currHp = Integer.parseInt(activePokemon[2].split("/")[0]);
        			oppActive=Pokemon.createPokemon(name, level);
            		oppActive.setCurrStat(Stat.HP, (int)(Math.floor(oppActive.getBaseStat(Stat.HP)*(currHp/100.0))));
            		i+=2;
            		continue; // Loop will enter at i+=3
            	}
            	if(data[i].equals("request")&&data.length>(i+1)) {
            		String json = data[i+1];
            		JSONObject turnData = new JSONObject(json);
            		parseTurn(turnData);
            		i+=1;
            		continue; // Loop will enter at i+=2
            	}
            	//TODO: parse some websocket message to get enemy side (if possible...)
            }
        }
	}
	
	private void parseTurn(JSONObject turnData) {
		System.out.println("Parsing TURN!");
		JSONObject activeJSON = turnData.getJSONArray("active").getJSONObject(0);
		JSONObject sideJSON = turnData.getJSONObject("side");
		System.out.println(sideJSON.toString());
		JSONArray pokemonArray = sideJSON.getJSONArray("pokemon");
		System.out.println(sideList.length);
		if(sideList.length==0||sideList[0]==null) { //First time:
			System.out.println("POKEMON ARRAY: "+pokemonArray.toString());
			System.out.println("POKEMON ARRAY LENGTH: "+pokemonArray.length());
			sideActive=Pokemon.createPokemon(pokemonArray.getJSONObject(0));
			for(int i=0; i<pokemonArray.length(); i++) {
				sideList[i] = Pokemon.createPokemon(pokemonArray.getJSONObject(i));
			}
			//TODO: Do something with activeJSON
		}
		else {
			for(int i=0; i<pokemonArray.length(); i++) {
				JSONObject pokemon = pokemonArray.getJSONObject(i);
				sideList[i].updatePokemon(pokemon);
			}
			//TODO: Update active pokemon with activeJSON
		}
		// If activeJSON todos for firsttime and subsequent are same, move code here.
		if(sideActive==null) {
			System.out.println("Game::parseTurn() sideActive is null - Something went totally wrong");
			return;
		}
		System.out.println("Side Active: "+sideActive);
		System.out.println("Side List: "+Arrays.toString(sideList));
	}
	
}
