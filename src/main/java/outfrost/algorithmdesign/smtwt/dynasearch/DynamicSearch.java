package outfrost.algorithmdesign.smtwt.dynasearch;

import outfrost.algorithmdesign.smtwt.Job;
import outfrost.algorithmdesign.smtwt.JobOrder;
import outfrost.algorithmdesign.smtwt.heuristic.EarliestDueDateHeuristic;

import java.util.Collections;
import java.util.Comparator;

public class DynamicSearch {
	
	private static final Comparator<? super Job> initialHeuristic = new EarliestDueDateHeuristic();
	
	public static void sort(JobOrder jobs) {
		jobs.sort(initialHeuristic);
	}
	
	public static void findSolution(JobOrder jobs) {
		findSolution(jobs, jobs.size());
	}
	
	public static void findSolution(JobOrder jobs, int subsequenceLength) {
		if (subsequenceLength > 2) {
			findSolution(jobs, subsequenceLength - 1);
		}
		
		int bestWeightedTardiness = jobs.totalWeightedTardiness(subsequenceLength);
		int bestSwapPosition = subsequenceLength - 1;
		
		for (int i = 0; i < subsequenceLength - 1; i++) {
			Collections.swap(jobs, i, subsequenceLength - 1);
			int newWeightedTardiness = jobs.totalWeightedTardiness(subsequenceLength);
			
			if (newWeightedTardiness < bestWeightedTardiness) {
				bestWeightedTardiness = newWeightedTardiness;
				bestSwapPosition = i;
			}
			
			Collections.swap(jobs, i, subsequenceLength - 1);
		}
		
		if (bestSwapPosition != subsequenceLength - 1) {
			Collections.swap(jobs, bestSwapPosition, subsequenceLength - 1);
			findSolution(jobs, subsequenceLength);
		}
	}
	
}
