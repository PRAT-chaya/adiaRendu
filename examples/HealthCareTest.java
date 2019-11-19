/**
 *
 * @author 21600639 : DENOUAL Axel
 * @author 21910036 : ROUSSEAU Alexy
 * @author 21907858 : SABATIER Brian
 * 
 */

package examples;

import java.util.Random;
import planning.Action;
import planning.State;
import representation.Variable;

public class HealthCareTest {

    public static void main(String[] args) {
        HealthCare hc = new HealthCare();
        Random r = new Random();
        Action action = hc.getMedecine().get(0);
        System.out.println(action);
        State state = new State();
        state.add(HealthCare.fever,"NONE");
        state.add(HealthCare.buttons,"NONE");
        state.add(HealthCare.cough,"NONE");
        state.add(HealthCare.angina,"TRUE");
        
        state.apply(hc.getGuerison());
        for (Variable var : state.getAffectation().keySet()){
            System.out.print(var.getName() + " : ");
            System.out.print(state.getAffectation().get(var));
            System.out.print(", ");
        }
        System.out.println();
    }
}
