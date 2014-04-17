package si.gto76.basketstats.coreclasses;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.Util;

public class RecordingStats {
	private static final Map<Stat,  Set<Stat>> DEPENDENCIES;
	private static final Map<Stat, Set<Stat>> ANTI_DEPENDENCIES;
	// Fill the maps:
	static {
		// Dependent values:
		Map<Stat,  Set<Stat>> dependenciesBuilder = new HashMap<Stat, Set<Stat>>();
		Util.putSetInMap(dependenciesBuilder, Stat.IIPF, Stat.IIPM);
		Util.putSetInMap(dependenciesBuilder, Stat.TPM, Stat.IIPM);
		Util.putSetInMap(dependenciesBuilder, Stat.TPF, Stat.IIPM, Stat.TPM, Stat.IIPF);
		Util.putSetInMap(dependenciesBuilder, Stat.FTM, Stat.IIPM);
		Util.putSetInMap(dependenciesBuilder, Stat.FTF, Stat.IIPM, Stat.FTM);
		Util.putSetInMap(dependenciesBuilder, Stat.PM, Stat.IIPM);
		DEPENDENCIES = Collections.unmodifiableMap(dependenciesBuilder);
		
		// Anti-dependant values:
		Map<Stat, Set<Stat>> antiDependenciesBuilder = new HashMap<Stat, Set<Stat>>();
		Util.putSetInMap(antiDependenciesBuilder, Stat.OFF, Stat.REB);
		Util.putSetInMap(antiDependenciesBuilder, Stat.DEF, Stat.REB);
		Util.putSetInMap(antiDependenciesBuilder, Stat.REB, Stat.OFF, Stat.DEF);
		ANTI_DEPENDENCIES = Collections.unmodifiableMap(antiDependenciesBuilder);
	}	
	
	/////////////////////////////////////////////////
	public final Set<Stat> values;
	/////////////////////////////////////////////////	
	
	public RecordingStats(Set<Stat> recordingStats) {
		if (isValidSet(recordingStats)) {
			this.values = Collections.unmodifiableSet(recordingStats);
		} else {
			throw new IllegalArgumentException("Passed set does not represent valid recording set: " + 
				Arrays.toString(recordingStats.toArray()));
		}
	}
	
	/////////////////////////////////////////////////	
	
	/*
	 * GET VALID SET:
	 */

	public static Set<Stat> getValidSet(Set<Stat> recordingStatsIn) {
		// Null/empty check, init
		if (recordingStatsIn == null) {
			return getOrderedSetWithIIPM();
		}
		Set<Stat> recordingStats = new HashSet<Stat>(recordingStatsIn);
		recordingStats.retainAll(Arrays.asList(Stat.nbaRecordingStats));
		if (recordingStats.isEmpty()) {
			return getOrderedSetWithIIPM();
		}
		// Dependencies
		Set<Stat> mandatoryStats = getMandatoryStats(recordingStats);
		recordingStats.addAll(mandatoryStats);
		// Anti-Dependencies
		Set<Stat> forbidenStats = getForbidenStats(recordingStats);
		recordingStats.removeAll(forbidenStats);
		
		return Util.getOrderedSet(recordingStats);
	}


	private static Set<Stat> getOrderedSetWithIIPM() {
		Set<Stat> orderedSet = new LinkedHashSet<Stat>();
		orderedSet.add(Stat.IIPM);
		return orderedSet;
	}
	
	/////////////////////////////////////////////////
	
	/*
	 * IS VALID SET:
	 */

	public static boolean isValidSet(Set<Stat> recordingStats) {
		// Null/empty check
		if (recordingStats == null || recordingStats.isEmpty()) {
			return false;
		}
		// Check if any stat is not input stat
		if (containsAnyNonInputStats(recordingStats)) {
			return false;
		}
		// Check if all dependencies hold
		if (violatesAnyDependency(recordingStats)) {
			return false;
		}
		return true;
	}

	private static boolean containsAnyNonInputStats(Set<Stat> recordingStats) {
		for (Stat stat : recordingStats) {
			if (!stat.isInputValue()) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean violatesAnyDependency(Set<Stat> recordingStats) {
		Set<Stat> mandatories = getMandatoryStats(recordingStats);
		if (!recordingStats.containsAll(mandatories)) {
			return true;
		}
		Set<Stat> forbidens = getForbidenStats(recordingStats);
		if (Util.containsAny(recordingStats, forbidens)) {
			return true;
		}
		return false;
	}
	
	///////////////////////////
	
	/*
	 * COMMON:
	 */

	private static Set<Stat> getMandatoryStats(Set<Stat> recordingStats) {
		Set<Stat> mandatoryStats = new HashSet<Stat>();
		// Special many to one case
		if (recordingStats.contains(Stat.IIPF) && recordingStats.contains(Stat.TPM)) {
			mandatoryStats.add(Stat.TPF);
		}
		// One to many dependencies
		for (Stat stat : recordingStats) {
			Set<Stat> dependencies = DEPENDENCIES.get(stat);
			if (dependencies == null) {
				continue;
			}
			mandatoryStats.addAll(dependencies);
		}
		return mandatoryStats;
	}
	
	private static Set<Stat> getForbidenStats(Set<Stat> recordingStats) {
		Set<Stat> forbidenStats = new HashSet<Stat>();
		if (recordingStats.contains(Stat.OFF) || recordingStats.contains(Stat.DEF)) {
			forbidenStats.add(Stat.REB);
		}
		return forbidenStats;
	}
	
	///////////////////////////
	
	@Override
	public String toString() {
		return Arrays.toString(values.toArray());
	}
	
}