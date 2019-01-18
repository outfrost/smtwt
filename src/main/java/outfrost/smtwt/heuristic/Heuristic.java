package outfrost.smtwt.heuristic;

import outfrost.smtwt.Job;

import java.util.Comparator;

public abstract class Heuristic {
	
	public abstract float valueFor(Job job, int startTime);
	
}
