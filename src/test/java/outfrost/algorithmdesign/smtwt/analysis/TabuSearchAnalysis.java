package outfrost.algorithmdesign.smtwt.analysis;

import outfrost.algorithmdesign.smtwt.tabusearch.TabuSearch;
import outfrost.algorithmdesign.smtwt.JobOrder;
import outfrost.algorithmdesign.smtwt.util.OrlibLoader;
import outfrost.algorithmdesign.smtwt.util.SmallwstLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.function.BiConsumer;

public class TabuSearchAnalysis {
	
	private static final int passes = 125;
	private static final int warmupPasses = 5;
	private static final int orlibInstancesPerFile = 125;
	
	private static final boolean verbose = false;
	
	public static void main(String[] args) {
		
		String[] smallwstPaths = {
				"data/smallwst/data10.txt",
				"data/smallwst/data11.txt",
				"data/smallwst/data12.txt",
				"data/smallwst/data13.txt",
				"data/smallwst/data14.txt",
				"data/smallwst/data15.txt",
				"data/smallwst/data16.txt",
				"data/smallwst/data17.txt",
				"data/smallwst/data18.txt",
				"data/smallwst/data19.txt",
				"data/smallwst/data20.txt"
		};
		
		String orlibPath = "data/orlib/wt40.txt";
		
		Random r = new Random();
		
		int[] orlibInstanceIndices = new int[orlibInstancesPerFile];
		for (int i = 0; i < orlibInstanceIndices.length; i++) {
			orlibInstanceIndices[i] = i;
		}
		
		int errorCount = 0;
		
		long overallStartTime = System.nanoTime();
		
		LinkedHashMap<Integer, Long> instanceSizeMeanTimes = new LinkedHashMap<>();
		LinkedHashMap<Integer, Long> passTimes = new LinkedHashMap<>(passes);
		
		BiConsumer<Integer, JobOrder> runAndBenchmark = (pass, jobs) -> {
			long startTime, timeTaken;
			startTime = System.nanoTime();
			
			TabuSearch.sort(jobs);
			jobs = TabuSearch.findSolution(jobs);
			
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
			
			// ORLib
			int index = orlibInstanceIndices[pass];
			
			if (verbose) System.out.println("Loading ORLib instance # " + (index + 1)
			                                + " from " + orlibPath + " ...");
			try {
				JobOrder jobs = OrlibLoader.load(orlibPath, orlibInstancesPerFile, index);
				if (verbose) System.out.println("Read " + jobs.size() + " jobs.");
				
				runAndBenchmark.accept(pass, jobs);
				
			} catch (IOException | NumberFormatException e) {
				if (verbose) System.err.println("Error loading problem instance: "
				                                + e.getMessage());
				errorCount++;
			}
			if (verbose) System.out.println();
			
		}
		
		long overallTimeTaken = System.nanoTime() - overallStartTime;
		
		System.out.println("Completed "
		                   + passes + " passes of tabu search on "
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
		
		System.out.println("Total time spent in tabu search each pass");
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
