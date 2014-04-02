package si.gto76.basketstats;

import java.io.FileNotFoundException;
import si.gto76.basketstats.coreclasses.*;
import si.gto76.basketstats.swingui.SwinGui;

public class BasketStats {
	public static void main(String[] args) throws FileNotFoundException {
		Game derbi = Conf.getDefaultGame();
		new SwinGui(derbi);
		System.out.println(derbi);
	}
}
