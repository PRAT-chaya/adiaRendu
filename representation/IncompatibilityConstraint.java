/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author 21907858
 */
public class IncompatibilityConstraint extends Rule implements Constraint {

    private Set<RestrictedDomain> scope;

    public IncompatibilityConstraint(List<RestrictedDomain> premise, List<RestrictedDomain> conclusion) {
        super(premise, conclusion);
    }

    @Override
    public boolean isSatisfiedBy(List<RestrictedDomain> assessment) {
        boolean isSatisfied = true;
        //Pour chaque variable de l'instance testée
        for (int i = 0; i < assessment.size(); i++) {
            Variable assessmentPremVar = assessment.get(i).getVariable();
            //Si la prémisse est concernée par la variable
            for (int x = 0; x < premise.size(); x++) {
                if (premise.get(x).getVariable() == assessmentPremVar) {
                    /*Si le sous-domaine de la variable tel qu'incluse dans la
                (pseudo)prémisse contient la valeur associée à cette variable
                dans l'instance testée*/
                    if (premise.get(x).subDomainContains(assessment.get(i).getSubdomain())) {
                        //Pour chaque variable de l'instance testée
                        for (int j = 0; j < assessment.size(); j++) {
                            Variable assessmentCclVar = assessment.get(j).getVariable();
                            /*Si le domaine de la variable tel qu'incluse
                        dans la (pseudo)conclusion contient la valeur associée
                        à cette variable dans l'instance testée*/
                            for (int y = 0; y < conclusion.size(); y++) {
                                if (conclusion.get(y).getVariable() == assessmentCclVar) {
                                    if (conclusion.get(y).subDomainContains(assessment.get(j).getSubdomain())) {
                                        isSatisfied = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
            return isSatisfied;
    }
    
    @Override
    public boolean isSatisfiedBy(Map<Variable, String> assessment){
        return false;
    }
}
