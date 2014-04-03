package si.gto76.basketstats;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Location;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.Team;

public class Conf {
	public static final String APP_NAME = "HoopStats";
	public static final String VERSION = "0.9.1";
	
	static public final int WINDOW_WIDTH = 1365;
	static public final int WINDOW_HEIGHT = 350;
	static public final int MAIN_H_GAP = 20;
	static public final int MAIN_V_GAP = 5;

	static public final int NUMBER_OF_TABS_FOR_PLAYER_NAME = 3;
	static public final int TAB_WIDTH = 8;
	static public final int PLAYER_NAME_WIDTH = NUMBER_OF_TABS_FOR_PLAYER_NAME * TAB_WIDTH;
	
	public static final int INITAIAL_NUMBER_OF_PLAYERS_IN_ONE_TEAM = 2; 
	
	public static final long DOUBLE_CLICK_LAG =  250000000;
	
	// Icons
	public static final String ICON_FILENAME_S = "/resources/ba16.png"; 
	public static final String ICON_FILENAME_S_BLUE = "/resources/ba16blue.png"; // b8cfe5 is color of background
	public static final String ICON_FILENAME_M = "/resources/ba32.png"; 
	public static final String ICON_FILENAME_L = "/resources/ba64.png"; 
	public static final String ICON_FILENAME_XL = "/resources/ba128.png";
	
	public static final int PLAYERS_NAME_COLUMN_WIDTH = 80;
	
	public static Game getDefaultGame() {
		List<Player> ppp1 = new ArrayList<Player>();
		List<Player> ppp2 = new ArrayList<Player>();
		for (int i = 0; i < Conf.INITAIAL_NUMBER_OF_PLAYERS_IN_ONE_TEAM; i++) {
			ppp1.add( new Player("Player " + Integer.toString(i+1)) ); 
			ppp2.add( new Player("Player " + Integer.toString(i+1)) ); 
		}
		
		Team
		team1 = new Team("TEAM A", ppp1),
		team2 = new Team("TEAM B", ppp2);
		return new Game(team1, team2, new Date(), new Location("Venue"));
	}
	
}
