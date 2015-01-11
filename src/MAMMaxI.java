import org.sg.recognition.Algorithm;
import org.sg.recognition.AlgorithmInformation;
import org.sg.recognition.Pattern;
import org.sg.recognition.utils.Matrix;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sergio
 * Date: 28/05/13
 * Time: 07:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class MAMMaxI extends Algorithm {

    private Integer[][] M;

    public MAMMaxI() {
        super("Memoria Morfologica Autoasociativa MIN");
    }

	@Override
	public AlgorithmInformation getInformation() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
    public void train() {
        List<Pattern> train = getTrainPatterns();

        int n = train.get(0).size();
        int m = train.size(); //train.get(0).size();
        int p = train.size();

        // Entrenamiento
        M = Matrix.fill(m, n, Integer.MAX_VALUE);
        for ( int t = 0; t < p; t++ ) {
            Pattern<Integer> pattern  = train.get(t);
	        Integer[] xM = pattern.getFeaturesAsVector();
	        Integer[] yM = pattern.getFeaturesAsVector();

	        Integer[][] zM = Y(xM, yM);
            M = Matrix.min(zM, M);
        }

    }

    public void run() {
        double partialPeformance = 0;

        //List<Pattern> test = getTestPatterns();
        // Recuperacion
        /*for ( int t = 0; t < test.size(); t++ ) {
            Pattern<Integer> pattern  = test.get(t);
	        Integer[] xM  = pattern.getFeaturesAsVector();
	        //Integer[] yM  = pattern.getFeaturesAsVector();

            Pattern<Integer> recuperado = new Pattern<Integer>( Z(M, xM) );

            List<Pattern<Integer>> closest = closestPattern(getTrainPatterns(), recuperado, 1);
	        Map<Integer, Integer> clases = new HashMap<Integer, Integer>();
	        for( Pattern p : closest ){
		        clases.put(p.getClase(), clases.containsKey(p.getClase()) ? clases.get(p.getClase()) + 1 : 1);
	        }
	        int maxVotos = 0;
	        int claseCalculada = -1;
	        for( Integer clase : clases.keySet() ){
		        int votos = clases.get(clase);
		        if( votos > maxVotos ) {
			        maxVotos = votos;
			        claseCalculada = clase;
		        }
	        }

            if ( pattern.getClase() == claseCalculada ) {
                partialPeformance += 1;
            }
        }

        partialPeformance = partialPeformance / test.size();
        setPerformance(partialPeformance);
        */
    }

    public Integer[][] Y(Integer[] xM, Integer[] yM) {
	    Integer[][] zM = Matrix.fill(yM.length, xM.length, 0);
        for (int i = 0; i < yM.length; i++) {
            for (int j = 0; j < xM.length; j++) {
                zM[i][j] = A(yM[i], xM[j]);
            }
        }
        return zM;
    }

    public Integer[] Z(Integer[][] M, Integer[] xM) {
        int m1rows = M.length;
        int m1cols = M[0].length;

	    Integer[] result = new Integer[m1rows];

        for (int i = 0; i < m1rows; i++) {
	        Integer _max = -Integer.MAX_VALUE;
            for (int k = 0; k < m1cols; k++) {
	            Integer b = B(M[i][k], xM[k]);
                _max = b > _max ? b : _max;
            }
            result[i] = _max;
        }

        return result;
    }

    public Integer A(Integer x, Integer y){
        return round(x - y);
    }

    public Integer B(Integer x, Integer y){
        return round(x + y);
    }

    public List<Pattern<Integer>> closestPattern(List<Pattern> lookupTable, Pattern<Integer> other, int kNearest){
        double distmin = Double.MAX_VALUE;
        Map<Double, Pattern<Integer>> closests = new HashMap<Double, Pattern<Integer>>();
        /*for ( Pattern<Integer> pattern : lookupTable ) {
            double dist = Matrix.distance(pattern.getFeaturesAsVector(), other.getFeaturesAsVector());
            closests.put(dist, pattern);
        } */
	    Map<Double, Pattern<Integer>> sortedAsc = new TreeMap<Double, Pattern<Integer>>(closests);

        return (new ArrayList(closests.values())).subList(0, kNearest);
    }

    private Integer round(Integer value){
        return Math.round(value * 100) / 100;
    }
}
