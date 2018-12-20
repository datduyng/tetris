import java.util.ArrayList;
import java.util.Arrays;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class Shape {
	int id;
	int dx, dy; 
	ArrayList<int[]> coordinate = new ArrayList<int[]>(); // 0: x. 1: y
	
	final static int[][][] shapes =  {
		  { { 0, 1 }, 
	        { 0, 1 },
	        { 1, 1 }},
			 
		  { { 1, 1 }, 
		    { 1, 1 } },
		      
	      { { 1, 0 }, 
		    { 1, 0 },
		    { 1, 1 } },
	      
	      { { 1, 0 }, 
			{ 1, 1 },
			{ 0, 1 } },
	      
	      { { 0, 1, 0 }, 
		    { 1, 1, 1 } },
			
	      { { 1 }, 
			{ 1 },
	      	{ 1 }, 
			{ 1 }}
	};
	
	// there should be 7 shape
//	ArrayList<int[][]> shapes = new ArrayList<int[][]>();
	
	public Shape(int id) {
		this.id = id; 
		this.dx = 0; 
		this.dy = 1;
		genShapeCoor(this.id);
	}
	
	public void genShapeCoor(int id) {
		int[][] currShape = shapes[id];
		for (int y = 0; y < currShape.length; y++) {
			for (int x = 0; x < currShape[0].length; x++) {
				// Set the offset according to board size
				if(currShape[y][x] == 1) {
					int[] tmp = new int[] { x + Board.pixelWidth/2, y };
					coordinate.add(tmp);
				}
			}//end x
				
		}// ednd y
	}
	
	
}
