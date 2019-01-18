package outfrost.smtwt.heuristic;

import outfrost.smtwt.Job;

import java.util.Comparator;

public abstract class Heuristic implements Comparator<Job> {
	
	public abstract float valueFor(Job job);
	
}
