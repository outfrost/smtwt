package outfrost.algorithmdesign;

public class JobOrderCacheItem {
	
	private final JobOrder jobOrder;
	private final int totalWeightedTardiness;
	
	public JobOrderCacheItem(JobOrder jobOrder) {
		this.jobOrder = jobOrder;
		totalWeightedTardiness = jobOrder.totalWeightedTardiness();
	}
	
	public JobOrder getJobOrder() {
		return jobOrder;
	}
	
	public int getTotalWeightedTardiness() {
		return totalWeightedTardiness;
	}
	
}
