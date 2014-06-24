package si.gto76.basketstats;

import java.io.FileNotFoundException;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import si.gto76.basketstats.coreclasses.*;
import si.gto76.basketstats.swingui.SwinGui;

public class BasketStats {
	public static void main(String[] args) throws FileNotFoundException {
		setLookAndFeel(Conf.LOOK_AND_FEEL);
		Game game = Conf.getDefaultGame();
		new SwinGui(game);
		System.out.println(game);
	}

	private static void setLookAndFeel(String lookAndFeel) {
		try {
	        UIManager.setLookAndFeel(lookAndFeel);
	    } 
	    catch (Exception e) {
	    }
	}
}
