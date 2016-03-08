package utilities;

public class Change<T> {	
	private T element;
	private ChangeType change;
	
	public Change(T element, ChangeType change) {
		this.element = element;
		this.change = change;
	}
	
	public T getElement() {
		return element;
	}
	
	public boolean wasAdded() {
		return change == ChangeType.ADDED;
	}
	public boolean wasRemoved() {
		return change == ChangeType.REMOVED;
	}
	public boolean wasChanged() {
		return change == ChangeType.CHANGED;
	}
}
