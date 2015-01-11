import org.primefaces.model.UploadedFile;

import java.io.File;
import java.util.List;

/**
 * Project Name: Sindromes
 * Project Url: http://www.dotrow.com/projects/java/jcase
 * Author: Sergio Ceron
 * Version: 1.0
 * Date: 10/11/13 10:55 AM
 * Desc:
 */
public interface FileManager {
	public void save(UploadedFile file);
	public void remove(String name);
	public File read(String name);
	public List<String> list();
}
