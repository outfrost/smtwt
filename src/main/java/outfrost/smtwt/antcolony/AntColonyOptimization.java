package outfrost.smtwt.antcolony;

import outfrost.smtwt.Job;
import outfrost.smtwt.JobOrder;
import outfrost.smtwt.heuristic.EarliestDueDateHeuristic;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AntColonyOptimization {
	
	private static final int ants = 8;
	private static final Comparator<? super Job> heuristic = new EarliestDueDateHeuristic();
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
					float bestTrailValue = 0.0f;
					
					for (Job job : jobs) {
						float trailValue = pheromoneTrail.get(i, job);
						if (!scheduled.get(job) && (trailValue > bestTrailValue
						                            || bestJob == null)) {
							bestJob = job;
							bestTrailValue = trailValue;
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
