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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import representation.RestrictedDomain;
import representation.Rule;
import representation.StrictRule;
import representation.Variable;

/**
 * On essaie de déterminer des règles d'association à partir de motifs fréquents
 * CAD que pour une source donnnées donnée
 * A partir de frequentItemSetMiner
 */
public class AssociationRuleMiner {

    private Map<Set<Variable>, Integer> frequentItemsets;
    private BooleanDatabase db;
    // Scope représente toutes les Variables qui apparaissent dans les ItemSet fréquents, préalablements extraits avec frequentItemSetMiner()
    private Set<Variable> scope;

    public AssociationRuleMiner(BooleanDatabase db, Map<Set<Variable>, Integer> frequentItemsets, Set<Variable> scope) {
        this.db = db;
        this.frequentItemsets = frequentItemsets;
        this.scope = scope;
    }

    
    /**
     * Méthode permettant de miner des règles strictes
     * Générer des règles candidates et vérifier si elles sont intéressantes
     * Appelle rule miner avec uniquement des règles strictes
     * @param minFreq
     * @param minConf
     * @return 
     */
    public Set<Rule> strictRuleMiner(int minFreq, double minConf) {
        return AssociationRuleMiner.confidenceFilter(
                minConf, this.frequentItemsets,
                ruleMiner(minFreq, this.db.getTransactions(), this.frequentItemsets, this.scope, true));
    }

    /**
     * Génère les règles candidates
     * @param minFreq
     * @param transactions
     * @param frequentItemsets
     * @param scope
     * @param strictRule
     * @return 
     */
    public static Map<Rule, Integer> ruleMiner(int minFreq,
            List<Map<Variable, Boolean>> transactions,
            Map<Set<Variable>, Integer> frequentItemsets,
            Set<Variable> scope, boolean strictRule) {
        Map<Rule, Integer> minedRules = new HashMap();
        for (int r = 1; r < scope.size(); r++) {
            Map<Rule, Integer> tempMinedRules = new HashMap();
            Set<Set<Variable>> varsets = new HashSet();
            for (Set<Variable> itemset : frequentItemsets.keySet()) {
                if (itemset.size() > r) {
                    varsets.add(itemset);
                }
            }
            Set<Rule> toCheck = new HashSet();
            for (Set<Variable> varset : varsets) {
                toCheck.addAll(rulesBuilder(varset, r, strictRule));
            }
            for (Map<Variable, Boolean> transaction : transactions) {
                Map<Variable, String> assignment = new HashMap();
                for (Variable var : transaction.keySet()) {
                    assignment.put(var, Variable.TRUE);
                }
                for (Rule rule : toCheck) {
                    //printBooleanRule(rule);
                    if (rule.isSatisfiedBy(assignment)) {
                        if (tempMinedRules.keySet().contains(rule)) {
                            int val = tempMinedRules.get(rule);
                            val++;
                            tempMinedRules.replace(rule, val);
                        } else {
                            tempMinedRules.put(rule, 1);
                        }
                        //System.out.println("freq: " + minedRules.get(rule));
                        //System.out.println();
                    }
                }
            }
            if (!tempMinedRules.isEmpty()) {
                boolean freqEnough = false;
                for (Integer freq : tempMinedRules.values()) {
                    if (freq >= minFreq) {
                        freqEnough = true;
                        break;
                    }
                }
                if (!freqEnough) {
                    break;
                } else {
                    Set<Rule>tempRuleSet = new HashSet();
                    tempRuleSet.addAll(tempMinedRules.keySet());
                    for(Rule rule : tempRuleSet) {
                        if (tempMinedRules.get(rule) < minFreq) {
                            tempMinedRules.remove(rule);
                        }
                    }
                    minedRules.putAll(tempMinedRules);
                }
            } else {
                break;
            }
        }
        return minedRules;
    }

    /**
     * Filtrer les règles à partir de leur confiance
     * 
     * @param minConf
     * @param frequentItemsets
     * @param minedRules
     * @return 
     */
    public static Set<Rule> confidenceFilter(double minConf, Map<Set<Variable>, Integer> frequentItemsets, Map<Rule, Integer> minedRules) {
        Set<Rule> filteredRules = new HashSet();
        filteredRules.addAll(minedRules.keySet());
        for(Rule rule : minedRules.keySet()){
            double ruleFreq = minedRules.get(rule);
            double itemsetFreq = frequentItemsets.get(rule.getPremiseScope());
            double ruleConf = ruleFreq/itemsetFreq;
            if(ruleConf < minConf){
                filteredRules.remove(rule);
            }
        }
        return filteredRules;
    }

    /**
     * 
     * @param itemset
     * @param r
     * @param strictRule
     * @return 
     */
    public static Set<Rule> rulesBuilder(Set<Variable> itemset, int r, boolean strictRule) {
        Set<Rule> rules = new HashSet();
        //Set<Variable> varset = new HashSet();
        //varset.addAll(itemset);
        //rules.addAll(rulesBuilder(r, varset, rules));
        List<Variable[]> premisesVars = new ArrayList();
        Variable vars[] = new Variable[itemset.size()];
        int i = 0;
        for (Variable var : itemset) {
            vars[i] = var;
            i++;
        }
        buildCombination(vars, vars.length, r, premisesVars);
        for (Variable[] premiseVars : premisesVars) {
            List<RestrictedDomain> premise = new ArrayList();
            List<RestrictedDomain> conclusion = new ArrayList();
            Set<Variable> temp = new HashSet();
            temp.addAll(itemset);
            for (Variable var : premiseVars) {
                premise.add(new RestrictedDomain(var, Variable.TRUE));
                temp.remove(var);
            }
            for (Iterator<Variable> tempIt = temp.iterator(); tempIt.hasNext();) {
                Variable var = tempIt.next();
                conclusion.add(new RestrictedDomain(var, Variable.TRUE));
            }
            if (!premise.isEmpty() && !conclusion.isEmpty()) {
                if (strictRule) {
                    rules.add(new StrictRule(premise, conclusion));
                } else {
                    rules.add(new Rule(premise, conclusion));
                }
            }
        }
        return rules;
    }

    private static void combinationUtil(Variable arr[], Variable data[], int start,
            int end, int index, int r, List<Variable[]> output) {

        // Current combination is ready to be printed, print it 
        if (index == r) {
            Variable[] temp = new Variable[r];
            temp = data.clone();
            output.add(temp);
            return;
        }

        // replace index with all possible elements. The condition 
        // "end-i+1 >= r-index" makes sure that including one element 
        // at index will make a combination with remaining elements 
        // at remaining positions 
        for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
            data[index] = arr[i];
            combinationUtil(arr, data, i + 1, end, index + 1, r, output);
        }
    }

    // Builds all combinations of size r in arr[] of size n.
    public static void buildCombination(Variable arr[], int n, int r, List<Variable[]> output) {
        // A temporary array to store all combination one by one 
        Variable data[] = new Variable[r];

        // Print all combination using temprary array 'data[]' 
        combinationUtil(arr, data, 0, n - 1, 0, r, output);
    }

    public static void printBooleanRule(Rule rule) {
        System.out.print("Rule: ");
        for (RestrictedDomain domain : rule.getPremise()) {
            System.out.print(domain.getVariable().getName());
        }
        System.out.print(" => ");
        for (RestrictedDomain domain : rule.getConclusion()) {
            System.out.print(domain.getVariable().getName());
        }
        System.out.println();
    }

    private static void printBooleanAssignment(Map<Variable, String> assignment) {
        System.out.print("Assignment: ");
        for (Variable var : assignment.keySet()) {
            System.out.print(var.getName());
        }
        System.out.println();
    }
}
