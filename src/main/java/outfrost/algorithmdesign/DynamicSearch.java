package outfrost.algorithmdesign;

import outfrost.algorithmdesign.heuristic.EarliestDueDateHeuristic;

import java.util.Comparator;

public class DynamicSearch {
	
	private final JobOrder jobOrder;
	private static final Comparator<? super Job> initialHeuristic = new EarliestDueDateHeuristic();
	
	public DynamicSearch(JobOrder jobs) {
		jobOrder = jobs;
	}
	
	public void findSolution() {
		jobOrder.sort(initialHeuristic);
	}
	
	public JobOrder getJobOrder() {
		return jobOrder;
	}
	
}
