package si.gto76.basketstats.coreclasses;

import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import si.gto76.basketstats.Conf;

public class Util {
	public static Set<Stat> arrayToSet(Stat[] array) {
		Set<Stat> set = new HashSet<Stat>();
		set.addAll(Arrays.asList(array));
		return set;
	}
	
	public static Set<Stat> arraysIntersection(Stat[] array1, Stat[] array2) {
		Set<Stat> intersection = new HashSet<Stat>(Arrays.asList(array1));
		intersection.retainAll(Arrays.asList(array2));
		return intersection;
	}
	
	public static <T> T checkNotNull(T reference) {
	    if (reference == null) {
	      throw new NullPointerException();
	    }
	    return reference;
	}
	
	public static double zeroIfDivideByZero(int dividend, int divisor) {
		if (divisor == 0) {
			return 0;
		}
		return ((double) dividend / divisor) * 100.0;
	}

	public static String[] removeFirstElement(String[] arrayIn) {
		List<String> list = new ArrayList<String>(Arrays.asList(arrayIn));
		list.remove(0);
		return list.toArray(new String[list.size()]);
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
	
	public static boolean areAllPositive(Collection<Integer> values) {
		for (Integer value : values) {
			if (value < 0) {
				return false;
			}
		}
		return true;
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

	public static String padEnd(String string, int minLength, char padChar) {
	    Util.checkNotNull(string);
	    if (string.length() >= minLength) {
	    	return string;
	    }
	    StringBuilder sb = new StringBuilder(minLength);
	    sb.append(string);
	    for (int i = string.length(); i < minLength; i++) {
	    	sb.append(padChar);
	    }
	    return sb.toString();
	}
	
	public static int zeroIfNull(Integer number) {
		if (number == null) {
			return 0;
		} else {
			return number;
		}
	}
	
	public static String padTab(String string) {
		return Util.padEnd(string, Conf.TAB_WIDTH, ' ');
	}

    /**
     * Throws IllegalArgumentException if name is null, empty or contains only whitespaces.
     */
	public static String checkNameForNullAndTrimIt(String name, int maxNameLength) {
		if (name == null) {
			throw new IllegalArgumentException("Name is null");
		}
		name = name.trim();
		if (name.length() == 0) {
			throw new IllegalArgumentException("Name is empty or only whitespaces.");
		}
		if (name.length() >= maxNameLength) {	
			name = name.substring(0, maxNameLength-1);
		}
		return name;
	}
	
	public static int zeroOrPositive(int num) {
		if (num < 0) {
			return 0;
		}
		return num;
	}

	public static GridBagConstraints getGridBagConstraints(int gridx, int gridy) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = gridx;       
		c.gridy = gridy;
		return c;
	}

}
