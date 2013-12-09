package si.gto76.basketstats.swingui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URISyntaxException;
import java.sql.Time;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerDateModel;
import javax.swing.WindowConstants;

import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.util.JDatePickerUtil;

import org.omg.PortableServer._ServantLocatorStub;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.HasName;
import si.gto76.basketstats.coreclasses.PlayerStats;
import si.gto76.basketstats.coreclasses.Stat;
import si.gto76.basketstats.coreclasses.StatCats;
import si.gto76.basketstats.coreclasses.Team;

public class SwingFiller implements KeyListener {
	Game game;
	JFrame frame = new JFrame(Conf.APP_NAME);
	Container container = new Container();
	private BasketMenu meni;

	Deque<Stat> stackOfCommands = new ArrayDeque<Stat>();
	List<Player> playersOnTheFloor = new ArrayList<Player>();

	JLabel team1Label = new JLabel();
	JLabel team2Label = new JLabel();

    static ArrayList<Image> iconsActive;
    static ArrayList<Image> iconsNotActive;

	{
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	public SwingFiller(final Game game) {
		this.game = game;
		setIcons();
		addMenus();
		initializeContainer();
		fillContainer();
		sealContainer();
		updateUndoLabel();
	}

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
	
	private void addMenus() {
		meni = new BasketMenu();
		frame.setJMenuBar(meni.getMenuBar());

		/*
		 * FILE
		 */
		// SAVE AS
		meni.menuFileSaveas.addActionListener(new SaveListener(game, frame));
		// OPEN
		meni.menuFileOpen.addActionListener(new LoadListener(this));
		// FILE EXIT
		meni.menuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onWindowClose();
			}
		});

		/*
		 * EDIT
		 */
		// UNDO
		meni.menuEditUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});
		// ADD PLAYER TO TEAM 1
		meni.menuEditAddPlayer1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Team team = game.getTeam1();
				addNewPlayerToTeam(team);
			}
		});		
		// ADD PLAYER TO TEAM 2
		meni.menuEditAddPlayer2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Team team = game.getTeam2();
				addNewPlayerToTeam(team);
			}
		});
		
        /*
         * HELP ABOUT
         */
        meni.menuHelpAbout.addActionListener (
			new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	try {
						new AboutDialog();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
                }
            }
        );

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				onWindowClose();
			}
		});
	}

	private void initializeContainer() {
		int rows = game.getNumberOfPlayers() + 3, columns = 1;
		GridLayout layout = new GridLayout(rows, columns);
		layout.setHgap(Conf.MAIN_H_GAP);
		layout.setVgap(Conf.MAIN_V_GAP);
		container.setLayout(layout);
	}

	/*
	 * FILL_CONTAINER
	 */
	private void fillContainer() {
		fillTimeAndPlace();
		fillTeam(game.getTeam1(), team1Label);
		fillTeam(game.getTeam2(), team2Label);
		updateScore();
	}

	private void fillTimeAndPlace() {
		//// TODO date and place setup
		// DATE
		Container dateAndPlaceContainer = new Container();
		GridLayout dateAndPlaceLayout = new GridLayout(1, 3);
		//layout.setHgap(Conf.MAIN_H_GAP);
		//layout.setVgap(Conf.MAIN_V_GAP);
		dateAndPlaceContainer.setLayout(dateAndPlaceLayout);
		dateAndPlaceContainer.add((Component)JDateComponentFactory.createJDatePicker());
		// TIME
		JSpinner timeSpinner = new JSpinner( new SpinnerDateModel() );
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
		timeSpinner.setEditor(timeEditor);
		timeSpinner.setValue(new Date()); // will only show the current time
		dateAndPlaceContainer.add(timeSpinner);
		// PLACE
		JPanel placeContainer = new JPanel();
		//dateAndPlaceContainer.add(new JLabel("Place")); //TODO place as class -> hasName
		addNamePanel(placeContainer, game.getLocation());
		dateAndPlaceContainer.add(placeContainer);
		
		container.add(dateAndPlaceContainer);
	}

	private void fillTeam(Team team, JLabel label) {
		// Team name and score
		JPanel p = new JPanel();
		addNamePanel(p, team);
		container.add(p);
		// Players names and buttons
		fillPlayers(team.getAllPlayersStats());
	}

	private void fillPlayers(Map<Player, PlayerStats> allPlayerStats) {
		for (Entry<Player, PlayerStats> playerWithStats : allPlayerStats
				.entrySet()) {
			Container playersContainer = new Container();
			int rows = 1, columns = PlayerStats.ASSIGNABLES_COUNT + 1;
			GridLayout layout = new GridLayout(rows, columns);
			layout.setHgap(1);
			layout.setVgap(1);
			playersContainer.setLayout(layout);
			container.add(playersContainer);

			fillPlayer(playersContainer, playerWithStats);
		}
	}

	private void fillPlayer(Container playersContainer,
			Entry<Player, PlayerStats> playerWithStats) {
		Player player = playerWithStats.getKey();
		PlayerStats stats = playerWithStats.getValue();

		addNamePanel(playersContainer, player);
		
		// BUTTONS
		List<JButton> buttons = new ArrayList<JButton>();
		for (Stat stat : stats.getStats()) {
			JButton button = createStatButton(stat);
			buttons.add(button);
		}

		// ON FLOOR SELECTOR
		JCheckBox onFloorSelector = new PlayersCheckBox(player,
				stats.getTeam(), buttons);
		onFloorSelector.setSelected(true);
		onFloorSelector.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				PlayersCheckBox cb = (PlayersCheckBox) itemEvent.getItem();
				int state = itemEvent.getStateChange();
				if (state == ItemEvent.SELECTED) {
					cb.team.putPlayerOnTheFloor(cb.player);
					cb.enableAllButtons();
				} else if (state == ItemEvent.DESELECTED) {
					cb.team.getPlayerOffTheFloor(cb.player);
					cb.disableAllButtons();
				}
			}
		});
		playersContainer.add(onFloorSelector);

		for (JButton button : buttons) {
			playersContainer.add(button);
		}
	}

	private void addNamePanel(Container container, final HasName pot) {
		JPanel namePanel = createStringPanel(pot.getName());
		// Updates team1Label or team2Label global variable, so that updateScore()
		// can update score by teams name.
		if (pot instanceof Team) {
			updateTeamLabelReference((Team) pot, (JLabel) namePanel.getComponent(0));
		}
		
		final Container nameContainer = new Container();
		nameContainer.addMouseListener(new MouseListener() {
				public void mouseReleased(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) {}
				public void mouseExited(MouseEvent arg0) {}
				public void mouseEntered(MouseEvent arg0) {}
				
				long clickedTimeOld = System.nanoTime();
				@Override
				public void mouseClicked(MouseEvent arg0) {
					long clickedTimeNew = System.nanoTime();
					long deltaTime = clickedTimeNew - clickedTimeOld;
					if (deltaTime < Conf.DOUBLE_CLICK_LAG) {
						switchNameLabelWithTextArea(nameContainer, pot);
					}
					else {
						clickedTimeOld = clickedTimeNew;
					}
				}
			}
		);
		nameContainer.setLayout(new GridLayout(1, 1));
		nameContainer.add(namePanel);
		container.add(nameContainer);
	}

	protected void switchNameLabelWithTextArea(final Container nameContainer,
			final HasName pot) {
		nameContainer.removeAll();
		final JTextArea textArea = new JTextArea(pot.getName());
		textArea.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
			    if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			    	e.consume();
			    	String name = textArea.getText();
			    	changeNameAndSwitchBackToLabel(nameContainer, pot, name);
			    	if (pot instanceof Team) {
			    		// Extra work because of score by team name
			    		changeNameAndSwitchBackToLabel(nameContainer, pot, name);
			    		JPanel jp = (JPanel) nameContainer.getComponent(0);
			    		JLabel teamLabel = (JLabel) jp.getComponent(0);
			    		Team team = (Team) pot;
			    		updateTeamLabelReference(team, teamLabel);
			    		updateScore();
			    	}
			    	System.out.println(game);
			    }
			}
		});
		nameContainer.add(textArea);
		nameContainer.validate();
		textArea.requestFocus();
		textArea.selectAll();
	}

	protected void updateTeamLabelReference(Team team, JLabel teamLabel) {
		if (game.getTeam1().equals(team)) {
			team1Label = teamLabel;
		}
		else {
			team2Label = teamLabel;
		}
	}

	protected void changeNameAndSwitchBackToLabel(Container nameContainer, HasName pot,
				String name) {
    	pot.setName(name);
    	nameContainer.removeAll();
    	nameContainer.add(createStringPanel(pot.getName()));
    	nameContainer.validate();
	}

	private JButton createStatButton(final Stat stat) {
		String statsName = stat.getName();
		JButton button = new JButton(statsName);
		if (statsName.equals("Off") || statsName.equals("Def")) {
			button.setBackground(Color.GRAY);
		}
		button.addKeyListener(this);

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer scoreDelta = stat.fireAction();
				if (scoreDelta != null) {
					Team playersTeam = stat.getTeam();
					setPlusMinus(scoreDelta, playersTeam);
				}
				pushCommandOnStack(stat);
				System.out.println(game);
			}
		});
		return button;
	}
	/*
	 * FILL_CONTAINER END
	 */
	
	private void sealContainer() {
		frame.add(container, BorderLayout.CENTER);
		frame.setSize(Conf.WINDOW_WIDTH, Conf.WINDOW_HEIGHT);
		frame.setVisible(true);
	}

	private void pushCommandOnStack(Stat stat) {
		stackOfCommands.push(stat);
		updateUndoLabel();
	}

	private void updateUndoLabel() {
		Stat stat = stackOfCommands.peek();
		if (stat != null) {
			meni.menuEditUndo.setEnabled(true);
			meni.menuEditUndo.setText("Undo " + stat.getName() + " "
				+ stat.getPlayer().getName());
		}
		else {
			meni.menuEditUndo.setEnabled(false);
			meni.menuEditUndo.setText("Undo");
		}
	}

	private JPanel createStringPanel(String name) {
		JPanel p = new JPanel();
		JLabel nameLabel = new JLabel(name);
		p.add(nameLabel);
		return p;
	}
	
	protected void addNewPlayerToTeam(Team team) {
		int noOfPlayers = team.getNumberOfPlayers();
		Player player = new Player("Player " + Integer.toString(noOfPlayers+1));
		team.addPlayer(player);
		container.removeAll();
		initializeContainer();
		fillContainer();
		System.out.println(game);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {	}

	@Override
	public void keyReleased(KeyEvent arg0) { }

	@Override
	public void keyTyped(KeyEvent arg0) {
		char c = arg0.getKeyChar();
		if ( c == 26 ) {
			undo();
		}
	}

	private void undo() {
		if (stackOfCommands.size() == 0) {
			return;
		}
		Stat lastCommand = popCommandFromStack();
		Integer scoreDelta = lastCommand.undoAction();
		if (scoreDelta != null) {
			Team team = lastCommand.getTeam();
			setPlusMinus(scoreDelta, team);
		}
		System.out.println(game);
		System.out.println("UNDO!");
	}

	private Stat popCommandFromStack() {
		Stat command = stackOfCommands.pop();
		updateUndoLabel();
		return command;
	}

	private void setPlusMinus(Integer scoreDelta, Team team) {
		team.changePlusMinus(scoreDelta);
		Team otherTeam = game.getOtherTeam(team);
		otherTeam.changePlusMinus(scoreDelta * (-1));
		updateScore();
	}

	private void updateScore() {
		Team team1 = game.getTeam1();
		team1Label.setText(getTeamNameAndScore(team1));

		Team team2 = game.getTeam2();
		team2Label.setText(getTeamNameAndScore(team2));
	}

	private String getTeamNameAndScore(Team team) {
		return team.getName() + ": " + team.get(StatCats.PTS);
	}

	public static void onWindowClose() {
		confirmExit();
	}

	private static void confirmExit() {
		String ObjButtons[] = { "Yes", "No" };
		int PromptResult = JOptionPane.showOptionDialog(null,
				"Are you sure you want to exit?", "",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
				ObjButtons, ObjButtons[1]);
		if (PromptResult == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

}
