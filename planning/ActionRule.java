/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planning;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import representation.RestrictedDomain;
import representation.Variable;

/**
 *
 * @author axel
 */
public class ActionRule {
    protected Map<Variable,String> preconditions;
    protected Map<Variable,String> effets;

    public ActionRule() {
        this.preconditions = new HashMap();
        this.effets = new HashMap();
    }
    
    public ActionRule(Map<Variable,String> pre, Map<Variable, String> effets) {
        this.preconditions = pre;
        this.effets = effets;
    }
    
    public void ajoutPrecondition(Variable var, String val){
        this.preconditions.put(var, val);
    }
    
    public void ajoutEffet(Variable var, String val){
        this.effets.put(var, val);
    }

    public Map<Variable,String> getPreconditions() {
        return preconditions;
    }

    public Map<Variable, String> getEffets() {
        return effets;
    }

    @Override
    public String toString() {
        String ts = "";
        if(!preconditions.isEmpty()){
            for (Variable x : preconditions.keySet()) {
                ts += x.getName() + ":" + preconditions.get(x) +  " ∧ ";
                }
        }else{
         ts += " T ";   
        }
        ts += " => ";
        for (Variable x : effets.keySet()) {
            ts += x.getName() + ":" + effets.get(x) + " ∨ ";
        }
        return ts;
        
    }
    
    
    
    
}
