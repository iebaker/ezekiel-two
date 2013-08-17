import processing.core.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class BallGame {
  // default size (in pixels) for the game canvas
  private final static int default_height = 550;
  private final static int default_width  = 900;

	public static void main(String[] args) {
    // create a new window for the application
		final JFrame frame = new JFrame("Ezekiel Two");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize( new Dimension( default_width, default_height ) );

    // create container panel with border layout (for sizing)
		JPanel panel = new JPanel( new BorderLayout() );

    // create new game instance with default size and run
		final EzekielTwo e2 = new EzekielTwo( default_width, default_height );
		e2.init();

    // attach the game to the frame
		panel.add( e2, BorderLayout.CENTER );
		frame.add( panel );

    // calculate component sizes and show the window
    frame.pack();
		frame.setVisible(true);
	}
}
   
