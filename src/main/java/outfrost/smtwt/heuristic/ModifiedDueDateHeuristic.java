package outfrost.smtwt.heuristic;

import outfrost.smtwt.Job;

import java.util.Comparator;

public class ModifiedDueDateHeuristic extends Heuristic {
	
	@Override
	public float valueFor(Job job, int startTime) {
		return 1.0f / (float) Math.max(startTime + job.getProcessingTime(), job.getDueTime());
	}
	
}
