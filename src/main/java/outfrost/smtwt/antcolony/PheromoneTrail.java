package outfrost.smtwt.antcolony;

import outfrost.smtwt.Job;
import outfrost.smtwt.JobOrder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class PheromoneTrail {
	
	private float evaporation;
	//private float[] trail;
	
	private Map<Integer, Map<Job, Float>> trail;
	
	PheromoneTrail(Collection<Job> jobs, float evaporation) {
		assert evaporation > 0.0f && evaporation <= 1.0f;
		this.evaporation = evaporation;
		//trail = new float[jobCount * jobCount];
		trail = new HashMap<>();
		for (int i = 0; i < jobs.size(); i++) {
			trail.put(i, new HashMap<>());
			for (Job job : jobs) {
				trail.get(i).put(job, 1.0f);
			}
		}
	}
	
	float get(int position, Job job) {
		//return trail[position * jobCount + jobIndex];
		return trail.get(position).get(job);
	}
	
	void update(JobOrder currentSolution) {
		assert currentSolution.size() == trail.size();
		for (int i = 0; i < currentSolution.size(); i++) {
			trail.get(i).compute(currentSolution.get(i),
					(k, v) -> (1.0f - evaporation) * v
					          + evaporation * (1.0f / (float) currentSolution.totalWeightedTardiness()));
		}
	}
	
	void intermediateUpdate(int position, Job job, float factor, float vector) {
		trail.get(position).compute(job,
				(k, v) -> (1.0f - factor) * v
				          + factor * vector);
	}
	
}
