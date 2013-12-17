package si.gto76.basketstats.swingui;

import java.awt.Component;
import java.net.URISyntaxException;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class HelpDialog extends JFrame {

	private static final long serialVersionUID = 4236082473760097536L;
	protected JPanel p;
	protected JDialog dlg;
	protected JOptionPane op;

    public HelpDialog() throws URISyntaxException {
		p = new JPanel();
    	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
    	op = new JOptionPane(p,
    		JOptionPane.PLAIN_MESSAGE,
    		JOptionPane.CLOSED_OPTION);
    	dlg = op.createDialog(this, "Help");
    	
    	addLabelToPanel(" ", p);
    	addLabelToPanel("To change name of player name, team name,", p);
    	addLabelToPanel("venue or date, double click on the label.", p);
    	addLabelToPanel(" ", p);
    	addLabelToPanel("If you want to delete a player or change", p);
    	addLabelToPanel("a stat, save a game, then open the game file", p);
    	addLabelToPanel("with any plain text editor (Notepad, but not Word)", p);
    	addLabelToPanel("and edit it there. Just don't change the formating.", p);
    	addLabelToPanel("When changing stats only change players shots,", p);
    	addLabelToPanel("def and off rebounds, ast, pf, st, to and bs.", p);
    	addLabelToPanel("All other stats like players score, total rebounds", p);
    	addLabelToPanel("and all team stats will be ignored and calculated", p);
    	addLabelToPanel("anew next time you load the game in HoopStats.", p);
    	
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
