import org.sg.recognition.Algorithm;
import org.sg.recognition.classifiers.Euclidean;
import org.sg.recognition.classifiers.kNN;
import org.sg.recognition.classifiers.mNN;
import org.sg.recognition.filters.BinaryFilter;
import org.sg.recognition.filters.BinaryHotFilter;
import org.sg.recognition.filters.Filter;
import org.sg.recognition.filters.GrayFilter;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.HashMap;
import java.util.Map;

/**
 * Project Name: Sindromes
 * Project Url: http://www.dotrow.com/projects/java/jcase
 * Author: Sergio Ceron
 * Version: 1.0
 * Date: 16/10/13 08:15 PM
 * Desc:
 */
@ManagedBean
@FacesConverter(value = "filterConverter")
public class FilterConverter implements Converter {
	private Map<String, Filter> filters = new HashMap<String, Filter>();

	public FilterConverter() {
		Filter[] _filters = { new BinaryFilter(), new BinaryHotFilter(), new GrayFilter() };
		for( Filter filter : _filters ){
			filters.put(filter.getName(), filter);
		}
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return filters.get(value);
	}
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if( value instanceof Filter ){
			return ((Filter) value).getName();
		}
		return null;
	}
}