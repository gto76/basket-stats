package si.gto76.basketstats.swingfiller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.PlayerStats;
import si.gto76.basketstats.coreclasses.Stat;
import si.gto76.basketstats.coreclasses.Team;

public class SwingFiller implements KeyListener {
	private static final int WINDOW_WIDTH = 1365;
	private static final int WINDOW_HEIGHT = 500;
	private static final int MAIN_H_GAP = 20;
	private static final int MAIN_V_GAP = 5;
	Game game;
	JFrame frame = new JFrame("Control Frame");
	Container container = new Container();
	
	Deque<Stat> stackOfCommands = new ArrayDeque<Stat>();
	List<Player> playersOnTheFloor = new ArrayList<Player>();

	{
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
	public SwingFiller(Game game) {
		this.game = game;
        int rows = game.getNumberOfPlayers()+2,
        	columns = 1;
		GridLayout layout = new GridLayout(rows, columns);
		layout.setHgap(MAIN_H_GAP);
        layout.setVgap(MAIN_V_GAP);
		container.setLayout(layout);

		fill();
		seal();
	}

	private void fill() {
		fillTeam(game.getTeam1());
		fillTeam(game.getTeam2());
	}
	
	private void fillTeam(Team team) {
		// team name:
		String teamName = team.getName();
		addStringPanel(teamName);
		// team players:
		fillPlayers(team.getAllPlayersStats());
	}
	
	private void fillPlayers(Map<Player, PlayerStats> allPlayerStats) {
		for (Entry<Player, PlayerStats> playerWithStats : allPlayerStats.entrySet()) {
			Container playersContainer = new Container();
			int rows = 1,
		        columns =  PlayerStats.ASSIGNABLES_COUNT + 1;
			GridLayout layout = new GridLayout(rows, columns);
			layout.setHgap(1);
	        layout.setVgap(1);
	        playersContainer.setLayout(layout);
	        container.add(playersContainer);
			
	        fillPlayer(playersContainer, playerWithStats);
		}
	}
	
	private void fillPlayer(Container playersContainer, Entry<Player, PlayerStats> playerWithStats) {
		Player player = playerWithStats.getKey();
		PlayerStats stats = playerWithStats.getValue();
		
		// NAME PANEL
		JPanel namePanel = createStringPanel(player.getShortName());
		playersContainer.add(namePanel);
		
		// BUTTONS
		List<JButton> buttons = new ArrayList<JButton>();		
		for (Stat stat : stats.getStats()) {
			JButton button = createStatButton(stat);
			buttons.add(button);
		}
		
		JCheckBox onFloorSelector = new PlayersCheckBox(player, stats.getTeam(), buttons);
		onFloorSelector.setSelected(true);
		onFloorSelector.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
			     	// AbstractButton abstractButton = (AbstractButton)itemEvent.getSource();
					PlayersCheckBox cb = (PlayersCheckBox) itemEvent.getItem();
					int state = itemEvent.getStateChange();
					if (state == ItemEvent.SELECTED) {
						cb.team.putPlayerOnTheFloor(cb.player);
						cb.enableAllButtons();
					}
					else if (state == ItemEvent.DESELECTED) {
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
				stackOfCommands.push(stat);
				System.out.println(game);
			}
			
		});
		return button;
	}


	private void addStringPanel(String name) {
		JPanel p = createStringPanel(name);
		container.add(p);
	}
	private JPanel createStringPanel(String name) {
		JPanel p = new JPanel();
		JLabel nameLabel = new JLabel(name);
		p.add(nameLabel);
		return p;
	}

	private void seal() {
		frame.add(container, BorderLayout.CENTER);
		frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
        char c = arg0.getKeyChar();
        if (c == 'z') {
        	undo();
        }
	}

	private void undo() {
		if (stackOfCommands.size() == 0) {
			return;
		}
		Stat lastCommand = stackOfCommands.pop();
		Integer scoreDelta = lastCommand.undoAction();
		if (scoreDelta != null) {
			Team team = lastCommand.getTeam();
			setPlusMinus(scoreDelta, team);
		}
    	System.out.println(game);
    	System.out.println("UNDO!");
	}
	
	private void setPlusMinus(Integer scoreDelta, Team team) {
		team.changePlusMinus(scoreDelta);
		Team otherTeam = game.getOtherTeam(team);
		otherTeam.changePlusMinus(scoreDelta*(-1));
	}
}
