import javax.swing.*;
import java.awt.*;

public class Sample extends JFrame {

	private static final int FRAME_WIDTH	= 300;
	private static final int FRAME_HEIGHT	= 200;
	private static final int FRAME_X_ORIGIN	= 150;
	private static final int FRAME_Y_ORIGIN	= 250;

	private static final int BUTTON_WIDTH	= 80;
	private static final int BUTTON_HEIGHT	= 30;
	
	private JButton newButton;
	private JButton oldButton;
	
	public static void main(String[] args) {
		Sample frame = new Sample();
		frame.setVisible(true);
	}

	public Sample () {

		Container contentPane = getContentPane();
		
		setTitle("Program Ch14JButtonFrame");
		setSize (FRAME_WIDTH, FRAME_HEIGHT);
		//setResizable (false);
		setLocation (FRAME_X_ORIGIN, FRAME_Y_ORIGIN);

		contentPane.setLayout(null);
		
		newButton = new JButton("New");
		newButton.setBounds(50,50,BUTTON_WIDTH,BUTTON_HEIGHT);
		newButton.addActionListener(new ButtonHandler());
		contentPane.add(newButton);
		
		oldButton = new JButton("Old");
		oldButton.setBounds(150,50,BUTTON_WIDTH,BUTTON_HEIGHT);
		contentPane.add(oldButton);
		
		setDefaultCloseOperation (EXIT_ON_CLOSE);
	}
	
}
