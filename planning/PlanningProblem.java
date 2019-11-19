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

    public static Deque<Action> dfs(PlanningProblem problem, State state,
            Deque<Action> plan, Set<State> closed, int maxR) {
        if (plan.size() > maxR) {
            return null;
        }
        if (state.satisfies(problem.getGoal())) {
            return plan;
        } else {
            for (Action action : problem.getAvailableActions()) {
                if (action.is_applicable(state)) {
                    State next = state.apply(action);
                    problem.increaseDfsProbe();
                    if (!closed.contains(next)) {
                        plan.push(action);
                        closed.add(next);
                        Deque<Action> subplan = dfs(problem, next, plan, closed, maxR);
                        if (subplan != null && !subplan.isEmpty()) {
                            return subplan;
                        } else {
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

    public static Deque<Action> bfs(PlanningProblem problem) {
        Map<State, State> father = new HashMap();
        Map<State, Action> plan = new HashMap();
        Set<State> closed = new HashSet();
        Deque<State> open = new LinkedList();
        open.add(problem.getInitialState());
        father.put(problem.getInitialState(), null);
        while (!open.isEmpty()) {
            State state = open.pollFirst();
            closed.add(state);
            for (Action action : problem.getAvailableActions()) {
                if (action.is_applicable(state)) {
                    problem.increaseBfsProbe();
                    State next = state.apply(action);
                    if (!closed.contains(next) && !open.contains(next)) {
                        father.put(next, state);
                        plan.put(next, action);
                        if (next.satisfies(problem.getGoal())) {
                            return getBFSPlan(father, plan, next);
                        } else {
                            open.add(next);
                        }

                    }
                }
            }
        }
        return null;
    }

    protected static Deque<Action> getBFSPlan(Map<State, State> father, Map<State, Action> action, State goal) {
        Deque<Action> stackPlan = new LinkedList();
        while (goal != null) {
            stackPlan.push(action.get(goal));
            goal = father.get(goal);
        }
        return reverse(stackPlan);
    }
    
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
