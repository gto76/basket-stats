package si.gto76.basketstats;

import java.io.FileNotFoundException;
import si.gto76.basketstats.coreclasses.*;
import si.gto76.basketstats.swingui.SwingFiller;

// TODO keep size when adding new player
public class BasketStats {
	public static void main(String[] args) throws FileNotFoundException {
		Game derbi = Conf.getDefaultGame();
		new SwingFiller(derbi);
		System.out.println(derbi);
	}
}
