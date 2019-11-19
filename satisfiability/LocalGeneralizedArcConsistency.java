/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package satisfiability;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import representation.Constraint;
import representation.Domains;
import representation.RestrictedDomain;
import representation.Variable;

/**
 *
 * @author ordinaute
 */
public class LocalGeneralizedArcConsistency extends GeneralizedArcConsistency {

    public LocalGeneralizedArcConsistency(Set<Constraint> constraints) {
        super(constraints);
    }

    public void enforceLocalArcConsistency(List<RestrictedDomain> domains, Variable toFilter)
            throws IllegalArgumentException {
        boolean hasChanged = true;
        while (hasChanged) {
            for (Constraint constraint : this.constraints) {
                hasChanged = LocalGeneralizedArcConsistency.enforceLocalAcrConsistency(constraint, new Domains(domains), toFilter);
            }
        }
    }

    public static boolean enforceLocalAcrConsistency(Constraint constraint,
            Domains domains, Variable toFilter) throws IllegalArgumentException {
        boolean hasChanged = false;
        if (domains.containsVar(toFilter)) {
            RestrictedDomain domain = domains.getDomain(toFilter);
            for (String val : toFilter.getDomain()) {
                if (!GeneralizedArcConsistency.isConsistent(toFilter, val, constraint, domains)) {
                    hasChanged = true;
                    domain.getSubdomain().remove(val);
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
        if (hasChanged) {
            GeneralizedArcConsistency.enforceArcConsistency(constraint, domains);
        }
        return false;
    }
}
