/**
 *
 * @author 21600639 : DENOUAL Axel
 * @author 21910036 : ROUSSEAU Alexy
 * @author 21907858 : SABATIER Brian
 * 
 */
public class IncompatibilityConstraint implements Constraint {

    private Set<Variable> scope;
    private Map<Variable, Set<String>> terms;

    public IncompatibilityConstraint(Map<Variable, Set<String>> terms) {
        this.terms = terms;
        this.scope = new HashSet();
        for (Variable var : terms.keySet()) {
            scope.add(var);
        }
    }

    public IncompatibilityConstraint(List<RestrictedDomain> terms) {
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
        boolean isSatisfied = true;

        return isSatisfied;
    }

    @Override
    public boolean isSatisfiedBy(Map<Variable, String> assessment) {
        boolean isSatisfied = true;
        if (assessment.keySet().containsAll(scope)) {
            isSatisfied = false;
            for (Variable var : scope) {
                if (!this.terms.get(var).contains(assessment.get(var))) {
                    isSatisfied = true;
                }
            }
        }
        return isSatisfied;
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
