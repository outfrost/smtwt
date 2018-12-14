package outfrost.algorithmdesign.smtwt.bruteforce;

import outfrost.algorithmdesign.smtwt.Job;
import outfrost.algorithmdesign.smtwt.JobOrder;

import java.util.LinkedList;

public class BruteForce {
	
	private JobOrder jobs;
	private JobOrder bestOrder;
	private int bestWeightedTardiness;
	
	public BruteForce(JobOrder jobs) {
		this.jobs = jobs;
		bestOrder = new JobOrder(jobs);
		bestWeightedTardiness = bestOrder.totalWeightedTardiness();
	}
	
	public static JobOrder findSolution(JobOrder jobs) {
		return new BruteForce(jobs).findSolution();
	}
	
	public JobOrder findSolution() {
		enumerateSolutions(new JobOrder(jobs.size()));
		return bestOrder;
	}
	
	private void enumerateSolutions(JobOrder startingOrder) {
		LinkedList<Job> availableJobs = new LinkedList<>();
		for (Job job : jobs) {
			if (!startingOrder.contains(job)) {
				availableJobs.add(job);
			}
		}
		
		if (startingOrder.size() < jobs.size() - 1) {
			for (Job job : availableJobs) {
				JobOrder newOrder = new JobOrder(jobs.size());
				newOrder.addAll(startingOrder);
				newOrder.add(job);
				enumerateSolutions(newOrder);
			}
		}
		else {
			JobOrder newOrder = new JobOrder(jobs.size());
			newOrder.addAll(startingOrder);
			newOrder.add(availableJobs.pop());
			int newWeightedTardiness = newOrder.totalWeightedTardiness();
			if (newWeightedTardiness < bestWeightedTardiness) {
				bestOrder = newOrder;
				bestWeightedTardiness = newWeightedTardiness;
			}
		}
	}
	
}
