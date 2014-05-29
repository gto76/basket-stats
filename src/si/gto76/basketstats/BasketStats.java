package si.gto76.basketstats;

import java.io.FileNotFoundException;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import si.gto76.basketstats.coreclasses.*;
import si.gto76.basketstats.swingui.SwinGui;

public class BasketStats {
	public static void main(String[] args) throws FileNotFoundException {
		//setLookAndFeel(UIManager.getInstalledLookAndFeels()[0]);
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		
		Game game = Conf.getDefaultGame();
		new SwinGui(game);
		System.out.println(game);
	}

	private static void setLookAndFeel(LookAndFeelInfo laf) {
		try {
	        UIManager.setLookAndFeel(laf.getClassName());
	    } 
	    catch (Exception e) {
	    }
	}
}
