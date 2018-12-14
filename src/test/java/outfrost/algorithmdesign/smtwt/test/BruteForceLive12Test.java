package outfrost.algorithmdesign.smtwt.test;

import outfrost.algorithmdesign.smtwt.JobOrder;
import outfrost.algorithmdesign.smtwt.bruteforce.BruteForce;
import outfrost.algorithmdesign.smtwt.util.SmallwstLoader;

import java.io.IOException;
import java.util.Arrays;

public class BruteForceLive12Test extends Test {
	
	@Override
	public void run() {
		System.out.println("Running live data test with brute force...");
		System.out.println("12-job smallWST instance");
		
		try {
			JobOrder jobs = SmallwstLoader.load("data/smallwst/data12.txt");
			
			JobOrder expectedOrder = new JobOrder(Arrays.asList(
				jobs.get(5),
				jobs.get(0),
				jobs.get(1),
				jobs.get(11),
				jobs.get(4),
				jobs.get(2),
				jobs.get(3),
				jobs.get(8),
				jobs.get(9),
				jobs.get(10),
				jobs.get(6),
				jobs.get(7)
			));
			
			System.out.println("Initial sequence:");
			System.out.println(jobs.toString());
			System.out.println("Expected result:");
			System.out.println(expectedOrder.toString());
			
			long startTime = System.nanoTime();
			
			jobs = BruteForce.findSolution(jobs);
			
			long timeTaken = System.nanoTime() - startTime;
			
			System.out.println("Solution:");
			System.out.println(jobs.toString());
			System.out.println("Time taken: " + timeTaken + " ns");
			
			if (!jobs.equals(expectedOrder)) {
				System.err.println("BruteForceLive12Test: The result did not match the expected sequence");
			}
		} catch (IOException e) {
			System.err.println("Error loading problem instance: " + e.getMessage());
		} finally {
			System.out.println();
		}
	}
	
}
