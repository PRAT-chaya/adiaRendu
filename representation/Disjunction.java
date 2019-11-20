/**
 *
 * @author 21600639 : DENOUAL Axel
 * @author 21910036 : ROUSSEAU Alexy
 * @author 21907858 : SABATIER Brian
 * 
 */
package representation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import satisfiability.GeneralizedArcConsistency;

public class Disjunction implements Constraint{

    private Set<Variable> scope;
    private Map<Variable, Set<String>> terms;

    public Disjunction(Map<Variable, Set<String>> terms) {
        this.terms = terms;
        this.scope = new HashSet();
        for (Variable var : terms.keySet()) {
            scope.add(var);
        }
    }

    public Disjunction(List<RestrictedDomain> terms) {
        this.terms = new HashMap();
        for (RestrictedDomain domain : terms) {
            this.terms.put(domain.getVariable(), domain.getSubdomain());
        }
        this.scope = new HashSet();
        for (Variable var : this.terms.keySet()) {
            this.scope.add(var);
        }
    }

    @Override
    public boolean isSatisfiedBy(List<RestrictedDomain> assessment) {
        for(Variable var : scope){
            for(RestrictedDomain domain : assessment){
                if(domain.getVariable() == var){
                    if(terms.get(var).containsAll(domain.getSubdomain())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isSatisfiedBy(Map<Variable, String> assessment) {
        for(Variable var : scope){
            if (assessment.containsKey(var)){
                if(terms.get(var).contains(assessment.get(var))){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Set<Variable> getScope() {
        return this.scope;
    }

    @Override
    public List<RestrictedDomain> getDomains() {
        List<RestrictedDomain> domains = new ArrayList();
        for (Variable var : terms.keySet()) {
            domains.add(new RestrictedDomain(var, var.getDomain()));
        }
        return domains;
    }

    @Override
    public boolean filter(List<RestrictedDomain> toCheck, Map<Variable, Set<String>> variables) {
        GeneralizedArcConsistency.enforceArcConsistency(this, new Domains(toCheck));
        for (RestrictedDomain domain : toCheck) {
            if (domain.getSubdomain() != variables.get(domain.getVariable())) {
                return true;
            }
        }
        return false;
    }
}
