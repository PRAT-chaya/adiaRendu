/**
 *
 * @author 21600639 : DENOUAL Axel
 * @author 21910036 : ROUSSEAU Alexy
 * @author 21907858 : SABATIER Brian
 * 
 */

package planning;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import representation.Variable;

public class State {

    private Map<Variable, String> affectation;

    public State() {
        this.affectation = new HashMap();
    }

    public void add(Variable var, String val) {
        this.affectation.put(var, val);
    }
    
    public void addAll(Map<Variable, String> affectation){
        this.affectation.putAll(affectation);
    }

    public Map<Variable, String> getAffectation() {
        return this.affectation;
    }

    /**
     * Vérifie si un Etat satisfait une précondition
     * @param partialState
     * @return 
     */
    public boolean satisfies(Map<Variable, String> partialState) {
        //Si l'affectation n'est pas contenue dans notre attribut Map<Variable, String> affectation on retourne vrai
        if (partialState.isEmpty()) {
            return true;
        }
        for (Variable x : partialState.keySet()) {
            // Ou si la variable de notre Etat partiel récupéree n'est pas égale à celle de l'affectation on retourne faux
            if (!(affectation.containsKey(x)) || !(partialState.get(x).equals(affectation.get(x)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Application d'une action
     * @param action
     * @return
     * @throws IllegalArgumentException 
     */
    public State apply(Action action) throws IllegalArgumentException {
        // Si une action est applicable alors on peut avancer (disjonction)
        if (action.is_applicable(this)) {
            State newState = copy();
            // Parcours des règles de l'action
            for (ActionRule rule : action.getRules()) {
                // Si le nouvel etat satisfait les préconditions de la règle
                if (newState.satisfies(rule.getPreconditions())) {
                    // Alors on met à jour les effets du nouvel etat (State)
                    for (Variable var : rule.getEffets().keySet()) {
                        newState.affectation.replace(var, rule.getEffets().get(var));
                    }
                }
            }
            return newState;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Méthode permettant de copier un etat
     * @return 
     */
    public State copy() {
        State stateCopy = new State();
        this.affectation.entrySet().forEach((entry) -> {
            stateCopy.add(entry.getKey(), entry.getValue());
        });
        return stateCopy;
    }

    public void printAffectation() {
        for (Variable var : this.affectation.keySet()) {
            System.out.println(var.getName() + " => " + this.affectation.get(var));
        }
        System.out.println("}");
    }

}
