package si.gto76.basketstats;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;

import si.gto76.basketstats.coreclasses.Action;
import si.gto76.basketstats.coreclasses.Stat;

public class Util {
	public static void setButtonText(JButton button, Action action) {
		if (Conf.SHOW_STAT_VALUE_ON_BUTTON_LABEL) {
			String name = action.getStatName();
			if (action.getStat() == Stat.REB) {
				name = "Reb";
			}
			button.setText(formatButtonText(name, action.getStatValue()));
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
	
	public static Map<Stat,Integer> subMap(Map<Stat,Integer> mapIn, Stat[] set) {
		Map<Stat,Integer> mapOut = new HashMap<Stat,Integer>();
		for (Stat stat : set) {
			if (mapIn.containsKey(stat)) {
				mapOut.put(stat, mapIn.get(stat));
			}
		}
		return mapOut;
	}
	
	public static void assertPositive(Collection<Integer> values) {
		for (Integer value : values) {
			if (value < 0) {
				throw new IllegalArgumentException("Some of shot values are negative: " + values.toString());
			}
		}
	}	
	
	public static void putSetInMap(Map<Stat, Set<Stat>> map, Stat key, Stat... values) {
		Set<Stat> valueSet = new HashSet<Stat>();
		for (Stat value : values) {
			valueSet.add(value);
		}
		map.put(key, valueSet);
	}
	
	public static Set<Stat> getSetRecursively(Map<Stat, Stat> map, Stat key) {
		Set<Stat> valueSet = new HashSet<Stat>();
		Stat value = map.get(key);
		valueSet.add(value);
		if (value != null) {
			Set<Stat> recValueSet = getSetRecursively(map, value);
			valueSet.addAll(recValueSet);
		}
		return valueSet;
	}
	
	public static boolean containsAny(Set<Stat> set, Set<Stat> elements) {
		for (Stat element : elements) {
			if (set.contains(element)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean containsAny(Set<Stat> set, Stat... elements) {
		for (Stat element : elements) {
			if (set.contains(element)) {
				return true;
			}
		}
		return false;
	}
	
	public static Set<Stat> getOrderedSet(Set<Stat> originalSet) {
		Set<Stat> orderedSet = new LinkedHashSet<Stat>();
		for (Stat stat : Stat.values()) {
			if (originalSet.contains(stat)) {
				orderedSet.add(stat);
			}
		}
		return orderedSet;
	}
	
	public static String enumNameToLowerCase(String name) {
		StringBuilder sb = new StringBuilder();
		String[] tokens = name.split("_");
		for (int i = 0; i < tokens.length; i++) {
			String head = tokens[i].substring(0, 1); 
			String tail = tokens[i].substring(1).toLowerCase();
			if (i != 0) {
				head = " " + head;
			}
			sb.append(head.concat(tail));
		}
		return sb.toString();
	}
	
}
