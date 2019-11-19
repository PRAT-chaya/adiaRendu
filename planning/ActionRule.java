/**
 *
 * @author 21600639 : DENOUAL Axel
 * @author 21910036 : ROUSSEAU Alexy
 * @author 21907858 : SABATIER Brian
 * 
 */

package planning;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import representation.RestrictedDomain;
import representation.Variable;

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
