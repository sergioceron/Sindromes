/**
 * Project Name: Sindromes
 * Project Url: http://www.dotrow.com/projects/java/jcase
 * Author: Sergio Ceron
 * Version: 1.0
 * Date: 24/10/13 10:19 PM
 * Desc:
 */
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;
import org.sg.recognition.Algorithm;
import org.sg.recognition.AlgorithmInformation;

public class AlgorithmModel extends ListDataModel<Algorithm> implements SelectableDataModel<Algorithm> {

	public AlgorithmModel() {
	}

	public AlgorithmModel(List<Algorithm> data) {
		super(data);
	}

	public AlgorithmInformation getInfo(Algorithm algorithm){
		return algorithm.getInformation();
	}

	@Override
	public Algorithm getRowData(String rowKey) {
		//In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data

		List<Algorithm> algorithms = (List<Algorithm>) getWrappedData();

		for(Algorithm algorithm : algorithms) {
			if(algorithm.getName().equals(rowKey))
				return algorithm;
		}

		return null;
	}

	@Override
	public Object getRowKey(Algorithm algorithm) {
		return algorithm.getName();
	}
}