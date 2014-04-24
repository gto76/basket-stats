package si.gto76.basketstats.swingui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.Util;
import si.gto76.basketstats.coreclasses.Event;
import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.Action;
import si.gto76.basketstats.coreclasses.Stat;
import si.gto76.basketstats.coreclasses.Team;

public class SwinGui {
    //private static ArrayList<Image> iconsActive;
    private static ArrayList<Image> iconsNotActive;
    //private static String os = System.getProperty("os.name");
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    //////////////////////////////////////////////////////////////
	Game game;
	private Deque<Event> stackOfCommands = new ArrayDeque<Event>();
	boolean stateChangedSinceLastSave = false;
	
	JFrame frame = new JFrame(Conf.APP_NAME);
	JPanel mainPanel;
	private Menu menu;
	private JLabel team1Label = new JLabel();
	private JLabel team2Label = new JLabel();

	private int windowWidth = Math.min(Conf.WINDOW_WIDTH, screenSize.width - 50);
	private int windowHeight = Math.min(Conf.WINDOW_HEIGHT, screenSize.height - 50);
	
	final Map<Action, JButton> buttonMap = new HashMap<Action, JButton>();
	final Map<Player, PlayersRow> playersRowMap = new HashMap<Player, PlayersRow>();
	protected boolean warnAboutUnevenSquads = true;
	//////////////////////////////////////////////////////////////
	
    /*
	 * #### #### #### #### #### #### ####
	 * INIT INIT INIT INIT INIT INIT INIT
	 * #### #### #### #### #### #### ####
	 */
	{
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	    ToolTipManager.sharedInstance().setInitialDelay(Conf.TOOLTIP_DELAY);
	}
	public SwinGui(final Game game) {
		this.game = game;
		setIcons();
		addMenus();
		setUpNewContainer();
		updateUndoLabel();
	}

	/*
	 * 1. SET ICONS
	 */
	private void setIcons() {
    	final ImageIcon iconImgS = new ImageIcon(getClass().getResource(Conf.ICON_FILENAME_S));
//    	final ImageIcon iconImgSBlue = new ImageIcon(getClass().getResource(Conf.ICON_FILENAME_S_BLUE));
    	final ImageIcon iconImgM = new ImageIcon(getClass().getResource(Conf.ICON_FILENAME_M));
    	final ImageIcon iconImgL = new ImageIcon(getClass().getResource(Conf.ICON_FILENAME_L));
    	final ImageIcon iconImgXL = new ImageIcon(getClass().getResource(Conf.ICON_FILENAME_XL));
    	
//    	iconsActive = new ArrayList<Image>() {
//			private static final long serialVersionUID = 4560955969369357297L;
//			{add(iconImgSBlue.getImage()); add(iconImgM.getImage()); add(iconImgL.getImage()); add(iconImgXL.getImage());}
//    	};
    	iconsNotActive = new ArrayList<Image>() {
			private static final long serialVersionUID = -337325274310404675L;
			{add(iconImgS.getImage()); add(iconImgM.getImage()); add(iconImgL.getImage()); add(iconImgXL.getImage());}
    	};
    	
//    	if (os.startsWith("linux")) {
//    		frame.setIconImages(iconsActive);
//			frame.addWindowFocusListener(new WindowAdapter() {
//	            public void windowGainedFocus(WindowEvent e) {
//	              frame.setIconImages(SwinGui.iconsActive);
//	            }
//	            public void windowLostFocus(WindowEvent e) {
//	              frame.setIconImages(SwinGui.iconsNotActive);
//	            }
//	        });
//    	} else {
    		frame.setIconImages(iconsNotActive);
//		}
	}
	
	/*
	 * 2. ADD MENUS
	 */
	private void addMenus() {
		menu = new Menu();
		frame.setJMenuBar(menu.getMenuBar());
		ListenersMenu.add(menu, this);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				onWindowClose();
			}
		});
	}
	
	/*
	 * 3. ADD CONTAINER
	 */
	protected void setUpNewContainer() {
		initializeContainer();
		fillContainer();
		sealContainer();
	}
	
	private void initializeContainer() {
		mainPanel = new JPanel(new GridBagLayout());
	}

	private void fillContainer() {
		MainContainer.fill(this, team1Label, team2Label);
		updateScore();
	}
	
	private void sealContainer() {
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 6, 1, 2));
		frame.setContentPane(mainPanel);
		frame.setSize(windowWidth, windowHeight);
		frame.setVisible(true);
	}
	
	/*
	 * #### #### #### #### #### #### ####
	 * UNDO UNDO UNDO UNDO UNDO UNDO UNDO
	 * #### #### #### #### #### #### ####
	 */
	
	protected void undo() {
		if (stackOfCommands.size() == 0) {
			return;
		}
		// Put right players on floor
		Event lastCommand = popCommandFromStack();
		setPlayersOnFloorAndUpdatePlayersRow(lastCommand);
		// Undo the action
		Action lastAction = lastCommand.action;
		//Integer scoreDelta = lastAction.undo();
		lastAction.undo();
		updateScore();
		// Update the button
		JButton button = buttonMap.get(lastAction);
		Util.setButtonText(button, lastAction);
		// Print
		System.out.println(game);
		System.out.println("UNDO!");
		setUpNewContainer(); // because of remove player popup menu
	}

	private void setPlayersOnFloorAndUpdatePlayersRow(Event command) {
		Set<Player> team1players = command.team1PlayersOnTheFloor;
		Set<Player> team2players = command.team2PlayersOnTheFloor;

		for (Entry<Player, PlayersRow> tuple : playersRowMap.entrySet()) {
			Player player = tuple.getKey(); 
			PlayersRow playersRow = tuple.getValue();
			if (team1players.contains(player) || team2players.contains(player)) {
				playersRow.enable();
			} else {
				playersRow.disable();
			}
		}
	}
	
	protected void pushCommandOnStack(Action action) {
		stateChangedSinceLastSave = true;
		Event event = new Event(action, new HashSet<Player>(game.getTeam1().getPlayersOnTheFloor()), 
				new HashSet<Player>(game.getTeam2().getPlayersOnTheFloor()));
		stackOfCommands.push(event);
		updateUndoLabel();
		setUpNewContainer(); // because of remove player popup menu
	}

	private Event popCommandFromStack() {
		stateChangedSinceLastSave = true;
		Event command = stackOfCommands.pop();
		updateUndoLabel();
		return command;
	}
	
	private void updateUndoLabel() {
		Event command = stackOfCommands.peek();
		if (command != null) {
			Action action = command.action;
			menu.menuEditUndo.setEnabled(true);
			menu.menuEditUndo.setText("Undo \"" + action.getStat().getExplanation() +
					" by " + action.getPlayer().getName() + "\"");
		} else {
			menu.menuEditUndo.setEnabled(false);
			menu.menuEditUndo.setText("Undo");
		}
	}

	/*
	 * ##### ##### ##### ##### ##### ##### #####
	 * OTHER OTHER OTHER OTHER OTHER OTHER OTHER 
	 * ##### ##### ##### ##### ##### ##### #####
	 */
	
	protected void updateScore() {
		Team team1 = game.getTeam1();
		team1Label.setText(getTeamNameAndScore(team1));
		Team team2 = game.getTeam2();
		team2Label.setText(getTeamNameAndScore(team2));
	}
	
	protected void updateTeamLabelReference(Team team, JLabel teamLabel) {
		if (game.getTeam1().equals(team)) {
			team1Label = teamLabel;
		} else {
			team2Label = teamLabel;
		}
		updateAddNewPlayerMenuItem();
	}
	
	private void updateAddNewPlayerMenuItem() {
		menu.menuEditAddPlayer1.setText("Add Player to " + game.getTeam1().getName());
		menu.menuEditAddPlayer2.setText("Add Player to " + game.getTeam2().getName());
	}
	
	protected void addNewPlayerToTeam(Team team) {
		int noOfPlayers = team.getNumberOfPlayers();
		Player player = new Player("Player " + Integer.toString(noOfPlayers+1));
		team.addPlayer(player);
		windowHeight = frame.getHeight();
		windowWidth = frame.getWidth();
		setUpNewContainer();
		System.out.println(game);
	}
	
	/*
	 * ON EXIT
	 */
	protected void onWindowClose() {
		if (stateChangedSinceLastSave) {
			confirmExit();
		} else {
			System.exit(0);
		}
	}

	private void confirmExit() {
		if (exitDialog("Game was not saved.\n" +
				"Are you sure you want to exit?")) {
			System.exit(0);
		}
	}
	
	/*
	 * ##### ##### ##### ##### ##### ##### #####
	 * UTILS UTILS UTILS UTILS UTILS UTILS UTILS 
	 * ##### ##### ##### ##### ##### ##### #####
	 */
	
	protected static boolean exitDialog(String text) {
		String ObjButtons[] = { "Yes", "No" };
		int PromptResult = JOptionPane.showOptionDialog(null,
				text, "",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
				ObjButtons, ObjButtons[1]);
		if (PromptResult == JOptionPane.YES_OPTION) {
			return true;
		} else {
			return false;
		}
	}

	private static String getTeamNameAndScore(Team team) {
		return team.getName() + ": " + team.get(Stat.PTS);
	}
}
