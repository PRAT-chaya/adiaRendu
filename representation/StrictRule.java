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
 * @author ordinaute
 */
public class StrictRule extends Rule{
    public StrictRule(List<RestrictedDomain> premise, List<RestrictedDomain> conclusion){
        super(premise, conclusion);
    }
    
    @Override
    public boolean isSatisfiedBy(Map<Variable, String> assessment){
        boolean isSatisfied = false;
        if(this.isInPremiseScope(assessment)){
            Map<Variable, Set<String>> conclusionMap = this.getConclusionMap();
            for(Variable var : conclusionMap.keySet()){
                if (assessment.containsKey(var)){
                    if(!conclusionMap.get(var).contains(assessment.get(var))){
                        return false;
                    }
                } else {
                    return false;
                }
            }
            isSatisfied = true;
        }
        return isSatisfied;
    }

    @Override
    public boolean isSatisfiedBy(List<RestrictedDomain> assessment){
        boolean isSatisfied = false;
        if(this.isInPremiseScope(assessment)){
            Map<Variable, Set<String>> conclusionMap = this.getConclusionMap();
            for(Variable var : conclusionMap.keySet()){
                for (RestrictedDomain domain : assessment){
                    if(var == domain.getVariable()){
                        if(conclusionMap.get(var).containsAll(domain.getSubdomain())){
                            isSatisfied = true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return isSatisfied;
    }
    
    @Override
    public boolean isInPremiseScope(Map<Variable, String> assignment){
        boolean isInPremiseScope = false;
        for(RestrictedDomain prd : premise){
            if(assignment.containsKey(prd.getVariable())){
                if (!prd.getSubdomain().contains(assignment.get(prd.getVariable()))) {
                    return false;
                }                
            } else {
              return false;
            }
            isInPremiseScope = true;
        }
        return isInPremiseScope;
    }
}
