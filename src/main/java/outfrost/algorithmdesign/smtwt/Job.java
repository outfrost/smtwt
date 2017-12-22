package outfrost.algorithmdesign.smtwt;

public class Job implements java.io.Serializable {
	
	private int id = 0;
	
	private int processingTime = 0;
	private int dueTime = 0;
	private int weight = 0;
	
	public Job() { }
	
	public Job(int id, int processingTime, int dueTime, int weight) {
		setId(id);
		setProcessingTime(processingTime);
		setDueTime(dueTime);
		setWeight(weight);
	}
	
	public int weightedTardiness(int completionTime) {
		return Math.max(0, completionTime - dueTime) * weight;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getProcessingTime() {
		return processingTime;
	}
	
	public void setProcessingTime(int processingTime) {
		this.processingTime = processingTime;
	}
	
	public int getDueTime() {
		return dueTime;
	}
	
	public void setDueTime(int dueTime) {
		this.dueTime = dueTime;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	@Override
	public String toString() {
		return "Job@" + System.identityHashCode(this) + "{ id=" + id + "; processingTime=" + processingTime + "; dueTime=" + dueTime + "; weight=" + weight + "; }";
	}
	
}
