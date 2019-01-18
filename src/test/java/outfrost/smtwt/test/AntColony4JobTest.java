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
		
		int successCount = 0;
		for (int i = 0; i < 100; i++) {
			JobOrder solution = AntColonyOptimization.findSolution(jobs);
			if (solution.totalWeightedTardiness() == 201) {
				successCount++;
			}
		}
		
		System.out.println("Reached optimal solution in " + successCount + "% of attempts");
		
		if (successCount < 90) {
			System.err.println("AntColony4JobTest: Success rate was under 90%");
		}
		
		System.out.println();
	}
	
}
