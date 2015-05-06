
import org.underserver.jbigmining.Algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Name: Sindromes
 * Project Url: http://www.dotrow.com/projects/java/jcase
 * Author: Sergio Ceron
 * Version: 1.0
 * Date: 15/10/13 10:43 PM
 * Desc:
 */
public class ValidationResult {
	private Algorithm algorithm;
	private List<ValidationResult> partials;
	private int indexTest = 0;
	private double performance;
	private double rvp = 0d;
	private double rfp = 0d;
	private double time;

	public ValidationResult() {
		partials = new ArrayList<ValidationResult>();
	}

	public void addPartial(ValidationResult result){
		partials.add(result);
	}

	public List<ValidationResult> getPartials() {
		return partials;
	}

	public void setPartials( List<ValidationResult> partials ) {
		this.partials = partials;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm( Algorithm algorithm ) {
		this.algorithm = algorithm;
	}

	public int getIndexTest() {
		return indexTest;
	}

	public void setIndexTest( int indexTest ) {
		this.indexTest = indexTest;
	}

	public double getPerformance() {
		return performance;
	}

	public void setPerformance( double performance ) {
		this.performance = performance;
	}

	public double getRvp() {
		return rvp;
	}

	public void setRvp( double rvp ) {
		this.rvp = rvp;
	}

	public double getRfp() {
		return rfp;
	}

	public void setRfp( double rfp ) {
		this.rfp = rfp;
	}

	public double getTime() {
		return time;
	}

	public void setTime( double time ) {
		this.time = time;
	}
}
