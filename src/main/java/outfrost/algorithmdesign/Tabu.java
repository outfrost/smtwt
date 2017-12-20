package outfrost.algorithmdesign;

import java.io.Serializable;

public class Tabu implements Serializable {
	
	private Job job1;
	private Job job2;
	
	public Tabu() {
	
	}
	
	public boolean equals(Tabu other) {
		return (this == other || (other != null && job1 == other.getJob1() && job2 == other.getJob2()));
	}
	
	public Tabu(Job job1, Job job2) {
		setJob1(job1);
		setJob2(job2);
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
