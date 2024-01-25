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
	private static WebsocketClientEndpoint socket;
	private static ShowdownSocket showdownSocketSingleton;
	
	static {
		showdownSocketSingleton=new ShowdownSocket();
	}
	
	public static ShowdownSocket getShowdownSocketSingleton() {
		return showdownSocketSingleton;
	}
	
	private ShowdownSocket() {

	}
	
	private void initMessageListener() {
        socket.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
            public void handleMessage(String message) {
            	MessageSanitizer.processMessage(message);
            }
        });
	}
	
	public void sendMessage(String message) {
		socket.sendMessage(message);
	}
	
	public void start() {
        try {
        	//Establishing websocket connection...
    		socket = new WebsocketClientEndpoint(new URI("ws://sim.smogon.com:8000/showdown/websocket"));
            // add listener
			initMessageListener();
			// starting protocols
			socket.sendMessage("|/autojoin");
		} 
        catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void quit() {
		try {
			socket.userSession.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void newGame() {
		socket.sendMessage("|/search gen8randombattle");
	}
}
