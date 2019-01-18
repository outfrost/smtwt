package outfrost.smtwt.test;

import outfrost.smtwt.JobOrder;
import outfrost.smtwt.antcolony.AntColonyOptimization;
import outfrost.smtwt.util.OrlibLoader;

import java.io.IOException;

public class AntColonyLive40Test extends Test {
	
	@Override
	public void run() {
		System.out.println("Running live data test with ant colony optimization...");
		System.out.println("40-job ORLib instance # " + (DynamicSearchLive40Test.instanceIndex + 1));
		
		try {
			JobOrder jobs = OrlibLoader.load("data/orlib/wt40.txt", 125, DynamicSearchLive40Test.instanceIndex);
			
			System.out.println("Initial sequence:");
			System.out.println(jobs.toString());
			
			long startTime = System.nanoTime();
			
			jobs = AntColonyOptimization.findSolution(jobs);
			
			long timeTaken = System.nanoTime() - startTime;
			
			System.out.println("Solution:");
			System.out.println(jobs.toString());
			System.out.println("Time taken: " + timeTaken + " ns");
		} catch (IOException e) {
			System.err.println("Error loading problem instance: " + e.getMessage());
		} finally {
			System.out.println();
		}
	}
	
}
