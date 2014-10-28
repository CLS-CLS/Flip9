package cls.lytsiware.common.search.breadth;

public interface Condition<T> {
	
	
	boolean isMet(T current);
	

}
