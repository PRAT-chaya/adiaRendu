/**
 *
 * @author 21600639 : DENOUAL Axel
 * @author 21910036 : ROUSSEAU Alexy
 * @author 21907858 : SABATIER Brian
 * 
 */

package representation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BinaryConstraint implements Constraint{
    
    RestrictedDomain term1;
    RestrictedDomain term2;
    List<RestrictedDomain> domains;
    Set<Variable> scope;

    public BinaryConstraint(RestrictedDomain term1, RestrictedDomain term2) {
        this.term1 = term1;
        this.term2 = term2;
        this.scope = new HashSet();
        this.scope.add(term1.getVariable());
        this.scope.add(term2.getVariable());
        this.domains = new ArrayList();
        this.domains.add(term1);
        this.domains.add(term2);
    }

    @Override
    public Set<Variable> getScope() {
        return this.scope;
    }

    @Override
    public List<RestrictedDomain> getDomains() {
        return this.domains;
    }

    @Override
    public boolean isSatisfiedBy(List<RestrictedDomain> assessment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isSatisfiedBy(Map<Variable, String> assessment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean filter(List<RestrictedDomain> toCheck, Map<Variable, Set<String>> variables) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
