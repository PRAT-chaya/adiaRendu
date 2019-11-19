/**
 *
 * @author 21600639 : DENOUAL Axel
 * @author 21910036 : ROUSSEAU Alexy
 * @author 21907858 : SABATIER Brian
 * 
 */

package planning;

import java.util.Map;
import representation.Variable;

public class SimpleHeuristic implements Heuristic {

    private final int totalSum;

    public SimpleHeuristic(PlanningProblem problem) {
        Map<Variable, String> initialAssignment = problem.getInitialState().getAffectation();
        this.totalSum = count(initialAssignment);
    }
    
    @Override
    public int getHeuristic(State s) {
        Map<Variable, String> assignment = s.getAffectation();
        int currentSum = count(assignment);
        return currentSum;
    }

    private static int count(Map<Variable, String> assignment) {
        int sum = 0;
        for (Variable var : assignment.keySet()) {
            switch (assignment.get(var)) {
                case "NONE":
                    break;
                case "LOW":
                    sum += 1;
                    break;
                case "MEDIUM":
                    sum += 2;
                    break;
                case "HIGH":
                    sum += 3;
                    break;
                case "TRUE":
                    sum += 1;
                    break;
                case "FALSE":
                    break;
            }
        }
        return sum;
    }
}
