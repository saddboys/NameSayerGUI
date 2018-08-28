package other;

import java.io.File;

//make all the initial files
public class Initialise {
	public static void makefolders() {
		new File(System.getProperty("user.dir")+"/temp").mkdirs();
		new File(System.getProperty("user.dir")+"/creations").mkdirs();
		new File(System.getProperty("user.dir")+"/thumb").mkdirs();
		
	}
}
