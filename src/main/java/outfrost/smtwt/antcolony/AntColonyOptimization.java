package outfrost.smtwt.antcolony;

import outfrost.smtwt.Job;
import outfrost.smtwt.JobOrder;
import outfrost.smtwt.heuristic.EarliestDueDateHeuristic;
import outfrost.smtwt.heuristic.Heuristic;

import java.util.HashMap;
import java.util.Map;

public class AntColonyOptimization {
	
	private static final int ants = 8;
	private static final Heuristic heuristic = new EarliestDueDateHeuristic();
	private static final float heuristicExponent = 2.0f;
	private static final float evaporation = 0.1f;
	private static final float intermediateTrailFactor = 0.1f;
	
	public static JobOrder findSolution(JobOrder jobs) {
		JobOrder currentSolution = new JobOrder(jobs);
		int currentSolutionTardiness = currentSolution.totalWeightedTardiness();
		PheromoneTrail pheromoneTrail = new PheromoneTrail(jobs, evaporation);
		float intermediateTrailVector;
		
		int turnsWithoutImprovement = 0;
		
		while (turnsWithoutImprovement < 64) {
			turnsWithoutImprovement++;
			intermediateTrailVector = 1.0f / ((float) jobs.size()
			                                  * (float) currentSolutionTardiness);
			
			for (int ant = 0; ant < ants; ant++) {
				JobOrder newSequence = new JobOrder(jobs.size());
				//boolean[] scheduled = new boolean[jobs.size()];
				Map<Job, Boolean> scheduled = new HashMap<>(jobs.size() + 1, 1.0f);
				for (Job job : jobs) {
					scheduled.put(job, false);
				}
				
				for (int i = 0; i < jobs.size(); i++) {
					Job bestJob = null;
					float bestMetric = 0.0f;
					
					for (Job job : jobs) {
						float metric = pheromoneTrail.get(i, job)
						               * (float) Math.pow(heuristic.valueFor(job), heuristicExponent);
						if (!scheduled.get(job) && (metric > bestMetric
						                            || bestJob == null)) {
							bestJob = job;
							bestMetric = metric;
						}
					}
					assert bestJob != null;
					
					newSequence.add(bestJob);
					scheduled.put(bestJob, true);
					
					pheromoneTrail.intermediateUpdate(i, bestJob,
							intermediateTrailFactor, intermediateTrailVector);
				}
				
				int newSequenceTardiness = newSequence.totalWeightedTardiness();
				if (newSequenceTardiness < currentSolutionTardiness) {
					currentSolution = newSequence;
					currentSolutionTardiness = newSequenceTardiness;
					turnsWithoutImprovement = 0;
				}
			}
			
			pheromoneTrail.update(currentSolution);
		}
		return currentSolution;
	}
	
}
