import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
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
public class S3Manager implements FileManager {
	private final static String ACCESS_KEY = "AKIAJBJ5NBKNSO4ONEQA";
	private final static String SECRET_KEY = "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";

	private String bucket = ".";

	private AmazonS3 s3;

	public S3Manager(String bucket) {
		this.bucket = bucket;
		this.s3 = new AmazonS3Client(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY));
	}

	public void save(UploadedFile file){
		try {
			InputStream in = file.getInputstream();

			s3.putObject(bucket, file.getFileName(), in, new ObjectMetadata());

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void remove(String name){
		s3.deleteObject(bucket, name);
	}

	public File read(String name){
		s3.getObject(
				new GetObjectRequest(bucket, name),
				new File(name)
		);
		return new File(name);
	}

	public List<String> list(){
		List<String> files = new ArrayList<String>();
		ObjectListing objects = s3.listObjects(bucket);
		do {
			for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
				String file = objectSummary.getKey();
				if( file.endsWith(".arff") ){
					files.add(file);
				}
			}
			objects = s3.listNextBatchOfObjects(objects);
		} while (objects.isTruncated());
		return files;
	}

}
