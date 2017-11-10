import javax.swing.JFrame;

public class ImageFrame extends JFrame{

	ImagePanel iPane;
	
	public ImageFrame(String imageName) {
		iPane = new ImagePanel(this.getWidth(), this.getHeight(), imageName);
		
	}
	
	
	public static void main(String[] args) {
		ImageFrame iframe = new ImageFrame("Panda.jpg");
		
		iframe.setTitle("Circle Detection");
		iframe.setSize(2100, 1080);
		iframe.setLocationRelativeTo(null);
		iframe.setContentPane(iframe.iPane);
		iframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		iframe.setVisible(true);
	}

}
