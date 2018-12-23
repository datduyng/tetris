
public class Debug {
	public static void main (String args[]) {
		Shape s = new Shape(3);
		s.coordinate.add(new int [] {29, 1, 0});
		s.coordinate.add(new int [] {29, 2, 1});
		s.coordinate.add(new int [] {29, 3, 0});
		s.coordinate.add(new int [] {29, 4, 0});
		
		System.out.println("Before rotate...");
		for (int c [] : s.coordinate) {
			System.out.println(c[0] + " " +  c[1]);
		}
		s.rotate();
		System.out.println("Debugging...");
		for (int c [] : s.coordinate) {
			System.out.println(c[0] + " " +  c[1]);
		}
		
	}
}
