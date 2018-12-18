import java.applet.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

/**
 * 
 * @author Huy Vuong and Dat Nguyen Version of Date: 12/18/18 Base on :
 *         http://www.edu4java.com/en/game/game2.html
 *
 */

// for keyboard input
public class Board extends Applet implements KeyListener {

	
	
	private boolean running = true;
	private final static int WIDTH = 600;
	private final static int HEIGHT = 800;

	private static int numW = 30;
	private static int numH = 40;
	private int pixelWidth = WIDTH / numW;
	private int pixelHeight = HEIGHT / numH;

	static int M[][] = new int[numH][numW];
	
	

	@Override
	public void init() {
	}

	@Override
	public void start() {
//		M = new int[numH][numW];
//		this.setBackground(Color.BLACK);
		Thread t = new Thread() {

			@Override
			public void run() {
				while (running) {
					try {
						update();
						repaint();
						System.out.println("Running..");
						Thread.sleep(100);
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
				g2d.setColor(Color.RED);
				if (M[y][x] == 1) {
					g2d.fillRect(pixelWidth * x, pixelHeight * y, 20, 20);
				}
			}
		}

	}
	static int i = 2;
	static int j = 4;
	public void update() {
		M[i][j] = 1;
		i++;
		j++;
		if (j == 30) {
			j--;
		}
		
		if (i == 39) {
			i--;
		}
	}

	public void drawPixel(int x, int y) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


}
