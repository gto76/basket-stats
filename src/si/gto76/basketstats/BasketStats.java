package si.gto76.basketstats;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import si.gto76.basketstats.coreclasses.*;
import si.gto76.basketstats.swingui.SwingFiller;

public class BasketStats {

	public static void main(String[] args) throws FileNotFoundException {
		/*
		Player
		jure = new Player("Jure", "Sorn"),
		robi = new Player("Robert", "Music"),
		luka = new Player("Luka", "Kuhar"),
		pedza = new Player("Predrag", "Mitrovic"),
		kantri = new Player("Rok", "Kovac"),
		nejko = new Player("Nejc", "Levstek"),
		gindi = new Player("Jakob", "Gindiciosi"),
		breza = new Player("Rok", "Brezovar"),
		avsenak = new Player("Matej", "Avsenak");
		//nadan = new Player("Nadan", "Gregoric"),
		//rok = new Player("Rok", "Koderman");
		
		Team
		kekci = new Team("KEKCI", Arrays.asList(jure, robi, pedza, breza, nejko)),
		rozleti = new Team("ROZLETI", Arrays.asList(luka, kantri, gindi, avsenak, nejko));
		Long startTime = Date.parse("Tue, 17 april 2012 21:30");
		Game derbi = new Game(kekci, rozleti, new Date(startTime), "Dvorana Trnovo");
		*/
		
		//String FILE_NAME = "game1";
		//String gameString = new Scanner( new File(FILE_NAME) ).useDelimiter("\\A").next();
		//System.out.println(gameString);
		//Game derbi2 = GameLoader.createGameFromString(gameString);

		List<Player> ppp1 = new ArrayList<>();
		List<Player> ppp2 = new ArrayList<>();
		for (int i = 0; i < Conf.INITAIAL_NUMBER_OF_PLAYERS_IN_ONE_TEAM; i++) {
			ppp1.add( new Player("Player " + Integer.toString(i+1)) ); 
			ppp2.add( new Player("Player " + Integer.toString(i+1)) ); 
		}
		
		Team
		team1 = new Team("TEAM A", ppp1),
		team2 = new Team("TEAM B", ppp2);
		Long startTime = Date.parse("Tue, 17 april 2012 21:30"); 
		Game derbi = new Game(team1, team2, new Date(startTime), new Location("Dvorana Trnovo"));
		
		new SwingFiller(derbi);
		System.out.println(derbi);
	}
	
}
