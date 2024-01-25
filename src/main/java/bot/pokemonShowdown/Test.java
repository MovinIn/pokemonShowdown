package bot.pokemonShowdown;

import java.util.Arrays;

public class Test {
    public static void main(String args[]) {
        String lol = ">battle-gen8randombattle-2042574113\n|init|battle\n|title|MovinIn vs. Ghost445\n|j|☆MovinIn\n|j|☆Ghost445\n|t:|1706063996\n|gametype|singles\n|player|p1|MovinIn|266|\n|player|p2|Ghost445|265|\n|teamsize|p1|6\n|teamsize|p2|6\n|gen|8\n|tier|[Gen 8] Random Battle\n|rule|Species Clause: Limit one of each Pokémon\n|rule|HP Percentage Mod: HP is shown in percentages\n|rule|Sleep Clause Mod: Limit one foe put to sleep\n|rule|Illusion Level Mod: Illusion disguises the Pokémon's true level\n|\n|t:|1706063996\n|start\n|switch|p1a: Politoed|Politoed, L86, M|100/100\n|switch|p2a: Hitmonchan|Hitmonchan, L86, M|100/100\n|-weather|RainDance|[from] ability: Drizzle|[of] p1a: Politoed\n|turn|1\n|l|☆Ghost445\n|player|p2|\n";
        String[] splitBy = lol.split("\\n");
        System.out.println(splitBy.length);
        for(String p:splitBy){
            String[] data = p.split("\\|");
            System.out.println(Arrays.toString(data));
        }
    }
}
