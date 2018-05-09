package Network.GridComputing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

import lib.lang.PathTreat;

public class FileStreamProvider extends StreamProvider {
	protected File baseDir;

	public FileStreamProvider() {
		this(PathTreat.getCurrentDir());
	}

	public FileStreamProvider(String dir) {
		this(new File(dir));
	}

	public FileStreamProvider(File dir) {
		baseDir = dir;
		baseDir.mkdirs();
	}

	public FileStreamProvider(Path path){
		this(path.toFile());
	}

	protected File toFile(String path) {
		File file = new File(path);
		if (!file.isAbsolute()) {
			file = new File(baseDir, path);
		}
		return file;
	}

	@Override
	public OutputStream getOutputStream(String path) throws IOException {
		File file = toFile(path);
		if (!baseDir.equals(file.getParentFile())) {
			file.getParentFile().mkdirs();
		}
		/*
		 * TODO: file overwriting prompt
		if (file.exists()) {
			System.out.println("==============================================");
			System.out.print(file +" is already exits. Do you want to overwrite it? [y/1/n/0]\n> ");
			int bool = System.in.read();
			System.out.println("\n\n" + bool +"\n\n");
			System.out.println(System.in.read());

			if (bool != '1' && bool != 'Y' && bool != 'y')
				throw new FileAlreadyExistsException(file.getAbsolutePath());
		}
		*/
		return new FileOutputStream(file);
	}
}
