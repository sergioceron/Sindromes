
import org.underserver.jbigmining.Algorithm;
import org.underserver.jbigmining.classifiers.Euclidean;
import org.underserver.jbigmining.classifiers.kNN;
import org.underserver.jbigmining.classifiers.mNN;

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
@FacesConverter(value = "algorithmConverter")
public class AlgorithmConverter implements Converter {
	private Map<String, Algorithm> algorithms = new HashMap<String, Algorithm>();

	public AlgorithmConverter() {
		Algorithm[] _algorithms = { new mNN(),	new kNN(),	new Euclidean() };
		for( Algorithm algorithm :_algorithms ){
			algorithms.put(algorithm.getName(), algorithm);
		}
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return algorithms.get(value);
	}
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if( value instanceof Algorithm ){
			return ((Algorithm) value).getName();
		}
		return null;
	}
}