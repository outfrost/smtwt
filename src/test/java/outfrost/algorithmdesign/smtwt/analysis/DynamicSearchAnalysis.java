package outfrost.algorithmdesign.smtwt.analysis;

import outfrost.algorithmdesign.smtwt.dynasearch.DynamicSearch;
import outfrost.algorithmdesign.smtwt.JobOrder;
import outfrost.algorithmdesign.smtwt.util.OrlibLoader;
import outfrost.algorithmdesign.smtwt.util.SmallwstLoader;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;

public class DynamicSearchAnalysis {
	
	private static final int passes = 25;
	private static final int warmupPasses = 5;
	private static final int orlibInstancesPerFile = 125;
	
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
		int[] orlibInstanceIndices = {
				// Has to contain at least `passes` items
				// in the range `[0; orlibInstancesPerFile)`
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile),
				r.nextInt(orlibInstancesPerFile)
		};
		
		int errorCount = 0;
		
		long overallStartTime = System.nanoTime();
		
		LinkedHashMap<Integer, Long> instanceSizeMeanTimes = new LinkedHashMap<>();
		LinkedHashMap<Integer, Long> passTimes = new LinkedHashMap<>(passes);
		
		BiConsumer<Integer, JobOrder> runAndBenchmark = (pass, jobs) -> {
			long startTime, timeTaken;
			startTime = System.nanoTime();
			
			DynamicSearch.sort(jobs);
			DynamicSearch.findSolution(jobs);
			
			timeTaken = System.nanoTime() - startTime;
			
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
			
			// smallWST
			for (String path : smallwstPaths) {
				System.out.println("Loading smallWST instance from " + path + " ...");
				try {
					JobOrder jobs = SmallwstLoader.load(path);
					System.out.println("Read " + jobs.size() + " jobs.");
					
					runAndBenchmark.accept(pass, jobs);
					
				} catch (IOException | NumberFormatException e) {
					System.err.println("Error loading problem instance: "
					                   + e.getMessage());
					errorCount++;
				}
				System.out.println();
				
			}
			
			// ORLib
			int index = orlibInstanceIndices[pass];
			
			System.out.println("Loading ORLib instance # " + (index + 1)
			                   + " from " + orlibPath + " ...");
			try {
				JobOrder jobs = OrlibLoader.load(orlibPath, orlibInstancesPerFile, index);
				System.out.println("Read " + jobs.size() + " jobs.");
				
				runAndBenchmark.accept(pass, jobs);
				
			} catch (IOException | NumberFormatException e) {
				System.err.println("Error loading problem instance: "
				                   + e.getMessage());
				errorCount++;
			}
			System.out.println();
			
		}
		
		long overallTimeTaken = System.nanoTime() - overallStartTime;
		
		System.out.println("Completed "
		                   + passes + " passes of dynamic search on "
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
		
		System.out.println("Total time spent in dynamic search each pass");
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
