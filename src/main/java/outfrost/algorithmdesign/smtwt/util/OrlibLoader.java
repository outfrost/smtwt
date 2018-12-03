package outfrost.algorithmdesign.smtwt.util;

import outfrost.algorithmdesign.smtwt.Job;
import outfrost.algorithmdesign.smtwt.JobOrder;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class OrlibLoader {
	
	public static JobOrder load(int instances, int instanceIndex) throws IOException, NumberFormatException {
		return load(System.in, instances, instanceIndex);
	}
	
	public static JobOrder load(String filePath, int instances, int instanceIndex) throws IOException, NumberFormatException {
		return load(new FileInputStream(filePath), instances, instanceIndex);
	}
	
	public static JobOrder load(InputStream stream, int instances, int instanceIndex) throws IOException, NumberFormatException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		ArrayList<String> lines = new ArrayList<>();
		while (reader.ready()) {
			lines.add(reader.readLine());
		}
		
		reader.close();
		
		ArrayList<String> processingTimes = new ArrayList<>();
		ArrayList<String> weights = new ArrayList<>();
		ArrayList<String> dueTimes = new ArrayList<>();
		
		int instanceLineCount = lines.size() / instances;
		
		for (int i = instanceLineCount * instanceIndex; i < (instanceLineCount * instanceIndex) + (instanceLineCount / 3); i++) {
			processingTimes.addAll(Arrays.asList(lines.get(i).split("( )+")));
			weights.addAll(Arrays.asList(lines.get(i + (instanceLineCount / 3)).split("( )+")));
			dueTimes.addAll(Arrays.asList(lines.get(i + (instanceLineCount / 3) * 2).split("( )+")));
		}
		
		processingTimes.removeIf(s -> s.equals(""));
		weights.removeIf(s -> s.equals(""));
		dueTimes.removeIf(s -> s.equals(""));
		
		if (processingTimes.size() != weights.size() || processingTimes.size() != dueTimes.size()) {
			throw new IOException("The numbers of entries for processing times, weights and/or due times did not match up.");
		}
		
		JobOrder result = new JobOrder();
		
		for (int i = 0; i < processingTimes.size(); i++) {
			try {
				Job job = new Job();
				job.setId(i + 1);
				job.setProcessingTime(Integer.parseInt(processingTimes.get(i)));
				job.setWeight(Integer.parseInt(weights.get(i)));
				job.setDueTime(Integer.parseInt(dueTimes.get(i)));
				result.add(job);
			} catch (NumberFormatException e) {
				System.err.println(e.getClass().getName()
				                   + " in ORLib instance file");
				System.err.println("on line " + (i + 3));
				throw e;
			}
		}
		
		return result;
	}
	
}
