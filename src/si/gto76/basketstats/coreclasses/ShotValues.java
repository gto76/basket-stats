package si.gto76.basketstats.coreclasses;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that contains unmodifiable map of score changing Stats and their values.
 * It tells how much points normal shot, three pointer and free throw are worth.
 */
public class ShotValues {
	private static final int MAX_VALUE = 10;
	private static final Integer[] FULL_COURT_ARRAY = {1,2,3};
	private static final Integer[] HALF_COURT_ARRAY = {1,1,1};
	public static final ShotValues FULL_COURT = new ShotValues(getValuesMap(FULL_COURT_ARRAY));
	public static final ShotValues HALF_COURT = new ShotValues(getValuesMap(HALF_COURT_ARRAY));
	//////////////////////////////////
	public final Map<Stat, Integer> values;
	//////////////////////////////////
	
	public ShotValues(Map<Stat, Integer> shotPoints) {
		Map<Stat, Integer> mapBuilder = new HashMap<Stat,Integer>(getValuesMap(FULL_COURT_ARRAY));
		for (Stat stat : Stat.scoreChangingStats) {
			Integer value = shotPoints.get(stat);
			if (value == null) {
				continue;
			}
			asertValidValue(value);
			mapBuilder.put(stat, value);
		}
		this.values = Collections.unmodifiableMap(shotPoints);
	}
	
	//////////////////////////////////

	private static void asertValidValue(int value) {
		if (value < 1) {
			throw new IllegalArgumentException("Shot value is less than 1: "+ value);
		}
		if (value > MAX_VALUE) {
			throw new IllegalArgumentException("Shot value is larger than " +MAX_VALUE+ ": "+ value);
		}
	}
	
	public static Map<Stat,Integer> getValuesMap(Integer[] shotValues) {
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
