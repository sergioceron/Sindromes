/**
 * Created with IntelliJ IDEA.
 * User: sergio
 * Date: 30/05/13
 * Time: 04:26 PM
 * To change this template use File | Settings | File Templates.
 */

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.UploadedFile;
import org.sg.recognition.*;
import org.sg.recognition.classifiers.Euclidean;
import org.sg.recognition.classifiers.EuclideanMod;
import org.sg.recognition.classifiers.kNN;
import org.sg.recognition.classifiers.mNN;
import org.sg.recognition.filters.*;
import org.sg.recognition.validations.KFoldCrossValidation;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@SessionScoped
public class VerificatorController {
	private Dao<DataSet<Double>> dao = new MongoDataSetDao("datasets");

	private FileManager fileManagerTrain = new LocalFileManager("./train");//new S3Manager("bancosfundamental"); // new LocalFileManager("./train");
	private FileManager fileManagerTest  = new LocalFileManager("./test");//new S3Manager("bancosprueba"); // new LocalFileManager("./test");

	private AlgorithmModel algorithmModel;
	private DualListModel<Filter> filterModel;

	private Algorithm selectedAlgorithm;
	private Algorithm configureAlgorithm;
	private List<Filter> selectedFilters;

	private File trainFile;
	private File testFile;

	private UploadedFile uploadTrainFile;
	private UploadedFile uploadTestFile;

	private String trainName;
	private String testName;

	private int step = 0;
	private int type = 1;

	private List<Pattern> resultClassify = new ArrayList<Pattern>();
	private List<ValidationResult> resultValidation = new ArrayList<ValidationResult>();

	private Pattern<Double> classifyPattern;

	private DataSet<Double> dataSet;

	public VerificatorController() {
		List<Algorithm> algorithms = new ArrayList<Algorithm>();
		algorithms.add(new mNN());
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

	public void uploadFileTrain(FileUploadEvent event){
		UploadedFile uploadedFile = event.getFile();
		fileManagerTrain.save(uploadedFile);
	}

	public void uploadFileTest(FileUploadEvent event){
		UploadedFile uploadedFile = event.getFile();
		fileManagerTest.save(uploadedFile);
	}

    public void train() {
	    dataSet = dao.load(DataSet.class, trainName);
	    if( dataSet.getAttributes() == null ) {
		    trainFile = fileManagerTrain.read(trainName);
		    Parser<Double> parser = new ArffParser<Double>(trainFile);
		    dataSet = parser.parse();
		    dataSet.setName(trainName);
		    dao.save(dataSet);
	    }

	    selectedAlgorithm.setTrainPatterns(dataSet.getInstances());
	    selectedAlgorithm.train();

	    classifyPattern = new Pattern<Double>();
	    for( int i = 0; i < dataSet.getAttributes().size(); i++ ) {
		    classifyPattern.addFeature(0d);
	    }

	    step = 2;
    }

    public void classify() {
	    List<Pattern<Double>> instances = new ArrayList<Pattern<Double>>();
	    if( type == 2 ){
	        testFile = fileManagerTest.read(testName);
	        Parser<Double> parser = new ArffParser<Double>(testFile);
	        DataSet<Double> dataSet1 = parser.parse();
	        dataSet1.setName(testName);
		    instances = dataSet1.getInstances();
	    } else {
		    instances.add(classifyPattern);
	    }

	    resultClassify.clear();
	    for( Pattern p : instances ){
		    if( selectedAlgorithm instanceof Classifier ){
			    int clazz = ((Classifier)(selectedAlgorithm)).classify(p);
			    p.setClase(clazz);
			    resultClassify.add(p);
		    }
	    }

	    step = 3;
    }

	public void onTabChange(TabChangeEvent event) {
		String id = event.getTab().getId();
		if( id.equals("i1") ) type = 1;
		if( id.equals("i2") ) type = 2;
	}

	public String className(int clazzIndex){
		String name = "";
		for( String clazz : dataSet.getClasses().keySet() ){
			Integer index = dataSet.getClasses().get(clazz);
			if( index == clazzIndex ){
				name = clazz;
				break;
			}
		}
		return name;
	}

	public FileManager getFileManagerTrain() {
		return fileManagerTrain;
	}

	public FileManager getFileManagerTest() {
		return fileManagerTest;
	}

	public AlgorithmModel getAlgorithmModel() {
		return algorithmModel;
	}

	public void setAlgorithmModel( AlgorithmModel algorithmModel ) {
		this.algorithmModel = algorithmModel;
	}

	public Algorithm getSelectedAlgorithm() {
		return selectedAlgorithm;
	}

	public void setSelectedAlgorithm( Algorithm selectedAlgorithm ) {
		this.selectedAlgorithm = selectedAlgorithm;
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

	public File getTrainFile() {
		return trainFile;
	}

	public void setTrainFile( File trainFile ) {
		this.trainFile = trainFile;
	}

	public File getTestFile() {
		return testFile;
	}

	public void setTestFile( File testFile ) {
		this.testFile = testFile;
	}

	public UploadedFile getUploadTrainFile() {
		return uploadTrainFile;
	}

	public void setUploadTrainFile( UploadedFile uploadTrainFile ) {
		this.uploadTrainFile = uploadTrainFile;
	}

	public UploadedFile getUploadTestFile() {
		return uploadTestFile;
	}

	public void setUploadTestFile( UploadedFile uploadTestFile ) {
		this.uploadTestFile = uploadTestFile;
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

	public String getTestName() {
		return testName;
	}

	public void setTestName( String testName ) {
		this.testName = testName;
	}

	public int getStep() {
		return step;
	}

	public void setStep( int step ) {
		this.step = step;
	}

	public Pattern<Double> getClassifyPattern() {
		return classifyPattern;
	}

	public void setClassifyPattern( Pattern<Double> classifyPattern ) {
		this.classifyPattern = classifyPattern;
	}

	public DataSet<Double> getDataSet() {
		return dataSet;
	}
}
