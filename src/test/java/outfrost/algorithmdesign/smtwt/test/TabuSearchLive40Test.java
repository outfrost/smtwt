package outfrost.algorithmdesign.smtwt.test;

import outfrost.algorithmdesign.smtwt.JobOrder;
import outfrost.algorithmdesign.smtwt.util.OrlibLoader;
import outfrost.algorithmdesign.smtwt.tabusearch.TabuSearch;

import java.io.FileInputStream;
import java.io.IOException;

public class TabuSearchLive40Test extends Test {
	
	@Override
	public void run() {
		System.out.println("Running live data test with tabu search...");
		System.out.println("40-job ORLib instance # " + (DynamicSearchLive40Test.instanceIndex + 1));
		
		JobOrder jobs = null;
		try {
			jobs = OrlibLoader.load(new FileInputStream("data/orlib/wt40.txt"), 125, DynamicSearchLive40Test.instanceIndex);
			
			TabuSearch tabuSearch = new TabuSearch(jobs);
			
			System.out.println("Initial sequence:");
			System.out.println(jobs.toString());
			
			tabuSearch.sort();
			
			System.out.println("After initial heuristic sort:");
			System.out.println(jobs.toString());
			
			long startTime = System.nanoTime();
			
			tabuSearch.findSolution();
			
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
