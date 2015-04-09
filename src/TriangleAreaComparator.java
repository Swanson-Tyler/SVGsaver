import java.util.Comparator;


public class TriangleAreaComparator implements Comparator<Triangle> {

	@Override
	public int compare(Triangle o1, Triangle o2) {
		// TODO Auto-generated method stub
		if( o1.area >= o2.area){
			return 1;
		}else{
			return -1;
		}
	}

	

}
