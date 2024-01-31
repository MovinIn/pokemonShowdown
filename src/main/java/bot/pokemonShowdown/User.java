package bot.pokemonShowdown;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

public abstract class User {
	public final static String name = "Ghost445";
	public final static String password = "fs3HEShRZteSF!4";
	public static String challStr;
	public static void buildLogin(String challStr) throws IllegalArgumentException {
		User.challStr=challStr;
		String assertion = fetchAssertion(challStr);
		if(assertion==null) throw new IllegalArgumentException("challStr likely incorrect");
		String message = "|/trn "+name+",0,"+assertion;
		App.socket.sendMessage(message);
		System.out.println("login: "+message);
	}

	private static String fetchAssertion(String challStr) throws IllegalArgumentException {
		try { 		// Fetch assertion using http post
			System.out.println("Fetching assertion using http post..");
			HttpClient httpclient = HttpClients.createDefault();
			URIBuilder builder;
			builder = new URIBuilder("https://play.pokemonshowdown.com/~~showdown/action.php");
			builder.setParameter("act", "login");
			builder.setParameter("name", name);
			builder.setParameter("pass", password);
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
						return o.getString("assertion");
					}
					else {
						System.out.println("password wrong or server down.");
						throw new IllegalArgumentException("password likely incorrect.");
					}
				}
			}
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
