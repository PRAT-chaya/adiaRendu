/**
 *
 * @author 21600639 : DENOUAL Axel
 * @author 21910036 : ROUSSEAU Alexy
 * @author 21907858 : SABATIER Brian
 * 
 */

package representation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Constraint {
    public Set<Variable> getScope();
    public List<RestrictedDomain> getDomains();
    public boolean isSatisfiedBy(List<RestrictedDomain> assessment);
    public boolean isSatisfiedBy(Map<Variable, String> assessment);
    public boolean filter(List<RestrictedDomain> toCheck, Map<Variable, Set<String>> variables);
}

