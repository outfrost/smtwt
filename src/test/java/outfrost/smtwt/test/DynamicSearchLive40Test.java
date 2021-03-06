package outfrost.smtwt.test;

import outfrost.smtwt.dynasearch.DynamicSearch;
import outfrost.smtwt.JobOrder;
import outfrost.smtwt.util.OrlibLoader;

import java.io.IOException;
import java.util.Random;

public class DynamicSearchLive40Test extends Test {
	
	public static int instanceIndex = Math.abs(new Random().nextInt() % 125);
	
	@Override
	public void run() {
		System.out.println("Running live data test with dynamic search...");
		System.out.println("40-job ORLib instance # " + (instanceIndex + 1));
		
		try {
			JobOrder jobs = OrlibLoader.load("data/orlib/wt40.txt", 125, instanceIndex);
			
			System.out.println("Initial sequence:");
			System.out.println(jobs.toString());
			
			DynamicSearch.sort(jobs);
			
			System.out.println("After initial heuristic sort:");
			System.out.println(jobs.toString());
			
			long startTime = System.nanoTime();
			
			DynamicSearch.findSolution(jobs);
			
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
