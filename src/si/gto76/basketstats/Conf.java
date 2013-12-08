package si.gto76.basketstats;

public class Conf {
	public static final String APP_NAME = "HoopStats";
	public static final String VERSION = "0.9.0";
	
	static public final int WINDOW_WIDTH = 1365;
	static public final int WINDOW_HEIGHT = 500;
	static public final int MAIN_H_GAP = 20;
	static public final int MAIN_V_GAP = 5;

	static public final int NUMBER_OF_TABS_FOR_PLAYER_NAME = 3;
	static public final int TAB_WIDTH = 8;
	static public final int PLAYER_NAME_WIDTH = NUMBER_OF_TABS_FOR_PLAYER_NAME * TAB_WIDTH;
	
	public static final int INITAIAL_NUMBER_OF_PLAYERS_IN_ONE_TEAM = 3; 
	
	public static final long DOUBLE_CLICK_LAG =  250000000;
	
	// Icons
	public static final String ICON_FILENAME_S = "/resources/ba16.png"; 
	public static final String ICON_FILENAME_S_BLUE = "/resources/ba16blue.png"; // b8cfe5 is color of background
	public static final String ICON_FILENAME_M = "/resources/ba32.png"; 
	public static final String ICON_FILENAME_L = "/resources/ba64.png"; 
	public static final String ICON_FILENAME_XL = "/resources/ba128.png";
}
