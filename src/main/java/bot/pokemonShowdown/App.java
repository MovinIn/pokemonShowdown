package bot.pokemonShowdown;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
	public static ShowdownSocket s;
    public static void main( String[] args )
    {
    	Scanner scanner = new Scanner(System.in);
    	while(scanner.hasNext()) {
    		if(scanner.nextLine().equalsIgnoreCase("start")) {
    	    	s = new ShowdownSocket();
    		}
    		if(scanner.nextLine().equalsIgnoreCase("close")) {
    			s.quit();
    		}
    		if(scanner.nextLine().equalsIgnoreCase("newGame")) {
    			System.out.println("newGame?");
    			s.newGame();
    		}
    		if(scanner.nextLine().equalsIgnoreCase("quit")) {
    			s.quit();
    			return;
    		}
    	}
    }
}
