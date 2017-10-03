import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStream;

public class Workhorse {
	
	public String getInput(String request) {
		
		String response = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			
			System.out.print(request);
			response = br.readLine();
				
			if("exit".equals(response)) {
				System.out.print("Closing...");
				System.exit(0);
			}
			
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	public URL createURL(String IDCode){
		String emailID = IDCode;
		String URLRoot = "http://www.ecs.soton.ac.uk/people/";
		
		String fullURL = URLRoot + emailID;
		
		URL link = null;
		try {
		link = new URL(fullURL);
		} catch(MalformedURLException e) {
			e.printStackTrace();
		}
		
		return link;
	}
	
	public String getSource(URL u) {
		StringBuilder s = null;
		try {
			InputStream is = u.openStream();
			BufferedReader buffer = null;
			buffer = new BufferedReader(new InputStreamReader(is, "iso-8859-9"));
			int byteRead;
			s = new StringBuilder();
			
			while((byteRead = buffer.read()) != -1) {
				s.append((char) byteRead);
			}
		
			buffer.close();
			
			} catch(IOException e) {
			e.printStackTrace();
		}
		if(s != null) {
		return s.toString();
		}
		return null;
	}
	
	public String findName(String source) {
		int titleStart = source.indexOf("<title>");
		int slashTitleStart = source.indexOf("</title>");
		
		String name = source.substring(titleStart + 7, slashTitleStart);
		String people = name.substring(0,6);
		
		if(people.equals("People") != true) {
			System.out.println("Their name, department and affiliation are: " + name);
		} else {
			System.out.println("This person does not have a profile here.");
		}
		
		return name;
	}
	
}