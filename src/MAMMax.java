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
public class MAMMax extends Algorithm {

    private Double[][] M;

    public MAMMax() {
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
        int m = train.get(0).size();
        int p = train.size();

        // Entrenamiento
        M = Matrix.fill(m, n, Double.MAX_VALUE);
        for ( int t = 0; t < p; t++ ) {
            Pattern<Double> pattern  = train.get(t);
            Double[] xM = pattern.getFeaturesAsVector();
            Double[] yM = pattern.getFeaturesAsVector();
            Double[][] zM = Y(xM, yM);
            M = Matrix.min(zM, M);
        }

    }

    public void run() {
        double partialPeformance = 0;

       //List<Pattern> test = getTestPatterns();
        // Recuperacion
        /*for ( int t = 0; t < test.size(); t++ ) {
            Pattern<Double> pattern  = test.get(t);
            Double[] xM  = pattern.getFeaturesAsVector();
            Double[] yM  = pattern.getFeaturesAsVector();

            Pattern<Double> recuperado = new Pattern<Double>( Z(M, xM) );
	        List<Pattern<Double>> closest = closestPattern(getTrainPatterns(), recuperado, 5);

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
	        } */
            /*
            Pattern<Double> closest = closestPattern(getTrainPatterns(), recuperado);
            if ( pattern.getClase() == closest.getClase() ) {
                partialPeformance += 1;
            } */
      //  }

        //partialPeformance = partialPeformance / test.size();
        //setPerformance(partialPeformance);
    }

    public Double[][] Y(Double[] xM, Double[] yM) {
        Double[][] zM = Matrix.fill(yM.length, xM.length, 0d);
        for (int i = 0; i < yM.length; i++) {
            for (int j = 0; j < xM.length; j++) {
                zM[i][j] = A(yM[i], xM[j]);
            }
        }
        return zM;
    }

    public Double[] Z(Double[][] M, Double[] xM) {
        int m1rows = M.length;
        int m1cols = M[0].length;

        Double[] result = new Double[m1rows];

        for (int i = 0; i < m1rows; i++) {
            Double _max = -Double.MAX_VALUE;
            for (int k = 0; k < m1cols; k++) {
                Double b = B(M[i][k], xM[k]);
                _max = b > _max ? b : _max;
            }
            result[i] = _max;
        }

        return result;
    }

    public Double A(Double x, Double y){
        return round(x - y);
    }

    public Double B(Double x, Double y){
        return round(x + y);
    }

    public Pattern<Double> closestPattern(List<Pattern> lookupTable, Pattern<Double> other){
        double distmin = Double.MAX_VALUE;
        Pattern<Double> closest = null;
        /*for ( Pattern<Double> pattern : lookupTable ) {
            double dist = Matrix.distance(pattern.getFeaturesAsVector(), other.getFeaturesAsVector());
            if ( dist < distmin ) {
                closest = pattern;
                distmin = dist;
            }
        } */
        return closest;
    }

	public List<Pattern<Double>> closestPattern(List<Pattern> lookupTable, Pattern<Double> other, int kNearest){
		double distmin = Double.MAX_VALUE;
		Map<Double, Pattern<Double>> closests = new HashMap<Double, Pattern<Double>>();
		/*for ( Pattern<Double> pattern : lookupTable ) {
			double dist = Matrix.distance(pattern.getFeaturesAsVector(), other.getFeaturesAsVector());
			closests.put(dist, pattern);
		}*/
		Map<Double, Pattern<Double>> sortedAsc = new TreeMap<Double, Pattern<Double>>(closests);

		return (new ArrayList(sortedAsc.values())).subList(0, kNearest);
	}

    private Double round(Double value){
        return (double) Math.round(value * 100) / 100;
    }
}
