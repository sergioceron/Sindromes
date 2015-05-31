import org.underserver.jbigmining.core.Pattern;

/**
 * Created by sergio on 22/04/15.
 */
public class ComparatorEntity {
    private Pattern pattern;
    private int realClass;
    private boolean correct = false;

    public ComparatorEntity(Pattern pattern, int realClass, boolean correct) {
        this.pattern = pattern;
        this.realClass = realClass;
        this.correct = correct;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public int getRealClass() {
        return realClass;
    }

    public void setRealClass(int realClass) {
        this.realClass = realClass;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
