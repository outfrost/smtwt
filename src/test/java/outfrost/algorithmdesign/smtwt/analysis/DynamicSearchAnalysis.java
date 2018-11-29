package outfrost.algorithmdesign.smtwt.analysis;

import outfrost.algorithmdesign.smtwt.dynasearch.DynamicSearch;
import outfrost.algorithmdesign.smtwt.JobOrder;
import outfrost.algorithmdesign.smtwt.util.OrlibLoader;
import outfrost.algorithmdesign.smtwt.util.SmallwstLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class DynamicSearchAnalysis {
	
	private static final int passes = 5;
	//private final LinkedList<JobOrder> instances = new LinkedList<>();
	
	public static void main(String[] args) {
		
		List<String> smallwstPaths = Arrays.asList(
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
		);
		
		String orlibPath = "data/orlib/wt40txt";
		
		int errorCount = 0;
		
		long meanTimeTaken = 0;
		int timeSamples = 0;
		LinkedHashMap<Integer, Long> instanceSizeMeanTimes = new LinkedHashMap<>();
		LinkedHashMap<Integer, Long> passMeanTimes = new LinkedHashMap<>(passes);
		
		for (int pass = 0; pass < passes; pass++) {
			long passMeanTime = -1;
			
			for (int i = 0; i < instanceCount; i++) {
				System.out.println("Loading problem instance # " + (i + 1) + " ...");
				
				try {
					JobOrder jobs = OrlibLoader.load(new FileInputStream(dataPath), instanceCount, i);
					
					if (jobs.size() == jobCount) {
						System.out.println("Read " + jobs.size() + " jobs.");
						
						long startTime = System.nanoTime();
						
						DynamicSearch.sort(jobs);
						DynamicSearch.findSolution(jobs);
						
						long timeTaken = System.nanoTime() - startTime;
						
						System.out.println("Reached total weighted tardiness of " + jobs.totalWeightedTardiness() + " after " + timeTaken + " ns.");
						
						System.out.println("Solution:");
						for (int k = 0; k < jobs.size() - 1; k++) {
							System.out.print(jobs.get(k).getId() + ", ");
						}
						if (jobs.size() > 0) {
							System.out.print(jobs.get(jobs.size() - 1).getId());
						}
						
						System.out.println();
						
						if (timeSamples == 0) {
							meanTimeTaken = timeTaken;
						}
						else {
							meanTimeTaken = (meanTimeTaken * timeSamples + timeTaken) / (timeSamples + 1);
						}
						timeSamples++;
						
						if (passMeanTime == -1) {
							passMeanTime = timeTaken;
						}
						else {
							passMeanTime = (passMeanTime * pass + timeTaken) / (pass + 1);
						}
						
						int instancePassCount = instancePassCounts.getOrDefault(i, 0);
						instanceMeanTimes.compute(i, (instance, meanTime) -> (meanTime == null) ? timeTaken : (meanTime * instancePassCount + timeTaken) / (instancePassCount + 1));
						instancePassCounts.compute(i, (instance, passCount) -> (passCount == null) ? 1 : passCount++);
						
					} else {
						System.err.println("Instance size " + jobs.size() + " did not match the expected number of jobs.");
						errorCount++;
					}
				} catch (IOException e) {
					System.err.println("Error loading problem instance: " + e.getMessage());
					errorCount++;
				} finally {
					System.out.println();
				}
			}
			
			passMeanTimes.put(pass, passMeanTime);
		}
		
		System.out.println("Dynamic search execution on " + instanceCount + " instances of " + jobCount + " jobs in " + dataPath + " finished.");
		System.out.println("( " + errorCount + " errors )");
		
		System.out.println("Statistics:");
		System.out.println("Average search time in " + timeSamples + " samples was " + meanTimeTaken / 1000 + " µs.");
		System.out.println("Average search time by instance, in microseconds:");
		
		for (int i = 0; i < instanceCount; i++) {
			Long instanceMeanTime = instanceMeanTimes.get(i);
			System.out.print(i + " -> " + ((instanceMeanTime != null) ? (instanceMeanTime / 1000) : instanceMeanTime) + "; ");
			if (i % 10 == 9) {
				System.out.println();
			}
		}
		System.out.println();
		
		System.out.println("Average search time by pass, in microseconds:");
		
		for (int i = 0; i < passes; i++) {
			Long passMeanTime = passMeanTimes.get(i);
			System.out.print(i + " -> " + ((passMeanTime != null) ? (passMeanTime / 1000) : passMeanTime) + "; ");
			if (i % 10 == 9) {
				System.out.println();
			}
		}
		System.out.println();
	}
	
}
