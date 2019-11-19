/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planning.test;

import examples.HealthCare;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import planning.Action;
import planning.Heuristic;
import planning.PlanningProblemWithCost;
import planning.SimpleHeuristic;
import planning.State;
import representation.Variable;

/**
 *
 * @author ordinaute
 */
public class SimpleHeuristicTest {

    public static void main(String[] args) {
        HealthCare hc = new HealthCare();
        Set<Action> actions = new HashSet();
        actions.addAll(hc.makeNMedecine(10, 3, 2));
        actions.add(hc.getGuerison());
        actions.addAll(hc.getMedecine());
        State initialState = hc.genInitialState(new Random());
        State finalState = new State();
        for (Variable var : hc.getMaladies()) {
            if (initialState.getAffectation().containsKey(var)) {
                finalState.add(var, "FALSE");
            }
        }
        for (Variable var : hc.getSyptms()) {
            finalState.add(var, "NONE");
        }

        PlanningProblemWithCost pb = new PlanningProblemWithCost(initialState,
                finalState.getAffectation(), actions);
        Heuristic h = new SimpleHeuristic(pb);
        System.out.println(h.getHeuristic(finalState));

        State s = new State();
        for (Variable var : hc.getSyptms()) {
            s.add(var, "LOW");
        }
        s.add(hc.getMaladies().get(0), "TRUE");
        System.out.println(h.getHeuristic(s));
        
    }
}
