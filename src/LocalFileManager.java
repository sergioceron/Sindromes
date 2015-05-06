import org.primefaces.model.UploadedFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Project Name: Sindromes
 * Project Url: http://www.dotrow.com/projects/java/jcase
 * Author: Sergio Ceron
 * Version: 1.0
 * Date: 1/08/13 02:33 PM
 * Desc:
 */
public class LocalFileManager implements FileManager {
	private String bucket = ".";
	private File folder;

	public LocalFileManager() {
	}

	public LocalFileManager( String bucket ) {
		this.bucket = bucket;
		folder = new File( bucket );
		if( !folder.exists() ) {
			System.out.printf( "Folder %s doesn't exists, creating %s\n", bucket, folder.getAbsolutePath()  );
			folder.mkdir();
		}
	}

	public void save(UploadedFile file){
		OutputStream out = null;
		File save = null;
		try {
			InputStream in = file.getInputstream();
			save = new File(bucket + "/" + file.getFileName());
			out = new FileOutputStream(save);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			System.out.println("File copied.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void remove(String name){
		File file = new File(bucket + "/" + name);
		file.delete();
	}

	public File read(String name){
		return new File(bucket + "/" + name);
	}

	public List<String> list(){
		File directory = new File(bucket);
		List<String> files = new ArrayList<String>();
		for( File file : directory.listFiles() ){
			if( !file.isDirectory() && (file.getName().endsWith(".arff")) ){
				files.add(file.getName());
			}
		}
		return files;
	}

}
