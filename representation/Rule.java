/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import satisfiability.GeneralizedArcConsistency;

/**
 *
 * @author 21907858
 */
public class Rule implements Constraint {

    protected Set<Variable> scope;
    protected Set<Variable> premiseScope;
    protected List<RestrictedDomain> premise;
    protected List<RestrictedDomain> conclusion;

    public Rule(List<RestrictedDomain> premise, List<RestrictedDomain> conclusion) {
        this.premise = premise;
        this.conclusion = conclusion;
        this.scope = new HashSet();
        this.premiseScope = new HashSet();
        for (int i = 0; i < premise.size(); i++) {
            Variable var = premise.get(i).getVariable();
            this.scope.add(var);
            this.premiseScope.add(var);
        }
        for (int i = 0; i < conclusion.size(); i++) {
            this.scope.add(conclusion.get(i).getVariable());
        }
    }

    @Override
    public Set<Variable> getScope() {
        return scope;
    }

    public Set<Variable> getPremiseScope() {
        return premiseScope;
    }

    @Override
    public List<RestrictedDomain> getDomains() {
        List<RestrictedDomain> domains = new ArrayList();
        domains.addAll(premise);
        domains.addAll(conclusion);
        return domains;
    }

    public List<RestrictedDomain> getPremise() {
        return premise;
    }

    public List<RestrictedDomain> getConclusion() {
        return conclusion;
    }

    @Override
    public boolean isSatisfiedBy(List<RestrictedDomain> assessment) {
        boolean isSatisfied = true; // Si l'assignation n'est pas concernée par la prémisse elle est satisfaisante
        if (this.isInPremiseScope(assessment)) {
            isSatisfied = false;
            for (RestrictedDomain crd : conclusion) {
                for (RestrictedDomain assessmentRd : assessment) {
                    if (crd.getVariable() == assessmentRd.getVariable()) {
                        for (String val : assessmentRd.getSubdomain()) {
                            if (crd.getSubdomain().contains(val)) {
                                isSatisfied = true;
                            }
                        }
                    }
                }
            }
        } else {
            isSatisfied = true;
        }
        return isSatisfied;
    }

    @Override
    public boolean isSatisfiedBy(Map<Variable, String> assessment) {
        boolean isSatisfied = true; // Si l'assignation n'est pas concernée par la prémisse elle est satisfaisante
        if (this.isInPremiseScope(assessment)) {
            isSatisfied = false;
            for (RestrictedDomain crd : conclusion) {
                if (assessment.keySet().contains(crd.getVariable())) {
                    for (Variable var : assessment.keySet()) {
                        if (crd.getSubdomain().contains(assessment.get(var)) && crd.getVariable() == var) {
                            isSatisfied = true;
                        }
                    }
                }
            }
        }
        return isSatisfied;
    }

    public boolean isInPremiseScope(List<RestrictedDomain> assessment) {
        boolean isInPremiseScope = true;
        for (RestrictedDomain prd : premise) {
            for (RestrictedDomain assessmentRd : assessment) {
                if (prd.getVariable() == assessmentRd.getVariable()) {
                    for (String val : assessmentRd.getSubdomain()) {
                        if (!prd.getSubdomain().contains(val)) {
                            isInPremiseScope = false;
                        }
                    }
                }
            }
        }
        return isInPremiseScope;
    }

    public boolean isInPremiseScope(Map<Variable, String> assessment) {
        boolean isInPremiseScope = true;
        for (RestrictedDomain prd : premise) {
            for (Variable var : assessment.keySet()) {
                if (prd.getVariable() == var) {
                    if (!prd.getSubdomain().contains(assessment.get(var))) {
                        isInPremiseScope = false;
                    }
                }
            }
        }
        return isInPremiseScope;
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

    @Override
    public String toString() {
        String premStr = "PREM{\n";
        String cclStr = "CCL{\n";
        for (RestrictedDomain domain : this.premise) {
            premStr += domain.toString() + ",\n";
        }
        for (RestrictedDomain domain : this.conclusion) {
            cclStr += domain.toString() + ",\n";
        }
        premStr += "}";
        cclStr += "}";
        String ruleStr = "***RULE***\n" + premStr + "\n" + cclStr;
        return ruleStr;
    }
    
    protected Map<Variable, Set<String>> getPremiseMap(){
        return Rule.getDomainsMap(this.premise);
    }
    
    protected Map<Variable, Set<String>> getConclusionMap(){
        return Rule.getDomainsMap(this.conclusion);
    }
    
    protected static Map<Variable, Set<String>> getDomainsMap(List<RestrictedDomain> domains){
        Map<Variable, Set<String>> domainsMap = new HashMap();
        for(RestrictedDomain domain : domains){
            domainsMap.put(domain.getVariable(), domain.getSubdomain());
        }
        return domainsMap;
    }
}
