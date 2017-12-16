package outfrost.algorithmdesign;

public class Job implements java.io.Serializable {
	
	private int processingTime = 0;
	private int dueTime = 0;
	private int weight = 0;
	
	public Job() { }
	
	public Job(int processingTime, int dueTime, int weight) {
		setProcessingTime(processingTime);
		setDueTime(dueTime);
		setWeight(weight);
	}
	
	public int weightedTardiness(int completionTime) {
		return Math.max(0, completionTime - dueTime) * weight;
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
		return "Job@" + System.identityHashCode(this) + "{ processingTime=" + processingTime + "; dueTime=" + dueTime + "; weight=" + weight + "; }";
	}
	
}
