//********************************************************************
//  DirectionPanel.java       Author: Lewis/Loftus
//  Represents the primary display panel for the Direction program.
//********************************************************************

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Sample extends JPanel {
   private final int WIDTH = 300, HEIGHT = 200;
   private final int JUMP = 10;  // increment for image movement

   private final int IMAGE_SIZE = 31;

   private ImageIcon up, down, right, left, currentImage;
   private int x, y;

   public static void main (String[] args) {
	      JFrame frame = new JFrame ("Direction");
	      frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

	      frame.getContentPane().add (new Sample());

	      frame.pack();
	      frame.setVisible(true);
	   }
   //-----------------------------------------------------------------
   //  Constructor: Sets up this panel and loads the images.
   //-----------------------------------------------------------------
   public Sample() {
      addKeyListener (new DirectionListener());

      x = WIDTH / 2;
      y = HEIGHT / 2;

      up = new ImageIcon ("./img/arrowUp.gif");
      down = new ImageIcon ("./img/arrowDown.gif");
      left = new ImageIcon ("./img/arrowLeft.gif");
      right = new ImageIcon ("./img/arrowRight.gif");

      currentImage = right;

      setBackground (Color.black);
      setPreferredSize (new Dimension(WIDTH, HEIGHT));
      setFocusable(true);
   }

   //-----------------------------------------------------------------
   //  Draws the image in the current location.
   //-----------------------------------------------------------------
   public void paintComponent (Graphics page) {
      super.paintComponent (page);
      currentImage.paintIcon (this, page, x, y);
   }

   //*****************************************************************
   //  Represents the listener for keyboard activity.
   //*****************************************************************
   private class DirectionListener implements KeyListener {
      //--------------------------------------------------------------
      //  Responds to the user pressing arrow keys by adjusting the
      //  image and image location accordingly.
      //--------------------------------------------------------------
      public void keyPressed (KeyEvent event) {
         switch (event.getKeyCode()) {
            case KeyEvent.VK_UP:
               currentImage = up;
               y -= JUMP;
               break;
            case KeyEvent.VK_DOWN:
               currentImage = down;
               y += JUMP;
               break;
            case KeyEvent.VK_LEFT:
               currentImage = left;
               x -= JUMP;
               break;
            case KeyEvent.VK_RIGHT:
               currentImage = right;
               x += JUMP;
               break;
         }
         //System.out.print("Hello");
         repaint();
      }

      //--------------------------------------------------------------
      //  Provide empty definitions for unused event methods.
      //--------------------------------------------------------------
      public void keyTyped (KeyEvent event) {}
      public void keyReleased (KeyEvent event) {}
   }
}
