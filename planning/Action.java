/**
 *
 * @author 21600639 : DENOUAL Axel
 * @author 21910036 : ROUSSEAU Alexy
 * @author 21907858 : SABATIER Brian
 * 
 */

package planning;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import representation.Variable;

public class Action {
    
    protected Set<ActionRule>  rules;
    protected final String name;
    protected final int cost;
    

    public Action() {
        this.rules = new HashSet();
        this.name = "";
        this.cost = 0;
    }
    
    public Action(String name){
        this.rules = new HashSet();
        this.name = name;
        this.cost = 0;
    }
    
    public Action(int cost) {
        this.rules = new HashSet();
        this.name = "";
        this.cost = cost;
    }
    
    public Action(String name, int cost){
        this.rules = new HashSet();
        this.name = name;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public Set<ActionRule> getRules() {
        return rules;
    }
    
    public void addRule(ActionRule rule){
        this.rules.add(rule);
    }
       
    public boolean is_applicable(State state){
        for(ActionRule rule : this.rules){
            if(state.satisfies(rule.getPreconditions())){
                return true;   
            }
        }
        return false;

    }

    @Override
    public String toString() {
        String ts = "Action{ \n";
        if(this.name != ""){
            ts = "Action " + this.name + "{ \n";
        }
        for(Iterator<ActionRule> ruleIt = rules.iterator(); ruleIt.hasNext();){
            ts += ruleIt.next().toString() + "\n";

        }
        ts += '}';
        return ts;
    }
}
