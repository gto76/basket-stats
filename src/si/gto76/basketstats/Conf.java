package si.gto76.basketstats;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import si.gto76.basketstats.coreclasses.Game;
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
	public static final String EMAIL = "sornjure@gmail.com";
	public static final String YEARS = "2013-2014";
	
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
	public static final int TOOLTIP_DELAY = 1000;
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
	
	// Stat Combinations Presets
	public enum StatComb {
		FULL_COURT_STATS(new Stat[]{Stat.IIPM, Stat.IIPF, Stat.TPM, Stat.TPF, Stat.FTM, Stat.FTF, Stat.PM, Stat.OFF, Stat.DEF, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA}),
		SIMPLIFIED_FULL_COURT_STATS(new Stat[]{Stat.IIPM, Stat.IIPF, Stat.TPM, Stat.TPF, Stat.FTM, Stat.FTF, Stat.PM, Stat.REB, Stat.AST}),
		NBA_RECORDING_STATS_SIMPLIFIED_NO_FT_MISSES(new Stat[]{Stat.IIPM, Stat.IIPF, Stat.TPM, Stat.TPF, Stat.FTM, Stat.PM, Stat.REB, Stat.AST}),
		NBA_RECORDING_STATS_NO_MISES(new Stat[]{Stat.IIPM, Stat.TPM, Stat.FTM, Stat.PM, Stat.OFF, Stat.DEF, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA}),
		NBA_RECORDING_STATS_NO_MISES_SIMPLIFIED(new Stat[]{Stat.IIPM, Stat.TPM,  Stat.FTM, Stat.PM, Stat.REB, Stat.AST}),
		STREET_BALL_STATS(new Stat[]{Stat.IIPM, Stat.IIPF, Stat.PM, Stat.REB, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA}),
		SIMPLIFIED_STREET_BALL_STATS(new Stat[]{Stat.IIPM, Stat.IIPF, Stat.PM, Stat.REB, Stat.AST}),
		STREET_BALL_RECORDING_STATS_NO_MISSES(new Stat[]{Stat.IIPM, Stat.PM, Stat.REB, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA}),
		STREET_BALL_RECORDING_STATS_NO_MISSES_SIMPLIFIED(new Stat[]{Stat.IIPM, Stat.PM, Stat.REB, Stat.AST}),
		STREET_BALL_RECORDING_STATS_NO_MISSES_SIMPLIFIED_NO_P_M(new Stat[]{Stat.OFF, Stat.DEF, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA}),
		NO_SCORRING(new Stat[]{Stat.OFF, Stat.DEF, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA}),
		JUST_FOULS(new Stat[]{Stat.PF}),
		;;;;;;;
		public final Stat[] stats;
		private StatComb(Stat[] stats) {
			this.stats = stats;
		}
		@Override
		public String toString() {
			return Util.enumNameToLowerCase(name());
		}
	}
	
	// Default Game
	public static Game getDefaultGame() {
		//RecordingStats recordingStats = new RecordingStats(Util.arrayToSet(Stat.nbaRecordingStats));
		return getDefaultGame(RecordingStats.DEFAULT, ShotValues.DEFAULT);
	}
	
	public static Game getDefaultGame(RecordingStats recordingStats, ShotValues shotValues) {
		List<Player> ppp1 = new ArrayList<Player>();
		List<Player> ppp2 = new ArrayList<Player>();
		for (int i = 0; i < Conf.INITAIAL_NUMBER_OF_PLAYERS_IN_ONE_TEAM; i++) {
			ppp1.add( new Player("Player " + Integer.toString(i+1)) ); 
			ppp2.add( new Player("Player " + Integer.toString(i+1)) ); 
		}
		return new Game("TEAM A", ppp1, "TEAM B", ppp2, new Date(), new Venue("Venue"), 
				recordingStats, shotValues);
	}
	
	// Help Text
	static public final String HELP_TEXT =
		" \n" +
		"To change players name, team name,\n" +
		"venue or date, double click on the label.\n" +
		" \n" +
		"If you want to delete a player or change\n" +
		"a stat, save a game, then open the game file\n" +
		"with any plain text editor (Notepad, but not Word)\n" +
		"and edit it there. Just don't change the formating.\n" +
		"When changing stats only change players shots,\n" +
		"def and off rebounds, ast, pf, st, to and bs.\n" +
		"All other stats like players score, total rebounds\n" +
		"and all team stats will be ignored and calculated\n" +
		"anew next time you load the game in HoopStats.\n";
	
}
