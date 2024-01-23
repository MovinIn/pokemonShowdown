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
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

public class WebSocketExample {
	public static final String START_GUEST = "|/autojoin ";
	public static final String RANDOM_GEN8_OU = "|/search gen8randombattle";
	public static final String CHANGE_NAME = "";
	public String challStr;
	public static void main(String[]args) {
        try {
            // open websocket to showdown
            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("ws://sim.smogon.com:8000/showdown/websocket"));
            
            // add listener
            clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                    if(message.contains("updateuser")) {
                    	clientEndPoint.sendMessage(RANDOM_GEN8_OU);
                    }
                }
            });

            // send message to websocket
            clientEndPoint.sendMessage(START_GUEST);
            // wait 10 seconds for messages from websocket
            Thread.sleep(10000);

        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
	}
}
