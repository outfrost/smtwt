package outfrost.algorithmdesign.smtwt.analysis;

import outfrost.algorithmdesign.smtwt.tabusearch.TabuSearch;
import outfrost.algorithmdesign.smtwt.JobOrder;
import outfrost.algorithmdesign.smtwt.util.OrlibLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

public class TabuSearchAnalysis implements Runnable {
	
	private final static int passes = 5;
	
	private String dataPath;
	private int instanceCount;
	private int jobCount;
	
	public TabuSearchAnalysis(String path, int instanceCount, int jobCount) {
		dataPath = path;
		this.instanceCount = instanceCount;
		this.jobCount = jobCount;
	}
	
	@Override
	public void run() {
		int errorCount = 0;
		
		long meanTimeTaken = 0;
		int timeSamples = 0;
		LinkedHashMap<Integer, Long> instanceMeanTimes = new LinkedHashMap<>(instanceCount);
		LinkedHashMap<Integer, Integer> instancePassCounts = new LinkedHashMap<>(instanceCount);
		LinkedHashMap<Integer, Long> passMeanTimes = new LinkedHashMap<>(passes);
		
		for (int pass = 0; pass < passes; pass++) {
			long passMeanTime = -1;
			
			for (int i = 0; i < instanceCount; i++) {
				System.out.println("Loading problem instance # " + (i + 1) + " ...");
				
				try {
					JobOrder jobs = OrlibLoader.load(new FileInputStream(dataPath), instanceCount, i);
					
					if (jobs.size() == jobCount) {
						System.out.println("Read " + jobs.size() + " jobs.");
						
						TabuSearch tabuSearch = new TabuSearch(jobs);
						
						long startTime = System.nanoTime();
						
						tabuSearch.sort();
						tabuSearch.findSolution();
						
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
		
		System.out.println("Tabu search execution on " + instanceCount + " instances of " + jobCount + " jobs in " + dataPath + " finished.");
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
	
	public String getDataPath() {
		return dataPath;
	}
	
	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}
	
	public int getInstanceCount() {
		return instanceCount;
	}
	
	public void setInstanceCount(int instanceCount) {
		this.instanceCount = instanceCount;
	}
	
	public int getJobCount() {
		return jobCount;
	}
	
	public void setJobCount(int jobCount) {
		this.jobCount = jobCount;
	}
	
	public static void main(String[] args) {
		new TabuSearchAnalysis("data/wt40.txt", 125, 40).run();
	}
	
}
