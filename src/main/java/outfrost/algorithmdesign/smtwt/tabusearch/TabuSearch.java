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
	
	public static JobOrder findSolution(JobOrder jobs) {
		JobOrder currentSolution = new JobOrder(jobs);
		int currentSolutionTardiness = currentSolution.totalWeightedTardiness();
		TabuList tabuList = new TabuList(4);
		int turnsWithoutImprovement = 0;
		
		while (turnsWithoutImprovement < 32) {
			turnsWithoutImprovement++;
			
			int bestWeightedTardiness = 0;
			int bestSwapA = 0;
			int bestSwapB = 0;
			
			for (int i = 0; i < jobs.size(); i++) {
				for (int k = i + 1; k < jobs.size(); k++) {
					if (!tabuList.contains(new Tabu(jobs.get(i), jobs.get(k)))) {
						Collections.swap(jobs, i, k);
						
						int newWeightedTardiness = jobs.totalWeightedTardiness();
						if (newWeightedTardiness < bestWeightedTardiness
						    || (bestSwapA == 0 && bestSwapB == 0)) {
							bestWeightedTardiness = newWeightedTardiness;
							bestSwapA = i;
							bestSwapB = k;
						}
						
						Collections.swap(jobs, i, k);
					}
				}
			}
			
			if (bestSwapA != 0 || bestSwapB != 0) {
				Collections.swap(jobs, bestSwapA, bestSwapB);
				if (bestWeightedTardiness < currentSolutionTardiness) {
					currentSolution = new JobOrder(jobs);
					currentSolutionTardiness = bestWeightedTardiness;
					turnsWithoutImprovement = 0;
				}
			}
			
			tabuList.add(new Tabu(jobs.get(bestSwapA), jobs.get(bestSwapB)));
		}
		
		return currentSolution;
	}
	
}
