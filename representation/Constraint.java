/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author 21907858
 */
public interface Constraint {
    public Set<Variable> getScope();
    public List<RestrictedDomain> getDomains();
    public boolean isSatisfiedBy(List<RestrictedDomain> assessment);
    public boolean isSatisfiedBy(Map<Variable, String> assessment);
    public boolean filter(List<RestrictedDomain> toCheck, Map<Variable, Set<String>> variables);
}

