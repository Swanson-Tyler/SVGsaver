
public class Triangle {
	EdgeVertex a,b,c;
	float area;
	float width,height;
	public Triangle(EdgeVertex a, EdgeVertex b, EdgeVertex c, int w, int h){
		this.a = a;
		this.b = b;
		this.c = c;
		this.width = (float) w;
		this.height = (float)  h;
		area = calculateTriangleArea(a,b,c);
	}
	
    //Calculates the area of a triangle given 3 points
	public float calculateTriangleArea(EdgeVertex a, EdgeVertex b, EdgeVertex c){
		float xA = a.x * width;
		float yA = a.y * height;
		float xB = b.x * width;
		float yB = b.y * height;
		float xC = c.x * width;
		float yC = c.y * height;
		
		return   .5f * Math.abs( ((xA - xC) * (yB - yA)) - ((xA - xB)* (yC - yA))   ); 
		
	}
}
