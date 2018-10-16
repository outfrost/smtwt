package outfrost.algorithmdesign.smtwt.util;

import outfrost.algorithmdesign.smtwt.Job;
import outfrost.algorithmdesign.smtwt.JobOrder;

import java.io.*;
import java.util.ArrayList;

public class SmallwstLoader {
	
	public static JobOrder load() throws IOException, NumberFormatException {
		return load(System.in);
	}
	
	public static JobOrder load(String filePath) throws IOException, NumberFormatException {
		return load(new FileInputStream(filePath));
	}
	
	public static JobOrder load(InputStream stream) throws IOException, NumberFormatException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		ArrayList<String> lines = new ArrayList<>();
		while (reader.ready()) {
			lines.add(reader.readLine());
		}
		
		reader.close();
		
		System.out.println("Instance \"" + lines.get(0) + "\"");
		
		JobOrder result = new JobOrder();
		int jobCount = Integer.parseInt(lines.get(1));
		
		for (int i = 0; i < jobCount; i++) {
			Job job = new Job();
			job.setId(i + 1);
			String[] jobProperties = lines.get(i + 2).split(" +");
			job.setProcessingTime(Integer.parseInt(jobProperties[0]));
			job.setWeight(Integer.parseInt(jobProperties[1]));
			job.setDueTime(Integer.parseInt(jobProperties[2]));
			result.add(job);
		}
		
		return result;
	}
	
}
