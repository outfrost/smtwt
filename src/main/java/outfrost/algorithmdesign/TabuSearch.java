package outfrost.algorithmdesign;

import outfrost.algorithmdesign.heuristic.EarliestDueDateHeuristic;

import java.util.Collections;
import java.util.Comparator;

public class TabuSearch {

	private static final Comparator<? super Job> initialHeuristic = new EarliestDueDateHeuristic();
	
	private JobOrder jobs;
	
	private int initialWeightedTardiness;
	private TabuList tabuList;
	private TabuList longTermTabuList;
	
	public TabuSearch(JobOrder jobs) {
		setJobs(jobs);
		initialWeightedTardiness = jobs.totalWeightedTardiness();
		tabuList = new TabuList();
		longTermTabuList = new TabuList(32);
	}
	
	public TabuSearch(JobOrder jobs, int tabuListCapacity, int longTermTabuListCapacity) {
		setJobs(jobs);
		initialWeightedTardiness = jobs.totalWeightedTardiness();
		tabuList = new TabuList(tabuListCapacity);
		longTermTabuList = new TabuList(longTermTabuListCapacity);
	}
	
	public void sort() {
		jobs.sort(initialHeuristic);
	}
	
	public void findSolution() {
		findSolution(jobs.size());
	}
	
	public void findSolution(int subsequenceLength) {
		if (subsequenceLength > 2) {
			findSolution(subsequenceLength - 1);
		}
		
		int bestWeightedTardiness = jobs.totalWeightedTardiness(subsequenceLength);
		int bestSwapPosition = subsequenceLength - 1;
		
		for (int i = 0; i < subsequenceLength - 1; i++) {
			
			Collections.swap(jobs, i, subsequenceLength - 1);
			
			Tabu producedArc1 = i > 0 ? new Tabu(jobs.get(i - 1), jobs.get(i)) : null;
			Tabu producedArc2 = new Tabu(jobs.get(i), jobs.get(i + 1));
			Tabu producedArc3 = new Tabu(jobs.get(subsequenceLength - 2), jobs.get(subsequenceLength - 1));
			
			if (!tabuList.contains(producedArc1) &&
			    !tabuList.contains(producedArc2) &&
			    !tabuList.contains(producedArc3) &&
				!longTermTabuList.contains(producedArc3)) {
				
				int newWeightedTardiness = jobs.totalWeightedTardiness(subsequenceLength);
				
				if (newWeightedTardiness < bestWeightedTardiness) {
					bestWeightedTardiness = newWeightedTardiness;
					bestSwapPosition = i;
				}
				else if (newWeightedTardiness > initialWeightedTardiness) {
					longTermTabuList.add(producedArc3);
				}
			}
			
			Collections.swap(jobs, i, subsequenceLength - 1);
		}
		
		if (bestSwapPosition != subsequenceLength - 1) {
			tabuList.add(bestSwapPosition > 0 ? new Tabu(jobs.get(bestSwapPosition - 1), jobs.get(bestSwapPosition)) : null);
			tabuList.add(new Tabu(jobs.get(bestSwapPosition), jobs.get(bestSwapPosition + 1)));
			tabuList.add(new Tabu(jobs.get(subsequenceLength - 2), jobs.get(subsequenceLength - 1)));
			
			Collections.swap(jobs, bestSwapPosition, subsequenceLength - 1);
			
			findSolution(subsequenceLength);
		}
	}
	
	public JobOrder getJobs() {
		return jobs;
	}
	
	public void setJobs(JobOrder jobs) {
		this.jobs = jobs;
	}
	
}
