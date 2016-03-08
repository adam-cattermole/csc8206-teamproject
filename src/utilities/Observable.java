package utilities;

public interface Observable<T> {
	public void addChangeListener(ChangeListener<T> listener);
	public void removeChangeListener(ChangeListener<T> listener);
}
