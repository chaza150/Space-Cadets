
import java.net.URL;

public class SpaceCadetsGo {
	
	
	public static void main(String[] args) {
		Workhorse w = new Workhorse();
			while(true) {
				String emailID = w.getInput("Enter the email ID: ");
				URL link = w.createURL(emailID);
				String source = w.getSource(link);
				w.findName(source);
				w.getInput("If you wish to exit, please type 'exit'\nIf not, please press enter. ");
			//Space Cadets Recommit
			}
		}
}
