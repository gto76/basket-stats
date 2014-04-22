package si.gto76.basketstats.swingui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.Conf.StatComb;
import si.gto76.basketstats.Util;
import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.RecordingStats;
import si.gto76.basketstats.coreclasses.ShotValues;
import si.gto76.basketstats.coreclasses.Stat;
import si.gto76.basketstats.test.Test;

public class DialogChangeStats extends JFrame {
	public static final int WIDTH = 240, HEIGHT = 105;
	private static final long serialVersionUID = 4236082473760097536L;
	//////////////////////////////////////////
	protected JPanel mainPanel;
	protected JDialog dialog;
	protected JOptionPane optionPane;
	private Map<Stat,JCheckBox> checkBoxMap = new HashMap<Stat,JCheckBox>();
	
	RecordingStats oldRecordingStats;
	Game game;
	//////////////////////////////////////////

	/*
	 * recordedStats signify wether stat was fired at least one. Stat that can be included are:
	 * REB, IIPM, TPM, FTM
	 */
	public static RecordingStats showDialog(Game game) {
		try {
			DialogChangeStats dlg = new DialogChangeStats(game);
			if (dlg.optionPane.getValue() == null || (Integer)dlg.optionPane.getValue() == 2) {
				return null;
			}
			return dlg.getRecordingStats();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    public DialogChangeStats(Game game) throws URISyntaxException {
    	this.oldRecordingStats = new RecordingStats(game.recordingStats);
    	this.game = game;
    	
    	mainPanel = new JPanel(new GridBagLayout());
    	
    	optionPane = new JOptionPane(mainPanel,
    		JOptionPane.PLAIN_MESSAGE,
    		JOptionPane.OK_CANCEL_OPTION);
    	dialog = optionPane.createDialog(this, "Stat Selector");
    	
    	addCheckBoxes();
    	setCheckBoxes(oldRecordingStats.values);
    	
    	dialog.setSize(WIDTH, HEIGHT);
    	dialog.pack();
    	dialog.setAlwaysOnTop(true);
    	dialog.setVisible(true);
    	dialog.dispose();
	}

    /////////////////////////////////
    
    public RecordingStats getRecordingStats() {
    	return new RecordingStats(getEnabledStats());
    }
    
    /////////////////////////////////

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
    
	/*
     * CHECKBOXES:
     */
	private void addCheckBoxes() {
		addCheckBox(Stat.IIPM, 0, 0, 1); 
		addCheckBox(Stat.IIPF, 0, 1, 1);
		addCheckBox(Stat.TPM, 0, 2, 1);
		addCheckBox(Stat.TPF, 0, 3, 1);
		addCheckBox(Stat.FTM, 0, 4, 1);
		addCheckBox(Stat.FTF, 0, 5, 1);
		addCheckBox(Stat.PM, 0, 6, 1);
		
		addCheckBox(Stat.OFF, 1, 0, 1);
		addCheckBox(Stat.DEF, 1, 1, 1);
		addCheckBox(Stat.REB, 1, 2, 1);

		addCheckBox(Stat.AST, 2, 0, 1);
		addCheckBox(Stat.PF, 2, 1, 1);
		addCheckBox(Stat.ST, 2, 2, 1);
		addCheckBox(Stat.TO, 2, 3, 1);
		addCheckBox(Stat.BS, 2, 4, 1);
		addCheckBox(Stat.BA, 2, 5, 1);
	}
	
	private void addCheckBox(final Stat stat, int x, int y, int width) {
		final JCheckBox checkBox = new JCheckBox(stat.getName());
	    ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				Set<Stat> enabledStats = getEnabledStats();
				RecordingStats rsNew;
				
				if (checkBox.isSelected()) { // was checked
					enabledStats.remove(stat);
					RecordingStats rsOld = new RecordingStats(enabledStats);
					if (Conf.DEBUG) System.out.println("Old recording stats: " + rsOld);
					rsNew = rsOld.add(stat);
					if (!game.areValid(rsNew)) {
						rsNew = rsOld;
					}
				} else { // was unchecked
					enabledStats.add(stat);
					RecordingStats rsOld = new RecordingStats(enabledStats);
					if (Conf.DEBUG) System.out.println("Old recording stats: " + rsOld);
					rsNew = rsOld.remove(stat);
					if (rsNew == null || !game.areValid(rsNew)) {
						rsNew = rsOld;
					}
				}
				if (Conf.DEBUG) System.out.println("New recording stats: " + rsNew);
				setCheckBoxes(rsNew.values);
			}
	    };
		
		checkBox.addActionListener(actionListener);
		checkBoxMap.put(stat, checkBox);
		addComponent(checkBox, x, y, width, GridBagConstraints.WEST);
	}

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

}
