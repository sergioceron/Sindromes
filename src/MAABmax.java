
import org.underserver.jbigmining.Algorithm;
import org.underserver.jbigmining.AlgorithmInformation;
import org.underserver.jbigmining.Pattern;
import org.underserver.jbigmining.utils.Matrix;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sergio
 * Date: 28/05/13
 * Time: 07:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class MAABmax extends Algorithm {

	private Integer[][] M;

    public MAABmax() {
        super("Memoria Alfa Beta Autoasociativa MAX");
    }

	@Override
	public AlgorithmInformation getInformation() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void train() {
		/*List<Pattern> train = getTrainPatterns();
		int n = train.get(0).size();
		int m = train.get(0).size();
		int p = train.size();

		// Entrenamiento
		M = Matrix.fill(m, n, -Integer.MAX_VALUE);
		for ( int t = 0; t < p; t++ ) {
			Pattern<Integer> pattern  = train.get(t);
			Integer[] xM = pattern.getFeaturesAsVector();
			Integer[] yM = pattern.getFeaturesAsVector();
			Integer[][] zM = Y(xM, yM);
			M = Matrix.max(zM, M);
		}   */
	}

    public void run() {
		double partialPeformance = 0;

		//List<Pattern> test = getTestPatterns();
		// Recuperacion
		/*for ( int t = 0; t < test.size(); t++ ) {
			Pattern<Integer> pattern  = test.get(t);
			Integer[] xM  = pattern.getFeaturesAsVector();
			Integer[] yM  = pattern.getFeaturesAsVector();

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
			} */

			/*Pattern<Integer> closest = closestPattern(train, recuperado);

			if ( pattern.getClase() == closest.getClase() ) {
				partialPeformance += 1;
			} */
		//}

		//partialPeformance = partialPeformance / test.size();
		//setPerformance(partialPeformance);
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
            int _min = 2;
            for (int k = 0; k < m1cols; k++) {
                int b = B(M[i][k], xM[k]);
                _min = b < _min ? b : _min;
            }
            result[i] = _min;
        }

        return result;
    }

    public Integer A(Integer x, Integer y){
        int z = 0;
        if (x == 0 && y == 0) {
            z = 1;
        } else if (x == 0 && y == 1) {
            z = 0;
        } else if (x == 1 && y == 0) {
            z = 2;
        } else if (x == 1 && y == 1) {
            z = 1;
        }
        return z;
    }

    public Integer B(Integer x, Integer y){
        int z = 0;
        if (x == 0 && y == 0 ) {
            z = 0;
        } else if (x == 0 && y == 1) {
            z = 0;
        } else if (x == 1 && y == 0) {
            z = 0;
        } else if (x == 1 && y == 1) {
            z = 1;
        } else if (x == 2 && y == 0) {
            z = 1;
        } else if (x == 2 && y == 1) {
            z = 1;
        }
        return z;
    }

    public Pattern closestPattern(List<Pattern> lookupTable, Pattern other){
        double distmin = Double.MAX_VALUE;
        Pattern closest = null;
        /*for ( Pattern<Integer> pattern : lookupTable ) {
            double dist = Matrix.distance(pattern.getFeaturesAsVector(), other.getFeaturesAsVector());
            if ( dist < distmin ) {
                closest = pattern;
                distmin = dist;
            }
        } */
        return closest;
    }

	public List<Pattern> closestPattern(List<Pattern> lookupTable, Pattern other, int kNearest){
		double distmin = Double.MAX_VALUE;
		Map<Double, Pattern> closests = new HashMap<Double, Pattern>();
		/*for ( Pattern<Integer> pattern : lookupTable ) {
			double dist = Matrix.distance(pattern.getFeaturesAsVector(), other.getFeaturesAsVector());
			closests.put(dist, pattern);
		}*/
		Map<Double, Pattern> sortedAsc = new TreeMap<Double, Pattern>(closests);

		return (new ArrayList(sortedAsc.values())).subList(0, kNearest);
	}

    private Double round(Double value){
        return (double) Math.round(value * 100) / 100;
    }
}
