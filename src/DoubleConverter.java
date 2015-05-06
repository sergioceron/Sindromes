
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
/**
 * Project Name: Sindromes
 * Project Url: http://www.dotrow.com/projects/java/jcase
 * Author: Sergio Ceron
 * Version: 1.0
 * Date: 16/10/13 08:15 PM
 * Desc:
 */
@ManagedBean
@FacesConverter(value = "doubleConverter")
public class DoubleConverter implements Converter {

    public DoubleConverter() {
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return Double.valueOf(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value + "";
    }
}