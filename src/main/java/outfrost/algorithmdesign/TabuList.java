package outfrost.algorithmdesign;

import java.util.LinkedHashSet;

public class TabuList extends LinkedHashSet<Tabu> {
	
	private final int capacityLimit;
	
	public TabuList(int capacityLimit) {
		super(capacityLimit);
		this.capacityLimit = capacityLimit;
	}
	
	public TabuList() {
		super(16);
		capacityLimit = 16;
	}
	
	@Override
	public boolean add(Tabu tabu) {
		while (size() >= capacityLimit) {
		
		}
		return super.add(tabu);
	}
}
