package si.gto76.basketstats.swingui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.Action;
import si.gto76.basketstats.coreclasses.Stat;
import si.gto76.basketstats.coreclasses.Team;

public class SwinGui implements KeyListener {
    private static ArrayList<Image> iconsActive;
    private static ArrayList<Image> iconsNotActive;
    ////////////////////////////////////////
	Game game;
	private Deque<Action> stackOfCommands = new ArrayDeque<Action>();
	boolean stateChangedSinceLastSave = false;
	
	JFrame frame = new JFrame(Conf.APP_NAME);
	JPanel mainPanel;
	private Menu menu;
	private JLabel team1Label = new JLabel();
	private JLabel team2Label = new JLabel();

	private int windowWidth = Conf.WINDOW_WIDTH;
	private int windowHeight = Conf.WINDOW_HEIGHT;
	
	Map<Action, JButton> buttonMap = new HashMap<Action, JButton>();
	////////////////////////////////////////
	
    /*
	 * #### #### #### #### #### #### ####
	 * INIT INIT INIT INIT INIT INIT INIT
	 * #### #### #### #### #### #### ####
	 */
	{
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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
    	final ImageIcon iconImgSBlue = new ImageIcon(getClass().getResource(Conf.ICON_FILENAME_S_BLUE));
    	final ImageIcon iconImgM = new ImageIcon(getClass().getResource(Conf.ICON_FILENAME_M));
    	final ImageIcon iconImgL = new ImageIcon(getClass().getResource(Conf.ICON_FILENAME_L));
    	final ImageIcon iconImgXL = new ImageIcon(getClass().getResource(Conf.ICON_FILENAME_XL));
    	
    	iconsActive = new ArrayList<Image>() {
			private static final long serialVersionUID = 4560955969369357297L;
			{add(iconImgSBlue.getImage()); add(iconImgM.getImage()); add(iconImgL.getImage()); add(iconImgXL.getImage());}
    	};
    	iconsNotActive = new ArrayList<Image>() {
			private static final long serialVersionUID = -337325274310404675L;
			{add(iconImgS.getImage()); add(iconImgM.getImage()); add(iconImgL.getImage()); add(iconImgXL.getImage());}
    	};
        frame.setIconImages(iconsActive);
	}
	
	/*
	 * 2. ADD MENUS
	 */
	private void addMenus() {
		menu = new Menu();
		frame.setJMenuBar(menu.getMenuBar());
		MenuActionListeners.add(menu, this);
		
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
	private void setUpNewContainer() {
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
		Action lastCommand = popCommandFromStack();
		Integer scoreDelta = lastCommand.undo();
		if (scoreDelta != null) {
			Team team = lastCommand.getTeam();
			setPlusMinus(scoreDelta, team);
		}
		JButton button = buttonMap.get(lastCommand);
		MainContainer.setButtonText(button, lastCommand);
		System.out.println(game);
		System.out.println("UNDO!");
	}

	protected void pushCommandOnStack(Action action) {
		stateChangedSinceLastSave = true;
		stackOfCommands.push(action);
		updateUndoLabel();
	}

	private Action popCommandFromStack() {
		stateChangedSinceLastSave = true;
		Action command = stackOfCommands.pop();
		updateUndoLabel();
		return command;
	}
	
	private void updateUndoLabel() {
		Action action = stackOfCommands.peek();
		if (action != null) {
			menu.menuEditUndo.setEnabled(true);
			menu.menuEditUndo.setText("Undo " + action.getStatName() + " "
				+ action.getPlayer().getName());
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
	
	/*
	 * Run after every score change.
	 */
	protected void setPlusMinus(Integer scoreDelta, Team team) {
		team.changePlusMinus(scoreDelta);
		Team otherTeam = game.getOtherTeam(team);
		otherTeam.changePlusMinus(scoreDelta * (-1));
		updateScore();
	}
	
	protected void updateTeamLabelReference(Team team, JLabel teamLabel) {
		if (game.getTeam1().equals(team)) {
			team1Label = teamLabel;
		} else {
			team2Label = teamLabel;
		}
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
	 * KEY LISTENER METHODS
	 */
	public void keyPressed(KeyEvent arg0) {	}
	public void keyReleased(KeyEvent arg0) { }
	public void keyTyped(KeyEvent arg0) {
		char c = arg0.getKeyChar();
		if (c == 26) {
			undo();
		}
	}
	
	/*
	 * ##### ##### ##### ##### ##### ##### #####
	 * UTILS UTILS UTILS UTILS UTILS UTILS UTILS 
	 * ##### ##### ##### ##### ##### ##### #####
	 */
	
	protected static void setAllSizes(Component comp, int width, int height) {
		Dimension dim = new Dimension(width, height);
		comp.setMinimumSize(dim);
		comp.setMaximumSize(dim);
		comp.setPreferredSize(dim);
	}
	
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
