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

public class PlanningProblemWithCost extends PlanningProblem {

    private int dijkstraProbe;
    private int aStarProbe;

    public PlanningProblemWithCost(State initialState, Map<Variable, String> goal, Set<Action> availableActions) {
        super(initialState, goal, availableActions);
        this.dijkstraProbe = 0;
        this.aStarProbe = 0;
    }

    private void increaseDijkstraProbe() {
        this.dijkstraProbe++;
    }

    private void increaseAStarProbe() {
        this.aStarProbe++;
    }

    public int getDijkstraProbe() {
        return dijkstraProbe;
    }

    public int getAStarProbe() {
        return aStarProbe;
    }

    public Deque<Action> dijkstra() {
        return dijkstra(this);
    }
    
    public Deque<Action> aStar() {
        return aStar(this, new SimpleHeuristic(this));
    }

    public static Deque<Action> dijkstra(PlanningProblemWithCost problem) {
        Map<State, Integer> distance = new HashMap();
        Map<State, State> father = new HashMap();
        Map<State, Action> plan = new HashMap();
        Set<State> goals = new HashSet();
        Set<State> open = new HashSet();
        open.add(problem.getInitialState());
        distance.put(problem.getInitialState(), 0);
        father.put(problem.getInitialState(), null);

        while (!open.isEmpty()) {
            System.out.println(open.size());
            State state = argmin(open, distance);
            problem.increaseDijkstraProbe();
            open.remove(state);
            if (state.satisfies(problem.getGoal())) {
                if (!goals.contains(state)) {
                    goals.add(state);
                }
                System.out.println("Goal reached");
            }
            for (Action action : problem.getAvailableActions()) {
                if (action.is_applicable(state)) {
                    State next = state.apply(action);
                    next.printAffectation();
                    if (!distance.containsKey(next)) {
                        distance.put(next, Integer.MAX_VALUE);
                    }
                    if (distance.get(next) > distance.get(state) + action.getCost()) {
                        distance.replace(next, distance.get(state) + action.getCost());
                        System.out.println(distance.get(next));
                        if (!father.containsKey(next)) {
                            father.put(next, state);
                        } else {
                            father.replace(next, state);
                        }
                        if (!plan.containsKey(next)) {
                            plan.put(next, action);
                        } else {
                            plan.put(next, action);
                        }
                        if (!open.contains(next)) {
                            open.add(next);
                        }
                    }
                }
            }
        }

        return getDijkstraPlan(father, plan, goals, distance);
    }

    public static State argmin(Set<State> stateSet, Map<State, Integer> valueMap) {
        int minDistance = -1;
        State node = null;
        for (State state : stateSet) {
            if (minDistance < 0) {
                minDistance = valueMap.get(state);
                node = state;
            }
            if (minDistance == 0) {
                return node;
            } else if (valueMap.get(state) < minDistance) {
                minDistance = valueMap.get(state);
                node = state;
            }
        }
        return node;
    }

    public static Deque<Action> getDijkstraPlan(Map<State, State> father, Map<State, Action> actions,
            Set<State> goals, Map<State, Integer> distance) {
        Deque<Action> plan = new LinkedList();
        State goal = argmin(goals, distance);
        while (goal != null) {
            plan.push(actions.get(goal));
            goal = father.get(goal);
        }
        return reverse(plan);
    }

    public static Deque<Action> aStar(PlanningProblemWithCost problem, Heuristic heuristic) {
        State initialState = problem.getInitialState();
        Set<State> open = new HashSet();
        open.add(initialState);
        Map<State, State> father = new HashMap();
        father.put(initialState, null);
        Map<State, Integer> distance = new HashMap();
        distance.put(initialState, 0);
        Map<State, Integer> value = new HashMap();
        value.put(initialState, heuristic.getHeuristic(initialState));
        Map<State, Action> actions = new HashMap();
        while (!open.isEmpty()) {
            State state = argmin(open, value);
            problem.increaseAStarProbe();
            if (state.satisfies(problem.getGoal())) {
                return PlanningProblem.getBFSPlan(father, actions, state);
            } else {
                open.remove(state);
                for (Action action : problem.getAvailableActions()) {
                    if (action.is_applicable(state)) {
                        State next = state.apply(action);
                        if (!distance.containsKey(next)) {
                            distance.put(next, Integer.MAX_VALUE);
                        }
                        if (distance.get(next) > distance.get(state) + action.getCost()) {
                            distance.replace(next, distance.get(state) + action.getCost());
                            if (!value.containsKey(next)) {
                                value.put(next, distance.get(next) + heuristic.getHeuristic(next));
                            } else {
                                value.replace(next, distance.get(next) + heuristic.getHeuristic(next));
                            }
                            if (!father.containsKey(next)) {
                                father.put(next, state);
                            } else {
                                father.replace(next, state);
                            }
                            if (!actions.containsKey(next)) {
                                actions.put(next, action);
                            } else {
                                actions.replace(next, action);
                            }
                            if (!open.contains(next)) {
                                open.add(next);
                            }
                        }
                    }
                }
            }
        }
        
        return null;
    }
}
