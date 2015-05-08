/**
 * Created by sergio on 07/05/15.
 */
public class DualSet {
    private String dataSet;
    private String training;
    private String testing;
    private String url;
    private String description;

    public DualSet(String dataSet, String training, String testing) {
        this.dataSet = dataSet;
        this.training = training;
        this.testing = testing;
    }

    public String getId(){
        return dataSet + training;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getTraining() {
        return training;
    }

    public void setTraining(String training) {
        this.training = training;
    }

    public String getTesting() {
        return testing;
    }

    public void setTesting(String testing) {
        this.testing = testing;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
