package outfrost.algorithmdesign.smtwt.tabusearch;

import java.util.LinkedList;

public class TabuList extends LinkedList<Tabu> {
	
	private final int capacity;
	
	public TabuList(int capacity) {
		super();
		this.capacity = capacity;
	}
	
	@Override
	public boolean contains(Object o) {
		if (o instanceof Tabu) {
			return contains((Tabu) o);
		}
		return false;
	}
	
	public boolean contains(Tabu value) {
		if (value == null){
			return false;
		}
		for (Tabu tabu : this) {
			if (tabu.equals(value)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean add(Tabu tabu) {
		if (tabu != null && !contains(tabu)) {
			while (size() >= capacity) {
				remove(); // removes first
			}
			return super.add(tabu);
		}
		return false;
	}
}
