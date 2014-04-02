package si.gto76.basketstats;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Location;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.Team;

public class Conf {
	// App Name and Version
	public static final String APP_NAME = "HoopStats";
	public static final String VERSION = "0.9.1";
	
	// Window
	static public final int WINDOW_WIDTH = 1365;
	static public final int WINDOW_HEIGHT = 350;
	static public final int MAIN_H_GAP = 20;
	static public final int MAIN_V_GAP = 5;
	public static final int PLAYERS_NAME_COLUMN_WIDTH = 80;
	public static final long DOUBLE_CLICK_LAG =  250000000;
	public static final int INITAIAL_NUMBER_OF_PLAYERS_IN_ONE_TEAM = 2;
	
	// Buttons
	public static boolean SHOW_STAT_VALUE_ON_BUTTON_LABEL = true;
	public static String BUTTON_TEXT_SEPARATOR = "  -  ";
	public static final boolean BUTTONS_TOOLTIP = true;
	public static final int TOOLTIP_DELAY = 2000;
	public static Color REBOUND_BUTTON_COLOR = Color.GRAY;
	public static boolean COLORED_MADE_BUTTONS = true;
	public static Color MADE_SHOT_BUTTON_COLOR = Color.getHSBColor((float)0.25,(float)0.35,(float)0.95);
	public static boolean COLORED_MISSED_BUTTONS = false;
	public static Color MISSED_SHOT_BUTTON_COLOR = Color.getHSBColor((float)1,(float)0.25,(float)0.95);
	public static boolean COLORED_TURNOVER_BUTTONS = false;
	public static Color TURNOVER_BUTTON_COLOR = Color.getHSBColor((float)1,(float)0.25,(float)0.95);
	
	// Icons
	public static final String ICON_FILENAME_S = "/resources/ba16.png"; 
	public static final String ICON_FILENAME_S_BLUE = "/resources/ba16blue.png"; // b8cfe5 is color of background
	public static final String ICON_FILENAME_M = "/resources/ba32.png"; 
	public static final String ICON_FILENAME_L = "/resources/ba64.png"; 
	public static final String ICON_FILENAME_XL = "/resources/ba128.png";
	
	// Text Formatting
	static public final int NUMBER_OF_TABS_FOR_PLAYER_NAME = 3;
	static public final int TAB_WIDTH = 8;
	static public final int PLAYER_NAME_WIDTH = NUMBER_OF_TABS_FOR_PLAYER_NAME * TAB_WIDTH;
	
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
