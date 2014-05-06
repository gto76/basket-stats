package si.gto76.basketstats.swingui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import si.gto76.basketstats.Conf.StatComb;
import si.gto76.basketstats.coreclasses.RecordingStats;
import si.gto76.basketstats.coreclasses.ShotValues;
import si.gto76.basketstats.coreclasses.Stat;

public class DialogNewGame extends JFrame {
	public static final int WIDTH = 240, HEIGHT = 105;
	private static final int COMBO_HEIGHT = 25;
	private static final long serialVersionUID = 4236082473760097536L;
	//////////////////////////////////////////
	protected JPanel mainPanel;
	protected JDialog dialog;
	protected JOptionPane optionPane;
	private JComboBox<StatsAndShotValues> comboBox;
	private Map<Stat,JCheckBox> checkBoxMap = new HashMap<Stat,JCheckBox>();
	private Map<Stat,JSpinner> spinnerMap = new HashMap<Stat,JSpinner>();
	//////////////////////////////////////////

	public static Tuple<RecordingStats,ShotValues> showDialogReturnNullIfCanceled() {
		try {
			DialogNewGame dlg = new DialogNewGame();
			if (dlg.optionPane.getValue() == null || (Integer)dlg.optionPane.getValue() == 2) {
				return null;
			}
			return dlg.getStatsAndValues();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return new Tuple<RecordingStats,ShotValues>(RecordingStats.DEFAULT, ShotValues.FULL_COURT);
	}
	
    public DialogNewGame() throws URISyntaxException {
    	mainPanel = new JPanel(new GridBagLayout());
    	
    	optionPane = new JOptionPane(mainPanel,
    		JOptionPane.PLAIN_MESSAGE,
    		JOptionPane.OK_CANCEL_OPTION);
    	dialog = optionPane.createDialog(this, "Stat Selector");
    	
    	addStuff(mainPanel);
    	comboBox.setSelectedItem(comboBox.getSelectedItem());
    	
    	dialog.setSize(WIDTH, HEIGHT);
    	dialog.pack();
    	dialog.setAlwaysOnTop(true);
    	dialog.setVisible(true);
    	dialog.dispose();
	}

    /////////////////////////////////
    
    public Tuple<RecordingStats,ShotValues> getStatsAndValues() {
    	RecordingStats rc = new RecordingStats(getEnabledStats());
    	ShotValues sv = getShotPoints();
    	return new Tuple<RecordingStats,ShotValues>(rc, sv);
    }
    
    /////////////////////////////////
    
    private ShotValues getShotPoints() {
    	Map<Stat,Integer> shotPoints = new HashMap<Stat,Integer>();
    	for (Entry<Stat,JSpinner> entry : spinnerMap.entrySet()) {
    		shotPoints.put(entry.getKey(), (Integer) entry.getValue().getValue());
    	}
    	return new ShotValues(shotPoints);
    }
    
    private Set<Stat> getEnabledStats() {
    	Set<Stat> enabledStats = new HashSet<Stat>();
    	for (Entry<Stat,JCheckBox> entry : checkBoxMap.entrySet()) {
    		if (entry.getValue().isSelected()) {
    			enabledStats.add(entry.getKey());
    		}
    	}
    	return enabledStats;
    }
    
    private void setCheckBoxes(Set<Stat> enabledStats) {
    	for (Entry<Stat,JCheckBox> entry : checkBoxMap.entrySet()) {
    		if (enabledStats.contains(entry.getKey())) {
    			entry.getValue().setSelected(true);
    		} else {
    			entry.getValue().setSelected(false);
    		}
    	}
    }
    
    /////////////////////////////////
    
    private void addStuff(JPanel mainPanel) {
    	addDropDown();
    	addSpinners();
    	addCheckBoxes();
	}
    
    /////////////////////////////////
    
    /*
     * COMBO:
     */
	private void addDropDown() {
		final JComboBox<StatsAndShotValues> combo = new JComboBox<StatsAndShotValues>();
		combo.addItem(new StatsAndShotValues(StatComb.FULL_COURT_STATS, ShotValues.FULL_COURT));
		combo.addItem(new StatsAndShotValues(StatComb.SIMPLIFIED_FULL_COURT_STATS, ShotValues.FULL_COURT));
		combo.addItem(new StatsAndShotValues(StatComb.HALF_COURT_STATS, ShotValues.HALF_COURT));
		combo.addItem(new StatsAndShotValues(StatComb.SIMPLIFIED_HALF_COURT_STATS, ShotValues.HALF_COURT));
		combo.setPreferredSize(new Dimension(WIDTH, COMBO_HEIGHT));

		ActionListener actionListener = new ComboBoxListener();
		combo.addActionListener(actionListener);
		comboBox = combo;
		
		addComponent(combo, 0, 0, 3, GridBagConstraints.CENTER);
	}
	
	class ComboBoxListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			@SuppressWarnings("unchecked")
			JComboBox<StatsAndShotValues> comboBox = (JComboBox<StatsAndShotValues>) e.getSource();
			StatsAndShotValues selectedTuple = (StatsAndShotValues) comboBox.getSelectedItem();
			RecordingStats stats = selectedTuple.statComb.stats;
			setCheckBoxes(stats.values);

			for (Entry<Stat, Integer> entry : selectedTuple.shotValues.values.entrySet()) {
				JSpinner spinner = spinnerMap.get(entry.getKey());
				spinner.setValue(entry.getValue());
			}
		}
	}
	
    /////////////////////////////////

	/*
     * SPINNERS:
     */
	private void addSpinners() {
		initSpinner("FT", Stat.FTM, 0);
		initSpinner("2P", Stat.IIPM, 1);
		initSpinner("3P", Stat.TPM, 2);
	}
	
	private void initSpinner(String text, Stat stat, int x) {
		JPanel panel = new JPanel();
		panel.add(new JLabel(text+":"));
		JSpinner spinner = createSpinner(ShotValues.FULL_COURT.values.get(stat));
		spinnerMap.put(stat, spinner);
		panel.add(spinner);
		addComponent(panel, x, 1, 1, GridBagConstraints.CENTER);

	}
	
	private JSpinner createSpinner(int defaultValue) {
	    Integer current = new Integer(defaultValue);
	    Integer min = new Integer(1);
	    Integer max = new Integer(10);
	    Integer step = new Integer(1);
	    SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(current, min, max, step);
	    return new JSpinner(spinnerNumberModel);
	}
	
    /////////////////////////////////
	
	/*
     * CHECKBOXES:
     */
	private void addCheckBoxes() {
		addCheckBox(Stat.IIPM, 0, 2, 1); 
		addCheckBox(Stat.IIPF, 0, 3, 1);
		addCheckBox(Stat.TPM, 0, 4, 1);
		addCheckBox(Stat.TPF, 0, 5, 1);
		addCheckBox(Stat.FTM, 0, 6, 1);
		addCheckBox(Stat.FTF, 0, 7, 1);
		addCheckBox(Stat.PM, 0, 8, 1);
		
		addCheckBox(Stat.OFF, 1, 2, 1);
		addCheckBox(Stat.DEF, 1, 3, 1);
		addCheckBox(Stat.REB, 1, 4, 1);

		addCheckBox(Stat.AST, 2, 2, 1);
		addCheckBox(Stat.PF, 2, 3, 1);
		addCheckBox(Stat.ST, 2, 4, 1);
		addCheckBox(Stat.TO, 2, 5, 1);
		addCheckBox(Stat.BS, 2, 6, 1);
		addCheckBox(Stat.BA, 2, 7, 1);
	}
	
	private void addCheckBox(final Stat stat, int x, int y, int width) {
		final JCheckBox checkBox = new JCheckBox(stat.getName());
		checkBox.addActionListener(new CheckBoxListener(checkBox, stat));
		checkBoxMap.put(stat, checkBox);
		addComponent(checkBox, x, y, width, GridBagConstraints.WEST);
	}
	
	class CheckBoxListener implements ActionListener {
		JCheckBox checkBox;
		Stat stat;
		public CheckBoxListener(JCheckBox checkBox, Stat stat) {
			this.checkBox = checkBox;
			this.stat = stat;
		}
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			Set<Stat> enabledStats = getEnabledStats();
			RecordingStats rsNew;
			if (checkBox.isSelected()) {
				enabledStats.remove(stat);
				RecordingStats rsOld = new RecordingStats(enabledStats);
				rsNew = rsOld.add(stat);
			} else {
				enabledStats.add(stat);
				RecordingStats rsOld = new RecordingStats(enabledStats);
				rsNew = rsOld.remove_Nullable(stat);
			}
			setCheckBoxes(rsNew.values);
		}
    };

	/////////////////////////////////
	
	private void addComponent(Component component, int x, int y, int width, int anchor) {
		GridBagConstraints constraints = getConstraints(x, y, width, anchor);
		mainPanel.add(component, constraints);
	}
	
    /////////////////////////////////
	
	private static GridBagConstraints getConstraints(int x, int y, int width, int anchor) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = x;       
		constraints.gridy = y;       
		constraints.gridwidth = width;   
		constraints.anchor = anchor;
		return constraints;
	}
	
	public class StatsAndShotValues { 
		public final StatComb statComb;
		public final ShotValues shotValues; 
		public StatsAndShotValues(StatComb statComb, ShotValues shotValues) { 
			this.statComb = statComb; 
			this.shotValues = shotValues; 
		} 
		@Override
		public String toString() {
			return statComb.toString();
		}
	} 

}
