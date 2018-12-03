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
		
		JobOrder result = new JobOrder();
		if (lines.size() < 2) {
			throw new EOFException("Not enough lines to parse file");
		}
		
		String instanceName = lines.get(0);
		System.out.println("Instance \"" + instanceName + "\"");
		
		int jobCount;
		try {
			jobCount = Integer.parseInt(lines.get(1));
		} catch (NumberFormatException e) {
			System.err.println(e.getClass().getName()
			                   + " in \"" + instanceName + "\" on line 2");
			throw e;
		}
		
		if (lines.size() < 2 + jobCount) {
			throw new EOFException("Not enough lines for the expected number of jobs");
		}
		
		for (int i = 0; i < jobCount; i++) {
			try {
				Job job = new Job();
				job.setId(i + 1);
				String[] jobProperties = lines.get(i + 2).split(" +");
				job.setProcessingTime(Integer.parseInt(jobProperties[0]));
				job.setWeight(Integer.parseInt(jobProperties[1]));
				job.setDueTime(Integer.parseInt(jobProperties[2]));
				result.add(job);
			} catch (NumberFormatException e) {
				System.err.println(e.getClass().getName()
				                   + " in \"" + instanceName
				                   + "\" on line " + (i + 3));
				throw e;
			}
		}
		
		return result;
	}
	
}
