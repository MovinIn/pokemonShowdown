package bot.pokemonShowdown;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

public class ShowdownSocket {
	private WebsocketClientEndpoint socket;
	public static String challStr;
	public final static String password = "la20jose61cwp";
	public final static String name = "Ghost445";
	public boolean inGame=false;
	ShowdownSocket() {
        try {
        	//Establishing websocket connection...
			socket = new WebsocketClientEndpoint(new URI("ws://sim.smogon.com:8000/showdown/websocket"));
			
            // add listener
			initMessageListener();
			// starting protocols
			start();
		} 
        catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initMessageListener() {
        socket.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
            public void handleMessage(String message) {
            	// Welp, I adding spagetti here or...?
            	if(message.length()>1000&&message.contains("|request|{\"active\":")) {
            		String[] messageArr = message.split("\\|");
            		String gameCode = messageArr[0].substring(1).replace("\\n", "");
            		String jsonStr = messageArr[1];
            		BattleBot.getBattleBot(gameCode).setJSON(new JSONObject(jsonStr));
            	}
            	if(message.length()<300&&message.contains("|init|battle")) {
            		String gameCode = message.split("\\|")[0].substring(1).replace("\\n", "");
            		System.out.println("GAMECODE: "+gameCode);
            		BattleBot.createBattleBot(gameCode);
            	}
            	if(message.contains("challstr")) {
            		challStr = message.substring("|challstr|".length());
            		System.out.println("CHALLSTR: "+challStr);
            		login(name,password,challStr);
            	}
            	else {
            		System.out.println(message);
            	}
            }
        });
	}
	
	private void login(String name,String pass,String challStr) {
		try { 		// Fetch assertion using http post
			System.out.println("Fetching assertion using http post..");
	    	HttpClient httpclient = HttpClients.createDefault();
	    	URIBuilder builder;
			builder = new URIBuilder("https://play.pokemonshowdown.com/~~showdown/action.php");
	    	builder.setParameter("act", "login");
	    	builder.setParameter("name", name);
	    	builder.setParameter("pass", pass);
	    	builder.setParameter("challstr", challStr);
	    	HttpPost post = new HttpPost(builder.build());
	    	
	    	//Execute and get the response.
	    	HttpResponse response = httpclient.execute(post);
	    	HttpEntity entity = response.getEntity();
	    	if (entity != null) {
	    	    try (InputStream instream = entity.getContent()) {
	    	        // do something useful
	        		 Scanner s = new Scanner(instream).useDelimiter("\\A");
	        		 String result = s.hasNext() ? s.next() : "";
	        		 System.out.println("result from http post: "+result);
	        		 JSONObject o=new JSONObject(result.substring(1));
	        		 if(o.getBoolean("actionsuccess")) {
		        		 String assertion = o.getString("assertion");
		        		 setName(name,assertion);
	        		 }
	        		 else {
	        			 System.out.println("password wrong or server down.");
	        		 }
	    		 }
	    	}
		}
		catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void start() {
		socket.sendMessage("|/autojoin");
	}
	private void setName(String name,String assertion) {
		 String message = "|/trn "+name+",0,"+assertion;
		 System.out.println(message);
		 socket.sendMessage(message);
	}
	public static String getChallStr() {
		return challStr;
	}
	public void quit() {
		try {
			socket.userSession.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void newGame() {
		if(inGame) return;
		inGame=true;
		socket.sendMessage("|/search gen8randombattle");
	}
}
