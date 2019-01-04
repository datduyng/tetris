import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Sample extends JFrame implements ActionListener{
	private static final int FRAME_WIDTH	= 300;
	private static final int FRAME_HEIGHT	= 200;
	private static final int FRAME_X_ORIGIN	= 150;
	private static final int FRAME_Y_ORIGIN	= 250;

	private static final int BUTTON_WIDTH	= 80;
	private static final int BUTTON_HEIGHT	= 30;

	private JTextField textField;

	public static void main(String[] args) {
		Sample frame = new Sample();
		frame.setVisible(true);	
	}

	public Sample(){
		setSize (FRAME_WIDTH, FRAME_HEIGHT);
		setResizable (true);
		setTitle ("Program JTextFieldTest");
		setLocation (FRAME_X_ORIGIN, FRAME_Y_ORIGIN);

		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setBounds(30, 70, 130, 90);

		contentPane.add(textField);
		textField.addActionListener(this);
				
		setDefaultCloseOperation( EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent event){
		if(event.getSource() instanceof JTextField){
			setTitle("Content of JTextField = " + textField.getText());
		}
	}
}
