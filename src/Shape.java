import java.util.ArrayList;

//import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class Shape {
	int id;
	int dx, dy;
	ArrayList<int[]> coordinate = new ArrayList<int[]>(); // 0: x. 1: y 2: COM

	final static int[][][] shapes = { { { 0, 1 }, { 0, 1 }, { 1, 2 } },

			{ { 1, 1 }, { 1, 1 } },

			{ { 1, 0 }, { 1, 0 }, { 2, 1 } },

			{ { 1, 0 }, { 1, 2 }, { 0, 1 } },

			{ { 0, 1, 0 }, { 1, 2, 1 } },

			{ { 1 }, { 2 }, { 1 }, { 1 } } };

	// there should be 7 shape
	// ArrayList<int[][]> shapes = new ArrayList<int[][]>();

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
				if (currShape[y][x] == 1) {
					int[] tmp = new int[] { x + Board.pixelWidth / 2, y, 0 };
					coordinate.add(tmp);
				} else if (currShape[y][x] == 2) {
					int[] tmp = new int[] { x + Board.pixelWidth / 2, y, 1 };
					coordinate.add(tmp);
				}
			} // end x

		} // end y
	}

	/*
	 * Function to rotate a block based on it's current coordinate
	 */

	public void rotate() {
		int originX = 0, originY = 0;
		for (int[] coor : coordinate) {
			if (coor[2] == 1) {
				// Get the COM of the block
				originX = coor[0];
				originY = coor[1];
			}
		}
		for (int[] c : coordinate) {
			c[0] -= originX;
			c[1] -= originY;
		}
		
		for (int[] c : coordinate) {
			int tmp = c[0];
			c[0] = c[1];
			c[1] = tmp;
		}
		
		for (int[] c : coordinate) {
			c[0] += originY;
			c[1] += originX;
		}
	}

}
