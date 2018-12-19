import java.applet.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.InvalidKeyException;
import java.util.*;

/**
 * 
 * @author Huy Vuong and Dat Nguyen Version of Date: 12/18/18 Base on :
 *         http://www.edu4java.com/en/game/game2.html fix flickering:
 *         http://www.dmc.fmph.uniba.sk/public_html/doc/Java/ch10.htm
 */

// for keyboard input
public class Board extends Applet implements KeyListener, Runnable {

	static ArrayList<int[][]> shapes = new ArrayList<int[][]>(7);
	ArrayList<int[]> coordinate = new ArrayList<int[]>(); // 0: x. 1: y

	private boolean running = true;
	private final static int WIDTH = 600;
	private final static int HEIGHT = 800;

	private static int numW = 30;
	private static int numH = 40;
	private int pixelWidth = WIDTH / numW;
	private int pixelHeight = HEIGHT / numH;

	private static int M[][] = new int[numH][numW];

	private int dx = 0;
	private int dy = 1;
	private int idx_currShape;
	LinkedHashMap<Integer, ArrayList<Integer>> setToDraw = new LinkedHashMap<Integer, ArrayList<Integer>>();

	public void initShape() {
		int[][] square = new int[][] { { 1, 1 }, { 1, 1 } };
		shapes.add(square);

	}

	public void generateShape() {
		Random r = new Random();
		this.idx_currShape = r.nextInt(7);
		int[][] currShape = shapes.get(0);
		for (int y = 0; y < currShape.length; y++) {
			for (int x = 0; x < currShape[0].length; x++) {
				// Set the offset according to board size
				int[] tmp = new int[] { x + 14, y };
				coordinate.add(tmp);
			}
		}
	}

	@Override
	// Call first by the browser
	public void init() {
		initShape();
		generateShape();
		this.setBackground(Color.WHITE);
		addKeyListener(this);
	}

	@Override
	public void start() {

		// M = new int[numH][numW];
		// this.setBackground(Color.BLACK);
		Thread t = new Thread() {

			@Override
			public void run() {
				while (running) {
					try {
						update();
						repaint();
						System.out.println("Running..");
						Thread.sleep(200);
					} catch (InterruptedException e) {
						System.out.println("Interupt ERROR");
					}
				}
			}
		};
		t.start();
	}

	@Override
	public void stop() {
		running = false;
	}

	@Override
	public void destroy() {
		running = false;

	}

	@Override
	public void paint(Graphics g) {
		// super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		System.out.println("Painting..");
		this.setSize(WIDTH, HEIGHT);
		g2d.setColor(Color.RED);
		g2d.drawString("Tetris game", 60, 20);
		showStatus("Testing...");
		for (int[] coor : coordinate) {
			if (M[coor[1]][coor[0]] == 1) {
				g2d.fillRect(pixelWidth * coor[0], pixelHeight * coor[1], 20, 20);
			}
		}
		
		// Draw previous blocks
		for (Integer key : setToDraw.keySet()) {
			System.out.println(key);
			ArrayList<Integer> row = setToDraw.get(key);
			for (Integer point : row) {
				g2d.fillRect(pixelWidth * point, pixelHeight * key, 20, 20);		
			}
		}
		

	}

	public void update() {
		// Graphics2D g2d = (Graphics2D) g;
		//
		// delete old coor before moving
		// paint(g); //keep what was there before
		for (int[] coor : coordinate) {
			M[coor[1]][coor[0]] = 0;
		}
		
		// Check if the block is movable or not
		for (int[] coor : coordinate) {
			if (coor[1] >= numH - 1) {
				dy = 0;
				dx = 0;
				updateBoard();
				coordinate.clear();
				generateShape();
				break;
			} if (coor[0] == 0 || coor[0] > numW - 2) {
				dx = 0;
			} if (M[coor[1] + dy][coor[0]] == 1) {
				dx = 0;
				dy = 0;
				updateBoard();
				coordinate.clear();
				generateShape();
				break;
			}
		}
		// Update moving box
		for (int[] coor : coordinate) {
			coor[1] += dy;
			coor[0] += dx;
		}
		// Change to rand idx once all shape got set up
		for (int[] coor : coordinate) {
			M[coor[1]][coor[0]] = 1;
		}
		repaint();

	}
	
	/*
	 * Draw the block if it is not movable
	 */
	
	public void updateBoard() {
		for (int[] coorToDraw : coordinate) {
			M[coorToDraw[1]][coorToDraw[0]] = 1;// Update matrix board
			if (setToDraw.get(coorToDraw[1]) == null) {
				ArrayList<Integer> val = new ArrayList<Integer>();
				setToDraw.put(coorToDraw[1], val);
			} else {
				setToDraw.get(coorToDraw[1]).add(coorToDraw[0]);
			}
		}
		System.out.println(setToDraw.toString());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("Starting keyListen");
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			dx = -1;
			dy = 1;
			System.out.println("Going left");
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			dx = 1;
			dy = 1;
			System.out.println("Going Right");
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			dx = 0;
			dy = 3;
		} else {
			dy = 1;
			dx = 0;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		dx = 0;
		dy = 1;

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
	}

}