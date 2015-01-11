import com.mongodb.*;
import org.sg.recognition.DataSet;
import org.sg.recognition.Pattern;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project Name: Sindromes
 * Project Url: http://www.dotrow.com/projects/java/jcase
 * Author: Sergio Ceron
 * Version: 1.0
 * Date: 1/08/13 02:44 PM
 * Desc:
 */
public class MongoDataSetDao implements Dao<DataSet<Double>> {

	DBCollection collection;

	public MongoDataSetDao( String store ) {
		try {
			MongoClient mongoClient = new MongoClient( "localhost" , 27017 ); //new MongoClient( "ec2-54-200-247-135.us-west-2.compute.amazonaws.com" , 27017 );
			DB db = mongoClient.getDB("sindromes5"); // 10gen driver
			collection = db.getCollection(store);
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}

	@Override
	public void save( DataSet<Double> dataSet ) {
		BasicDBObjectBuilder builder = BasicDBObjectBuilder.start()
				.add("name", dataSet.getName());

		List<BasicDBObject> instances = new ArrayList<BasicDBObject>();
		for( Pattern<Double> pattern : dataSet.getInstances() ){
			BasicDBObject instance = new BasicDBObject("class", pattern.getClase());
			instance.append("features", pattern.getFeatures());
			instances.add(instance);
		}

		builder.add("instances", instances);
		builder.add("features", dataSet.getAttributes());

		BasicDBObject classes = new BasicDBObject();
		for( String clazz : dataSet.getClasses().keySet() ){
			classes.append(clazz, dataSet.getClasses().get(clazz));
		}

		builder.add("classes", classes);

		DBObject entity = builder.get();

		collection.insert(entity);
	}

	@Override
	public DataSet load( Class clazz, Serializable id ) {
		DataSet<Double> dataSet = new DataSet<Double>(id.toString());
		BasicDBObject query = new BasicDBObject("name", id.toString());

		DBCursor cursor = collection.find(query);
		try {
			while(cursor.hasNext()) {    // Just 1 time
				DBObject entity = cursor.next();
				List<Pattern<Double>> instances = new ArrayList<Pattern<Double>>();
				List<DBObject> patterns = (List<DBObject>) entity.get("instances");
				for(DBObject p : patterns){
					Pattern<Double> pattern = new Pattern<Double>();

					pattern.setClase((Integer) p.get("class"));
					pattern.setFeatures((List<Double>) p.get("features"));
					instances.add(pattern);
				}

				dataSet.setInstances(instances);
				dataSet.setAttributes((List<String>) entity.get("features"));

				DBObject classes = (DBObject) entity.get("classes");
				Map<String, Integer> classesMap = new HashMap<String, Integer>();
				for( String className : classes.keySet() ){
					classesMap.put(className, (Integer) classes.get(className));
				}

				dataSet.setClasses(classesMap);
			}
		} finally {
			cursor.close();
		}
		return dataSet;
	}


}
