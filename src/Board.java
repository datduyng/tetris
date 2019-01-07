import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

/**
 * 
 * @author Huy Vuong and Dat Nguyen Version of Date: 12/18/18 Base on :
 *         http://www.edu4java.com/en/game/game2.html fix flickering:
 *         http://www.dmc.fmph.uniba.sk/public_html/doc/Java/ch10.htm
 */

// for keyboard input
public class Board extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	static boolean running = false;
	boolean pause = false;
	final static int WIDTH = 600;
	final static int HEIGHT = 1000;

	final static int numW = 12;
	final static int numH = 20;
	final static int pixelWidth = WIDTH / numW;
	final static int pixelHeight = HEIGHT / numH;
	final static int playButtonWidth = 439;
	final static int playButtonHeight = 173;
	static int totalScore = 0;
	static int speed = 150;
	static Color M[][] = new Color[numH][numW];

	static boolean falling;
	static int loopCounter = 0;
	static boolean play = false;

	Color color;
	Shape currShape;
	Random rand = new Random();

	static Thread t;
	private JButton playButton;
	static Container contentPane;
	private JTextField nameField;
	private JLabel label;
	static private JLabel scoresBoard;
	static JFrame f;
	static String playerName;

	/*
	 * A main driver that starts a thread and execute GUI app
	 */
	public static void main(String args[]) {
		f = new JFrame("Tetris");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		contentPane = f.getContentPane();
		f.setBackground(Color.BLACK);
		Board b = new Board();
		f.add(b);
		f.pack();
		f.setVisible(true);
		f.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					b.currShape.dx = -1;
					b.currShape.dy = 1;
					b.update(true, false); // Increase left speed, down remain
					break;
				case KeyEvent.VK_RIGHT:
					b.currShape.dx = 1;
					b.currShape.dy = 1;
					b.update(true, false);// Increase right speed, down remain
					break;
				case KeyEvent.VK_DOWN:
					b.currShape.dx = 0;
					b.currShape.dy = 1;
					b.update(false, true); // Increase down speed, sides remain
					break;
				case KeyEvent.VK_UP:
					// delete old filled tile before rotate
					for (int[] coor : b.currShape.coordinate) {
						M[coor[1]][coor[0]] = Color.BLACK;
					}
					b.currShape.rotate();
					break;
				case KeyEvent.VK_SPACE:
					b.pause = !b.pause;
					break;
				default:
					b.currShape.dy = 1;
					b.currShape.dx = 0;
					break;
				}

			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				b.currShape.dx = 0;
				b.currShape.dy = 1;
				setSpeed(150);
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		});

		t = new Thread() {

			@Override
			public void run() {
				while (!play) {
					// Halt
					System.out.println(play);
				}
				if (play) {
					contentPane.remove(b.playButton);
					contentPane.remove(b.nameField);
					contentPane.remove(b.label);
				}
				while (running) {
					try {
						if (!b.pause)
							b.update(true, true);
						f.repaint();
						Thread.sleep(speed);
					} catch (InterruptedException e) {
						System.out.println("Interupt ERROR");
					}
				}
				String data = Board.playerName + "," + Board.totalScore + "\n";
				String filePath = "./data/score_board.dat";
				appendUsingFileWriter(filePath, data);
				displayLeaderBoard(filePath);
			}
		};

		t.start();

	}
	
	/*
	 * Functions for button handlers
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() instanceof JTextField) {

		}
		if (nameField.getText().isEmpty()) {
			playerName = "Anonymous";
		} else {
			playerName = nameField.getText();
		}
		f.setTitle("Player : " + nameField.getText());
		play = true;
		running = true;
	}

	/*
	 * Default constructor for the game
	 */
	public Board() {
		init();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		ImageIcon playButtonIcon = new ImageIcon("img/play-button.png");
		playButton = new JButton();
		label = new JLabel("Enter your name and play", SwingConstants.CENTER);
		label.setBounds(90, 200, playButtonWidth, 50);
		label.setFont(new Font("Serif", Font.PLAIN, 20));
		label.setForeground(Color.YELLOW);
		contentPane.add(label);
		nameField = new JTextField();
		nameField.setBounds(90, 250, playButtonWidth, 50);
		contentPane.add(nameField);
		playButton.setIcon(playButtonIcon);
		playButton.setBounds(90, 300, playButtonWidth, playButtonHeight);
		playButton.addActionListener(this);
		contentPane.add(playButton);
		scoresBoard = new JLabel(
				"<html>Tetris game<br/>Player :" + playerName + "<br/>" + "Scores: " + Board.totalScore + "</html>");
		scoresBoard.setBounds(400, 20, 200, 100);
		scoresBoard.setFont(new Font("Serif", Font.PLAIN, 20));
		scoresBoard.setForeground(Color.YELLOW);
		contentPane.add(scoresBoard);

	}

	// Call first by the browser
	public void init() {

		for (Color r[] : M) {
			for (int i = 0; i < r.length; i++) {
				r[i] = Color.BLACK;
			}
		}
		currShape = new Shape(rand.nextInt(Shape.shapes.length));
	}

	/*
	 * Debugging purpose function
	 */
	public void printBoard() {
		for (int i = 0; i < numH; i++) {
			for (int j = 0; j < numW; j++) {
				System.out.print(M[i][j]);
			}
			System.out.println("");
		}
	}

	static public boolean checkFullRow(int row) {
		for (int i = 0; i < M[row].length; i++) {
			if (M[row][i] == Color.BLACK)
				return false;
		}
		return true;
	}

	public void stop() {
		System.out.println("Game over!");
		// System.exit(1);
//		contentPane.add(playButton);
		Board.running = false;
	}

	public void destroy() {
		Board.running = false;
	}

	public void clearBoard() {
		for (int i = 0; i < numH; i++) {
			clearRow(i);
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		
		scoresBoard.setText(
				"<html>Tetris game<br/>Player :" + playerName + "<br/>" + "Scores: " + Board.totalScore + "</html>");
		for (int[] coor : currShape.coordinate) {
			g2d.fillRoundRect(pixelWidth * coor[0], pixelHeight * coor[1], Board.pixelWidth - 5, Board.pixelHeight - 5,
					15, 15);
		}
		
		for (int x = 0; x < Board.numW; x++) {
			for (int y = 0; y < Board.numH; y++) {
				// make color random in here
				Color c = M[y][x];
				g2d.setColor(c.brighter());
				if (c != Color.BLACK)
					g2d.drawRoundRect(pixelWidth * x, pixelHeight * y, Board.pixelWidth - 5, Board.pixelHeight - 5, 15,
							15);
				g2d.fillRoundRect(pixelWidth * x, pixelHeight * y, Board.pixelWidth - 10, Board.pixelHeight - 10, 15,
						15);
			}
		}
	}

	public void genShape() {
		// delete old/free old
		currShape = new Shape(rand.nextInt(Shape.shapes.length));
		falling = true;
		if (!currShape.generated)
			this.stop();
	}
	
	public void update(boolean left_right, boolean down) {

		// delete old coor before moving
		for (int[] coor : currShape.coordinate) {
			M[coor[1]][coor[0]] = Color.BLACK;
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
					// System.out.println("Currshape:coordinate:"+currShape.coordinate.toString());
					currShape.dy = 0; // delay logic
					for (int[] co : currShape.coordinate) {
						if (shapeHandler(co)) {
							System.out.println("Stop:" + currShape.dx);
							break;
						}
					}

				}
				break;

			}

			if (M[coor[1] + 1][coor[0]] != Color.BLACK) {
				if (loopCounter > 3) {

					currShape.dy = 0;
					currShape.dx = 0;
					lockShape();
					currShape.coordinate.clear();
					genShape();

				} else {

					loopCounter++;
					currShape.dy = 0; // delay logic
					for (int[] c : currShape.coordinate)
						if (shapeHandler(c))
							break;

				}
				break;

			}
			if (shapeHandler(coor))
				break;
		}

		// Update moving box
		for (int[] coor : currShape.coordinate) {
			// prevent falling to fast
			if (coor[1] + currShape.dy <= numH)
				if (M[coor[1] + currShape.dy][coor[0]] != Color.BLACK)
					currShape.dy = 1;

			if (down)
				coor[1] += currShape.dy;
			if (Board.M[coor[1]][coor[0] + currShape.dx] != Color.BLACK)
				currShape.dx = 0;
			if (left_right && !shapeHandler(coor)) {
				// System.out.println("Update dx:"+currShape.dx);
				coor[0] += currShape.dx;
			}
		}
		// Change to rand idx once all shape got set up
		for (int[] coor : currShape.coordinate) {
			M[coor[1]][coor[0]] = currShape.color;
		}
	}

	public boolean shapeHandler(int[] coor) {
		// If shape near left or right border, set it's horizontal speed to 0
		int x = coor[0], y = coor[1];
		// check for out of bound
		if (x + currShape.dx > Board.numW - 1 || x + currShape.dx < 0) {
			currShape.dx = 0;
			return true;
		} else if (M[y][x + currShape.dx] != Color.BLACK) {
			currShape.dx = 0;
			return true;
		}
		// check diagonal
		if (x + currShape.dx <= Board.numW - 1 && x + currShape.dx >= 0 && y + currShape.dy <= Board.numH - 1) {
			if (Board.M[y + currShape.dy][x + currShape.dx] != Color.BLACK && (currShape.dx == 1 || currShape.dx == -1)
					&& currShape.dy == 1) {
				currShape.dx = 0;
				return true;
			}
		}
		return false;

	}

	public static boolean clearRow(int row) {
		if (row < 0 || row > Board.numH - 1)
			return false;
		for (int j = 0; j < Board.numH; j++) {
			Board.M[row][j] = Color.BLACK;
		}
		return false;
	}

	public static boolean copyRowFromTo(int from, int to) {
		if (from < 0 || from > Board.numH - 1 || to < 0 || to > Board.numH - 1)
			return false;
		for (int j = 0; j < Board.numW; j++) {
			Board.M[to][j] = Board.M[from][j];
		}
		return true;
	}

	/*
	 * Draw the block if it's done moving
	 */
	public void lockShape() {
		falling = false;
		Board.loopCounter = 0;

		// Lock shape on the board
		for (int[] coorToDraw : currShape.coordinate) {
			M[coorToDraw[1]][coorToDraw[0]] = currShape.color;// Update matrix board
		}

		// Score here

		// square block score
		int blockcombo = 0;

		// interlace layer cobo score
		int runningCombo = 0;// this is harder to earn to get higher score
		// only check row where tile just landed. these are the possible scoring spot
		for (int[] coor : currShape.coordinate) {
			int y = coor[1];// unpack value
			if (Board.checkFullRow(y)) {// if the row is full
				runningCombo += 1;
				int continuousY = y;
				while (Board.checkFullRow(continuousY)) {
					continuousY -= 1;
					blockcombo += 1;
				}

				// shift block down
				int shiftTo = y;
				for (int j = continuousY; j >= 0; j--) {
					copyRowFromTo(j, shiftTo);
					shiftTo -= 1;
				}
				Board.totalScore += blockcombo * 300;
				blockcombo = 0;
			}
		}
		Board.totalScore += Math.max((runningCombo - 1), 0) * 600;
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

	private static void appendUsingFileWriter(String filePath, String text) {
		File file = new File(filePath);
		FileWriter fr = null;
		try {
			// Below constructor argument decides whether to append or override
			fr = new FileWriter(file, true);
			fr.write(text);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void displayLeaderBoard(String filePath) {
		DefaultListModel<String> leaderBoard = new DefaultListModel<>();
		File file = new File(filePath);
		try {
			Scanner s = new Scanner(file);
			while(s.hasNextLine()) {
				String raw = s.nextLine();
				String data [] = raw.split(",");
				String line = String.format("<html> <font size=\"10\">%s : %s </font> </br> </html>", data[0], data[1]);
				leaderBoard.addElement(line);
			}
			s.close();
		} catch (Exception e) {
			System.out.println("Error error...");
		}
		JList<String> list = new JList<String>(leaderBoard);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL_WRAP);
		list.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				fixRowCountForVisibleColumns(list);
			}
		});
		JScrollPane scrollableList = new JScrollPane(list);
		scrollableList.setBounds(100, 100, 250, 400);
		scrollableList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollableList.setPreferredSize(new Dimension(230, 410));
		contentPane.add(scrollableList);

//		contentPane.setBackground(Color.white);
	}
	private static void fixRowCountForVisibleColumns(JList<String> list) {
		int nCols = computeVisibleColumnCount(list);
		int nItems = list.getModel().getSize();

		// Compute the number of rows that will result in the desired number of
		// columns
		if (nCols > 0) {
			int nRows = nItems / nCols;
			if (nItems % nCols > 0)
				nRows++;
			list.setVisibleRowCount(nRows);
		}
	}

	private static int computeVisibleColumnCount(JList<String> list) {
		// It's assumed here that all cells have the same width. This method
		// could be modified if this assumption is false. If there was cell
		// padding, it would have to be accounted for here as well.
		int cellWidth = list.getCellBounds(0, 0).width;
		int width = list.getVisibleRect().width;
		return width / cellWidth;
	}

}