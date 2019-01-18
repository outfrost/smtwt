package outfrost.smtwt.antcolony;

import outfrost.smtwt.Job;
import outfrost.smtwt.JobOrder;
import outfrost.smtwt.heuristic.Heuristic;
import outfrost.smtwt.heuristic.ModifiedDueDateHeuristic;
import outfrost.util.BernoulliDistribution;
import outfrost.util.TriFunction;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class AntColonyOptimization {
	
	private static final int ants = 8;
	private static final BernoulliDistribution exploitationRng
			= new BernoulliDistribution(0.9);
	private static final Random selectionRng = new Random();
	private static final Heuristic heuristic = new ModifiedDueDateHeuristic();
	private static final float heuristicExponent = 2.0f;
	private static final float evaporation = 0.1f;
	private static final float intermediateTrailFactor = 0.1f;
	
	public static JobOrder findSolution(JobOrder jobs) {
		JobOrder currentSolution = new JobOrder(jobs);
		int currentSolutionTardiness = currentSolution.totalWeightedTardiness();
		PheromoneTrail pheromoneTrail = new PheromoneTrail(jobs, evaporation);
		float intermediateTrailVector = 1.0f / ((float) jobs.size()
		                                        * (float) currentSolutionTardiness);
		
		TriFunction<Integer, Job, Integer, Float> computeMetric = (i, job, startTime) -> {
			return pheromoneTrail.get(i, job)
			       * (float) Math.pow(heuristic.valueFor(job, startTime), heuristicExponent);
		};
		
		int iterationsWithoutImprovement = 0;
		
		while (iterationsWithoutImprovement < 256) {
			iterationsWithoutImprovement++;
			
			for (int ant = 0; ant < ants; ant++) {
				JobOrder newSequence = new JobOrder(jobs.size());
				int timeElapsed = 0;
				//boolean[] scheduled = new boolean[jobs.size()];
				Map<Job, Boolean> scheduled = new HashMap<>(jobs.size() + 1, 1.0f);
				for (Job job : jobs) {
					scheduled.put(job, false);
				}
				
				for (int i = 0; i < jobs.size(); i++) {
					Job bestJob = null;
					
					if (exploitationRng.nextBoolean()) { // Exploitation
						float bestMetric = 0.0f;
						
						for (Job job : jobs) {
							float metric = computeMetric.apply(i, job, timeElapsed);
							if (!scheduled.get(job) && (metric > bestMetric
							                            || bestJob == null)) {
								bestJob = job;
								bestMetric = metric;
							}
						}
					}
					else { // Exploration
						Map<Job, Float> metrics = new LinkedHashMap<>(jobs.size() + 1, 1.0f);
						for (Job job : jobs) {
							if (!scheduled.get(job)) {
								metrics.put(job, computeMetric.apply(i, job, timeElapsed));
							}
						}
						/*
						int selection = selectionRng.nextInt(metrics.size());
						for (Job job : metrics.keySet()) {
							if (selection-- >= 0) {
								bestJob = job;
							}
						}
						*/
						float sumMetrics = 0.0f;
						for (float metric : metrics.values()) {
							sumMetrics += metric;
						}
						
						double selection = selectionRng.nextDouble();
						double probabilitySum = 0.0;
						Job lastJob = null;
						for (Map.Entry<Job, Float> entry : metrics.entrySet()) {
							if (bestJob == null) {
								probabilitySum += (double) entry.getValue() / (double) sumMetrics;
								if (probabilitySum > selection) {
									bestJob = entry.getKey();
								}
								//probabilitySum += (double) entry.getValue() / (double) sumMetrics;
								lastJob = entry.getKey();
							}
						}
						/* This is to prevent floating point precision errors
						   from causing a null selection */
						if (bestJob == null) {
							bestJob = lastJob;
						}
					}
					assert bestJob != null;
					
					newSequence.add(bestJob);
					timeElapsed += bestJob.getProcessingTime();
					scheduled.put(bestJob, true);
					
					pheromoneTrail.intermediateUpdate(i, bestJob,
							intermediateTrailFactor, intermediateTrailVector);
				}
				
				int newSequenceTardiness = newSequence.totalWeightedTardiness();
				if (newSequenceTardiness < currentSolutionTardiness) {
					currentSolution = newSequence;
					currentSolutionTardiness = newSequenceTardiness;
					iterationsWithoutImprovement = 0;
				}
			}
			
			pheromoneTrail.update(currentSolution);
		}
		return currentSolution;
	}
	
}
