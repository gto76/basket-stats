package si.gto76.basketstats.coreclasses;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShotValues {

	private static final Map<Stat, Integer> DEFAULT_SHOT_VALUES;
	private static final Map<Stat, Integer> STREETBALL_SHOT_VALUES;
	static {
		DEFAULT_SHOT_VALUES = Collections.unmodifiableMap(getVluesMap(new Integer[]{1,2,3}));
		STREETBALL_SHOT_VALUES = Collections.unmodifiableMap(getVluesMap(new Integer[]{1,1,1}));
	}
	public static final ShotValues DEFAULT = new ShotValues(DEFAULT_SHOT_VALUES);
	public static final ShotValues STREETBALL = new ShotValues(STREETBALL_SHOT_VALUES);
	
	/////////////////////////////
	public final Map<Stat, Integer> values;
	/////////////////////////////
	
	public ShotValues() {
		this.values = Collections.unmodifiableMap(DEFAULT_SHOT_VALUES);
	}
	
	public ShotValues(Map<Stat, Integer> shotPoints) {
		Map<Stat, Integer> mapBuilder = new HashMap<Stat, Integer>(DEFAULT_SHOT_VALUES);
		for (Stat stat : Stat.scoreChangingValues) {
			Integer value = shotPoints.get(stat);
			if (value == null) {
				continue;
			}
			asertValidValue(value);
			mapBuilder.put(stat, value);
		}
		this.values = Collections.unmodifiableMap(shotPoints);
	}

	private static void asertValidValue(int value) {
		if (value < 1) {
			throw new IllegalArgumentException("Shot value is less than 1: "+ value);
		}
		if (value > 10) {
			throw new IllegalArgumentException("Shot value is more than 10: "+ value);
		}
	}
	
	//////////////////////////////////

	public static Map<Stat, Integer> getVluesMap(Integer[] shotValues) {
		if (shotValues.length != 3) {
			throw new IllegalArgumentException("There are not 3 elements in array: "+ Arrays.toString(shotValues));
		}
		Map<Stat, Integer> valuesMap = new HashMap<Stat, Integer>();
		valuesMap.put(Stat.FTM, shotValues[0]);
		valuesMap.put(Stat.IIPM, shotValues[1]);
		valuesMap.put(Stat.TPM, shotValues[2]);
		return valuesMap;
	}
}
