package bot.pokemonShowdown;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
/**
 * Unit test for simple App.
 */

public class AppTest 
{
	@Test
	@DisplayName("\"ElEctRiC\" = Type.ELECTRIC")
	public void stringToType() {
		assertEquals(Type.ELECTRIC,Type.toType("ElEctRiC"),"ElEctRiC should equal Type.ELECTRIC");
	}
}
