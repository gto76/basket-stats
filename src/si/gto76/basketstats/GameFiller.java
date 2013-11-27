package si.gto76.basketstats;

import java.util.Arrays;
import java.util.Date;

import si.gto76.basketstats.coreclasses.*;
import si.gto76.basketstats.swingfiller.SwingFiller;

public class GameFiller {

	public static void main(String[] args) {
			
		Player 
		jure = new Player("Jure", "Sorn"),
		robi = new Player("Robert", "Music"),
		luka = new Player("Luka", "Kuhar"),
		pedza = new Player("Predrag", "Mitrovic"),
		kantri = new Player("Rok", "Kovac"),
		nejko = new Player("Nejc", "Levstek"),
		gindi = new Player("Jakob", "Gindiciosi"),
		breza = new Player("Rok", "Brezovar"),
		avsenak = new Player("Matej", "Avsenak"),
		nadan = new Player("Nadan", "Gregoric"),
		rok = new Player("Rok", "Koderman");
		
		Team 
		kekci = new Team("KEKCI", Arrays.asList(jure, robi, pedza, breza, nejko)),
		rozleti = new Team("ROZLETI", Arrays.asList(luka, kantri, gindi, avsenak, nejko));
		Long startTime = Date.parse("Tue, 17 april 2012 21:30");
		Game derbi = new Game(kekci, rozleti, new Date(startTime), "Dvorana Trnovo");
		
		new SwingFiller(derbi);
		System.out.println(derbi);
	}
	
}
