package outfrost.algorithmdesign;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

public class DynamicSearchLive40Test extends Test {
	
	public static int instanceIndex = new Random().nextInt() % 125;
	
	@Override
	public void run() {
		System.out.println("Running live data test with dynamic search...");
		System.out.println("40-job instance # " + (instanceIndex + 1));
		
		JobOrder jobs = null;
		try {
			jobs = JobOrderLoader.load(new FileInputStream("wt40.txt"), 125, instanceIndex);
			
			System.out.println("Initial sequence:");
			System.out.println(jobs.toString());
			
			DynamicSearch.sort(jobs);
			
			System.out.println("After initial heuristic sort:");
			System.out.println(jobs.toString());
			
			DynamicSearch.findSolution(jobs);
			
			System.out.println("Solution:");
			System.out.println(jobs.toString());
		} catch (IOException e) {
			System.err.println("Error loading problem instance: " + e.getMessage());
		} finally {
			System.out.println();
		}
	}
	
}
