package bot.pokemonShowdown;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
	public static ShowdownSocket socket;
	public static void main( String[] args )
	{
		Scanner scanner = new Scanner(System.in);
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if(line.equalsIgnoreCase("start")) {
				socket = ShowdownSocket.getShowdownSocketSingleton();
				socket.start();
			}
			else if(line.equalsIgnoreCase("close")) {
				socket.quit();
			}
			else if(line.equalsIgnoreCase("newGame")) {
				System.out.println("newGame?");
				socket.newGame();
			}
			else if(line.equalsIgnoreCase("quit")) {
				socket.quit();
				scanner.close();
				return;
			}
		}
	}
}
