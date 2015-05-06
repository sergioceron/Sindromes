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
import org.underserver.jbigmining.*;
import org.underserver.jbigmining.classifiers.*;
import org.underserver.jbigmining.exceptions.ParserException;
import org.underserver.jbigmining.filters.*;
import org.underserver.jbigmining.parsers.ARFFParser;
import org.underserver.jbigmining.parsers.SmartParser;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@SessionScoped
public class VerificatorController {
    private FileManager fileManagerTrain = new LocalFileManager("./train");
    private FileManager fileManagerTest = new LocalFileManager("./test");

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
    private String key;

    private List<ComparatorEntity> resultClassify = new ArrayList<ComparatorEntity>();
    private List<ValidationResult> resultValidation = new ArrayList<ValidationResult>();

    private Pattern classifyPattern;

    private DataSet dataSet;

    public VerificatorController() {

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new NormalizeFilter());
        filters.add(new IntegerFilter());
        filters.add(new BinaryFilter());
        filters.add(new BinaryHotFilter());
        filters.add(new GrayFilter());

        selectedFilters = new ArrayList<Filter>();
        filterModel = new DualListModel<Filter>(filters, selectedFilters);

    }

    public String login(){
        List<Algorithm> algorithms = new ArrayList<Algorithm>();
        if( key.equals("mdk2") ){
            algorithms.add(new Gamma());
            algorithmModel = new AlgorithmModel(new ArrayList<Algorithm>(algorithms));
            return "success";
        } else if( key.equals("v3c1") ){
            algorithms.add(new GammaDummy());
            algorithmModel = new AlgorithmModel(new ArrayList<Algorithm>(algorithms));
            return "success";
        }
        return "error";
    }

    public void step(int id) {
        step = id;
    }

    public void next() {
        if (step < 3)
            step = step + 1;
    }

    public void back() {
        if (step > 0)
            step = step - 1;
    }

    public void uploadFileTrain(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        fileManagerTrain.save(uploadedFile);
    }

    public void uploadFileTest(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        fileManagerTest.save(uploadedFile);
    }

    public void train() {
        try {
            trainFile = fileManagerTrain.read(trainName);

            Parser trainParser = new SmartParser(trainFile);
            dataSet = trainParser.parse();

            selectedAlgorithm.setTrainSet(dataSet);
            selectedAlgorithm.train();

            step = 2;
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }

    public void classify() {
        try {
            testFile = fileManagerTest.read(testName);

            ARFFParser testParser = new ARFFParser(testFile);
            DataSet testSet = new DataSet(dataSet);
            testParser.readData(testSet);

            resultClassify.clear();
            for (Pattern p : testSet) {
                int clazz = ((Classifier) (selectedAlgorithm)).classify(p);
                int real = -1;
                p.setClassIndex(clazz);
                resultClassify.add(new ComparatorEntity(p, real, p.getClassIndex() == real));
            }

            step = 3;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String className(int classIndex) {
        return dataSet.getClasses().getValues().get(classIndex);
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

    public void setAlgorithmModel(AlgorithmModel algorithmModel) {
        this.algorithmModel = algorithmModel;
    }

    public Algorithm getSelectedAlgorithm() {
        return selectedAlgorithm;
    }

    public void setSelectedAlgorithm(Algorithm selectedAlgorithm) {
        this.selectedAlgorithm = selectedAlgorithm;
    }

    public Algorithm getConfigureAlgorithm() {
        return configureAlgorithm;
    }

    public void setConfigureAlgorithm(Algorithm configureAlgorithm) {
        this.configureAlgorithm = configureAlgorithm;
    }

    public DualListModel<Filter> getFilterModel() {
        return filterModel;
    }

    public void setFilterModel(DualListModel<Filter> filterModel) {
        this.filterModel = filterModel;
    }

    public File getTrainFile() {
        return trainFile;
    }

    public void setTrainFile(File trainFile) {
        this.trainFile = trainFile;
    }

    public File getTestFile() {
        return testFile;
    }

    public void setTestFile(File testFile) {
        this.testFile = testFile;
    }

    public UploadedFile getUploadTrainFile() {
        return uploadTrainFile;
    }

    public void setUploadTrainFile(UploadedFile uploadTrainFile) {
        this.uploadTrainFile = uploadTrainFile;
    }

    public UploadedFile getUploadTestFile() {
        return uploadTestFile;
    }

    public void setUploadTestFile(UploadedFile uploadTestFile) {
        this.uploadTestFile = uploadTestFile;
    }

    public List<ComparatorEntity> getResultClassify() {
        return resultClassify;
    }

    public void setResultClassify(List<ComparatorEntity> resultClassify) {
        this.resultClassify = resultClassify;
    }

    public List<ValidationResult> getResultValidation() {
        return resultValidation;
    }

    public void setResultValidation(List<ValidationResult> resultValidation) {
        this.resultValidation = resultValidation;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Pattern getClassifyPattern() {
        return classifyPattern;
    }

    public void setClassifyPattern(Pattern classifyPattern) {
        this.classifyPattern = classifyPattern;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String[] getAttributes(){
        List<Attribute> attributeList = dataSet.getAttributes();
        String[] attributes = new String[attributeList.size()];
        for (int i = 0; i < attributeList.size(); i++) {
            attributes[i] = attributeList.get(i).getName();
        }
        return attributes;
    }
}
