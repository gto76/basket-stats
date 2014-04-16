package si.gto76.basketstats.swingui;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import si.gto76.basketstats.Conf;

public class NewFileDialog extends JFrame  {
	private static final long serialVersionUID = 4236082473760097536L;
	protected JPanel p;
	protected JDialog dlg;
	protected JOptionPane op;
	
	public NewFileDialog() {
		p = new JPanel();
    	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
    	op = new JOptionPane(p,
    		JOptionPane.PLAIN_MESSAGE,
    		JOptionPane.CLOSED_OPTION);
    	dlg = op.createDialog(this, "About...");
    	JLabel lbl;

    	lbl = new JLabel(Conf.APP_NAME, SwingConstants.CENTER); 
    	lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
    	lbl.setHorizontalAlignment(JLabel.CENTER);
    	p.add(lbl);
    	
    	dlg.setSize(240, 105);
    	dlg.pack();
    	dlg.setAlwaysOnTop(true);
    	dlg.setVisible(true);
    	dlg.dispose();
	}
	
	
}
