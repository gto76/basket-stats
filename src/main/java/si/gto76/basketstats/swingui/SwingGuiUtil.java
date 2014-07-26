package si.gto76.basketstats.swingui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.coreclasses.Action;

public class SwingGuiUtil {
	public static void setAllSizes(Component comp, int width, int height) {
		Dimension dim = new Dimension(width, height);
		comp.setMinimumSize(dim);
		comp.setMaximumSize(dim);
		comp.setPreferredSize(dim);
	}
		
	public static void setButtonText(JButton button, Action action) {
		if (Conf.SHOW_STAT_VALUE_ON_BUTTON_LABEL) {
			String name = action.getStatName();
			button.setText(formatButtonText(name, action.getStatValue()));
		} else {
			button.setText(action.getStatName());
		}
	}

	public static String formatButtonText(String name, int value) {
		return name + Conf.BUTTON_TEXT_SEPARATOR + value;
	}


    public static String getStringFromInputStream(java.io.InputStream inStr) {
        StringBuilder sb = new StringBuilder();
        if (inStr == null) {
            return "";
        }
        try {
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(inStr));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();
        } catch (java.io.IOException e) {
        }
        return sb.toString();
    }

}
