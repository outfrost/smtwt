package outfrost.smtwt;

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
		return totalWeightedTardiness(size());
	}
	
	public int totalWeightedTardiness(int subsequenceLength) {
		int result = 0;
		int moment = 0;
		for (int i = 0; i < subsequenceLength; i++) {
			moment += get(i).getProcessingTime();
			result += get(i).weightedTardiness(moment);
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "JobOrder@" + System.identityHashCode(this) + "{ totalWeightedTardiness()=" + totalWeightedTardiness() + "; " + super.toString() + " }";
	}
	
}
