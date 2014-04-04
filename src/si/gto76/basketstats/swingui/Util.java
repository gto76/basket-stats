package si.gto76.basketstats.swingui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.coreclasses.Action;

public class Util {
	static void setButtonText(JButton button, Action action) {
		if (Conf.SHOW_STAT_VALUE_ON_BUTTON_LABEL) {
			button.setText(formatButtonText(action.getStatName(), action.getStatValue()));
		} else {
			button.setText(action.getStatName());
		}
	}
	
	static String formatButtonText(String name, int value) {
		return name + Conf.BUTTON_TEXT_SEPARATOR + value;
	}
	
	static void setAllSizes(Component comp, int width, int height) {
		Dimension dim = new Dimension(width, height);
		comp.setMinimumSize(dim);
		comp.setMaximumSize(dim);
		comp.setPreferredSize(dim);
	}
}
