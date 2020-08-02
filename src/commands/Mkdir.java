// **********************************************************
// Assignment2:
// Student1:
// UTORID user_name: santhos7
// UT Student #: 1006094673
// Author: Shawn Santhoshgeorge
//
// Student2:
// UTORID user_name: shaiskan
// UT Student #: 1006243940
// Author: Keshavaa Shaiskandan
//
// Student3:
// UTORID user_name: patelt26
// UT Student #: 1005904103
// Author: Tirth Patel
//
// Student4:
// UTORID user_name: pate1101
// UT Student #: 1006315765
// Author: Abhay Patel
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package commands;

import java.util.ArrayList;
import java.util.Arrays;

import data.FileSystemI;
import data.Node;
import errors.DirectoryException;
import errors.InvalidArgsProvidedException;

/**
 * Class Mkdir handles making directories anywhere in the filesystem
 */
public class Mkdir extends DirectoryManager implements CommandI {
	/**
	 * Declare instance variable of ArrayList to hold all arguments
	 */
	ArrayList<String> args;

	private RedirectionManager rManager;

	private Cd traverse;

	//String fullinput;
	String fileName;
	String[] newArgs = {""};
	String[] currentPath = {""};
	FileSystemI fs;

	/**
	 * Constructor of Mkdir to initialize error
	 */
	public Mkdir() {
		this.rManager = new RedirectionManager();
		traverse = new Cd();
	}

	/**
	 * Generic run method to call on method that does the work of creating
	 * directories
	 * 
	 * @param args      the string array of all arguments
	 * @param fullInput the string of the entire raw input provided by user in
	 *                  JShell
	 * @return String null always
	 */
	public String run(FileSystemI filesys, String[] arg, String fullInput, boolean val) {
	    //Seperates the parameters from everything else from the user input
		String[] args = rManager.setParams(fullInput);
		try {
			fs = filesys;
			rManager.isRedirectionableCommand(fullInput);
			return MakeDirectory(args, filesys, fullInput);
		} catch (InvalidArgsProvidedException e) {
			return e.getLocalizedMessage();
		}
	}

	/**
	 * Makes directories at locations in filesystem based on the path given
	 * 
	 * @param arguments  The string array of all arguments provided
	 * @return String  An error message, else null
	 */
	public String MakeDirectory(String[] arguments, FileSystemI filesys, String fullinput) 
		throws InvalidArgsProvidedException, DirectoryException{
		this.args = new ArrayList<String>(Arrays.asList(arguments));
		currentPath[0] = fs.getCurrentPath();
		checkArgs(fs, arguments, fullinput);
		checkRepitition();
		
		for (int i = 0; i < args.size(); i++){
			setPathAndFile(i);
			traverse.run(fs, newArgs, "cd " + newArgs[0], false);
			Node newNode = new Node.Builder(true, fileName).setParent(fs.getCurrent()).build();
			fs.addToDirectory(newNode);
			traverse.run(fs, currentPath, "cd " + currentPath[0], false);
		}
		return null;
	
	}

	@Override
	public boolean checkArgs(FileSystemI fs, String[] arguments, String fullInput) 
		throws InvalidArgsProvidedException {	
		if (args.size() == 0) {
			traverse.run(currentPath, fs);
			throw new InvalidArgsProvidedException("Error: Invalid Argument : Expected at least 1 argument");
		}
		for (int i = 0; i < args.size(); i++){
			setPathAndFile(i);
			if (!fs.isValidName(fileName)){
				traverse.run(currentPath, fs);
				throw new InvalidArgsProvidedException("Error: Invalid Directory : "  
				+ fileName + " is not a valid directory name");
			}
			if (!traverse.run(newArgs, fs)){
				traverse.run(currentPath, fs);
				throw new InvalidArgsProvidedException("Error: Invalid Directory : " 
				+ newArgs[0] + " is not a valid directory");
			}
		}
		return true;
	}

	public void setPathAndFile(int i){
		if (args.get(i).contains("/")){
			newArgs[0] = args.get(i).substring(0, args.get(i).lastIndexOf('/'));
			if (newArgs[0].equals("")){
				newArgs[0] = "/";
			}
			fileName = args.get(i).substring(args.get(i).lastIndexOf('/') + 1);
		}else{
			newArgs[0] = fs.getCurrentPath();
			fileName = args.get(i);
		}
	}

	public void checkRepitition() throws DirectoryException{ 
		for (int i = 0; i < args.size(); i++){
			setPathAndFile(i);
			traverse.run(fs, newArgs,  "cd " + newArgs[0], false);
			ArrayList<Node> parentList = fs.getCurrent().getList();
			for (int j = 0; j < parentList.size(); j++){
				if (parentList.get(j).getName().equals(fileName) && parentList.get(j).getisDir()){
					traverse.run(currentPath, fs);
					throw new DirectoryException("Invalid Directory: "  + 
					fileName + " already exists in " + newArgs[0]);
				}
			}
		}
	}

}
