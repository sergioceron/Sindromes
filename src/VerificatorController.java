/**
 * Created with IntelliJ IDEA.
 * User: sergio
 * Date: 30/05/13
 * Time: 04:26 PM
 * To change this template use File | Settings | File Templates.
 */

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.UploadedFile;
import org.underserver.jbigmining.*;
import org.underserver.jbigmining.classifiers.Gamma;
import org.underserver.jbigmining.classifiers.GammaDummy;
import org.underserver.jbigmining.exceptions.ParserException;
import org.underserver.jbigmining.parsers.ARFFParser;
import org.underserver.jbigmining.parsers.SmartParser;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@SessionScoped
public class VerificatorController {
    private FileManager fileManagerPreloaded = new LocalFileManager("./preloaded");
    private FileManager fileManagerTrain = new LocalFileManager("./train");
    private FileManager fileManagerTest = new LocalFileManager("./test");

    private AlgorithmModel algorithmModel;

    private Algorithm selectedAlgorithm;
    private Algorithm configureAlgorithm;

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

    private String selectedBank;

    private Map<String, DualSet> selectedSet = new HashMap<String, DualSet>();

    private List<PreloadedSet> preloadedSets;

    public VerificatorController() {
        preloadedSets = new ArrayList<PreloadedSet>();
        File directory = new File("./preloaded");
        for (File file : directory.listFiles()) {
            if( file.isDirectory() ) {
                File folder = new File(file.getPath() + "/Split Data/");
                PreloadedSet preloadedSet = new PreloadedSet(file.getName());
                List<DualSet> sets = new ArrayList<DualSet>();
                for (String s : folder.list()) {
                    if(s.endsWith(".arff")) {
                        if (s.startsWith("Training")) {
                            String suffix = s.substring(s.indexOf("_"), s.indexOf("."));
                            sets.add(new DualSet(file.getName(), s, "Testing" + suffix + ".arff"));
                        }
                    }
                }
                preloadedSet.setFiles(sets);
                preloadedSets.add(preloadedSet);
            }
        }
    }

    public String login() {
        List<Algorithm> algorithms = new ArrayList<Algorithm>();
        if (key.equals("mdk2")) {
            algorithms.add(new Gamma());
            algorithmModel = new AlgorithmModel(new ArrayList<Algorithm>(algorithms));
            return "success";
        } else if (key.equals("v3c1")) {
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


    public void proccess(){
        train();
        classify();
    }

    public void train() {
        try {
            if( step == 1 ) {
                trainFile = fileManagerTrain.read(trainName);
            } else if ( step == 2 ){
                trainFile = fileManagerPreloaded.read(selectedSet.get(selectedBank).getDataSet() + "/Split Data/" + selectedSet.get(selectedBank).getTraining());
            }

            Parser trainParser = new SmartParser(trainFile);
            dataSet = trainParser.parse();

            selectedAlgorithm.setTrainSet(dataSet);
            selectedAlgorithm.train();
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }

    public void classify() {
        try {
            if( step == 1 ) {
                testFile = fileManagerTest.read(testName);
            } else if ( step == 2 ){
                testFile = fileManagerPreloaded.read(selectedSet.get(selectedBank).getDataSet() + "/Split Data/" + selectedSet.get(selectedBank).getTesting());
            }

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

    public List<PreloadedSet> getPreloadedSets() {
        return preloadedSets;
    }

    public void setPreloadedSets(List<PreloadedSet> preloadedSets) {
        this.preloadedSets = preloadedSets;
    }

    public Map<String, DualSet> getSelectedSet() {
        return selectedSet;
    }

    public void setSelectedSet(Map<String, DualSet> selectedSet) {
        this.selectedSet = selectedSet;
    }

    public void onTabChange(TabChangeEvent event) {
        selectedBank = ((PreloadedSet)event.getData()).getName();
    }

    public void onRowSelect(SelectEvent event) {
        DualSet dset = (DualSet) event.getObject();
        selectedSet.put(selectedBank, dset);
    }
    public String[] getAttributes() {
        List<Attribute> attributeList = dataSet.getAttributes();
        String[] attributes = new String[attributeList.size()];
        for (int i = 0; i < attributeList.size(); i++) {
            attributes[i] = attributeList.get(i).getName();
        }
        return attributes;
    }
}
