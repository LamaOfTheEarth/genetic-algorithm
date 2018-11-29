/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enterprise.systems;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adwaitchhetri
 */
public class GenerationalGA {

    static Individual individual = new Individual();
    static int popSize = 200; // only even numbers

    static int generation = 0;

    static Random rand = new Random();
    static Individual[] population = new Individual[popSize];
    static Individual[] offspring = new Individual[popSize];

    static int fittest = 0;
    static int leastFittest = 0;
    static int avgFitness = 0;
    static int bestFitnessIndex = 0;
    static int worstFitnessIndex = 0;
    static String headers = "Generation, Mean, Best";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        // create a new file to append value into
       // FileWriter csv_file = new FileWriter("Genetic Algorithm.csv");
       FileWriter csv_file = new FileWriter("Genetic Algorithm_dataset1.csv");
        csv_file.append(headers + "\n");
        
        
        
        
        // inistalising population and offspring
        for (int i = 0; i < popSize; i++) {
            population[i] = new Individual();
            offspring[i] = new Individual();
            population[i].initailiseRules();
            population[i].initailiseDataSet();
            population[i].setGenesEqualRules();
            population[i].readDataFile();
            offspring[i].initailiseRules();
            offspring[i].initailiseDataSet();
            offspring[i].setGenesEqualRules();
            offspring[i].readDataFile();
            //System.out.println(Arrays.toString(population[i].genes));

        }

        // calculate fitness for each individual
        for (Individual population1 : population) {
            population1.fitness_Function();
            //System.out.println(population1.fitness);

        }

        // while populaiton get's an individual with maximum fitness
        //while (fittest < 64){
        for (generation = 0; generation < 200 ; generation++) {

           // generation++;
            // do selection
            tournamentSelection();

            // do crossover
            singlePointCrossover();

            // do mutation
            bitWiseMutation();

            // set genes equal to rules
            for (Individual offspring1 : offspring) {
                offspring1.setGenesEqualRules();

            }

            // calculate offspring fitness 
            for (Individual offspring1 : offspring) {
                offspring1.fitness_Function();
                //System.out.println(offspring1.fitness);

            }

            // get worst fitness individual in offpsring
            getWorstIndividual(offspring);

            // replace worst fitness indiviudal with best fitness from previous generation
            for (int h = 0; h < individual.geneLength; h++) {
                int temp = population[bestFitnessIndex].genes[h];
                //System.out.println(worstFitnessIndex);
                offspring[worstFitnessIndex].genes[h] = temp;
                
                //System.out.println(offspring[worstFitnessIndex].fitness);

            }
            

            // manually copying each offspring array to population array
            for (int x = 0; x < popSize; x++) {
                for (int q = 0; q < individual.geneLength; q++) {

                    int temp = offspring[x].genes[q];

                    population[x].genes[q] = temp;

                }

            }

            // set genes equal to rules
            for (Individual population1 : population) {
                population1.setGenesEqualRules();

            }

            // update new population fitness
            for (Individual population1 : population) {
                population1.fitness_Function();
                //   System.out.println(population1.fitness);

            }

            // get's best individual of current generation
            getBestIndividual(population);

            // calcualtes the avg fitness of current generation
            calcAvgFitness(population);
            System.out.println("no. generations: " + generation);
            System.out.println("Population: " + popSize);
            System.out.println(Arrays.toString(population[bestFitnessIndex].genes));
            System.out.println("\n");
            
            // save values into csv file
            csv_file.append(generation + "," + avgFitness + "," + fittest + "\n");
            csv_file.flush();
            //csv_file.close();

        }

    }

    // offspring is not the same as the next generation - the fitness is not updated * solved by adding calcAvgFitness function
    public static void tournamentSelection() {

        for (int a = 0; a < popSize; a++) {
            int[] temp = new int[individual.geneLength];
            int[] temp2 = new int[individual.geneLength];

            int parent1 = rand.nextInt(popSize);
            int parent2 = rand.nextInt(popSize);

            for (int i = 0; i < individual.geneLength; i++) {
                temp[i] = population[parent1].genes[i];
                temp2[i] = population[parent2].genes[i];

            }

            if (population[parent1].fitness > population[parent2].fitness) {
                for (int b = 0; b < individual.geneLength; b++) {
                    offspring[a].genes[b] = temp[b];
                }
            } else {
                for (int b = 0; b < individual.geneLength; b++) {
                    offspring[a].genes[b] = temp2[b];
                }
            }
        }

    }

    public static void singlePointCrossover() {

        int temp = 0;
        int temp2 = 0;

        // increments in two, so population has to be even else error
        for (int i = 0; i < popSize; i += 2) {
            int crosspoint = rand.nextInt(individual.geneLength);

            for (int j = crosspoint; j < individual.geneLength; j++) {

                temp = offspring[i].genes[j];
                temp2 = offspring[i + 1].genes[j];

                offspring[i].genes[j] = temp2;

                offspring[i + 1].genes[j] = temp;

            }

        }

    }

    public static void bitWiseMutation() {
        Random rn = new Random();
        int mutationRate = 989;
        int temp = 0;

        for (int i = 0; i < popSize; i++) {

            int count = 0;

            for (int j = 0; j < individual.geneLength; j++) {
                int randInt = Math.abs(rn.nextInt() % 1000);

                if (count == individual.condLength) {
                    if (randInt > mutationRate) {
                        //System.out.println("before mutation: " + offspring[i].genes[j]);
                        offspring[i].genes[j] = offspring[i].genes[j] ^ 1;
                        //System.out.println("after mutation: " + offspring[i].genes[j]);
                    }

                    count = 0;
                } else {

                    if (randInt > mutationRate) {

                        if (offspring[i].genes[j] == 2) {
                            
                            offspring[i].genes[j] = rn.nextInt(2);
                        }
                           //offspring[i].genes[j] = Math.abs(rn.nextInt() % 3);
                        else if (offspring[i].genes[j] == 1){
                            if (rn.nextInt(2) == 1){
                                offspring[i].genes[j] = 0;
                            }
                            else{
                                offspring[i].genes[j] = 2;
                            }
                        }
                        else if(offspring[i].genes[j] == 0){
                            if (rn.nextInt(2) == 0){
                                offspring[i].genes[j] = 1;
                            }
                            else{
                                offspring[i].genes[j] = 2;
                            }
                        }
                    }

                    count++;
                }

            }
        }

    }

    public static int getBestIndividual(Individual[] individual) {

        int bestIndividual = individual[0].fitness;

        for (int i = 0; i < individual.length; i++) {

            if (individual[i].fitness > bestIndividual) {

                bestIndividual = individual[i].fitness;
                bestFitnessIndex = i;

            }

        }

        System.out.println("Best Fitness of an individual: " + bestIndividual);

        fittest = bestIndividual;

        return bestIndividual;
    }

    public static int getWorstIndividual(Individual[] individual) {
        int worstIndividual = individual[0].fitness;

        for (int i = 0; i < individual.length; i++) {
            if (worstIndividual > individual[i].fitness) {
                worstIndividual = individual[i].fitness;
                worstFitnessIndex = i;

            }
        }
        //System.out.println("Worst Individual Fitness: " + worstIndividual + " index: " + worstFitnessIndex);
        leastFittest = worstIndividual;
        return worstIndividual;
    }

    public static int calcAvgFitness(Individual[] individual) {
        int sum;
        int i;

        for (sum = 0, i = 0; i < popSize; i++) {
            sum += individual[i].fitness;

        }
        int avg = sum / popSize;

        System.out.println("Avg Fitness of Pop in current generation: " + avg);
        avgFitness = avg;
        return avg;
    }
    
    // creates a new file and then reads values from, gen, avg fitness and best fitness
    public void createFile(int generation, int avgFitness, int bestFitness){
        String headers = "Generation, Mean, Best";
        
        try {
            FileWriter csv_file = new FileWriter("Genetic Algorithm.csv");
            csv_file.append(headers + "\n");
           
        } catch (IOException ex) {
            System.out.println("File doesn't exist");
        }
        
    }

}
