import javax.swing.*;

public class SpiroWindow{

	public SpiroWindow() {
		init();
	}
	
	public void init() {
		SpiroFrame sf = new SpiroFrame();
		sf.setTitle("Spirograph");
		sf.setSize(800, 600);
		sf.setLocationRelativeTo(null);
		sf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sf.startup();
		sf.setVisible(true);
	}
	
	public static void main(String[] args) {
		SpiroWindow sw = new SpiroWindow();
	}

}
