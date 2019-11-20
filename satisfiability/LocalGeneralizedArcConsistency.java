/**
 *
 * @author 21600639 : DENOUAL Axel
 * @author 21910036 : ROUSSEAU Alexy
 * @author 21907858 : SABATIER Brian
 * 
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
        // Si notre liste de domaines (Domains = liste de RestrictedDomains) contient la variable à filtrer
        if (domains.containsVar(toFilter)) {
            // On récupère le domaine associé à notre variable à filtrer
            RestrictedDomain domain = domains.getDomain(toFilter);
            // Parcours des valeurs de notre domaine à filtrer
            for (String val : toFilter.getDomain()) {
                // Si notre domaine à filtrer n'est pas consistant
                // on déclare
                if (!GeneralizedArcConsistency.isConsistent(toFilter, val, constraint, domains)) {
                    hasChanged = true;
                    domain.getSubdomain().remove(val);
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
        // Tant qu'on a filtré on continue !
        if (hasChanged) {
            GeneralizedArcConsistency.enforceArcConsistency(constraint, domains);
        }
        return false;
    }
}
