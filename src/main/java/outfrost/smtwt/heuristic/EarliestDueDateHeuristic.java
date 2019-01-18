package outfrost.smtwt.heuristic;

import outfrost.smtwt.Job;

import java.util.Comparator;

/**
 * Note: this comparator imposes orderings that are inconsistent with equals.
 */
public class EarliestDueDateHeuristic extends Heuristic {
	
	@Override
	public int compare(Job o1, Job o2) {
		return Comparator.<Job>comparingInt(Job::getDueTime).compare(o1, o2);
	}
	
	@Override
	public float valueFor(Job job) {
		return 1.0f / (float) job.getDueTime();
	}
	
}
