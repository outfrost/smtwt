package outfrost.algorithmdesign.smtwt.tabusearch;

import outfrost.algorithmdesign.smtwt.Job;

import java.io.Serializable;

public class Tabu implements Serializable {
	
	public Job job1;
	public Job job2;
	
	public Tabu(Job job1, Job job2) {
		this.job1 = job1;
		this.job2 = job2;
	}
	/*
	@Override
	public int hashCode() {
		int job1HashCode = job1.hashCode();
		int job2HashCode = job2.hashCode();
		return (((job1HashCode >>> 16) - (job1HashCode & 0x0000FFFF)) << 16) |
			       ((job2HashCode >>> 16) - (job2HashCode & 0x0000FFFF));
	}
	*/
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Tabu && equals((Tabu) obj);
	}
	
	public boolean equals(Tabu other) {
		return (this == other
		        || (other != null
		            && ((job1 == other.job1 && job2 == other.job2)
		                || (job1 == other.job2 && job2 == other.job1))));
	}
	
}
