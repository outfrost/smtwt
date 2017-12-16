package outfrost.algorithmdesign;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;

public class JobOrder extends LinkedList<Job> {
	
	public JobOrder() {
		super();
	}
	
	public JobOrder(Collection<? extends Job> c) {
		super(c);
	}
	/*
	public JobOrder(int initialCapacity) {
		super(initialCapacity);
	}
	*/
	public void move(int fromIndex, int toIndex) {
		Job job = remove(fromIndex);
		add(toIndex, job);
	}
	
	public int totalWeightedTardiness() {
		return totalWeightedTardiness(size());
	}
	
	public int totalWeightedTardiness(int subsequenceLength) {
		int result = 0;
		int moment = 0;
		ListIterator<Job> iterator = listIterator();
		for (int i = 0; i < subsequenceLength; i++) {
			Job job = iterator.next();
			moment += job.getProcessingTime();
			result += job.weightedTardiness(moment);
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "JobOrder@" + System.identityHashCode(this) + "{ totalWeightedTardiness()=" + totalWeightedTardiness() + "; " + super.toString() + " }";
	}
	
}
