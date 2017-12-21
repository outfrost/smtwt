package outfrost.algorithmdesign;

import java.io.Serializable;

public class Tabu implements Serializable {
	
	private Job job1;
	private Job job2;
	
	public Tabu() {
	
	}
	
	public Tabu(Job job1, Job job2) {
		setJob1(job1);
		setJob2(job2);
	}
	
	@Override
	public int hashCode() {
		int job1HashCode = job1.hashCode();
		int job2HashCode = job2.hashCode();
		return (((job1HashCode >>> 16) - (job1HashCode & 0x0000FFFF)) << 16) |
			       ((job2HashCode >>> 16) - (job2HashCode & 0x0000FFFF));
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Tabu && equals((Tabu) obj);
	}
	
	public boolean equals(Tabu other) {
		return (this == other || (other != null && job1 == other.getJob1() && job2 == other.getJob2()));
	}
	
	public Job getJob1() {
		return job1;
	}
	
	public void setJob1(Job job1) {
		this.job1 = job1;
	}
	
	public Job getJob2() {
		return job2;
	}
	
	public void setJob2(Job job2) {
		this.job2 = job2;
	}
	
}
