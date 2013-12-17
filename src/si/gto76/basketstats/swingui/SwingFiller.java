package si.gto76.basketstats.swingui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
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
	// gridx, gridy			 - Pozicija
	// gridwidth, gridheight - Kolk celic zauzema
	// fill					 - Se širi skupaj z kontejnerjem, glede na weight
	// weightx, weighty		 - 
	// ipadx, ipady			 - kolk pixlov doda
	// insets				 - padding
	// anchor				 - anchor
	JPanel mainPanel;
	static int lastFilledRow;
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
	// TODO date z label or selector
	// TODO load game, da se tudi datum in place zlovdata
	//--------------------
	// TODO new game -> ask if it is cool if not saved
	// TODO open -> ask if it is cool if not saved
	// TODO exit -> ask if it is cool if not saved
	// TODO last dir
	// TODO da ce ponesreci zbrisemo lahko se vedno dvakrat kliknemo label
	public SwingFiller(final Game game) {
		this.game = game;
		setIcons();
		addMenus();
		
		initializeContainer();
		fillContainer();
		sealContainer();
		
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
         * HELP
         */
        meni.menuHelpHelp.addActionListener (
			new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	try {
						new HelpDialog();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
                }
            }
        );
      
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

	/*
	 * 3. INITIALIZE CONTAINER
	 */
	private void initializeContainer() {
		mainPanel = new JPanel(new GridBagLayout());
		lastFilledRow = 0;
	}

	/*
	 * 4. FILL CONTAINER
	 */
	private void fillContainer() {
		addPlace();
		addTime();
		addTeam(game.getTeam1(), team1Label);
		addTeam(game.getTeam2(), team2Label);
		updateScore();
	}

	private void addPlace() {
		JPanel placeContainer = new JPanel();
		new NamePanel(this, placeContainer, game.getLocation());
		//placeContainer.setPreferredSize(new Dimension(200,30)); //XXX
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;       
		c.gridy = 0;       
		//c.gridwidth = 2;   
		c.anchor = GridBagConstraints.WEST;
		mainPanel.add(placeContainer, c);
	}
	
	private void addTime() {
		JPanel timeContainer = new JPanel();
		new TimePanel(this, timeContainer);
	    
	    GridBagConstraints c = new GridBagConstraints();
		c.gridx = 2;       
		c.gridy = 0;       
		c.gridwidth = 1;   
		c.anchor = GridBagConstraints.EAST;
		//c.ipadx = 15;
		mainPanel.add(timeContainer, c);
		lastFilledRow++;
	}

	private void addTeam(Team team, JLabel label) {
		addTeamName(team);
		// Players names and buttons
		addPlayers(team.getAllPlayersStats());
	}

	private void addTeamName(Team team) {
		// Team name and score
		JPanel p = new JPanel();
		new NamePanel(this, p, team);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;       
		c.gridy = lastFilledRow;       
		c.gridwidth = 3;   
		c.weighty = 1.0;
		c.ipady = 0;
		mainPanel.add(p, c);
		lastFilledRow++;
	}
	
	private void addPlayers(Map<Player, PlayerStats> allPlayerStats) {
		for (Entry<Player, PlayerStats> playerWithStats : allPlayerStats.entrySet()) {
			addPlayer(playerWithStats.getKey(), playerWithStats.getValue());
		}
	}
	
	private void addPlayer(Player player, PlayerStats playerStats) {
		// NAME:
		addPlayersName(player);
		// CHECKBOX:
		List<JButton> buttons = createPlayersButtons(playerStats);
		PlayersCheckBox checkBox = createPlayersCheckBox(player, playerStats.getTeam(), buttons);
		addPlayersCheckBox(checkBox);
		// BUTTONS:
		addPlayersButtons(buttons);
		
		lastFilledRow++;
	}

	private void addPlayersName(Player player) {
		JPanel playersNameContainer = new JPanel();
		//playersNameContainer.setMinimumSize(new Dimension(240, 15));
		//playersNameContainer.setPreferredSize(new Dimension(140,15));
		new NamePanel(this, playersNameContainer, player);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;       
		c.gridy = lastFilledRow;       
		c.weighty = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL; //XXX
		//c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.CENTER;
		//c.ipadx = Conf.PLAYERS_NAME_COLUMN_WIDTH;
		mainPanel.add(playersNameContainer, c);
	}
	
	private PlayersCheckBox createPlayersCheckBox(Player player, Team team, List<JButton> buttons) {
		// ON FLOOR SELECTOR
		PlayersCheckBox onFloorSelector = new PlayersCheckBox(player,
				team, buttons);
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
		return onFloorSelector;
	}
	
	private void addPlayersCheckBox(PlayersCheckBox checkBox) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;       
		c.gridy = lastFilledRow;       
		c.weighty = 1.0;
		mainPanel.add(checkBox, c);
	}

	private List<JButton> createPlayersButtons(PlayerStats stats) {
		List<JButton> buttons = new ArrayList<JButton>();
		for (Stat stat : stats.getStats()) {
			JButton button = createStatButton(stat);
			
			//button.setMargin(m)
			//button.setMaximumSize(new Dimension(40, 10));
			//button.setMinimumSize(new Dimension(40, 10));
			//button.setPreferredSize(new Dimension(0, 0)); //XXX
			
			button.setMinimumSize(new Dimension(100, 10));
			button.setMaximumSize(new Dimension(100, 10));
			button.setPreferredSize(new Dimension(100, 10));
			
			
			buttons.add(button);
		}
		return buttons;
	}
	
	private void addPlayersButtons(List<JButton> bbb) {
		GridLayout layout = new GridLayout(1, bbb.size());
		layout.setHgap(1);
		JPanel panel = new JPanel(layout);
		for (JButton b : bbb) {
			panel.add(b);
		}
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 2;       
		c.gridy = lastFilledRow;       
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 0, 2, 0); 
		mainPanel.add(panel, c);
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
	 * 5. SEAL CONTAINER
	 */
	private void sealContainer() {
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 6, 1, 2));
		frame.setContentPane(mainPanel);
		frame.setSize(Conf.WINDOW_WIDTH, Conf.WINDOW_HEIGHT);
		frame.setVisible(true);
	}
	
	/*
	 * ##### ##### ##### ##### ##### ##### #####
	 * UTILS UTILS UTILS UTILS UTILS UTILS UTILS 
	 * ##### ##### ##### ##### ##### ##### #####
	 */
	protected void updateTeamLabelReference(Team team, JLabel teamLabel) {
		if (game.getTeam1().equals(team)) {
			team1Label = teamLabel;
		}
		else {
			team2Label = teamLabel;
		}
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
		//mainPanel.removeAll();
		initializeContainer();
		fillContainer();
		sealContainer();
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

	void updateScore() {
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
		/*
		String ObjButtons[] = { "Yes", "No" };
		int PromptResult = JOptionPane.showOptionDialog(null,
				"Are you sure you want to exit?", "",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
				ObjButtons, ObjButtons[1]);
		if (PromptResult == JOptionPane.YES_OPTION) {
			System.exit(0);
		}*/
		System.exit(0);
	}
	
	public static void setAllSizes(Component comp, int width, int height) {
		Dimension dim = new Dimension(width, height);
		comp.setMinimumSize(dim);
		comp.setMaximumSize(dim);
		comp.setPreferredSize(dim);
	}

}
