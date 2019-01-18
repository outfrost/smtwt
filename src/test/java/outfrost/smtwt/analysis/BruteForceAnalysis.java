package outfrost.smtwt.analysis;

import outfrost.smtwt.JobOrder;
import outfrost.smtwt.bruteforce.BruteForce;
import outfrost.smtwt.util.SmallwstLoader;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class BruteForceAnalysis {
	
	private static final int passes = 15;
	private static final int warmupPasses = 5;
	
	private static final boolean verbose = false;
	
	public static void main(String[] args) {
		
		String[] smallwstPaths = {
				"data/smallwst/data10.txt",
				"data/smallwst/data11.txt",
				/*"data/smallwst/data12.txt",
				"data/smallwst/data13.txt",
				"data/smallwst/data14.txt",
				"data/smallwst/data15.txt",
				"data/smallwst/data16.txt",
				"data/smallwst/data17.txt",
				"data/smallwst/data18.txt",
				"data/smallwst/data19.txt",
				"data/smallwst/data20.txt"*/
		};
		
		int errorCount = 0;
		
		long overallStartTime = System.nanoTime();
		
		LinkedHashMap<Integer, Long> instanceSizeMeanTimes = new LinkedHashMap<>();
		LinkedHashMap<Integer, Long> passTimes = new LinkedHashMap<>(passes);
		
		BiConsumer<Integer, JobOrder> runAndBenchmark = (pass, jobs) -> {
			long startTime, timeTaken;
			startTime = System.nanoTime();
			
			BruteForce.findSolution(jobs);
			
			timeTaken = System.nanoTime() - startTime;
			
			if (verbose) {
				System.out.println("Reached total weighted tardiness of "
				                   + jobs.totalWeightedTardiness()
				                   + " after " + timeTaken + " ns.");
				
				System.out.println("Solution:");
				for (int i = 0; i < jobs.size(); i++) {
					System.out.print(jobs.get(i).getId());
					if (i < jobs.size() - 1) {
						System.out.print(", ");
					}
				}
				System.out.println();
			}
			
			if (pass < warmupPasses) {
				return;
			}
			
			passTimes.compute(pass,
					(k, v) -> (v == null)
					          ? timeTaken
					          : v + timeTaken);
			instanceSizeMeanTimes.compute(jobs.size(),
					(k, v) -> (v == null)
					          ? timeTaken
					          : (v * pass + timeTaken) / (pass + 1));
		};
		
		for (int pass = 0; pass < passes; pass++) {
			
			if (verbose) {
				System.out.println();
				System.out.println("Pass # " + pass);
				System.out.println();
			}
			
			// smallWST
			for (String path : smallwstPaths) {
				if (verbose) System.out.println("Loading smallWST instance from " + path + " ...");
				try {
					JobOrder jobs = SmallwstLoader.load(path);
					if (verbose) System.out.println("Read " + jobs.size() + " jobs.");
					
					runAndBenchmark.accept(pass, jobs);
					
				} catch (IOException | NumberFormatException e) {
					if (verbose) System.err.println("Error loading problem instance: "
					                                + e.getMessage());
					errorCount++;
				}
				if (verbose) System.out.println();
				
			}
			
		}
		
		long overallTimeTaken = System.nanoTime() - overallStartTime;
		
		System.out.println("Completed "
		                   + passes + " passes of brute force on "
		                   + (smallwstPaths.length + 1)
		                   + " instance sizes");
		System.out.println("( " + errorCount + " errors )");
		System.out.println();
		
		System.out.println("Statistics:");
		System.out.println();
		
		System.out.println("Average search time per instance size");
		instanceSizeMeanTimes.forEach((size, meanTime) -> {
			System.out.println(size + ": " + meanTime + " ns");
		});
		System.out.println();
		
		System.out.println("Total time spent in brute force each pass");
		passTimes.forEach((pass, time) -> {
			System.out.println(pass + ": " + time + " ns");
		});
		System.out.println();
		
		System.out.println("Total analysis time: " + overallTimeTaken + " ns");
		long sumPassTimes = 0L;
		for (long time : passTimes.values()) {
			sumPassTimes += time;
		}
		System.out.println("IO and statistics overhead: " + (overallTimeTaken - sumPassTimes) + " ns");
	}
	
}
