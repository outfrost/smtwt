package outfrost.algorithmdesign.smtwt.test;

import outfrost.algorithmdesign.smtwt.Job;
import outfrost.algorithmdesign.smtwt.JobOrder;
import outfrost.algorithmdesign.smtwt.tabusearch.TabuSearch;

import java.util.Arrays;

public class TabuSearch4JobTest extends Test {
	
	@Override
	public void run() {
		System.out.println("Running trivial 4-job test with tabu search...");
		
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
		TabuSearch tabuSearch = new TabuSearch(jobs);
		
		JobOrder expectedOrder = new JobOrder(Arrays.asList(job0, job1, job3, job2));
		
		System.out.println("Initial sequence:");
		System.out.println(jobs.toString());
		System.out.println("Expected sequence after initial heuristic sort:");
		System.out.println(expectedOrder.toString());
		
		tabuSearch.sort();
		
		System.out.println("After initial heuristic sort:");
		System.out.println(jobs.toString());
		
		if(!jobs.equals(expectedOrder)) {
			System.err.println("DynamicSearch4JobTest: The result of initial heuristic sort did not match the expected sequence.");
		}
		
		tabuSearch.findSolution();
		
		System.out.println("Solution:");
		System.out.println(jobs.toString());
		
		System.out.println();
	}
	
}
