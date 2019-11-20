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
        Set<State> open = new HashSet(); // La liste des ouverts est vide par défaut
        open.add(problem.getInitialState()); // On remplit notre liste d'ouverts
        distance.put(problem.getInitialState(), 0);
        father.put(problem.getInitialState(), null);

        // Tant que notre liste d'ouverts n'est pas vide on continue l'exploration
        while (!open.isEmpty()) {
            System.out.println(open.size());
            State state = argmin(open, distance);
            problem.increaseDijkstraProbe();
            open.remove(state); // On supprime des ouverts l'etat
            // Si l'etat satisfait notre but et si notre liste de buts ne contient pas notre etat on l'ajoute 
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
                        // Si on ne connaît pas la distance de notre etat on lui applique celle maximale
                        distance.put(next, Integer.MAX_VALUE);
                    }
                    // Si notre distance au prochain etat est supérieure à celle actuelle + celle du coût on
                    // "avance" dans la résolution du problème
                    if (distance.get(next) > distance.get(state) + action.getCost()) {
                        distance.replace(next, distance.get(state) + action.getCost()); // On met à jour notre distance
                        System.out.println(distance.get(next));
                        // Si le père ne contient pas le nouvel etat on l'ajoute
                        if (!father.containsKey(next)) {
                            father.put(next, state);
                        } else {
                            father.replace(next, state); // Sinon on le met à jour
                        }
                        if (!plan.containsKey(next)) {
                            plan.put(next, action); // Si le plan ne contient pas l'Etat on l'ajoutre
                        } else {
                            plan.put(next, action); // Sinon on le MAJ
                        }
                        if (!open.contains(next)) {
                            open.add(next); // Enfin si les ouverts ne contient pas l'Etat on l'ajoute
                        }
                    }
                }
            }
        }

        return getDijkstraPlan(father, plan, goals, distance);
    }

    /**
     * Récupération de la valeur minimale de notre liste d'etats
     * @param stateSet
     * @param valueMap
     * @return 
     */
    public static State argmin(Set<State> stateSet, Map<State, Integer> valueMap) {
        int minDistance = -1;
        State node = null; // Noeud des visites à 0
        for (State state : stateSet) {
            // Passe nécessairement ici au début
            if (minDistance < 0) {
                minDistance = valueMap.get(state);
                node = state;
            }
            // On peut retourner notre etat courant si la distance est = à 0
            if (minDistance == 0) {
                return node;
            // Si la distance minimale de l'etat d'après est plus grande
            // Que l'etat en cours on MAJ notre argmin en conséquence
            } else if (valueMap.get(state) < minDistance) {
                minDistance = valueMap.get(state);
                node = state;
            }
        }
        return node;
    }

    
    /**
     * Méthode permettant de récupérer la liste des plans pour atteindre un but
     * Triée par ordre croissant
     * @param father
     * @param actions
     * @param goals
     * @param distance
     * @return 
     */
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
