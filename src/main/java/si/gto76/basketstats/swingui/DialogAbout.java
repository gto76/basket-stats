package si.gto76.basketstats.swingui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.coreclasses.Util;


public class DialogAbout extends JFrame {

	private static final long serialVersionUID = 4236082473760097536L;
	protected JPanel p;
	protected JDialog dlg;
	protected JOptionPane op;

    public DialogAbout() throws URISyntaxException {
		p = new JPanel();
    	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
    	op = new JOptionPane(p,
    		JOptionPane.PLAIN_MESSAGE,
    		JOptionPane.CLOSED_OPTION);
    	dlg = op.createDialog(this, "About...");
    	JLabel lbl;

    	// Name
    	lbl = new JLabel(Conf.APP_NAME, SwingConstants.CENTER); 
    	lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
    	Font newLabelFont =new Font(lbl.getFont().getName(), Font.BOLD, lbl.getFont().getSize()+6);
    	lbl.setFont(newLabelFont);
    	lbl.setHorizontalAlignment(JLabel.CENTER);
    	p.add(lbl);

    	// Icon
    	URL url = getClass().getResource(Conf.ICON_FILENAME_M);
    	ImageIcon icon = new ImageIcon(url);
	    JLabel label = new JLabel(icon);
	    label.setAlignmentX(Component.CENTER_ALIGNMENT);
    	p.add(label);

        // Version number
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream in = cl.getResourceAsStream(Conf.VERSION_FILENAME);
        String version = SwingGuiUtil.getStringFromInputStream(in);
        if (version.length() == 0) {
            version = Conf.VERSION;
        } else {
            int indexOfDash = version.lastIndexOf('-');
            version = version.substring(1, indexOfDash);
        }
        addLabelToPanel(" version " + version + " ", p);

    	// Other info
        addLabelToPanel("Copyright (c) " +Conf.YEARS+ " " +Conf.AUTHOR, p);
    	addLabelToPanel(Conf.EMAIL, p);
    	
    	dlg.setSize(240, 105);
    	dlg.pack();
    	dlg.setAlwaysOnTop(true);
    	dlg.setVisible(true);
    	dlg.dispose();
	}
    
    private static void addLabelToPanel(String txt, JPanel pnl) {
    	JTextField lbl=new JTextField(txt);
    	lbl.setFont(UIManager.getDefaults().getFont("TabbedPane.font"));
    	lbl.setEditable(false);
    	lbl.setBorder(null);
    	lbl.setBackground(null);
    	lbl.setDisabledTextColor(Color.black);    	
    	
    	JPanel p = new JPanel();
    	p.add(lbl);
    	
    	lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
    	pnl.add(p);
    }
    
}
