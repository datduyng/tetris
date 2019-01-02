import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

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

	final static int numW = 12;
	final static int numH = 16;
	final static int pixelWidth = WIDTH / numW;
	final static int pixelHeight = HEIGHT / numH;

	static int totalScore = 0;
	static int speed = 150;
	static int M[][] = new int[numH][numW];

	static Color[] colors = new Color[] {Color.YELLOW, Color.RED, Color.BLUE, Color.CYAN, Color.GREEN};
	static int loopCounter = 0;
	
	
	Shape currShape;
	Random rand = new Random();
	
	@Override
	// Call first by the browser
	public void init() {
		this.setBackground(Color.WHITE);
		addKeyListener(this);
		currShape = new Shape(rand.nextInt(Shape.shapes.length));
//		currShape = new Shape(5);
	}
	
	public void printBoard() {
		for(int i=0;i<numH;i++) {
			for(int j =0;j<numW;j++) {
				System.out.print(M[i][j]);
			}
			System.out.println("");
		}
	}
	
	static public boolean checkFullRow(int row) {
		for(int i = 0; i<M[row].length;i++) {
			if(M[row][i] == 0) return false;
		}
		return true;
	}
	@Override
	public void start() {

		// M = new int[numH][numW];
		this.setBackground(Color.BLACK);
		Thread t = new Thread() {

			@Override
			public void run() {
				while (running) {
					try {
						update(true, true);
						repaint();
						Thread.sleep(speed);
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
		System.out.println("Game over!");
//		 System.exit(1);
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

		// int R = (int) (Math.random( )*256);
		// int G = (int)(Math.random( )*256);
		// int B= (int)(Math.random( )*256);
		// Color randomColor = new Color(R, G, B);
		// g2d.setColor(randomColor);
		g2d.setColor(Color.YELLOW);
		Font serif = new Font("Serif", Font.BOLD, 40);
		g2d.setFont(serif);
		g2d.drawString("Tetris game", 30, 40);
		g2d.drawString("Score:"+Board.totalScore, 40, 80);
		showStatus("Testing...");
		for (int[] coor : currShape.coordinate) {
			g2d.fillRect(pixelWidth * coor[0], pixelHeight * coor[1], Board.pixelWidth - 2, Board.pixelHeight - 2);
		}
		
		//draw the whole board
		for(int x=0;x<Board.numW;x++) {
			for(int y=0;y<Board.numH;y++) {
				//make color random in here
				if(M[y][x] == 1) g2d.fillRect(pixelWidth * x, pixelHeight * y, Board.pixelWidth - 2, Board.pixelHeight - 2);
			}
		}
	}

	public void genShape() {
		// delete old/free old
		currShape = new Shape(rand.nextInt(Shape.shapes.length));
//		currShape = new Shape(5);
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
					System.out.println("Currshape:coordinate:"+currShape.coordinate.toString());
					currShape.dy = 0; // delay logic
					for(int[] co: currShape.coordinate) {if(shapeHandler(co)) { System.out.println("Stop:"+currShape.dx);break;}}
					
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
			if(shapeHandler(coor)) break;
		}
		

		// Update moving box
		for (int[] coor : currShape.coordinate) {
			// prevent falling to fast
			if (coor[1] + currShape.dy <= numH)
				if (M[coor[1] + currShape.dy][coor[0]] == 1)
					currShape.dy = 1;

			if (down)
				coor[1] += currShape.dy;
			if(Board.M[coor[1]][coor[0] + currShape.dx] == 1) currShape.dx = 0 ;
			if (left_right && !shapeHandler(coor)) {
//				System.out.println("Update dx:"+currShape.dx);
				coor[0] += currShape.dx;
			}
		}
		// Change to rand idx once all shape got set up
		for (int[] coor : currShape.coordinate) {
			M[coor[1]][coor[0]] = 1;
		}
	}

	public boolean shapeHandler(int[] coor) {
		// If shape near left or right border, set it's horizontal speed to 0
		int x = coor[0], y = coor[1]; 
		boolean state =false;
		//check for out of bound
		if(x+currShape.dx > Board.numW-1 || x+currShape.dx < 0) {
			currShape.dx = 0;
			return true;
		}else if(M[y][x+currShape.dx] == 1)	{
			currShape.dx = 0;
			return true;
		}
		//check diagonal 
		if(x+currShape.dx <= Board.numW-1 && x+currShape.dx >= 0 && y+currShape.dy <= Board.numH-1) {
			if(Board.M[y+currShape.dy][x+currShape.dx] == 1 && (currShape.dx == 1||currShape.dx == -1) && currShape.dy == 1 ) {
				currShape.dx = 0;
				return true;
			}
		}
		return false;
			
	
	}
	
	public static boolean clearRow(int row) {
		if(row < 0 || row > Board.numH-1) return false;
		for(int j=0;j<Board.numH;j++) {
			Board.M[row][j] = 0;
		}
		return false;
	}
	
	public static boolean copyRowFromTo(int from, int to) {
		if(from < 0 || from > Board.numH-1 || to < 0 || to>Board.numH-1) return false; 
		for(int j=0;j<Board.numW;j++) {
			Board.M[to][j] = Board.M[from][j];
		}
		return true;
	}
	/*
	 * Draw the block if it's done moving
	 */
	public void lockShape() {
		Board.loopCounter = 0;
		
		//Lock shape on the board
		for (int[] coorToDraw : currShape.coordinate) {
			M[coorToDraw[1]][coorToDraw[0]] = 1;// Update matrix board
		}
		
		//Score here
		
		//square block score
		int blockcombo = 0;
		
		//interlace layer cobo score
		int runningCombo = 0;// this is harder to earn to get higher score
		//only check row where tile just landed. these are the possible scoring spot
		for(int [] coor : currShape.coordinate) {
			int x = coor[0], y = coor[1];//unpack value
			if(Board.checkFullRow(y)) {// if the row is full
				runningCombo += 1;
				int continuousY = y;
				while(Board.checkFullRow(continuousY)) {continuousY -= 1;blockcombo+=1;}
				
				//shift block down
				int shiftTo = y;
				for(int j=continuousY;j>=0;j--) {
					copyRowFromTo(j, shiftTo);
					shiftTo -= 1;
				}
				Board.totalScore += blockcombo * 300;
				blockcombo = 0;
			}
		}
		Board.totalScore += Math.max((runningCombo-1),0) * 600;
		runningCombo = 0;
		repaint();
	}

	public static int getSpeed() {
		return speed;
	}

	public static void setSpeed(int speed) {
		// reresh rate [100, 200]
		Board.speed = speed;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			currShape.dx = -1;
			currShape.dy = 1;
			update(true, false); // Increase left speed, down remain
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			currShape.dx = 1;
			currShape.dy = 1;
			update(true, false);// Increase right speed, down remain
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			currShape.dx = 0;
			currShape.dy = 1;
			update(false, true); // Increase down speed, sides remain
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			// delete old filled tile before rotate
			for (int[] coor : currShape.coordinate) {
				M[coor[1]][coor[0]] = 0;
			}
			currShape.rotate();
		} else {
			currShape.dy = 1;
			currShape.dx = 0;
		}
		repaint();
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

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}