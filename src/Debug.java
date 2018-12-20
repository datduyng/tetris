
public class Debug {
	public static void main (String args[]) {
		Shape s = new Shape(3);
		s.coordinate.add(new int [] {2, 3, 0});
		s.coordinate.add(new int [] {2, 4, 1});
		s.coordinate.add(new int [] {3, 4, 0});
		s.coordinate.add(new int [] {3, 5, 0});
		s.rotate();
		System.out.println("Debugging...");
		for (int c [] : s.coordinate) {
			System.out.println(c[0] + " " +  c[1]);
		}
		
	}
}
