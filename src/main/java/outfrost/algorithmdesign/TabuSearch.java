package outfrost.algorithmdesign;

import outfrost.algorithmdesign.heuristic.EarliestDueDateHeuristic;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;

public class TabuSearch {

	private static final Comparator<? super Job> initialHeuristic = new EarliestDueDateHeuristic();
	
	private LinkedHashSet<Tabu> tabus;
	
	public static void sort(JobOrder jobs) {
		jobs.sort(initialHeuristic);
	}
	
	public void findSolution(JobOrder jobs, int subsequenceLength) {
		if (subsequenceLength > 2) {
			findSolution(jobs, subsequenceLength - 1);
		}
		
		int bestWeightedTardiness = jobs.totalWeightedTardiness(subsequenceLength);
		int bestSwapPosition = subsequenceLength - 1;
		
		for (int i = 0; i < subsequenceLength - 1; i++) {
			Tabu tabu1 = i > 0 ? new Tabu(jobs.get(i - 1), jobs.get(i)) : null;
			Tabu tabu2 = new Tabu(jobs.get(i), jobs.get(i + 1));
			Tabu tabu3 = new Tabu(jobs.get(subsequenceLength - 2), jobs.get(subsequenceLength - 1));
			
			if (!tabus.contains(tabu1) && !tabus.contains(tabu2) && !tabus.contains(tabu3)) {
				Collections.swap(jobs, i, subsequenceLength - 1);
				int newWeightedTardiness = jobs.totalWeightedTardiness(subsequenceLength);
				
				if (newWeightedTardiness < bestWeightedTardiness) {
					bestWeightedTardiness = newWeightedTardiness;
					bestSwapPosition = i;
				}
				
				Collections.swap(jobs, i, subsequenceLength - 1);
			}
			
			tabus.add(tabu1);
			tabus.add(tabu2);
			tabus.add(tabu3);
		}
		
		if (bestSwapPosition != subsequenceLength - 1) {
			Collections.swap(jobs, bestSwapPosition, subsequenceLength - 1);
			findSolution(jobs, subsequenceLength);
		}
	}

}
