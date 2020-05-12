package ourplugin.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Util class for loading and saving custom log messages in file 
 * 
 */
public class LogHelperClass {
	
	private static LogHelperClass logHelperClass = null;
	
	private String logFilePath = "";
	
	public static LogHelperClass getLogHelperClass() {
		if (logHelperClass == null) { 
			logHelperClass = new LogHelperClass();	
		}	
		return logHelperClass;
	}
	
	private LogHelperClass() {
		
	}
	
	public void saveToFile(List<String> logMessages) throws IOException {
		// Files.write(Paths.get(logFilePath), logMessages);
	}
	
	public List<String> loadFromFile() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(logFilePath));
		return lines;
	}

	public String getLogFilePath() {
		return logFilePath;
	}

	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}

}
