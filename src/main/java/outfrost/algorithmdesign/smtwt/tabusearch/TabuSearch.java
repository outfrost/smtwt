package outfrost.algorithmdesign.smtwt.tabusearch;

import outfrost.algorithmdesign.smtwt.Job;
import outfrost.algorithmdesign.smtwt.JobOrder;
import outfrost.algorithmdesign.smtwt.heuristic.EarliestDueDateHeuristic;

import java.util.Collections;
import java.util.Comparator;

public class TabuSearch {

	private static final Comparator<? super Job> initialHeuristic = new EarliestDueDateHeuristic();
	
	public static void sort(JobOrder jobs) {
		jobs.sort(initialHeuristic);
	}
	
	public void findSolution(JobOrder jobs) {
		TabuList tabuList = new TabuList(16);
		boolean betterSolutionFound = true;
		boolean overrideTabu = false;
		
		while (betterSolutionFound || overrideTabu) {
			int bestWeightedTardiness = jobs.totalWeightedTardiness();
			int bestSwapA = 0;
			int bestSwapB = 0;
			betterSolutionFound = false;
			
			for (int i = 0; i < jobs.size(); i++) {
				for (int k = i + 1; k < jobs.size(); k++) {
					if (overrideTabu || !tabuList.contains(new Tabu(jobs.get(i), jobs.get(k)))) {
						Collections.swap(jobs, i, k);
						
						int newWeightedTardiness = jobs.totalWeightedTardiness();
						if (newWeightedTardiness < bestWeightedTardiness) {
							bestWeightedTardiness = newWeightedTardiness;
							bestSwapA = i;
							bestSwapB = k;
							betterSolutionFound = true;
						}
						
						Collections.swap(jobs, i, k);
					}
				}
			}
			
			if (betterSolutionFound) {
				Collections.swap(jobs, bestSwapA, bestSwapB);
				tabuList.add(new Tabu(jobs.get(bestSwapA), jobs.get(bestSwapB)));
				overrideTabu = false;
			}
			else {
				overrideTabu = !overrideTabu;
			}
		}
	}
	
}
