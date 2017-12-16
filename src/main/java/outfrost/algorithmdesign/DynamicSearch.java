package outfrost.algorithmdesign;

import outfrost.algorithmdesign.heuristic.EarliestDueDateHeuristic;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class DynamicSearch {
	
	//private final JobOrder jobOrder;
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
		
		Collections.swap(jobs, bestSwapPosition, subsequenceLength - 1);
	}
	/*
	public DynamicSearch(JobOrder jobs) {
		jobOrder = jobs;
	}
	
	public void findSolution() {
		jobOrder.sort(initialHeuristic);
		
		for (int i = 0; i < jobOrder.size() - 1; i++) {
			Collections.swap(jobOrder, i, i+1);
			if (jobOrder.totalWeightedTardiness() < leastKnownTotalWeightedTardiness) {
				leastKnownTotalWeightedTardiness = jobOrder.totalWeightedTardiness();
			}
			
		}
		
		JobOrder bestKnownOrder = new JobOrder(jobOrder);
		bestKnownOrder.sort(initialHeuristic);
		int leastKnownTotalWeightedTardiness = bestKnownOrder.totalWeightedTardiness();
		
		HashMap<Long, JobOrderCacheItem> jobOrderCache = new HashMap<>();
		jobOrderCache.put
	}
	
	public JobOrder getJobOrder() {
		return jobOrder;
	}
	*/
}
