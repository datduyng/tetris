import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

/**
 * 
 * @author Huy Vuong and Dat Nguyen Version of Date: 12/18/18 Base on :
 *         http://www.edu4java.com/en/game/game2.html fix flickering:
 *         http://www.dmc.fmph.uniba.sk/public_html/doc/Java/ch10.htm
 */

// for keyboard input
public class Board extends Applet implements KeyListener, Runnable {

	boolean running = true;
	final static int WIDTH = 600;
	final static int HEIGHT = 800;

	final static int numW = 30;
	final static int numH = 40;
	final static int pixelWidth = WIDTH / numW;
	final static int pixelHeight = HEIGHT / numH;

	static int M[][] = new int[numH][numW];
	
	Shape currShape; 
	Random rand = new Random();

	LinkedHashMap<Integer, ArrayList<Integer>> setToDraw = new LinkedHashMap<Integer, ArrayList<Integer>>();


	@Override
	// Call first by the browser
	public void init() {
		this.setBackground(Color.WHITE);
		addKeyListener(this);
		currShape = new Shape(rand.nextInt(Shape.shapes.length)); 
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

		this.setSize(WIDTH, HEIGHT);
		g2d.setColor(Color.RED);
		g2d.drawString("Tetris game", 60, 20);
		showStatus("Testing...");
		for (int[] coor : currShape.coordinate) {
				g2d.fillRect(pixelWidth * coor[0], pixelHeight * coor[1], 20, 20);
		}
		
		
		// Draw previous blocks
		for (Integer key : setToDraw.keySet()) {
			ArrayList<Integer> row = setToDraw.get(key);
			for (Integer point : row) {
				g2d.fillRect(pixelWidth * point, pixelHeight * key, 20, 20);		
			}
		}
		

	}

	public void genShape() {
		//rand
		//delete old/free old 
		currShape = new Shape(rand.nextInt(Shape.shapes.length));
	}
	public void update() {
		// delete old coor before moving
		// paint(g); //keep what was there before
		for (int[] coor : currShape.coordinate) {
			M[coor[1]][coor[0]] = 0;
		}
		
		// Check if the block is movable or not
		for (int[] coor : currShape.coordinate) {
			if (coor[0] == 0 || coor[0] > numW - 2) {
				currShape.dx = 0;
			}
			if (coor[1] >= numH - 1) {
				currShape.dy = 0;
				currShape.dx = 0;
				updateBoard();
				currShape.coordinate.clear();
				genShape();
				break;
			}
			if (M[coor[1] + 1][coor[0]] == 1) {
				currShape.dy = 0;
				currShape.dx = 0;
				updateBoard();
				currShape.coordinate.clear();
				genShape();
				break;
			}
		}
		// Update moving box
		for (int[] coor : currShape.coordinate) {
			//prevent falling to fast 
			if(coor[1]+currShape.dy<=numH) if(M[coor[1]+currShape.dy][coor[0]] == 1) currShape.dy = 1;
			coor[1] += currShape.dy;
			coor[0] += currShape.dx;
		}
		// Change to rand idx once all shape got set up
		for (int[] coor : currShape.coordinate) {
			M[coor[1]][coor[0]] = 1;
		}
	}
	
	/*
	 * Draw the block if it is not movable
	 */
	
	public void updateBoard() {
		for (int[] coorToDraw : currShape.coordinate) {
			M[coorToDraw[1]][coorToDraw[0]] = 1;// Update matrix board
			if (setToDraw.get(coorToDraw[1]) == null) {
				ArrayList<Integer> val = new ArrayList<Integer>();
				val.add(coorToDraw[0]);
				setToDraw.put(coorToDraw[1], val);
			} else {
				setToDraw.get(coorToDraw[1]).add(coorToDraw[0]);
			}
		}
		System.out.println(setToDraw.toString());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			currShape.dx = -1;
			currShape.dy = 1;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			currShape.dx = 1;
			currShape.dy = 1;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			currShape.dx = 0;
			currShape.dy = 3;
		} else {
			currShape.dy = 1;
			currShape.dx = 0;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		currShape.dx = 0;
		currShape.dy = 1;

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
	}

}