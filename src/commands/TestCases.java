package commands;

import data.FileSystem;

public class TestCases {
  
  private FileSystem fs;
  
  private Cd cd;
  private Pwd pwd;
  private Mkdir mkdir;
  private Ls ls;
  private Echo echo;
  private Man man;
  
  private String output;
  private String currentPath;
  
  public TestCases() {
    this.fs = FileSystem.getFileSys();
    this.cd = new Cd();
    this.pwd = new Pwd();
    this.mkdir = new Mkdir();
    this.ls = new Ls();
    this.echo =  new Echo();
    this.man = new Man();
  }
  
  public void setupEnviro() {
    String[] setupDirs = new String[1];
    
    //C Folder
    setupDirs[0] = "users";
    mkdir.MakeDirectory(setupDirs);
    setupDirs[0] = "pics";
    mkdir.MakeDirectory(setupDirs);
    setupDirs[0] = "Sys";
    mkdir.MakeDirectory(setupDirs);
    
    //users Folder
    setupDirs[0] = "C/users";
    cd.run(setupDirs, "cd C/users");
    setupDirs[0] = "desktop";
    mkdir.MakeDirectory(setupDirs);
    
    //desktop Folder
    setupDirs[0] = "C/users/desktop";
    cd.run(setupDirs, "cd C/users/desktop");    
    echo.run(setupDirs, "echo \"Hello TA\" > CSCB07");
    echo.run(setupDirs, "echo \"2+2=5\" > Hwk");
    setupDirs[0] = "../../";
    cd.run(setupDirs, "cd ../../");
    
    //pics Folder
    echo.run(setupDirs, "echo \"this is a picturefile indeed\" > pics/picfile");
    //setupDirs[0] = "pics";
    //cd.run(setupDirs, "cd pics");
    //echo.run(setupDirs, "echo \"Hello TA\" > CSCB07");


  }
  
  public void cdTestCases() {
    
    //Case 1: No Input
    String[] testCase1 = {};
    
    output = cd.run(testCase1, "cd");
    currentPath = pwd.run(testCase1, "pwd");
    
    if(output.equals("Error : No parameters provided : ")) 
      System.out.println("Case #1 Passed");
    else
      System.out.println("Case #1 Failed");
    
    
   //Case 2: Change Directory to pics
    String[] testCase2 = new String[1];
    testCase2[0] = "pics";
        
    output = cd.run(testCase2, "cd pics");
    currentPath = pwd.run(testCase1, "pwd");
    
    
    if(currentPath.equals("C/pics")) 
      System.out.println("Case #2 Passed");
    else
      System.out.println("Case #2 Failed");
    
    //Case 3: Change Directory back one
    testCase2[0] = "..";
    
    output = cd.run(testCase2, "cd ..");
    currentPath = pwd.run(testCase1, "pwd");
    
    
    if(currentPath.equals("C")) 
      System.out.println("Case #3 Passed");
    else
      System.out.println("Case #3 Failed");
    
    //Case 4: Change Directory to users
    testCase2[0] = "users";
    
    output = cd.run(testCase2, "cd users");
    currentPath = pwd.run(testCase1, "pwd");
    
    
    if(currentPath.equals("C/users")) 
      System.out.println("Case #4 Passed");
    else
      System.out.println("Case #4 Failed");
  }
  
  public void manTestCases() {
	  
	  String output;
	  String[] testInput = {"ls"};
	  
	  System.out.println("Testing Command: man");
	  
	  //Case 1: Get the documentation for ls
	  output = man.run(testInput, "man");
	  
	  if (output.contains("Command: ls")) {
		  System.out.println("Case #1 Passed");
	  }else {
		  System.out.println("Case #1 Failed");
	  }
	  
	  //Case 2: Get documentation for invalid input
	  testInput[0] = "false command";
	  output = man.run(testInput, "man");
	  
	  if (output.contains("Invalid Argument")) {
		  System.out.println("Case #2 Passed");
	  }else {
		  System.out.println("Case #2 Failed");
	  }
	  
	  //Case 3: Get documetnation for multiple arguments (not supported)
	  String[] multipleArguments = {"ls", "cd"};
	  output = man.run(testInput, "man");
	  
	  if (output.contains("Invalid Argument")) {
		  System.out.println("Case #3 Passed");
	  }else {
		  System.out.println("Case #3 Failed");
	  }
  }
	
	
}