package si.gto76.basketstats.coreclasses;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/**
 * Class that contains unmodifiable set of input Stats that is guaranteed to be in legal state.
 * Legality is decided by rules that demand or forbid some Stats in presence of others.
 * (For example you can not track three point shots if you don't also track two pointers.)
 * Set that is sent to constructor should be checked for legality with static function isValidSet(Set<Stat>),
 * or sent to static function getValidSet(Set<Stat>), that will modify it into legal state.
 */
public class RecordingStats {
	private static final Map<Stat, Set<Stat>> STATS_THAT_NEED_TO_BE_ADDED_WHEN_ADDING;
	private static final Map<Stat, Set<Stat>> STATS_THAT_NEED_TO_BE_REMOVED_WHEN_ADDING;
	private static final Map<Stat, Set<Stat>> STATS_THAT_NEED_TO_BE_REMOVED_WHEN_REMOVING;
	// Fill the maps:
	static {
		Map<Stat,  Set<Stat>> dependenciesBuilder = new HashMap<Stat, Set<Stat>>();
		Util.putSetInMap(dependenciesBuilder, Stat.IIPF, Stat.IIPM);
		Util.putSetInMap(dependenciesBuilder, Stat.TPM, Stat.IIPM);
		Util.putSetInMap(dependenciesBuilder, Stat.TPF, Stat.IIPM, Stat.TPM, Stat.IIPF);
		Util.putSetInMap(dependenciesBuilder, Stat.FTM, Stat.IIPM);
		Util.putSetInMap(dependenciesBuilder, Stat.FTF, Stat.IIPM, Stat.FTM);
		Util.putSetInMap(dependenciesBuilder, Stat.PM, Stat.IIPM);
		STATS_THAT_NEED_TO_BE_ADDED_WHEN_ADDING = Collections.unmodifiableMap(dependenciesBuilder);
		
		dependenciesBuilder = new HashMap<Stat, Set<Stat>>();
		Util.putSetInMap(dependenciesBuilder, Stat.OFF, Stat.REB);
		Util.putSetInMap(dependenciesBuilder, Stat.DEF, Stat.REB);
		Util.putSetInMap(dependenciesBuilder, Stat.REB, Stat.OFF, Stat.DEF);
		STATS_THAT_NEED_TO_BE_REMOVED_WHEN_ADDING = Collections.unmodifiableMap(dependenciesBuilder);
		
		dependenciesBuilder = new HashMap<Stat, Set<Stat>>();
		Util.putSetInMap(dependenciesBuilder, Stat.IIPM, Stat.IIPF, Stat.TPM, Stat.TPF, Stat.FTM, Stat.FTF, Stat.PM);
		Util.putSetInMap(dependenciesBuilder, Stat.IIPF, Stat.TPF);
		Util.putSetInMap(dependenciesBuilder, Stat.TPM, Stat.TPF);
		Util.putSetInMap(dependenciesBuilder, Stat.TPF, Stat.IIPF);
		Util.putSetInMap(dependenciesBuilder, Stat.FTM, Stat.FTF);
		STATS_THAT_NEED_TO_BE_REMOVED_WHEN_REMOVING = Collections.unmodifiableMap(dependenciesBuilder);
	}
	public static final RecordingStats DEFAULT = new RecordingStats(Util.arrayToSet(Stat.nbaRecordingStats));
	
	/////////////////////////////////////////////////
	public final Set<Stat> values;
	/////////////////////////////////////////////////	

	public RecordingStats(RecordingStats recordingStats) {
		this(recordingStats.values);
	}
	
	/**
	 * Throws IllegalArgumentException if passed array does not contain valid combination of Stats.
	 */
	public RecordingStats(Stat... recordingStats) {
		this(new HashSet<Stat>(Arrays.asList(recordingStats)));
	}
	
	/**
	 * Throws IllegalArgumentException if passed set does not contain valid combination of Stats.
	 */
	public RecordingStats(Set<Stat> recordingStats) {
		if (isValidSet(recordingStats)) {
			this.values = Collections.unmodifiableSet(recordingStats);
		} else {
			throw new IllegalArgumentException("Passed set does not represent valid recording set: " + 
				Arrays.toString(Util.getOrderedSet(recordingStats).toArray()));
		}
	}

	/////////////////////////////////////////////////
	
	/*
	 * ####### ####### ####### ####### ####### #######
	 * GETTERS GETTERS GETTERS GETTERS GETTERS GETTERS
	 * ####### ####### ####### ####### ####### #######
	 */

	/**
	 * Returns new RecordingStats object. Old one stays unchanged.
	 * New object has additional stat that was passed, plus all the
	 * stats that had to be added to maintain legal state.
	 */
	public RecordingStats add(Stat stat) {
		Set<Stat> valuesOut = new HashSet<Stat>(values);
		if (STATS_THAT_NEED_TO_BE_REMOVED_WHEN_ADDING.containsKey(stat)) {
			valuesOut.removeAll(STATS_THAT_NEED_TO_BE_REMOVED_WHEN_ADDING.get(stat));
		}
		if (STATS_THAT_NEED_TO_BE_ADDED_WHEN_ADDING.containsKey(stat)) {
			valuesOut.addAll(STATS_THAT_NEED_TO_BE_ADDED_WHEN_ADDING.get(stat));
		}
		// Special cases that could not be expressed with map:
		if (stat == Stat.IIPF && values.contains(Stat.TPM)) {
			valuesOut.add(Stat.TPF);
		}
		if (stat == Stat.TPM && values.contains(Stat.IIPF)) {
			valuesOut.add(Stat.TPF);
		}
		valuesOut.add(stat);
		return new RecordingStats(valuesOut);
	}
	
	/**
	 * Returns new RecordingStats object. Old one stays unchanged.
	 * New object is without passed stat and also any stat that
	 * had to be removed with it to maintain legal state.
	 */
	public RecordingStats remove_Nullable(Stat stat) {
		Set<Stat> valuesOut = new HashSet<Stat>(values);
		if (STATS_THAT_NEED_TO_BE_REMOVED_WHEN_REMOVING.containsKey(stat)) {
			valuesOut.removeAll(STATS_THAT_NEED_TO_BE_REMOVED_WHEN_REMOVING.get(stat));
		}
		valuesOut.remove(stat);
		if (valuesOut.isEmpty()) {
			return null;
		}
		return new RecordingStats(valuesOut);
	}
	
	/*
	 * ################ ################ ################
	 * STATIC FUNCTIONS STATIC FUNCTIONS STATIC FUNCTIONS
	 * ################ ################ ################
	 */
	
	/*
	 * GET VALID SET:
	 */
	
	/**
	 * If passed set is in legal state, then returns a copy.
	 * It it's not, than returns a new one based on passed set, that is legal.
	 * Passed set is not changed.
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
		Set<Stat> forbiddenStats = getForbiddenStats(recordingStats);
		recordingStats.removeAll(forbiddenStats);
		
		return Util.getOrderedSet(recordingStats);
	}

	private static Set<Stat> getOrderedSetWithIIPM() {
		Set<Stat> orderedSet = new LinkedHashSet<Stat>();
		orderedSet.add(Stat.IIPM);
		return orderedSet;
	}
	
	/*
	 * IS VALID SET:
	 */

	public static boolean isValidSet(Set<Stat> recordingStats) {
		if (recordingStats == null || recordingStats.isEmpty()) {
			return false;
		}
		if (containsAnyNonInputStats(recordingStats)) {
			return false;
		}
		if (violatesAnyDependency(recordingStats)) {
			return false;
		}
		return true;
	}

	private static boolean containsAnyNonInputStats(Set<Stat> recordingStats) {
		for (Stat stat : recordingStats) {
			if (!stat.isRecordable()) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean violatesAnyDependency(Set<Stat> recordingStats) {
		Set<Stat> mandatories = getMandatoryStats(recordingStats);
		boolean stats_dont_contain_all_the_mandatories = !recordingStats.containsAll(mandatories);
		if (stats_dont_contain_all_the_mandatories) {
			return true;
		}
		Set<Stat> forbiddens = getForbiddenStats(recordingStats);
		if (Util.containsAny(recordingStats, forbiddens)) {
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
			Set<Stat> dependencies = STATS_THAT_NEED_TO_BE_ADDED_WHEN_ADDING.get(stat);
			if (dependencies == null) {
				continue;
			}
			mandatoryStats.addAll(dependencies);
		}
		return mandatoryStats;
	}
	
	private static Set<Stat> getForbiddenStats(Set<Stat> recordingStats) {
		Set<Stat> forbiddenStats = new HashSet<Stat>();
		if (recordingStats.contains(Stat.OFF) || recordingStats.contains(Stat.DEF)) {
			forbiddenStats.add(Stat.REB);
		}		
		return forbiddenStats;
	}
	
	///////////////////////////
	
	@Override
	public String toString() {
		return Arrays.toString(Util.getOrderedSet(values).toArray());
	}
	
}
