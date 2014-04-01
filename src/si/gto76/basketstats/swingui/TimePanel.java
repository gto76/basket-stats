package si.gto76.basketstats.swingui;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import si.gto76.basketstats.Conf;

public class TimePanel {
	SwinGui swingFiller;
	
	public TimePanel(SwinGui swingFiller, JPanel dateContainer) {
		this.swingFiller = swingFiller;
		addTimePanel(dateContainer);
	}

	private void addTimePanel(final JPanel dateContainer) {
		JLabel dateLabel = new JLabel(swingFiller.game.getDate().toString());
		
		dateContainer.addMouseListener(new MouseListener() {
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
						switchNameLabelWithSpinner(dateContainer);
					}
					else {
						clickedTimeOld = clickedTimeNew;
					}
				}
			}
		);
		dateContainer.setLayout(new GridLayout(1, 1));
		dateContainer.add(dateLabel);
	}

	private void switchNameLabelWithSpinner(final JPanel dateContainer) {
		dateContainer.removeAll();
		
		final JSpinner s = new JSpinner(new SpinnerDateModel(swingFiller.game.getDate(), null, null,
			        Calendar.MONTH));
	    JSpinner.DateEditor de = new JSpinner.DateEditor(s, "HH:mm dd/MM/yyyy");
	    s.setEditor(de);
		
	    // You need to put keylistener to spiner and its text field
	    s.addKeyListener(new SpinnerKeyListener(s, dateContainer));
	    ((JSpinner.DefaultEditor)s.getEditor()).getTextField().addKeyListener(new SpinnerKeyListener(s, dateContainer));
		
	    dateContainer.add(s);
		dateContainer.validate();
		s.requestFocus();
	}

	private void switchBackToLabel(JPanel dateContainer) {
		dateContainer.removeAll();
		dateContainer.add(new JLabel(swingFiller.game.getDate().toString()));
		swingFiller.mainPanel.validate();
	}
	
	class SpinnerKeyListener implements KeyListener {
		JSpinner s;
		JPanel dateContainer;

		public SpinnerKeyListener(JSpinner s, JPanel dateContainer) {
			this.s = s;
			this.dateContainer = dateContainer;
		}
		
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {}
		public void keyPressed(KeyEvent e) {
		    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		    	e.consume();
		    	Date newDate = (Date) s.getValue();
		    	swingFiller.game.setDate(newDate);
		    	switchBackToLabel(dateContainer);
		    	System.out.println(swingFiller.game);
		    }
		    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
		    	switchBackToLabel(dateContainer);
		    }
		}
	}
	
}
