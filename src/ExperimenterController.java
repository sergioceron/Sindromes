/**
 * Created with IntelliJ IDEA.
 * User: sergio
 * Date: 30/05/13
 * Time: 04:26 PM
 * To change this template use File | Settings | File Templates.
 */

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;
import org.sg.recognition.*;
import org.sg.recognition.classifiers.*;
import org.sg.recognition.filters.*;
import org.sg.recognition.validations.HoldOutValidation;
import org.sg.recognition.validations.KFoldCrossValidation;
import org.sg.recognition.validations.LeaveOneOutValidation;
import org.sg.recognition.validations.SuppliedSetValidation;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.*;
import java.util.*;

@ManagedBean
@SessionScoped
public class ExperimenterController {
	private Dao<DataSet<Double>> dao = new MongoDataSetDao("datasets");
	private DataSet<Double> dataSet;

	private FileManager fileManager = new LocalFileManager("./train");//new S3Manager("bancosfundamental"); // new LocalFileManager("./train");
	private FileManager tempManager = new LocalFileManager("./temp");//new S3Manager("bancostemp"); // new LocalFileManager("./temp");

	private AlgorithmModel algorithmModel;
	private DualListModel<Filter> filterModel;

	private Algorithm[] selectedAlgorithms;
	private Algorithm configureAlgorithm;
	private List<Filter> selectedFilters;

	private ValidationMethod validationMethod;

	private UploadedFile uploadTestFile;

	private String trainName;

	private int step = 0;
	private int configId = 1;  // 1=only k, 2=binary filter
	private int nTest = 1;

	private List<Pattern> resultClassify = new ArrayList<Pattern>();
	private List<ValidationResult> resultValidation = new ArrayList<ValidationResult>();

	private int folds = 10;
	private int percent = 70;

	private int validation = 4;

	private CartesianChartModel validationChart;

	public ExperimenterController() {
		List<Algorithm> algorithms = new ArrayList<Algorithm>();
		algorithms.add(new mNN());
		algorithms.add(new mNNMin());
		algorithms.add(new kNN());
		algorithms.add(new Euclidean());
		algorithms.add(new EuclideanMod());

		algorithmModel = new AlgorithmModel(new ArrayList<Algorithm>(algorithms));

		List<Filter> filters = new ArrayList<Filter>();
		filters.add( new NormalizeFilter() );
		filters.add( new DecimalFilter() );
		filters.add( new IntegerFilter() );
		filters.add( new BinaryFilter() );
		filters.add( new BinaryHotFilter() );
		filters.add( new GrayFilter() );

		selectedFilters = new ArrayList<Filter>();
		filterModel = new DualListModel<Filter>(filters, selectedFilters);

	}

	public void step(int id) {
	    step = id;
    }

	public void next() {
		if( step < 3 )
			step = step + 1;
	}
	public void back() {
		if( step > 0 )
			step = step - 1;
	}

	public void uploadFile(FileUploadEvent event){
		UploadedFile uploadedFile = event.getFile();
		fileManager.save(uploadedFile);
	}

	public void settings(){
		dataSet = dao.load(DataSet.class, trainName);
		if( dataSet.getAttributes() == null ) {
			File trainFile = fileManager.read(trainName);
			Parser<Double> parser = new ArffParser<Double>(trainFile);
			dataSet = parser.parse();
			dataSet.setName(trainName);
			dao.save(dataSet);
		}
		step = 2;
	}

	public void validate(){
		switch( validation ) {
			case 1:
				validationMethod = new SuppliedSetValidation(dataSet);
				break;
			case 2:
				tempManager.save(uploadTestFile);
				File testFile = tempManager.read(uploadTestFile.getFileName());
				Parser<Double> parser = new ArffParser<Double>(testFile);
				validationMethod = new SuppliedSetValidation(parser.parse());
				break;
			case 3:
				validationMethod = new LeaveOneOutValidation();
				break;
			case 4:
				validationMethod = new KFoldCrossValidation(folds);
				break;
			case 5:
				validationMethod = new HoldOutValidation(percent);
				break;
			default:
				validationMethod = new KFoldCrossValidation(10);
				break;
		}

		resultValidation.clear();

		for( Algorithm algorithm : selectedAlgorithms ){

			validationMethod.setDataSet(dataSet);
			validationMethod.setAlgorithm(algorithm);

			ValidationResult validationResult = new ValidationResult();
			validationResult.setAlgorithm(algorithm);

			double performanceProm = 0d;
			double rvpProm = 0d;
			double rfpProm = 0d;
			double timeProm = 0l;

			for( int n = 0; n < nTest; n++ ) {
				long start = System.currentTimeMillis();

				validationMethod.validate();

				double performance = 0d;
				double rvp = 0d;
				double rfp = 0d;

				List<Metrics> results =  validationMethod.getResults();
				for( int i = 0; i < results.size(); i++ ) {
					Metrics result = results.get(i);
					performance += result.getPerformance() * 100;
					rvp += result.getRvp();
					rfp += result.getRfp();
				}
				performance /= results.size();
				rvp /= results.size();
				rfp /= results.size();
				System.out.printf("per: %.4f, rvp: %.4f, rfp: %.4f <-- %s\n", performance,
						rvp, rfp, algorithm.getName());

				long end = System.currentTimeMillis();
				ValidationResult partial = new ValidationResult();
				partial.setAlgorithm(algorithm);
				partial.setIndexTest(n);
				partial.setPerformance(performance);
				partial.setRvp(rvp);
				partial.setRfp(rfp);
				partial.setTime((double)( end - start ) / 1000d);

				performanceProm += performance;
				rvpProm += rvp;
				rfpProm += rfp;
				timeProm += (double)( end - start ) / 1000d;

				validationResult.addPartial(partial);
			}

			validationResult.setPerformance(performanceProm/(double)nTest);
			validationResult.setRvp(rvpProm/(double)nTest);
			validationResult.setRfp(rfpProm/(double)nTest);
			validationResult.setTime(timeProm/(double)nTest);

			resultValidation.add(validationResult);
		}

		createCategoryModel(resultValidation);

		step = 3;
	}

	private void createCategoryModel(List<ValidationResult> resultValidation) {
		validationChart = new CartesianChartModel();

		for( ValidationResult validationResult : resultValidation ){
			LineChartSeries chart = new LineChartSeries();
			chart.setLabel(validationResult.getAlgorithm().getName());
			for( ValidationResult partial : validationResult.getPartials() ) {
				chart.set(partial.getIndexTest() + "", partial.getPerformance());
			}
			validationChart.addSeries(chart);
		}
	}

	public FileManager getFileManager() {
		return fileManager;
	}

	public void setFileManager( FileManager fileManager ) {
		this.fileManager = fileManager;
	}

	public AlgorithmModel getAlgorithmModel() {
		return algorithmModel;
	}

	public void setAlgorithmModel( AlgorithmModel algorithmModel ) {
		this.algorithmModel = algorithmModel;
	}

	public Algorithm[] getSelectedAlgorithms() {
		return selectedAlgorithms;
	}

	public void setSelectedAlgorithms( Algorithm[] selectedAlgorithms ) {
		this.selectedAlgorithms = selectedAlgorithms;
	}

	public Algorithm getConfigureAlgorithm() {
		return configureAlgorithm;
	}

	public void setConfigureAlgorithm( Algorithm configureAlgorithm ) {
		this.configureAlgorithm = configureAlgorithm;
	}

	public DualListModel<Filter> getFilterModel() {
		return filterModel;
	}

	public void setFilterModel( DualListModel<Filter> filterModel ) {
		this.filterModel = filterModel;
	}

	public UploadedFile getUploadTestFile() {
		return uploadTestFile;
	}

	public void setUploadTestFile( UploadedFile uploadTestFile ) {
		this.uploadTestFile = uploadTestFile;
	}

	public ValidationMethod getValidationMethod() {
		return validationMethod;
	}

	public void setValidationMethod( KFoldCrossValidation validationMethod ) {
		this.validationMethod = validationMethod;
	}

	public List<Pattern> getResultClassify() {
		return resultClassify;
	}

	public void setResultClassify( List<Pattern> resultClassify ) {
		this.resultClassify = resultClassify;
	}

	public List<ValidationResult> getResultValidation() {
		return resultValidation;
	}

	public void setResultValidation( List<ValidationResult> resultValidation ) {
		this.resultValidation = resultValidation;
	}

	public String getTrainName() {
		return trainName;
	}

	public void setTrainName( String trainName ) {
		this.trainName = trainName;
	}

	public int getStep() {
		return step;
	}

	public void setStep( int step ) {
		this.step = step;
	}

	public int getConfigId() {
		return configId;
	}

	public void setConfigId( int configId ) {
		this.configId = configId;
	}

	public int getnTest() {
		return nTest;
	}

	public void setnTest( int nTest ) {
		this.nTest = nTest;
	}

	public int getFolds() {
		return folds;
	}

	public void setFolds( int folds ) {
		this.folds = folds;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent( int percent ) {
		this.percent = percent;
	}

	public int getValidation() {
		return validation;
	}

	public void setValidation( int validation ) {
		this.validation = validation;
	}

	public CartesianChartModel getValidationChart() {
		return validationChart;
	}

	public void setValidationChart( CartesianChartModel validationChart ) {
		this.validationChart = validationChart;
	}

	public DataSet<Double> getDataSet() {
		return dataSet;
	}

	public void setDataSet( DataSet<Double> dataSet ) {
		this.dataSet = dataSet;
	}
}
