import java.applet.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

/**
 * 
 * @author Huy Vuong and Dat Nguyen Version of Date: 12/18/18 Base on :
 *         http://www.edu4java.com/en/game/game2.html
 *
 */

// for keyboard input
public class Board extends Applet implements KeyListener {

	// Board b = new Board() {
	//
	// };
	static ArrayList<int[][]> shapes = new ArrayList<int[][]>(7);
	static ArrayList<int[]> coordinate  = new ArrayList<int[]>(); //0: x. 1: y

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
				int [] tmp = new int [] {x + 14, y};
				coordinate.add(tmp);
			}
		}
	}
	
	

	@Override
	public void init() {
		initShape();
		generateShape();
		this.setBackground(Color.BLACK);
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
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						System.out.println("ERROR");
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
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		System.out.println("Painting..");
		this.setSize(WIDTH, HEIGHT);
		g2d.setColor(Color.orange);
		g2d.drawString("Tetris game", 60, 20);
		showStatus("Testing...");
		for (int y = 0; y < numH; y++) {
			for (int x = 0; x < numW; x++) {
				g2d.setColor(Color.YELLOW);
				if (M[y][x] == 1) {
					g2d.fillRect(pixelWidth * x, pixelHeight * y, 20, 20);
				}
			}
		}

	}

	static int i = 2;
	static int j = 4;

	public void update() {
		// Change to rand idx once all shape got set up
//		int[][] currShape = shapes.get(0);
		for (int [] coor : coordinate) {
			M[coor[1]][coor[0]] = 1;
		}
		
		for (int [] coor : coordinate) {
			coor[1] += dy;
			coor[0] += dx;
		}
		
	}

	public void drawPixel(int x, int y) {

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
		repaint();

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
//		dx = 0;
//		dy = 1;

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
