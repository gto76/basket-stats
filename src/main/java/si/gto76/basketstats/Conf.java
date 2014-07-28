package si.gto76.basketstats;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Util;
import si.gto76.basketstats.coreclasses.Venue;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.RecordingStats;
import si.gto76.basketstats.coreclasses.ShotValues;
import si.gto76.basketstats.coreclasses.Stat;

public class Conf {
	// Debug
	public static final boolean DEBUG = false;
	
	// App Name and Version
	public static final String APP_NAME = "HoopStats";
	public static final String VERSION = "0.9.3";
	public static final String AUTHOR = "Jure Sorn";
	public static final String EMAIL = "basketstats.gto76@gmail.com";
	public static final String YEARS = "2013-2014";
	public static final String FILE_EXTENSION = "hsg";

    // Version location
    public static final String VERSION_FILENAME = "version"; // Here is stored the real version that is
        // acquired from git at build time. The one above is just for backup.

	// Look and Feel
	public static final String LOOK_AND_FEEL = "CrossPlatformLookAndFeel";
	
	// Window
    public static final int WINDOW_WIDTH = 1365;
    public static final int WINDOW_HEIGHT = 350;
	public static final long DOUBLE_CLICK_LAG =  250000000;
	public static final int INITIAL_NUMBER_OF_PLAYERS_IN_ONE_TEAM = 2;
	
	// Buttons
	public static final boolean SHOW_STAT_VALUE_ON_BUTTON_LABEL = true;
	public static final String BUTTON_TEXT_SEPARATOR = "  -  ";
	public static final boolean BUTTONS_TOOLTIP = true;
	public static final int TOOLTIP_DELAY = 1000;

	private static final boolean COLORED_REBOUND_BUTTONS = true;
	private static final Color REBOUND_BUTTON_COLOR = Color.GRAY;
	private static final boolean COLORED_MADE_BUTTONS = true;
	private static final Color MADE_SHOT_BUTTON_COLOR = Color.getHSBColor((float)0.25,(float)0.35,(float)0.95);
	private static final boolean COLORED_MISSED_BUTTONS = false;
	private static final Color MISSED_SHOT_BUTTON_COLOR = Color.getHSBColor((float)1,(float)0.25,(float)0.95);
	private static final boolean COLORED_TURNOVER_BUTTONS = false;
	private static final Color TURNOVER_BUTTON_COLOR = Color.getHSBColor((float)1,(float)0.25,(float)0.95);
	
	public static Map<Stat,Color> BUTTON_COLORS = new HashMap<Stat,Color>();

	// Icons
    //public static final String BASE = "/main/resources/";
	public static final String ICON_FILENAME_S = "/resources/ba16.png";
	public static final String ICON_FILENAME_M = "/resources/ba32.png";
	public static final String ICON_FILENAME_L = "/resources/ba64.png";
	public static final String ICON_FILENAME_XL = "/resources/ba128.png";

	// Text Formatting
    public static final int NUMBER_OF_TABS_FOR_PLAYER_NAME = 3;
    public static final int TAB_WIDTH = 8;
    public static final int PLAYER_NAME_WIDTH = NUMBER_OF_TABS_FOR_PLAYER_NAME * TAB_WIDTH;

    public static final int MAX_VENUE_NAME_LENGTH = 100;
    public static final int MAX_TEAM_NAME_LENGTH = 100;
	
	// Stat Combinations Presets
	public enum StatComb {
		FULL_COURT_STATS(new RecordingStats(Stat.IIPM, Stat.IIPF, Stat.TPM, Stat.TPF, Stat.FTM, Stat.FTF, 
				Stat.PM, Stat.OFF, Stat.DEF, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA)),
		SIMPLIFIED_FULL_COURT_STATS(new RecordingStats(Stat.IIPM, Stat.IIPF, Stat.TPM, Stat.TPF, Stat.FTM, 
				Stat.FTF, Stat.PM, Stat.REB, Stat.AST)),
		NBA_RECORDING_STATS_SIMPLIFIED_NO_FT_MISSES(new RecordingStats(Stat.IIPM, Stat.IIPF, Stat.TPM, 
				Stat.TPF, Stat.FTM, Stat.PM, Stat.REB, Stat.AST)),
		NBA_RECORDING_STATS_NO_MISSES(new RecordingStats(Stat.IIPM, Stat.TPM, Stat.FTM, Stat.PM, Stat.OFF,
				Stat.DEF, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA)),
		NBA_RECORDING_STATS_NO_MISSES_SIMPLIFIED(new RecordingStats(Stat.IIPM, Stat.TPM,  Stat.FTM, Stat.PM,
				Stat.REB, Stat.AST)),
		HALF_COURT_STATS(new RecordingStats(Stat.IIPM, Stat.IIPF, Stat.PM, Stat.REB, Stat.AST, Stat.PF, 
				Stat.ST, Stat.TO, Stat.BS, Stat.BA)),
		SIMPLIFIED_HALF_COURT_STATS(new RecordingStats(Stat.IIPM, Stat.IIPF, Stat.PM, Stat.REB, Stat.AST)),
		HALF_COURT_RECORDING_STATS_NO_MISSES(new RecordingStats(Stat.IIPM, Stat.PM, Stat.REB, Stat.AST, 
				Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA)),
		HALF_COURT_RECORDING_STATS_NO_MISSES_SIMPLIFIED(new RecordingStats(Stat.IIPM, Stat.PM, Stat.REB, 
				Stat.AST)),
		HALF_COURT_RECORDING_STATS_NO_MISSES_SIMPLIFIED_NO_P_M(new RecordingStats(Stat.OFF, Stat.DEF, 
				Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA)),
		NO_SCORING(new RecordingStats(Stat.OFF, Stat.DEF, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS,
				Stat.BA)),
		JUST_FOULS(new RecordingStats(Stat.PF)),
		;;;;;;;
		public final RecordingStats stats;//Stat[] stats;
		private StatComb(RecordingStats stats) {
			this.stats = stats;
		}
		@Override
		public String toString() {
			return Util.enumNameToLowerCase(name());
		}
	}
	
	// Default Game
	public static Game getDefaultGame() {
		return getDefaultGame(RecordingStats.DEFAULT, ShotValues.FULL_COURT);
	}
	
	public static Game getDefaultGame(RecordingStats recordingStats, ShotValues shotValues) {
		List<Player> ppp1 = new ArrayList<Player>();
		List<Player> ppp2 = new ArrayList<Player>();
		for (int i = 0; i < Conf.INITIAL_NUMBER_OF_PLAYERS_IN_ONE_TEAM; i++) {
			ppp1.add( new Player("Player " + Integer.toString(i+1)) ); 
			ppp2.add( new Player("Player " + Integer.toString(i+1)) ); 
		}
		return new Game("TEAM A", ppp1, "TEAM B", ppp2, new Date(), new Venue("Venue"), 
				recordingStats, shotValues);
	}
	
	// Help Text
    public static final String HELP_TEXT =
		" \n" +
		"To change players name, team name,\n" +
		"venue or date, double click on the label.\n" +
		" \n" +
		"If you want to delete a player or change\n" +
		"a stat, save a game, then open the game file\n" +
		"with any plain text editor (Notepad, but not Word)\n" +
		"and edit it there. Just don't change the formatting.\n" +
		"When changing stats only change players shots,\n" +
		"def and off rebounds, ast, pf, st, to and bs.\n" +
		"All other stats like players score, total rebounds\n" +
		"and all team stats will be ignored and calculated\n" +
		"anew next time you load the game in HoopStats.\n";

	/*
	 * UTIL
	 */
	
	static {
		if (COLORED_REBOUND_BUTTONS) {
			fillButtonColorMap(REBOUND_BUTTON_COLOR, Stat.OFF, Stat.DEF, Stat.REB);
		}
		if (COLORED_MADE_BUTTONS) {
			fillButtonColorMap(MADE_SHOT_BUTTON_COLOR, Stat.IIPM, Stat.TPM, Stat.FTM);
		}
		if (COLORED_MISSED_BUTTONS) {
			fillButtonColorMap(MISSED_SHOT_BUTTON_COLOR, Stat.IIPF, Stat.TPF, Stat.FTF);
		}
		if (COLORED_TURNOVER_BUTTONS) {
			fillButtonColorMap(TURNOVER_BUTTON_COLOR, Stat.TO);
		}
	}
	
	private static void fillButtonColorMap(Color color, Stat... stats) {
		for (Stat stat : stats) {
			BUTTON_COLORS.put(stat, color);
		}
	}
	
}
