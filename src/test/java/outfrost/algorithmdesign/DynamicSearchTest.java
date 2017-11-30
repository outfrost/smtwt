package outfrost.algorithmdesign;

import java.util.Arrays;

public class DynamicSearchTest extends Test {
	
	@Override
	public void run() {
		/*
		j		processing_time		weight		due_after
		0		26					1			118
		1		24					10			122
		2		79					9			133
		3		46					10			127
		 */
		
		Job job0 = new Job(26, 118, 1);
		Job job1 = new Job(24, 122, 10);
		Job job2 = new Job(79, 133, 9);
		Job job3 = new Job(46, 127, 10);
		
		JobOrder jobs = new JobOrder(Arrays.asList(job0, job1, job2, job3));
		JobOrder expectedOrder = new JobOrder(Arrays.asList(job0, job1, job3, job2));
		
		System.out.println(jobs.toString());
		
		DynamicSearch dynamicSearch = new DynamicSearch(jobs);
		dynamicSearch.findSolution();
		
		JobOrder resultingOrder = dynamicSearch.getJobOrder();
		assert resultingOrder.equals(expectedOrder);
		
		System.out.println(resultingOrder.toString());
	}
	
}
