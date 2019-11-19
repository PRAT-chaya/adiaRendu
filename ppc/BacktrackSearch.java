/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppc;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import representation.*;
import satisfiability.GeneralizedArcConsistency;

/**
 *
 * @author ordinaute
 */
public class BacktrackSearch {

    protected final Set<Constraint> constraints;
    protected final Set<Variable> scope;
    protected final Map<Variable, Set<String>> varDomains;

    public BacktrackSearch(Set<Constraint> constraints) {
        this.constraints = constraints;
        this.scope = new HashSet();
        for (Constraint constraint : constraints) {
            this.scope.addAll(constraint.getScope());
        }
        this.varDomains = new HashMap();
        for(Variable var : this.scope){
            this.varDomains.put(var, var.getDomain());
        }
    }

    public Set<List<RestrictedDomain>> allSolutions() {
        Map<Variable, String> partialAssignment = new HashMap();
        Deque<Variable> unassignedVariables = new LinkedList();
        unassignedVariables.addAll(this.scope);
        Set<Map<Variable, String>> accumulator = new HashSet();
        Map<Variable, Set<String>> domains = new HashMap();
        for (Variable var : this.scope) {
            Set<String> domain = new HashSet();
            domain.addAll(var.getDomain());
            domains.put(var, domain);
        }

        GeneralizedArcConsistency gac = new GeneralizedArcConsistency(this.constraints);
        gac.enforceArcConsistency(domains);

        Set<Map<Variable, String>> solutions = solutions(partialAssignment, unassignedVariables, domains, accumulator);
        Set<List<RestrictedDomain>> allSolutions = new HashSet();
        for (Map<Variable, String> solution : solutions) {
            List<RestrictedDomain> rdSolution = new ArrayList();
            for (Variable var : solution.keySet()) {
                Set<String> domain = new HashSet();
                domain.add(solution.get(var));
                rdSolution.add(new RestrictedDomain(var, domain));
            }
            allSolutions.add(rdSolution);
        }
        return allSolutions;
    }

    public List<RestrictedDomain> solution() {
        List<RestrictedDomain> solution = new ArrayList();
        Map<Variable, String> partialAssignment = new HashMap();
        Deque<Variable> unassignedVariables = new LinkedList();
        unassignedVariables.addAll(this.scope);
        Map<Variable, String> solutionMap = new HashMap();
        Map<Variable, String> temp = solution(partialAssignment, unassignedVariables);
        if (temp != null) {
            solutionMap.putAll(temp);
        }
        for (Variable var : solutionMap.keySet()) {
            Set<String> domain = new HashSet();
            domain.add(solutionMap.get(var));
            solution.add(new RestrictedDomain(var, domain));
        }
        return solution;
    }

    public Map<Variable, String> solution(Map<Variable, String> partialAssignment,
            Deque<Variable> unassignedVariables) {
        if (unassignedVariables.isEmpty()) {
            if (satisfiesConstraints(partialAssignment)) {
                return partialAssignment;
            }
        } else {
            Variable var = unassignedVariables.pop();
            for (String val : var.getDomain()) {
                Deque<Variable> tempQ = new LinkedList();
                tempQ.addAll(unassignedVariables);
                partialAssignment.put(var, val);
                Map<Variable, String> tempPartialAssignment = new HashMap();
                tempPartialAssignment.putAll(partialAssignment);
                return solution(tempPartialAssignment, tempQ);
            }
        }
        return null;
    }

    public Set<Map<Variable, String>> solutions(Map<Variable, String> partialAssignment,
            Deque<Variable> unassignedVariables, Map<Variable, Set<String>> domains, Set<Map<Variable, String>> accumulator) {
        //Condition d'arrêt de la récursion
        if (unassignedVariables.isEmpty()) {
            printAssignment(partialAssignment);
            if (!accumulator.contains(partialAssignment)) {
                //Si l'assignation (complète) satisfait l'ensemble des contraintes
                if (satisfiesConstraints(partialAssignment)) {
                    //On ajoute cette assignation à l'ensemble des solutions et on retourne cet ensemble
                    accumulator.add(partialAssignment);
                    return accumulator;
                }
            }
        } else {
            //On avance que d'une variable par appel récursif
            Variable var = unassignedVariables.pop();
            for (String val : domains.get(var)) {
                /*Pour chaque valeur de chaque variable, on clone notre queue de
                *variables, notre assignation partielle et notre ensemble de solutions
                *pour pouvoir utiliser ces clones dans les appels récursifs que l'on va faire*/
                Deque<Variable> tempQ = new LinkedList();
                tempQ.addAll(unassignedVariables);
                partialAssignment.put(var, val);
                Set<Map<Variable, String>> tempAccu = new HashSet();
                tempAccu.addAll(accumulator);
                Map<Variable, String> tempPartialAssignment = new HashMap();
                tempPartialAssignment.putAll(partialAssignment);
                //Appel récursif qui nous renvoie toutes les solutions plus bas dans l'arbre de recherche
                Set<Map<Variable, String>> solutions = solutions(tempPartialAssignment, tempQ, domains, tempAccu);
                //S'il y en a on peut les ajouter à notre ensemble des solutions
                if (solutions != null) {
                    accumulator.addAll(solutions);
                }
            }
            //Si jamais on a essayé toutes les valeurs du domaine de la variable, on renvoit l'ensemble des solutions;
            /*Si on est à la première variable, l'exécution se termine 
            *(car arriver ici signifie que toutes les récursions suivantes
            *se sont terminées)*/
            return accumulator;
        }
        return null;
    }

    private boolean satisfiesConstraints(Map<Variable, String> assignment) {
        for (Constraint constraint : this.constraints) {
            if (!constraint.isSatisfiedBy(assignment)) {
                return false;
            }
        }
        return true;
    }

    public static void printAssignment(Map<Variable, String> assignment) {
        System.out.print("[");
        for (Variable var : assignment.keySet()) {
            System.out.print(var.getName() + ": " + assignment.get(var) + ",");
        }
        System.out.print("]");
        System.out.println();
    }

    public static void printQ(Deque<Variable> variables) {
        System.out.print("(");
        for (Variable var : variables) {
            System.out.print(var.getName() + ",");
        }
        System.out.print(")");
        System.out.println();
    }

    public static void printSolutions(Set<List<RestrictedDomain>> allSolutions) {
        for (List<RestrictedDomain> solution : allSolutions) {
            System.out.print("solution: {");
            for (RestrictedDomain domain : solution) {
                System.out.print(domain.getVariable().getName() + ": " + domain.getSubdomain().iterator().next() + ", ");
            }
            System.out.print("}");
            System.out.println();
        }
    }
}
