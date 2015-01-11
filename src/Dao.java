
import java.io.Serializable;

/**
 * Project Name: Sindromes
 * Project Url: http://www.dotrow.com/projects/java/jcase
 * Author: Sergio Ceron
 * Version: 1.0
 * Date: 3/11/13 03:49 PM
 * Desc:
 */
public interface Dao<T> {
	public void save( T entity );
	public T load( Class clazz, Serializable id );
}
