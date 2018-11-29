/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enterprise.systems;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adwait Chhetri
 */
public class Individual {

    int geneLength = 80;
    int numRule = 10;
    
    

    Data dataSet = new Data();
    int datasetLength = dataSet.datasetLength;
    
    Rule rule = new Rule();
    int condLength = rule.condLength;
    
    int[] genes = new int[geneLength];
    Rule[] ruleBase = new Rule[numRule];
    Data[] data1 = new Data[datasetLength];
    int fitness;

    // initailise ruleBase
    public void initailiseRules() {
        for (int i = 0; i < numRule; i++) {
            ruleBase[i] = new Rule();

        }
    }

    // initailise data set
    public void initailiseDataSet() {
        for (int i = 0; i < datasetLength; i++) {
            data1[i] = new Data();
        }
    }

    public Individual() {
        Random rn = new Random();
        int count = 0;

        //Set genes randomly for each individual
        for (int i = 0; i < genes.length; i++) {
            if (count == condLength){
                genes[i] = Math.abs(rn.nextInt() % 2);
                count = 0;
            }else{
                genes[i] = Math.abs(rn.nextInt() % 3);
                count++;
            }
            
            
            
        }

        fitness = 0;
    }

    //set genes and rules genes equal to each other 
    public void setGenesEqualRules() {

        int k = 0;

        for (int i = 0; i < numRule; i++) {

            for (int j = 0; j < condLength; j++) {
                //5 is suppose to be condLength

                ruleBase[i].cond[j] = genes[k++];
                //System.out.print(ruleBase[i].cond[j]);
            }

            ruleBase[i].output = genes[k++];

        }

    }

     //calculate fitness
    public int fitness_Function() {
        fitness = 0;
//        int [][] ruleArray = create2dRuleArray();
//        int [][] dataArray = create2dDataArray();

        for (int f = 0; f < datasetLength; f++) {
            for (int t = 0; t < numRule; t++) {
                // System.out.println(Arrays.toString(data1[t].variables));
                //System.out.println("hi");
                if (matchesCond(ruleBase[t].cond, data1[f].variables) == true
                    || checkWildCard(ruleBase[t].cond, data1[f].variables) == true ) {
                   // System.out.println("yes");
                    if (matchesOutput(ruleBase[t].output, data1[f].classs) == true) {
                        //System.out.println("hello");
                        fitness++;
                        
                    }
                    break;
                    

                }
                
            }

        }
        return fitness;
    }

//     // calculate original fitness
//    public int fitness_Function() {
//        fitness = 0;
//
//        for (int i = 0; i < geneLength ; i++) {
//            if(genes[i] == 1){
//                fitness++;
//            }
//
//
////            System.out.println("\n");
//        }
//        return fitness;
//
//    }
    
    
    
    public boolean matchesCond(int[] ruleArray, int[] dataArray) {
        boolean mC = false;

        // check wether rules and data are the same 
        for (int n = 0; n < numRule; n++) {
            for (int m = 0; m < datasetLength; m++) {
//                System.out.println("\nis " + Arrays.toString(ruleArray[n]) + " equals: " + Arrays.toString(dataArray[m]));

                //if (Arrays.equals(ruleArray[n], dataArray[m])) {
                if (Arrays.equals(ruleArray, dataArray)) {
                   // System.out.println(Arrays.toString(ruleArray) + "equals" + Arrays.toString(dataArray));
                    mC = true;
                    // return mC;

                }
                //break;

            }

        }
        return mC;
    }

    public boolean matchesOutput(int ruleOutput, int dataOutput) {

        boolean mO = false;

        for (int k = 0; k < numRule; k++) {
            for (int l = 0; l < datasetLength; l++) {
                if (ruleOutput == dataOutput) {
                    mO = true;
                    //return mO;
                }
                //break;
            }
        }
        return mO;
    }
    
    // checks if the value ruleArray contians wild card
    public boolean checkWildCard(int[] ruleArray, int[] dataArray){
        boolean cWC = false;
        int count = 0;
        // condLength is the size of cond in rule class
        for (int i =0; i < condLength; i++){
            if (ruleArray[i] == dataArray[i] || ruleArray[i] == 2){
                
               count++;
            }
        }
        if(count == condLength){
            cWC = true;
            
        }
        return cWC;
    }

    // read from data file into data object
    public void readDataFile() throws FileNotFoundException {

        File file = new File("/Users/sanyogchhetri/Documents/AC - Computer Sci "
                + "- 3rd year/Biocomputation/data1.txt");
        File file2 = new File("/Users/sanyogchhetri/Documents/AC - Computer Sci "
                + "- 3rd year/Biocomputation/data2.txt");

        Scanner sc = new Scanner(file2);

        sc.nextLine();

        //System.out.printf("%s, %s\n", a,b);
        for (int j = 0; j < datasetLength; j++) {
            // store variable into a and classs into b from file
            String a = sc.next();
            String b = sc.next();

            for (int k = 0; k < condLength; k++) {

                //set variables in data class from file
                int c = a.charAt(k) - '0';
                data1[j].variables[k] = c;

                //System.out.print(data1[j].variables[k]);
            }
            //System.out.println("\n");
            // set classs to variable from file 
            data1[j].classs = Integer.parseInt(b);
            //System.out.println(data1[j].classs);
        }

    }
}
