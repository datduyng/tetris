import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * 
 * @author Huy Vuong and Dat Nguyen Version of Date: 12/18/18 Base on :
 *         http://www.edu4java.com/en/game/game2.html fix flickering:
 *         http://www.dmc.fmph.uniba.sk/public_html/doc/Java/ch10.htm
 */

// for keyboard input
public class Board extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2270756732278832436L;
	static boolean running = true;
	final static int WIDTH = 600;
	final static int HEIGHT = 800;

	final static int numW = 12;
	final static int numH = 16;
	final static int pixelWidth = WIDTH / numW;
	final static int pixelHeight = HEIGHT / numH;

	static int speed = 150;
	static int M[][] = new int[numH][numW];

	// static String[] color = new String[] { "RED", "YELLOW", "BLUE", "ORANGE",
	// "PINK" };
	static int loopCounter = 0;

	static Shape currShape;
	static Random rand = new Random();
	
	public static void main(String args[]) {
		JFrame f = new JFrame("Tetris");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(Board.WIDTH, Board.HEIGHT);
		f.setVisible(true);
		f.setBackground(Color.BLACK);
//		f.addWindowListener(new java.awt.event.WindowAdapter() {
//		       public void windowClosing(java.awt.event.WindowEvent e) {
//		       System.exit(0);
//		       };
//		     });
		Board b = new Board();
		Board.init();
//		b.setSize(600, 800);
		f.add(b);
//		f.pack();

//		f.setSize(620, 900);
		// Keyboard controls
		f.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					Board.currShape.dx = -1;
					// currShape.dy = 1;
					b.update(true, false); // Increase left speed, down remain
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					Board.currShape.dx = 1;
					// currShape.dy = 1;
					b.update(true, false);// Increase right speed, down remain
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					Board.currShape.dx = 0;
					Board.currShape.dy = 1;
					b.update(false, true); // Increase down speed, sides remain
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
					// delete old filled tile before rotate
					for (int[] coor : Board.currShape.coordinate) {
						M[coor[1]][coor[0]] = 0;
					}
					Board.currShape.rotate();
				} else if (e.getKeyCode() == KeyEvent.VK_P) {
					b.stop();

				} else {
					Board.currShape.dy = 1;
					Board.currShape.dx = 0;
				}
//				repaint();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				currShape.dx = 0;
				currShape.dy = 1;
				setSpeed(200);
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		});
		
		new Thread() {

			@Override
			public void run() {
				while (Board.running) {
					try {
						b.printBoard();
						System.out.println("===============");
						b.update(true, true);
						f.repaint();
						Thread.sleep(speed);
					} catch (InterruptedException e) {
						System.out.println("Interupt ERROR");
					}
				}
			}
		}.start();
		
	}

	static LinkedHashMap<Integer, Set<Integer>> setToDraw = new LinkedHashMap<Integer, Set<Integer>>();

	
	// Call first by the browser
	public static void init() {
//		this.setBackground(Color.BLACK);
//		addKeyListener(this);
		currShape = new Shape(rand.nextInt(Shape.shapes.length));
	}

	public void printBoard() {
		for (int i = 0; i < numH; i++) {
			for (int j = 0; j < numW; j++) {
				System.out.print(M[i][j]);
			}
			System.out.println("");
		}
	}
	

	public void stop() {
		System.out.println("Game over!");
		// System.exit(1);
		running = false;
	}

	public void destroy() {
		running = false;

	}

	@Override
	public void paint(Graphics g) {
		// super.paint(g);
		System.out.println("Runing repaint");
//		Graphics2D g = (Graphics2D) g;
//		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//		this.setSize(WIDTH, HEIGHT);

//		 int R = (int) (Math.random( )*256);
//		 int G = (int)(Math.random( )*256);
//		 int B= (int)(Math.random( )*256);
//		 Color randomColor = new Color(R, G, B);
//		 g.setColor(randomColor);
		g.setColor(Color.YELLOW);
		g.drawString("Tetris game", 60, 20);
		
		for (int[] coor : currShape.coordinate) {
			g.fillRect(pixelWidth * coor[0], pixelHeight * coor[1], Board.pixelWidth - 2, Board.pixelHeight - 2);
		}

		// Draw previous blocks
		for (Integer key : setToDraw.keySet()) {
			Set<Integer> row = setToDraw.get(key);
			for (Integer point : row) {
				g.fillRect(pixelWidth * point, pixelHeight * key, Board.pixelWidth - 2, Board.pixelHeight - 2);
			}
		}

	}

	public void genShape() {
		// delete old/free old
		currShape = new Shape(rand.nextInt(Shape.shapes.length));
		if (!currShape.generated)
			this.stop();
	}

	public void update(boolean left_right, boolean down) {

		// delete old coor before moving
		for (int[] coor : currShape.coordinate) {
			M[coor[1]][coor[0]] = 0;
		}

		// Check if the block is movable or not
		for (int[] coor : currShape.coordinate) {
			System.out.println(coor[0] + " | " + coor[1]);
			// TODO: clean up logic
			if (coor[1] >= numH - 1) {
				if (loopCounter > 3) {

					currShape.dy = 0;
					currShape.dx = 0;
					lockShape();
					currShape.coordinate.clear();
					genShape();
				} else {

					loopCounter++;
					shapeHandler(coor);
					currShape.dy = 0; // delay logic
				}
				break;

			}

			if (M[coor[1] + 1][coor[0]] == 1) {
				if (loopCounter > 3) {

					currShape.dy = 0;
					currShape.dx = 0;
					lockShape();
					currShape.coordinate.clear();
					genShape();

				} else {

					loopCounter++;
					shapeHandler(coor);
					currShape.dy = 0; // delay logic
				}
				break;

			}
			if (shapeHandler(coor))
				break;
		}

		// Update moving box
		for (int[] coor : currShape.coordinate) {
			// prevent falling to fast
			if (coor[1] + currShape.dy < numH)
				if (M[coor[1] + currShape.dy][coor[0]] == 1)
					currShape.dy = 1;

			if (down)
				coor[1] += currShape.dy;
			if (Board.M[coor[1]][coor[0] + currShape.dx] == 1)
				currShape.dx = 0;
			if (left_right)
				coor[0] += currShape.dx;
		}
		// Change to rand idx once all shape got set up
		for (int[] coor : currShape.coordinate) {
			M[coor[1]][coor[0]] = 1;
		}
	}

	public boolean shapeHandler(int[] coor) {

		int x = coor[0], y = coor[1];
		boolean state = false;
		// check for out of bound
		// If shape near left or right border, set it's horizontal speed to 0
		if (x + currShape.dx > Board.numW - 1 || x + currShape.dx < 0) {
			currShape.dx = 0;
			state = true;
		}
		if (M[y][x + currShape.dx] == 1) {
			currShape.dx = 0;
			state = true;
		}
		// check diagonal
		if (x + currShape.dx <= Board.numW - 1 && x + currShape.dx >= 0 && y + currShape.dy <= Board.numH - 1) {
			if (Board.M[y + currShape.dy][x + currShape.dx] == 1 && (currShape.dx == 1 || currShape.dx == -1)
					&& currShape.dy == 1) {
				currShape.dx = 0;
				state = true;
			}
		}
		return state;

	}

	/*
	 * Draw the block if it's done moving
	 */
	public void lockShape() {
		Board.loopCounter = 0;
		for (int[] coorToDraw : currShape.coordinate) {
			M[coorToDraw[1]][coorToDraw[0]] = 1;// Update matrix board
			if (setToDraw.get(coorToDraw[1]) == null) {
				Set<Integer> val = new HashSet<Integer>();
				val.add(coorToDraw[0]);
				setToDraw.put(coorToDraw[1], val);
			} else {
				setToDraw.get(coorToDraw[1]).add(coorToDraw[0]);
			}
		}
	}

	public static int getSpeed() {
		return speed;
	}

	public static void setSpeed(int speed) {
		// reresh rate [100, 200]
		Board.speed = speed;
	}

}