import java.awt.Color;
import java.util.ArrayList;

public class Shape {
	int id;
	int rotateid;
	int dx, dy;
	Color color;
	ArrayList<int[]> coordinate = new ArrayList<int[]>(); // 0: x. 1: y 2: COM
	boolean generated = false;
	
	static Color[] colors = new Color[] {Color.YELLOW, Color.RED, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.GREEN, Color.GRAY};
	final static int[][][][] shapes =  {
				// Rervse L
				  { {{0, 1}, 
			         {0, 1},  
			         {1, 2}},
					  		   {{1, 0, 0}, 
			        	 	   {2, 1, 1}},
					 {{2, 1},
			          {1, 0},
			          {1, 0}},
					 			{{1, 1, 2},
			        	         {0, 0, 1}}
				  },
				 // Square
				  	  {{{1, 1}, 
					    {1, 1}}},
				// L
			  	 {  {{1, 0}, 
			         {1, 0},  
			         {2, 1}},
					  		  {{2, 1, 1}, 
			        	 	   {1, 0, 0}},
					 {{1, 2},
			          {0, 1},
			          {0, 1}},
					 			{{0, 0, 1},
			        	         {1, 1, 2}}
				  },
			  	 
			  	 {  {{0, 1}, 
			         {2, 1},  
			         {1, 0}},
					  		  {{1, 2, 0}, 
			        	 	   {0, 1, 1}},
					 {{0, 1},
			          {1, 2},
			          {1, 0}},
					 			{{1, 1, 0},
			        	         {0, 2, 1}}
				  },
			  	  { {{1, 0}, 
			         {2, 1},  
			         {0, 1}},
					  		   {{0, 2, 1}, 
			        	 	    {1, 1, 0}},
					 {{1, 0},
			          {1, 2},
			          {0, 1}},
					 			{{0, 1, 1},
			        	         {1, 2, 0}}
				  },
			  	 {   		  {{0, 1, 0}, 
			        	 	   {1, 2, 1}},
					 {{0, 1},
			          {1, 2},
			          {0, 1}},
					 			{{1, 2, 1},
			        	         {0, 1, 0}},
			          {{1, 0},
				       {2, 1},
				       {1, 0}}
			          },
			  	 {{{1},
				   {2},
				   {1},
				   {1}},
			        	  {{1, 2, 1, 1}}
				  }
	 	
				   };
					 


	// there should be 7 shape
	// ArrayList<int[][]> shapes = new ArrayList<int[][]>();

	public Shape(int id) {
		this.id = id;// change to id
		this.rotateid = 0;
		this.dx = 0;
		this.dy = 1;
		this.color = colors[this.id];
		genShapeCoor(this.id);
	}

	public boolean genShapeCoor(int id) {
		int[][] currShape = shapes[id][0];
		for (int y = 0; y < currShape.length; y++) {
			for (int x = 0; x < currShape[0].length; x++) {
				// Set the offset according to board size
				if (currShape[y][x] == 1) {
					int[] tmp = new int[] { x + Board.numW / 2, y, 0 };
					coordinate.add(tmp);
				} else if (currShape[y][x] == 2) {
					int[] tmp = new int[] { x + Board.numW / 2, y, 1 };
					coordinate.add(tmp);
				}
			} // end x

		} // end y
		for (int [] c : coordinate) {
			if (Board.M[c[1]][c[0]] != Color.BLACK) {
				generated = false;
				return generated;
			}
		}
		generated = true;
		return generated;
	}

	/*
	 * Algorithm.
	 * Matrix rotation formula : Mat dot product. 
	 * 		  -		
	 * [x'] = [cos(θ)  -sin(θ)][x]
	 * [y']   [sin(θ)   cos(θ)][y]  
	 * 
	 * Formula derivation:Rotating Points Using Rotation Matrices[patrickJMT]
	 *  https://www.youtube.com/watch?v=OYuoPTRVzxY
	 */           
	public ArrayList<int[]> deepcopy(ArrayList<int[]> arr) {
		ArrayList<int[]> copy = new ArrayList<int[]>();
		for (int [] c : arr) {
			int x = c[0];
			int y = c[1];
			int pivot = c[2];
			int [] copyPoint = new int [] {x, y, pivot};
			copy.add(copyPoint);
		}
		return copy;
		
	}
	
	public boolean rotate() {
		ArrayList<int[]> original = deepcopy(this.coordinate);
		int COMx = 0, COMy = 0;
		//find Center of mass coordinate x and y
		for(int[] coor : this.coordinate) 
			if(coor[2] == 1) {
				COMx = coor[0];
				COMy = coor[1];
				break;
			}
		
		if(COMx == 0 && COMy == 0) return false; // If there are no COM on object. then obj is symetric
		
		for(int[] coor: this.coordinate) {
			int shiftedx = coor[0] - COMx; 
			int shiftedy = coor[1] - COMy; 
			coor[0] = (int) (shiftedx*Math.round(Math.cos(Math.toRadians(90.0))) - shiftedy*Math.round(Math.sin(Math.toRadians(90.0)))) + COMx;
			coor[1] = (int) (shiftedx*Math.round(Math.sin(Math.toRadians(90.0))) - shiftedy*Math.round(Math.cos(Math.toRadians(90.0)))) + COMy;
			// Check if obj is out of bound after rotation
			if (coor[0] >= Board.numW || coor[0] < 0 || coor[1] >= Board.numH-1 || Board.M[coor[1]][coor[0]] != Color.BLACK) {
				this.coordinate.clear();
				this.coordinate = deepcopy(original);
				return false;
			}
			System.out.println("x:"+coor[0]+"|y:"+coor[1]);
		}// end for
		return true;
	}// end rotate
}
