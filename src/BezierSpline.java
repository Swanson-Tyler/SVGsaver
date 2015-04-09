import java.util.ArrayList;


public class BezierSpline {
	
	ArrayList<EdgeVertex> knots;
	
	public BezierSpline(ArrayList<EdgeVertex> knots){
		this.knots = knots;
	}
	
	public Object[] GetCurveControlPoints()
	{
		
		if (knots == null){
		
		}
		int n = knots.size() - 1;
		if (n < 1){
			
		}
			
		if (n == 1)
		{ // Special case: Bezier curve should be a straight line.
			EdgeVertex[] firstControlPoints = new EdgeVertex[1];
			// 3P1 = 2P0 + P3
			firstControlPoints[0].x = (2 * knots.get(0).x + knots.get(1).x) / 3;
			firstControlPoints[0].y = (2 * knots.get(0).y + knots.get(1).y) / 3;

			EdgeVertex[] secondControlPoints = new EdgeVertex[1];
			// P2 = 2P1 – P0
			secondControlPoints[0].x = 2 *
				firstControlPoints[0].x - knots.get(0).x;
			secondControlPoints[0].y = 2 *
				firstControlPoints[0].y - knots.get(0).y;
			return new Object[]{firstControlPoints, secondControlPoints};
		}

		// Calculate first Bezier control points
		// Right hand side vector
		float[] rhs = new float[n];

		// Set right hand side X values
		for (int i = 1; i < n - 1; ++i)
			rhs[i] = 4.0f * knots.get(i).x + 2.0f * knots.get(i+1).x;
		rhs[0] = knots.get(0).x + 2 * knots.get(1).x;
		rhs[n - 1] = (8.0f * knots.get(n-1).x + knots.get(n).x) / 2.0f;
		// Get first control points X-values
		float[] x = GetFirstControlPoints(rhs);

		// Set right hand side Y values
		for (int i = 1; i < n - 1; ++i)
			rhs[i] = 4.0f * knots.get(i).y + 2.0f * knots.get(i + 1).y;
		rhs[0] = knots.get(0).y + 2.0f * knots.get(1).y;
		rhs[n - 1] = (8.0f * knots.get(n-1).y + knots.get(n).y) / 2.0f;
		// Get first control points Y-values
		float[] y = GetFirstControlPoints(rhs);

		// Fill output arrays.
		EdgeVertex[] firstControlPoints = new EdgeVertex[n];
		EdgeVertex[] secondControlPoints = new EdgeVertex[n];
		for (int i = 0; i < n; ++i)
		{
			// First control point
			firstControlPoints[i] = new EdgeVertex(x[i], y[i]);
			// Second control point
			if (i < n - 1)
				secondControlPoints[i] = new EdgeVertex(2.0f * knots.get(i + 1).x - x[i + 1], 2.0f *
						knots.get(i+1).y - y[i + 1]);
			else
				secondControlPoints[i] = new EdgeVertex((knots.get(n).x + x[n - 1]) / 2.0f,
					(knots.get(n).y + y[n - 1]) / 2.0f);
		}
		return new Object[]{firstControlPoints, secondControlPoints};
	}


	private static float[] GetFirstControlPoints(float[] rhs)
	{
		int n = rhs.length;
		float[] x = new float[n]; // Solution vector.
		float[] tmp = new float[n]; // Temp workspace.

		float b = 2.0f;
		x[0] = rhs[0] / b;
		for (int i = 1; i < n; i++) // Decomposition and forward substitution.
		{
			tmp[i] = 1 / b;
			b = (i < n - 1 ? 4.0f : 3.5f) - tmp[i];
			x[i] = (rhs[i] - x[i - 1]) / b;
		}
		for (int i = 1; i < n; i++)
			x[n - i - 1] -= tmp[n - i] * x[n - i]; // Backsubstitution.

		return x;
	}
}