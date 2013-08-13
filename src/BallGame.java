import processing.core.*;
import javax.swing.*;

public class BallGame {
	public static void main(String[] args) {
		final JFrame frame = new JFrame("Ezekiel Two");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		JPanel panel = new JPanel();
		final EzekielTwo e2 = new EzekielTwo();
		e2.init();

		panel.add(e2);
		frame.add(panel);

		frame.setSize(e2.getSize().width, e2.getSize().height);
		frame.setVisible(true);
	}
}
   
