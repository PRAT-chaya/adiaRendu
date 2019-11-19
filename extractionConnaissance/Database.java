/**
 *
 * @author 21600639 : DENOUAL Axel
 * @author 21910036 : ROUSSEAU Alexy
 * @author 21907858 : SABATIER Brian
 * 
 */

package extractionConnaissance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import representation.Variable;

public class Database {

    
    private List<Variable> orderedVariables;
    private List<Map<Variable, String>> instances;

    public Database(List<Variable> orderedVariables, List<Map<Variable, String>> instances) {
        this.orderedVariables = orderedVariables;
        this.instances = instances;
    }
    
    public static BooleanDatabase booleanConvert(Database db){
        BooleanDatabase booleanDB = new BooleanDatabase();
        booleanDB.setVariables(db.getOrderedVariables());
        for(Map<Variable, String> instance : db.getInstances()){
            Map<Variable,Boolean> res = new HashMap();
            
            for(Variable var : instance.keySet()){
                switch(instance.get(var)){
                    case "0":
                        res.put(var, Boolean.FALSE);
                        break;
                        
                    case "1":
                        res.put(var, Boolean.TRUE);
                        break;
                        
                    default:
                        Variable newvar = new Variable(var.getName()+"_"+instance.get(var),"oui","non");
                        Variable existantVar = ListContainsVar(booleanDB.getVariables(), newvar);
                        if(existantVar != null){
                            
                            res.put(existantVar, Boolean.TRUE);
                        }
                        else{
                            booleanDB.getVariables().add(newvar);
                            res.put(newvar, Boolean.TRUE);
                        }
                        
                        break;
                }
                
            }
            booleanDB.getTransactions().add(res);
        }
        return booleanDB;
    }
    
    public static Variable ListContainsVar(List<Variable> liste, Variable var){
        Variable res = null;
        for(Variable listeVar: liste){
            if(var.getName().equals(listeVar.getName())){
                return res = listeVar;
            }
        }
        return res;
    }
    
    public static String ListToString(List<Variable> liste){
        String res = "( ";
        for(Variable listeVar: liste){
        res += listeVar.getName() + ", ";
        }
        res += ")";
        return res;
    }
    
    public static void addColumn(Variable var, BooleanDatabase booleanDB){
        for(Map<Variable, Boolean> instance : booleanDB.getTransactions()){
            Boolean isInInstance = false;
            for(Variable key : instance.keySet()){
                if(key.getName().equals(var.getName())){
                    isInInstance = true;
                }
            }
            if(!isInInstance){
                instance.put(var, Boolean.FALSE);
            }
        }
    }
    
    public List<Variable> getOrderedVariables() {
        return orderedVariables;
    }

    public List<Map<Variable, String>> getInstances() {
        return instances;
    }

    @Override
    public String toString() {
        String res = "Database : \n";
        for (int i = 0; i < 20; i++) {
            Map<Variable, String> instance = this.getInstances().get(i);
            for(Variable var : instance.keySet()){
                res += var.getName() + " : " + instance.get(var) + "; ";
            }
            res += "\n";
        }
        return res;
    }
}
