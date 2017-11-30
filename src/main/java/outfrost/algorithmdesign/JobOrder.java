package outfrost.algorithmdesign;

import java.util.ArrayList;
import java.util.Collection;

public class JobOrder extends ArrayList<Job> {
	
	public JobOrder() {
		super();
	}
	
	public JobOrder(Collection<? extends Job> c) {
		super(c);
	}
	
	public JobOrder(int initialCapacity) {
		super(initialCapacity);
	}
	
	public int totalWeightedTardiness() {
		int result = 0;
		int moment = 0;
		for (Job job : this) {
			moment += job.getProcessingTime();
			result += job.weightedTardiness(moment);
		}
		return result;
	}
	
	public long longHash() {
	
	}
	
}
