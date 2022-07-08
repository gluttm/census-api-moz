package mz.co.truetech.utils;

import java.io.File;
import java.nio.file.Path;

public class FileUtil {
	
	public FileUtil() {
		// TODO Auto-generated constructor stub
	}

	public final static String folderPath = "incoming-files//";
	public final static Path filePath = Path.of(folderPath);

	public void createFolderIfDoenstExists()
	{
		File directory = new File(FileUtil.folderPath);
		if(!directory.exists()) {
			directory.mkdir();
		}
	}
}
