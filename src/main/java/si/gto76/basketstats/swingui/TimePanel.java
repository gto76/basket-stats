package si.gto76.basketstats.swingui;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DateEditor;
import javax.swing.SpinnerDateModel;

import si.gto76.basketstats.Conf;

/**
 * Label showing time, that turns into time spinner when double clicked,
 * and back to label when enter or esc is pressed.
 */
public class TimePanel {
	private static final String SPINNER_DATE_FORMAT = "HH:mm dd/MM/yyyy";
	//////////////////////
	private final SwingGui swingGui;
	private final JPanel panel;
	//////////////////////
	public TimePanel(SwingGui swingGui, JPanel panel) {
		this.swingGui = swingGui;
		this.panel = panel;
		addTimeLabel();
	}
	//////////////////////
	
	private void addTimeLabel() {
		JLabel dateLabel = new JLabel(swingGui.game.getDate().toString());
		MouseListener listener = new SwitchLabelWithSpinnerOnDoubleClick();
		panel.addMouseListener(listener);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(dateLabel);
	}
	
	private class SwitchLabelWithSpinnerOnDoubleClick implements MouseListener {
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
				switchLabelWithSpinner();
			}
			else {
				clickedTimeOld = clickedTimeNew;
			}
		}
	}

	private void switchLabelWithSpinner() {
		panel.removeAll();
		JSpinner spinner = getSpinner();
	    addKeyListenerToSpinner(spinner);
	    panel.add(spinner);
		panel.validate();
		spinner.requestFocus();
	}
	
	private JSpinner getSpinner() {
		SpinnerDateModel dateModel = new SpinnerDateModel(swingGui.game.getDate(), null, null, Calendar.MONTH);
		JSpinner spinner = new JSpinner(dateModel);
		DateEditor dateEditor = new DateEditor(spinner, SPINNER_DATE_FORMAT);
		spinner.setEditor(dateEditor);
		return spinner;
	}
	
	private void addKeyListenerToSpinner(JSpinner spinner) {
	    KeyListener listener = new ChangeDateOnEnter(spinner);
	    spinner.addKeyListener(listener);
	    // Also add listener to spiners text field:
	    JFormattedTextField spinnersTextField = ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField();
	    spinnersTextField.addKeyListener(listener);
	}

	private class ChangeDateOnEnter implements KeyListener {
		JSpinner spinner;
		public ChangeDateOnEnter(JSpinner spinner) {
			this.spinner = spinner;
		}
		/////
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {}
		public void keyPressed(KeyEvent e) {
		    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		    	e.consume();
		    	setNewDate();
		    	switchSpinnerWithLabel();
		    }
		    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
		    	switchSpinnerWithLabel();
		    }
		}
		
		private void setNewDate() {
			Date newDate = (Date) spinner.getValue();
	    	swingGui.game.setDate(newDate);
	    	System.out.println(swingGui.game);
		}
	}

	private void switchSpinnerWithLabel() {
		panel.removeAll();
		panel.add(new JLabel(swingGui.game.getDate().toString()));
		swingGui.mainPanel.validate();
	}
	
}
