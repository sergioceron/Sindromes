import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

/**
 * Project Name: Sindromes
 * Project Url: http://www.dotrow.com/projects/java/jcase
 * Author: Sergio Ceron
 * Version: 1.0
 * Date: 24/10/13 10:19 PM
 * Desc:
 */

public class PreloadedSetModel extends ListDataModel<DualSet> implements SelectableDataModel<DualSet> {

	public PreloadedSetModel() {
	}

	public PreloadedSetModel(List<DualSet> data) {
		super(data);
	}

	@Override
	public DualSet getRowData(String rowKey) {
		//In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data

		List<DualSet> sets = (List<DualSet>) getWrappedData();

		for(DualSet set : sets) {
			if(rowKey.equals(set.getId()))
				return set;
		}

		return null;
	}

	@Override
	public Object getRowKey(DualSet set) {
		return set.getId();
	}
}