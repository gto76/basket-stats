package si.gto76.basketstats.coreclasses;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class RecordingStatsTest {
	
	/*
	 * RECORDING STATS TEST
	 */
	@Test
	public void recordingStatsTest() {
		// for all combinations of Input Stat run getValidSet
		Set<Set<Stat>> allCombinations = SaveAndLoadTest.powerSet(new HashSet<Stat>(Arrays.asList(Stat.recordableStats)));
		for (Set<Stat> combination : allCombinations) {
			Set<Stat> validSet = RecordingStats.getValidSet(combination);
			assertTrue(RecordingStats.isValidSet(validSet));
		}
	}
	
}
