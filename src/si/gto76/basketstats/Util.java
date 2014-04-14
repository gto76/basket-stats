package si.gto76.basketstats;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;

import si.gto76.basketstats.coreclasses.Action;
import si.gto76.basketstats.coreclasses.Stat;

public class Util {
	public static void setButtonText(JButton button, Action action) {
		if (Conf.SHOW_STAT_VALUE_ON_BUTTON_LABEL) {
			button.setText(formatButtonText(action.getStatName(), action.getStatValue()));
		} else {
			button.setText(action.getStatName());
		}
	}
	
	public static String formatButtonText(String name, int value) {
		return name + Conf.BUTTON_TEXT_SEPARATOR + value;
	}
	
	public static void setAllSizes(Component comp, int width, int height) {
		Dimension dim = new Dimension(width, height);
		comp.setMinimumSize(dim);
		comp.setMaximumSize(dim);
		comp.setPreferredSize(dim);
	}
	
	public static Set<Stat> arrayToSet(Stat[] array) {
		Set<Stat> set = new HashSet<Stat>();
		set.addAll(Arrays.asList(array));
		return set;
	}
	
	public static Set<Stat> arraysIntersection(Stat[] array1, Stat[] array2) {
		Set<Stat> intersection = new HashSet<Stat>(Arrays.asList(array1)); // use the copy constructor
		intersection.retainAll(Arrays.asList(array2));
		return intersection;
	}
	
	public static <T> T checkNotNull(T reference) {
	    if (reference == null) {
	      throw new NullPointerException();
	    }
	    return reference;
	}
	
	public static double zeroIfDevideByZero(int devidee, int devider) {
		if (devider == 0) {
			return 0;
		}
		return ((double) devidee / devider) * 100.0;
	}

	public static String[] removeFirstElement(String[] arrayIn) {
		List<String> list = new ArrayList(Arrays.asList(arrayIn));
		list.remove(0);
		return list.toArray(new String[0]); 
	}

	
}
