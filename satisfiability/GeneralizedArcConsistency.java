package satisfiability;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import representation.*;

/**
 * A class for enforcing generalized arc consistency to domains wrt to
 * variables. The algorithms are brute-force: for deciding whether a value is
 * GAC wrt a constraint, at worst all tuples in the Cartesian product of the
 * variables in the scope of the constraint are tested.
 */
public class GeneralizedArcConsistency {

    protected Set<Constraint> constraints;

    public GeneralizedArcConsistency(Set<Constraint> constraints) {
        this.constraints = constraints;
    }

    /**
     * Filters given domains by removing values which are not arc consistent
     * with respect to a given set of constraints.
     *
     * @param domains A map from (at least) the variables occurring in the scope
     * of the constraint to domains
     * @throws IllegalArgumentException if a variable occurring in the scope of
     * some constraint is mapped to no domain
     */
    public void enforceArcConsistency(List<RestrictedDomain> domains)
            throws IllegalArgumentException {
        boolean hasChanged = true;
        while (hasChanged) {
            for (Constraint constraint : this.constraints) {
                hasChanged = GeneralizedArcConsistency.enforceArcConsistency(constraint, new Domains(domains));
            }
        }
    }

    /**
     * Filters given domains by removing values which are not arc consistent
     * with respect to a given set of constraints.
     *
     * @param domains A map from (at least) the variables occurring in the scope
     * of the constraint to domains
     * @throws IllegalArgumentException if a variable occurring in the scope of
     * some constraint is mapped to no domain
     */
    public void enforceArcConsistency(Map<Variable, Set<String>> domains)
            throws IllegalArgumentException {
        boolean hasChanged = true;
        while (hasChanged) {
            for (Constraint constraint : this.constraints) {
                hasChanged = GeneralizedArcConsistency.enforceArcConsistency(constraint, new Domains(domains));
            }
        }
    }

    /**
     * Filters given domains by removing values which are not arc consistent
     * with respect to a given constraint.
     *
     * @param constraint A constraint
     * @param domains A map from (at least) the variables occurring in the scope
     * of the constraint to domains
     * @return true if at least one domain has changed, false otherwise
     * @throws IllegalArgumentException if a variable occurring in the scope of
     * the constraint is mapped to no domain
     */
    public static boolean enforceArcConsistency(Constraint constraint, Domains domains)
            throws IllegalArgumentException {
        boolean hasChanged = false;
        // on boucle sur les variables de la contraintes (var)
        for (Variable var : constraint.getScope()) {
            System.out.println("CURRENTLY ENFORCING ON :" + var.getName());
            // si var n'est pas dans le domains donnée, alors erreurs   
            if (domains.containsVar(var)) {
                // récupérer les valeurs de var dans domains 
                RestrictedDomain domain = domains.getDomain(var);
                Set<String> temp = new HashSet();
                temp.addAll(domain.getSubdomain());
                for (String value : temp) {
                    if (!GeneralizedArcConsistency.isConsistent(var, value, constraint, domains)) {
                        // faites les modification nécéssaire
                        hasChanged = true;
                        // enlever toutes les valeurs non viables
                        domain.getSubdomain().remove(value);
                    }
                }
                // en cas de changement
                // si oui 
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (hasChanged) {
            // appel récusive sur le rest de domains
            GeneralizedArcConsistency.enforceArcConsistency(constraint, domains);
            return true;
        }
        return false;
    }

    /**
     * Decides whether a given value is arc consistent for a given variable wrt
     * a given constraint and given domains.
     *
     * @param variable A variable
     * @param value A value
     * @param constraint A constraint
     * @param domains A map from (at least) the variables occurring in the scope
     * of the constraint to domains (except possibly the given variable)
     * @return true if the given value is arc consistent for the given variable
     * @throws IllegalArgumentException if a variable occurring in the scope of
     * the constraint (except possibly the given variable) is mapped to no
     * domain
     */
    public static boolean isConsistent(Variable variable, String value,
            Constraint constraint, Domains domains)
            throws IllegalArgumentException {
        Deque<Variable> unassignedVariables = new LinkedList<>(constraint.getScope());
        unassignedVariables.remove(variable);
        Map<Variable, String> partialAssignment = new HashMap<>();
        partialAssignment.put(variable, value);
        return GeneralizedArcConsistency.isConsistent(partialAssignment,
                unassignedVariables, constraint, domains);
    }

    // Helper method ===================================================
    protected static boolean isConsistent(Map<Variable, String> partialAssignment,
            Deque<Variable> unassignedVariables, Constraint constraint, Domains domains) {
        Deque<Variable> temp = new LinkedList();
        temp.addAll(unassignedVariables);
        if (unassignedVariables.isEmpty()) {
            System.out.print("Tried ");
            for (Variable var : partialAssignment.keySet()) {
                System.out.print(var.getName() + " " + partialAssignment.get(var));
                System.out.print(", ");
            }
            System.out.println();
            return constraint.isSatisfiedBy(partialAssignment);
        } else {
            Variable var = unassignedVariables.pop();
            Set<String> subdomain = domains.getDomain(var).getSubdomain();
            for (String val : subdomain) {
                partialAssignment.remove(var);
                partialAssignment.put(var, val);
                if (GeneralizedArcConsistency.isConsistent(partialAssignment, unassignedVariables, constraint, domains)) {
                    return true;
                }
            }
        }
        return false;
    }
}
