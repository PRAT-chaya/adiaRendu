/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractionConnaissance.test;

import extractionConnaissance.BooleanDatabase;
import extractionConnaissance.FrequentItemsetMiner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import representation.Variable;
import static extractionConnaissance.FrequentItemsetMiner.buildCombination;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ordinaute
 */
public class FrequentItemsetMinerTest {

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

        System.out.println("***FREQUENT SINGLETONS***");
        testFrequentSingletons(vars, minFreq, transactions);
        System.out.println("***COMBINATION UTIL***");
        testCombinationUtil(vars);
        System.out.println("***TUPLE IN TRANSACTION***");
        testIsTupleInTransaction(vars, transactions);
        System.out.println("***TUPLES FREQ***");
        testTuplesFrequence(vars, transactions);
        System.out.println("***FREQ FILTER***");
        testFreqFilter(vars, minFreq);
        System.out.println("***FREQUENT ITEMSETS***");
        testFrequentItemsets(vars, transactions);
    }

    static void testFrequentSingletons(List<Variable> vars, int minFreq, List<Map<Variable, Boolean>> transactions) {
        Map<Variable, Integer> expected = new HashMap();
        expected.put(vars.get(0), 3);
        expected.put(vars.get(1), 2);
        expected.put(vars.get(2), 2);

        Map<Variable, Integer> tested = FrequentItemsetMiner.frequentSingletons(minFreq, vars, transactions);

        for (Variable var : tested.keySet()) {
            System.out.println((expected.get(var) == tested.get(var)) + ", "
                    + expected.get(var) + " " + tested.get(var));
        }
    }

    static void testCombinationUtil(List<Variable> varList) {
        int r = 3;
        Variable vars[] = new Variable[varList.size()];
        for (int i = 0; i < varList.size(); i++) {
            vars[i] = varList.get(i);
        }
        List<Variable[]> toCheck = new ArrayList();
        buildCombination(vars, vars.length, r, toCheck);

        Variable[] abc = new Variable[]{
            varList.get(0),
            varList.get(1),
            varList.get(2)};
        Variable[] abd = new Variable[]{
            varList.get(0),
            varList.get(1),
            varList.get(3)};
        Variable[] abe = new Variable[]{
            varList.get(0),
            varList.get(1),
            varList.get(4)};
        Variable[] acd = new Variable[]{
            varList.get(0),
            varList.get(2),
            varList.get(3)};
        Variable[] ace = new Variable[]{
            varList.get(0),
            varList.get(2),
            varList.get(4)};
        Variable[] ade = new Variable[]{
            varList.get(0),
            varList.get(3),
            varList.get(4)};
        Variable[] bcd = new Variable[]{
            varList.get(1),
            varList.get(2),
            varList.get(3)};
        Variable[] bce = new Variable[]{
            varList.get(1),
            varList.get(2),
            varList.get(4)};
        Variable[] bde = new Variable[]{
            varList.get(1),
            varList.get(3),
            varList.get(4)};
        Variable[] cde = new Variable[]{
            varList.get(2),
            varList.get(3),
            varList.get(4)};

        List<Variable[]> expected = new ArrayList();
        expected.add(abc);
        expected.add(abd);
        expected.add(abe);
        expected.add(acd);
        expected.add(ace);
        expected.add(ade);
        expected.add(bcd);
        expected.add(bce);
        expected.add(bde);
        expected.add(cde);

        List<String> toCheckStr = new ArrayList();
        for (Variable[] tuple : toCheck) {
            String tupleStr = "";
            for (Variable var : tuple) {
                tupleStr += var.getName();
            }
            toCheckStr.add(tupleStr);
        }

        List<String> expectedStr = new ArrayList();
        for (Variable[] tuple : expected) {
            String tupleStr = "";
            for (Variable var : tuple) {
                tupleStr += var.getName();
            }
            expectedStr.add(tupleStr);
        }

        boolean passed = true;
        for (String str : expectedStr) {
            if (!toCheckStr.contains(str)) {
                passed = false;
            }
        }
        System.out.println(passed);
    }

    static void testTuplesFrequence(List<Variable> vars, List<Map<Variable, Boolean>> transactions) {
        Set<Set<Variable>> tuples = new HashSet();
        Set<Variable> ab = new HashSet<>(Arrays.asList(new Variable[]{vars.get(0), vars.get(1)}));
        Set<Variable> ac = new HashSet<>(Arrays.asList(new Variable[]{vars.get(0), vars.get(2)}));
        Set<Variable> ad = new HashSet<>(Arrays.asList(new Variable[]{vars.get(0), vars.get(3)}));
        Set<Variable> ae = new HashSet<>(Arrays.asList(new Variable[]{vars.get(0), vars.get(4)}));
        Set<Variable> bc = new HashSet<>(Arrays.asList(new Variable[]{vars.get(1), vars.get(2)}));
        Set<Variable> bd = new HashSet<>(Arrays.asList(new Variable[]{vars.get(1), vars.get(3)}));
        Set<Variable> be = new HashSet<>(Arrays.asList(new Variable[]{vars.get(1), vars.get(4)}));
        Set<Variable> cd = new HashSet<>(Arrays.asList(new Variable[]{vars.get(2), vars.get(3)}));
        Set<Variable> ce = new HashSet<>(Arrays.asList(new Variable[]{vars.get(2), vars.get(4)}));
        Set<Variable> de = new HashSet<>(Arrays.asList(new Variable[]{vars.get(3), vars.get(4)}));
        tuples.add(ab);
        tuples.add(ac);
        tuples.add(ad);
        tuples.add(ae);
        tuples.add(bc);
        tuples.add(bd);
        tuples.add(be);
        tuples.add(cd);
        tuples.add(ce);
        tuples.add(de);

        Map<Set<Variable>, Integer> expected = new HashMap();
        expected.put(ab, 1);
        expected.put(ac, 2);
        expected.put(ad, 1);
        expected.put(ae, 0);
        expected.put(bc, 1);
        expected.put(bd, 0);
        expected.put(be, 1);
        expected.put(cd, 0);
        expected.put(ce, 0);
        expected.put(de, 0);

        Map<Set<Variable>, Integer> toCheck = FrequentItemsetMiner.tuplesFrequence(tuples, transactions);

        System.out.println("---Control display---");
        for (Set<Variable> tuple : toCheck.keySet()) {
            for (Variable var : tuple) {
                System.out.print(var.getName());
            }
            System.out.print(" ");
            System.out.print(toCheck.get(tuple) == expected.get(tuple));
            System.out.print(", tc:" + toCheck.get(tuple) + " exp:" + expected.get(tuple));
            System.out.println();
        }
    }

    static void testIsTupleInTransaction(List<Variable> vars, List<Map<Variable, Boolean>> transactions) {
        Set<Set<Variable>> tuples = new HashSet();
        Set<Variable> ab = new HashSet<>(Arrays.asList(new Variable[]{vars.get(0), vars.get(1)}));
        Set<Variable> ac = new HashSet<>(Arrays.asList(new Variable[]{vars.get(0), vars.get(2)}));
        Set<Variable> ad = new HashSet<>(Arrays.asList(new Variable[]{vars.get(0), vars.get(3)}));
        Set<Variable> ae = new HashSet<>(Arrays.asList(new Variable[]{vars.get(0), vars.get(4)}));
        Set<Variable> bc = new HashSet<>(Arrays.asList(new Variable[]{vars.get(1), vars.get(2)}));
        Set<Variable> bd = new HashSet<>(Arrays.asList(new Variable[]{vars.get(1), vars.get(3)}));
        Set<Variable> be = new HashSet<>(Arrays.asList(new Variable[]{vars.get(1), vars.get(4)}));
        Set<Variable> cd = new HashSet<>(Arrays.asList(new Variable[]{vars.get(2), vars.get(3)}));
        Set<Variable> ce = new HashSet<>(Arrays.asList(new Variable[]{vars.get(2), vars.get(4)}));
        Set<Variable> de = new HashSet<>(Arrays.asList(new Variable[]{vars.get(3), vars.get(4)}));
        tuples.add(ab);
        tuples.add(ac);
        tuples.add(ad);
        tuples.add(ae);
        tuples.add(bc);
        tuples.add(bd);
        tuples.add(be);
        tuples.add(cd);
        tuples.add(ce);
        tuples.add(de);

        Map<Set<Variable>, Boolean> expected1 = new HashMap();
        expected1.put(ab, true);
        expected1.put(ac, true);
        expected1.put(ad, false);
        expected1.put(ae, false);
        expected1.put(bc, true);
        expected1.put(bd, false);
        expected1.put(be, false);
        expected1.put(cd, false);
        expected1.put(ce, false);
        expected1.put(de, false);
        Map<Set<Variable>, Boolean> expected2 = new HashMap();
        expected2.put(ab, false);
        expected2.put(ac, true);
        expected2.put(ad, false);
        expected2.put(ae, false);
        expected2.put(bc, false);
        expected2.put(bd, false);
        expected2.put(be, false);
        expected2.put(cd, false);
        expected2.put(ce, false);
        expected2.put(de, false);

        Map<Set<Variable>, Boolean> toCheck = new HashMap();
        for (Set<Variable> tuple : tuples) {
            toCheck.put(tuple, FrequentItemsetMiner.isTupleInTransaction(tuple, transactions.get(0)));
        }

        boolean correct1 = true;
        for (Set<Variable> tuple : tuples) {
            if (!(expected1.get(tuple) == toCheck.get(tuple))) {
                correct1 = false;
            }
        }

        for (Set<Variable> tuple : tuples) {
            toCheck.replace(tuple, FrequentItemsetMiner.isTupleInTransaction(tuple, transactions.get(1)));
        }
        boolean correct2 = true;
        for (Set<Variable> tuple : tuples) {
            if (!(expected2.get(tuple) == toCheck.get(tuple))) {
                correct2 = false;
            }
        }

        System.out.print("transaction 1 : ");
        System.out.print(correct1);
        System.out.println();
        System.out.print("transaction 2 : ");
        System.out.print(correct2);
        System.out.println();
    }

    static void testFreqFilter(List<Variable> vars, int minFreq) {
        Set<Set<Variable>> tuples = new HashSet();
        Set<Variable> ab = new HashSet<>(Arrays.asList(new Variable[]{vars.get(0), vars.get(1)}));
        Set<Variable> ac = new HashSet<>(Arrays.asList(new Variable[]{vars.get(0), vars.get(2)}));
        Set<Variable> ad = new HashSet<>(Arrays.asList(new Variable[]{vars.get(0), vars.get(3)}));
        Set<Variable> bc = new HashSet<>(Arrays.asList(new Variable[]{vars.get(1), vars.get(2)}));
        Set<Variable> be = new HashSet<>(Arrays.asList(new Variable[]{vars.get(1), vars.get(4)}));
        Map<Set<Variable>, Integer> tuplesFrequence = new HashMap();
        Map<Set<Variable>, Integer> expected = new HashMap();
        
        tuplesFrequence.put(ab, 1);
        tuplesFrequence.put(ac, 2);
        tuplesFrequence.put(ad, 1);
        tuplesFrequence.put(bc, 1);
        tuplesFrequence.put(be, 1);
        
        expected.put(ac, 2);
        
        
        tuplesFrequence = FrequentItemsetMiner.freqFilter(tuplesFrequence, minFreq);
        boolean correct = false;
        
        if (tuplesFrequence.keySet().contains(ac)){
            correct = true;
            for (Set<Variable> tuple : tuplesFrequence.keySet()){
                if (!(tuplesFrequence.get(tuple) == expected.get(tuple))){
                    correct = false;
                }
            }
        }
        System.out.print("Only ac left, ");
        System.out.print(correct);
        System.out.println();
        
        tuplesFrequence = FrequentItemsetMiner.freqFilter(tuplesFrequence, 3);
        System.out.println(tuplesFrequence.isEmpty());
    }
    
    static void testFrequentItemsets(List<Variable> varlist, List<Map<Variable, Boolean>> transactions){
        FrequentItemsetMiner miner = new FrequentItemsetMiner(new BooleanDatabase(varlist, transactions));
        Map<Set<Variable>, Integer> toCheck = miner.frequentItemsets(3);
        for (Set<Variable> tuple : toCheck.keySet()){
            for (Variable var : tuple){
                System.out.print(var.getName());
            }
            System.out.print(" : " + toCheck.get(tuple));
            System.out.println();
        }
    }
}
