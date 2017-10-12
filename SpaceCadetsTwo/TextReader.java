import java.io.*;
import java.util.ArrayList;

public class TextReader {

	String fileName = null;
	String fileType = null;
	String line = null;
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	ArrayList<String> code = new ArrayList<String>();
	String[][] arrayCode;
	Memory varMemory = new Memory();
	
	//Sets the file to read source from and defines file type
	public void setFileName() {
		
		System.out.println("Please specify file to open as source: ");
		
		try {
			fileName = br.readLine();
			fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
			System.out.println(fileType);
		} catch (IOException e) {
			System.out.println("Err 2: IO Exception");
			e.printStackTrace();
		}
		
	}
	
	//Breaks down code formatting into separate operations/commands
	public void storeCode() {
		if(fileType.equals("bb") || fileType.equals("txt")) {
			try {
				FileReader fileReader = new FileReader(fileName);
				BufferedReader bur = new BufferedReader(fileReader);
			
				while((line = bur.readLine()) != null) {
					line = line.replaceAll("\t", "");
					code.add(line);
				}
			
				bur.close();
			
			} catch(FileNotFoundException e) {
				System.out.println("Err 1: File Not Found");
				e.printStackTrace();
			} catch(IOException e) {
				System.out.println("Err 2: IO Exception");
				e.printStackTrace();
			}
		} else if (fileType.equals("bf")) {
			try {
				FileReader fileReader = new FileReader(fileName);
				BufferedReader bur = new BufferedReader(fileReader);
			
				while((line = bur.readLine()) != null) {
					line = line.replaceAll(" ", "");
					for(int charInd = 0; charInd < line.length(); charInd++) {
							String operation = Character.toString(line.charAt(charInd));
							code.add(operation);
					}
				}
			
				bur.close();
			
			} catch(FileNotFoundException e) {
				System.out.println("Err 1: File Not Found");
				e.printStackTrace();
			} catch(IOException e) {
				System.out.println("Err 2: IO Exception");
				e.printStackTrace();
			}
		}
	}
	
	//Turns functions from List code into standard operations
	public void storeCodeInArray() {
		arrayCode = new String[code.size()][3];
		if(fileType.equals("bb") || fileType.equals("txt")) {
			for(int i = 0; i < code.size(); i++) {
				switch (code.get(i).substring(0,code.get(i).indexOf(" ")+1)) {
				case "clear ":
					arrayCode[i][0] = "clear";
					arrayCode[i][1] = code.get(i).substring(6, code.get(i).indexOf(";"));
					break;
				case "incr ":
					arrayCode[i][0] = "incr";
					arrayCode[i][1] = code.get(i).substring(5, code.get(i).indexOf(";"));
					break;
				case "decr ":
					arrayCode[i][0] = "decr";
					arrayCode[i][1] = code.get(i).substring(5, code.get(i).indexOf(";"));
					break;
				case "while ":
					arrayCode[i][0] = "while";
					arrayCode[i][1] = code.get(i).substring(6, code.get(i).indexOf(" not"));
					arrayCode[i][2] = code.get(i).substring(code.get(i).indexOf("not")+4, code.get(i).indexOf(" do"));
					break;
				default:
					if(code.get(i).substring(0,3).equals("end")) {
						arrayCode[i][0] = "end";
						break;
					} else {
						System.out.print("Err 4: Unrecognized Command: ");
						System.out.println(code.get(i).substring(0,code.get(i).indexOf(" ")+1));
						System.exit(0);
					}
				}
			}
		} else if (fileType.equals("bf")) {
			int memPointer = 0;
			for(int i = 0; i < code.size(); i++) {
				switch (code.get(i).charAt(0)) {
				case '+':
					arrayCode[i][0] = "incr";
					arrayCode[i][1] = Integer.toString(memPointer);
					break;
				case '-':
					arrayCode[i][0] = "decr";
					arrayCode[i][1] = Integer.toString(memPointer);
					break;
				case '[':
					arrayCode[i][0] = "while";
					arrayCode[i][1] = Integer.toString(memPointer);
					arrayCode[i][2] = Integer.toString(0);
					break;
				case ']':
					arrayCode[i][0] = "end";
					break;
				case '>':
					memPointer++;
					arrayCode[i][0] = "fwd";
					break;
				case '<':
					memPointer--;
					arrayCode[i][0] = "bck";
					break;
				case '.':
					arrayCode[i][0] = "print";
					arrayCode[i][1] = Integer.toString(memPointer);
					break;
				case ',':
					arrayCode[i][0] = "input";
					arrayCode[i][1] = Integer.toString(memPointer);
					break;
				default:
						System.out.print("Err 4: Unrecognized Command: " );
						System.out.println(code.get(i).charAt(0) + " at character " + i+1);
						System.exit(0);
				}
			}
		}
	}
	
	public void runLines(int start, int finish) {
		for(int a = start - 1; a < finish; a++) {
			switch(arrayCode[a][0]) {
				case "clear":
					varMemory.clear(arrayCode[a][1]);
					System.out.println("Line = " + (a+1));
					break;
				case "incr":
					varMemory.incr(arrayCode[a][1]);
					System.out.println("Line = " + (a+1));
					break;
				case "decr":
					varMemory.decr(arrayCode[a][1]);
					System.out.println("Line = " + (a+1));
					break;
				case "while":
					int whileStart = a;
					int whileLevel = 0;
					int whileEnd = a+1;
					boolean ended = false;
					for(int b = whileStart + 1; b < finish; b++) {
						if(arrayCode[b][0].equals("while")) {
							whileLevel++;
						} else if(arrayCode[b][0].equals("end") && whileLevel == 0) {
							whileEnd = b;
							ended = true;
							break;
						} else if(arrayCode[b][0].equals("end") && whileLevel != 0) {
							whileLevel--;
						}
					}
					if(ended == false) {
						System.out.println("Err 3: No Endpoint for While Loop on line " + (a+1) + " Detected");
						System.exit(0);
						break;
					}
					
					while(varMemory.getVarValue(arrayCode[a][1]) != Integer.parseInt(arrayCode[a][2])) {
						this.runLines(whileStart + 2, whileEnd);
					}
					
					a = whileEnd;
					
					break;
				case "end":
					break;
				case "fwd":
					break;
				case "bck":
					break;
				case "print":
					System.out.println((char) varMemory.getVarValue(arrayCode[a][1]));
					break;
				case "input":
				try {
					System.out.println("Input: ");
					String input = br.readLine();
					int inputInt = ((int) input.charAt(0))-48;
					varMemory.setVarValue(arrayCode[a][1], inputInt);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
				default:
					System.out.println("Err 5: Unrecognized Command During Run");
					System.exit(0);
			}
		}
	}
	
	public void startRun() {
		this.runLines(1, code.size());
	}
	
}
