package outfrost.smtwt.test;

import outfrost.smtwt.Job;
import outfrost.smtwt.JobOrder;
import outfrost.smtwt.antcolony.AntColonyOptimization;

import java.util.Arrays;

public class AntColony4JobTest extends Test {
	
	@Override
	public void run() {
		System.out.println("Running trivial 4-job test with ant colony optimization...");
		
		/*
		j		processing_time		weight		due_after
		0		26					1			118
		1		24					10			122
		2		79					9			133
		3		46					10			127
		 */
		
		Job job0 = new Job(0, 26, 118, 1);
		Job job1 = new Job(1, 24, 122, 10);
		Job job2 = new Job(2, 79, 133, 9);
		Job job3 = new Job(3, 46, 127, 10);
		
		JobOrder jobs = new JobOrder(Arrays.asList(job0, job1, job2, job3));
		
		System.out.println("Initial sequence:");
		System.out.println(jobs.toString());
		
		jobs = AntColonyOptimization.findSolution(jobs);
		
		System.out.println("Solution:");
		System.out.println(jobs.toString());
		
		if (jobs.totalWeightedTardiness() != 201) {
			System.err.println("AntColony4JobTest: Did not reach optimal solution.");
		}
		
		System.out.println();
	}
	
}
