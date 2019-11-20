/**
 *
 * @author 21600639 : DENOUAL Axel
 * @author 21910036 : ROUSSEAU Alexy
 * @author 21907858 : SABATIER Brian
 * 
 */

package planning;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import representation.Variable;

public class PlanningProblem {

    private final State initialState;
    private final Map<Variable, String> goal;
    private final Set<Action> availableActions;
    private int dfsProbe;
    private int bfsProbe;

    public PlanningProblem(State initialState, Map<Variable, String> goal, Set<Action> availableActions) {
        this.initialState = initialState;
        this.availableActions = availableActions;
        this.goal = goal;
        this.dfsProbe = 0;
        this.bfsProbe = 0;
    }

    public State getInitialState() {
        return initialState;
    }

    public Map<Variable, String> getGoal() {
        return this.goal;
    }

    public Set<Action> getAvailableActions() {
        return availableActions;
    }

    public int getDfsProbe() {
        return dfsProbe;
    }

    public void increaseDfsProbe() {
        dfsProbe++;
    }

    public int getBfsProbe() {
        return bfsProbe;
    }

    public void increaseBfsProbe() {
        bfsProbe++;
    }

    public Deque<Action> dfs(int maxR) {
        State state = this.initialState;
        Deque<Action> plan = new LinkedList();
        Set<State> closed = new HashSet();
        return PlanningProblem.dfs(this, state, plan, closed, maxR);
    }

    /**
     * Méthode permettant d'effectuer une recherche en profondeur
     * @param problem
     * @param state
     * @param plan
     * @param closed
     * @param maxR
     * @return 
     */
    public static Deque<Action> dfs(PlanningProblem problem, State state,
            Deque<Action> plan, Set<State> closed, int maxR) {
        // Si la taille du plan dépasse celle définie par la recherche maximale en profondeur
        if (plan.size() > maxR) {
            return null;
        }
        // Si l'etat satisfait le but on le retourne
        if (state.satisfies(problem.getGoal())) {
            return plan;
        } else {
            for (Action action : problem.getAvailableActions()) {
                if (action.is_applicable(state)) {
                    State next = state.apply(action);
                    problem.increaseDfsProbe(); // Incrémentation de l'attribut DfsProbe
                    // Si les fermés ne contiennent pas notre etat
                    if (!closed.contains(next)) {
                        plan.push(action); // alors on ajoute notre action au plan
                        closed.add(next); // Et notre etat aux fermés
                        Deque<Action> subplan = dfs(problem, next, plan, closed, maxR);
                        // Si le sous-plan est égal à null ou qu'il n'est pas vide on le retourne 
                        if (subplan != null && !subplan.isEmpty()) {
                            return subplan;
                        } else { // Sinon on pioche le premier
                            plan.pop();
                        }
                    }
                }
            }
        }
        return null;
    }

    public Deque<Action> bfs() {
        return PlanningProblem.bfs(this);
    }

    /**
     * Recherche en largeur
     * @param problem
     * @return 
     */
    public static Deque<Action> bfs(PlanningProblem problem) {
        Map<State, State> father = new HashMap();
        Map<State, Action> plan = new HashMap();
        Set<State> closed = new HashSet();
        Deque<State> open = new LinkedList();
        open.add(problem.getInitialState());
        father.put(problem.getInitialState(), null);
        while (!open.isEmpty()) {
            State state = open.pollFirst(); // Récupération et suppression du 1er élément de la liste
            closed.add(state);
            for (Action action : problem.getAvailableActions()) {
                if (action.is_applicable(state)) {
                    problem.increaseBfsProbe(); // Incrémentation de bfsProbe
                    State next = state.apply(action);
                    if (!closed.contains(next) && !open.contains(next)) { // Si les ouverts & les fermés ne contiennent pas l'etat suivant
                        father.put(next, state);
                        plan.put(next, action);
                        if (next.satisfies(problem.getGoal())) { // Si il y a satisfaction on a trouvé une solution on la retourne
                            return getBFSPlan(father, plan, next);
                        } else {
                            open.add(next); // Sinon on l'ajoute aux ouverts et on itère encore...
                        }

                    }
                }
            }
        }
        return null; // Si tous les ouverts ont été parcouru et qu'aucune solution n'a été trouvée on retourne null
    }

    protected static Deque<Action> getBFSPlan(Map<State, State> father, Map<State, Action> action, State goal) {
        Deque<Action> stackPlan = new LinkedList();
        while (goal != null) {
            stackPlan.push(action.get(goal));
            goal = father.get(goal);
        }
        return reverse(stackPlan);
    }
    
    /**
     * On inverse notre pile pour obtenir l'etat final au début
     * @param plan
     * @return 
     */ 
     protected static Deque<Action> reverse(Deque<Action> plan){
        Deque<Action> reversePlan = new LinkedList();
        while (!plan.isEmpty()) {
            reversePlan.push(plan.pop());
        }
        return reversePlan;
    }

    public void printProblem() {
        System.out.println("Initial state {");
        this.initialState.printAffectation();
        System.out.println("Goal");
        State goalDisplay = new State();
        goalDisplay.addAll(goal);
        goalDisplay.printAffectation();
        System.out.println("Available actions");
        for (Action action : this.availableActions) {
            System.out.println(action);
        }
    }

    public void printPlan(Deque<Action> plan) {
        System.out.println("PLAN");
        if (plan != null) {
            while (!plan.isEmpty()) {
                System.out.println(plan.pop());
            }
        } else {
            System.out.println("no solutions");
        }
    }
}
