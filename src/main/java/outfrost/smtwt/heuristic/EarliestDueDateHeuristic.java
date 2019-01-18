package outfrost.smtwt.heuristic;

import outfrost.smtwt.Job;

import java.util.Comparator;

/**
 * Note: this comparator imposes orderings that are inconsistent with equals.
 */
public class EarliestDueDateHeuristic implements Comparator<Job> {
	
	@Override
	public int compare(Job o1, Job o2) {
		return Comparator.<Job>comparingInt(Job::getDueTime).compare(o1, o2);
	}
	
}
