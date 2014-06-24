package si.gto76.basketstats.swingui;

import java.awt.Component;
import java.net.URISyntaxException;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import si.gto76.basketstats.Conf;

public class DialogHelp extends JFrame {

	private static final long serialVersionUID = 4236082473760097536L;
	protected JPanel p;
	protected JDialog dlg;
	protected JOptionPane op;

    public DialogHelp() throws URISyntaxException {
		p = new JPanel();
    	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
    	op = new JOptionPane(p,
    		JOptionPane.PLAIN_MESSAGE,
    		JOptionPane.CLOSED_OPTION);
    	dlg = op.createDialog(this, "Help");
    	
    	for (String line : Conf.HELP_TEXT.split("\n")) {
    		addLabelToPanel(line, p);
    	}
    	
    	dlg.setSize(240, 105);
    	dlg.pack();
    	dlg.setAlwaysOnTop(true);
    	dlg.setVisible(true);
    	dlg.dispose();
	}
    
    private static void addLabelToPanel(String txt, JPanel pnl) {
    	JLabel lbl = new JLabel(txt);
    	lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
    	pnl.add(lbl);
    }
	
}
