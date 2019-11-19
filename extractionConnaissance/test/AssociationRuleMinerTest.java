/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractionConnaissance.test;

import extractionConnaissance.AssociationRuleMiner;
import extractionConnaissance.BooleanDatabase;
import extractionConnaissance.FrequentItemsetMiner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import representation.Variable;
import java.util.Set;
import representation.RestrictedDomain;
import representation.Rule;

/**
 *
 * @author ordinaute
 */
public class AssociationRuleMinerTest {

    public static void main(String[] args) {
        List<Variable> vars = new ArrayList();
        vars.add(Variable.makeBooleanVariable("A"));
        vars.add(Variable.makeBooleanVariable("B"));
        vars.add(Variable.makeBooleanVariable("C"));
        vars.add(Variable.makeBooleanVariable("D"));
        vars.add(Variable.makeBooleanVariable("E"));

        int minFreq = 3;
        Map<Variable, Boolean> transaction1 = new HashMap();
        transaction1.put(vars.get(0), Boolean.TRUE);
        transaction1.put(vars.get(1), Boolean.TRUE);
        transaction1.put(vars.get(2), Boolean.TRUE);
        transaction1.put(vars.get(3), Boolean.TRUE);
        transaction1.put(vars.get(4), Boolean.TRUE);

        Map<Variable, Boolean> transaction2 = new HashMap();
        transaction2.put(vars.get(0), Boolean.TRUE);
        transaction2.put(vars.get(2), Boolean.TRUE);

        Map<Variable, Boolean> transaction3 = new HashMap();
        transaction3.put(vars.get(0), Boolean.TRUE);
        transaction3.put(vars.get(1), Boolean.TRUE);
        transaction3.put(vars.get(2), Boolean.TRUE);
        transaction3.put(vars.get(3), Boolean.TRUE);

        Map<Variable, Boolean> transaction4 = new HashMap();
        transaction4.put(vars.get(1), Boolean.TRUE);
        transaction4.put(vars.get(2), Boolean.TRUE);
        
        Map<Variable, Boolean> transaction5 = new HashMap();
        transaction5.put(vars.get(0), Boolean.TRUE);
        transaction5.put(vars.get(1), Boolean.TRUE);
        transaction5.put(vars.get(2), Boolean.TRUE);
        
        Map<Variable, Boolean> transaction6 = new HashMap();
        transaction5.put(vars.get(4), Boolean.TRUE);

        List<Map<Variable, Boolean>> transactions = new ArrayList();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);
        transactions.add(transaction5);
        transactions.add(transaction6);
        BooleanDatabase db = new BooleanDatabase(vars, transactions);
        FrequentItemsetMiner fiMiner = new FrequentItemsetMiner(db);
        Map<Set<Variable>, Integer> frequentItemsets = fiMiner.frequentItemsets(2);
        for(Set<Variable> varset : frequentItemsets.keySet()){
            System.out.print("Itemset: ");
            for(Variable var : varset){
                System.out.print(var.getName());
            }
            System.out.println();
        }
        Set<Variable> scope = new HashSet();
        scope.addAll(fiMiner.getMinedScope());
        AssociationRuleMiner arMiner = new AssociationRuleMiner(db, frequentItemsets, scope);
        Map<Rule, Integer> minedRules = AssociationRuleMiner.ruleMiner(minFreq, db.getTransactions(), frequentItemsets, scope, true);
        Set<Rule> confFilteredRules = AssociationRuleMiner.confidenceFilter(0.6, frequentItemsets, minedRules);
        Set<Rule> rules = minedRules.keySet();
        for (Rule rule : confFilteredRules){
            printBooleanRule(rule);
            //System.out.println("FREQ: " + minedRules.get(rule));
        }
    }
    
    private static void printBooleanRule(Rule rule){
        System.out.print("Rule: ");
        for(RestrictedDomain domain : rule.getPremise()){
            System.out.print(domain.getVariable().getName());
        }
        System.out.print(" => ");
        for(RestrictedDomain domain : rule.getConclusion()){
            System.out.print(domain.getVariable().getName());
        }
        System.out.println();
    }

}
