import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergio on 07/05/15.
 */
public class PreloadedSet {
    private String name;
    private PreloadedSetModel files = new PreloadedSetModel();

    public PreloadedSet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PreloadedSetModel getFiles() {
        return files;
    }

    public void setFiles(PreloadedSetModel files) {
        this.files = files;
    }

    public void setFiles(List<DualSet> files) {
        this.files = new PreloadedSetModel(files);
    }
}
