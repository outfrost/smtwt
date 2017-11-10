package outfrost.algorithmdesign;

import java.util.ArrayList;

public class JobOrder extends ArrayList<Job> {

	public int totalWeightedTardiness() {
		int result = 0;
		int moment = 0;
		for (Job job : this) {
			moment += job.getProcessingTime();
			result += job.weightedTardiness(moment);
		}
		return result;
	}

}
