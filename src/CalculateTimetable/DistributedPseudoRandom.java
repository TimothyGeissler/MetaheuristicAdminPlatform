package CalculateTimetable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DistributedPseudoRandom extends Thread{
    private ArrayList<ArrayList<Integer>> population = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> oldGeneration = new ArrayList<>();
    private int noOfStudents;

    //Use same format as basic, but use Genetic Algorithm to find preferable distribution
    public ArrayList<Integer> calculateGeneticDistTimetable(int noOfStudents) {
        this.noOfStudents = noOfStudents;
        oldGeneration = population;
        int epochs = 0;
        spawnPopulation(10);
        System.out.println("Original avg fitness: " + getAvgFitness(population));
        while (isImproving() && getFitness(getBestSpecimen(population)) < 3.0) {//(getFitness(getBestSpecimen(population)) < 3.0) {
            //Save old generation in case of genetic regression
            oldGeneration = population;
            cross(getBest());
            mutate(0.0, population.size()/5, 5.0);
            cull();
            System.out.println("EPOCH: " + epochs + " -- AVG fitness: " + getAvgFitness(population) + ", population size: " + population.size());
            epochs++;
            //Cut if algorithm improvement stalls
            if (epochs == 1000) {
                break;
            }
            /*
            if (getAvgFitness() > 2.8) {
                break;
            }*/
        }
        System.out.println("FINAL EPOCH: " + epochs + " -- AVG fitness: " + getAvgFitness(oldGeneration));
        ArrayList<Integer> bestSpecimen = getBestSpecimen(oldGeneration);
        System.out.println("Best specimen found: " + bestSpecimen.toString() + " @ " + getFitness(bestSpecimen));
        return bestSpecimen;
    }

    private ArrayList<Integer> getBestSpecimen(ArrayList<ArrayList<Integer>> population) {
        ArrayList<Integer> bestSpecimen = population.get(0);
        for (ArrayList<Integer> chromosome: population) {
            if (getFitness(chromosome) > getFitness(bestSpecimen)) {
                bestSpecimen = chromosome;
            }
        }
        return bestSpecimen;
    }

    private boolean isImproving() {
        //Get new avg fitness
        boolean improving = false;
        if (getAvgFitness(oldGeneration) <= getAvgFitness(population)) {
            improving = true;
        }
        System.out.println("improving: " + improving);
        return improving;
    }

    private void mutate(double lowerLimit, double upperLimit, double bias) {
        //Raise rand num to bias power (rand^bias)
        //bias < 1 will tend to upper limit, bias > 1 will tend to lower limit
        double r = Math.random();
        //Create exponential function
        r = Math.pow(r, bias);
        //Shift to fit range
        double result = lowerLimit + (upperLimit - lowerLimit) * r;
        //System.out.println("\tMutation probability: " + (r * 100) + "%");
        for (int i = 0; i < population.size(); i++) {
            ArrayList<Integer> specimen = population.get(i);
            if (Math.random() < result) {
                //Remove specimen from population to mutate
                population.remove(specimen);
                //System.out.println("To mutate: " + specimen.toString());
                //Swap

                int index1 = (int) (Math.random() * specimen.size());
                int index2 = (int) (Math.random() * specimen.size());
                Collections.swap(specimen, index1, index2);
                //System.out.println("Mutated gene, swapped @ indices: " + index1 + ", " + index2);
                //Add back to population
                System.out.println("Adding: " + specimen.toString());
                population.add(specimen);
            }
        }
        /*Proof of concept:
        double total = 0.0;
        for (int i = 0; i < 100; i++) {
            total = total + mutate(0.0, 1.0, 5);
        }
        System.out.println("AVG: " + total/100.0);
        double randTotal = 0.0;
        for (int i = 0; i < 100; i++) {
            randTotal = randTotal + Math.random();
        }
        System.out.println("Rand AVG: " + randTotal/100.0);

        Get AVG population mutation
        double totalRate = 0.0;
        for (int i = 0; i < 1000000; i++) {
            totalRate = totalRate + mutate(0.0, population.size()/5,5.0);
        }
        System.out.println("AVG population mutation % = " + totalRate * 100/1000000);*/
    }

    private void cull() {
        //Get those below avg
        double avg = getAvgFitness(population);
        ArrayList<ArrayList<Integer>> toRemove = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            if (getFitness(population.get(i)) < avg) {
                toRemove.add(population.get(i));
            }
        }
        //remove
        for (int j = 0; j < toRemove.size(); j++) {
            population.remove(toRemove);
        }

    }

    private void cross(ArrayList<ArrayList<Integer>> group) {
        //Sort group, remove worst specimen if odd to make pairs
        if (group.size() % 2 != 0) {
            //System.out.println("Size: " + group.size() + " is odd...\nRemoving worst candidate.");
            //Find worst
            ArrayList<Integer> worst = group.get(0);
            for (ArrayList<Integer> specimen: group) {
                if (getFitness(specimen) < getFitness(worst)) {
                    worst = specimen;
                }
            }
            //System.out.println("Worst specimen found: " + worst.toString() + " @ " + getFitness(worst) + " fitness score");
            //Remove
            group.remove(worst);
            //System.out.println("Removed...");
        }
        //System.out.println("Group size: " + group.size() + ", can pair");
        //Cross and add back to population
        for (int i = 0; i < group.size() - 1; i++) {
            //Pair
            ArrayList<Integer> parent1 = group.get(i),parent2 = group.get(i + 1);
            //Random X-point
            int xPoint = (int) (Math.random() * (parent1.size() - 1)) + 1;
            //System.out.println("X-Point: " + xPoint);
            //Swap genes up to x point (use list to get subList)
            List<Integer> p1Exchange = parent1.subList(0, xPoint), p2Exchange = parent2.subList(0, xPoint),
                    p1Remainder = parent1.subList(xPoint, parent1.size()), p2Remainder = parent2.subList(xPoint, parent2.size());

            List<Integer> combinedList = Stream.of(p1Exchange, p2Remainder)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            List<Integer> combinedList2 = Stream.of(p2Exchange, p1Remainder)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            //Convert to ArrayList
            ArrayList<Integer> child1 = new ArrayList<>(), child2 = new ArrayList<>();
            child1.addAll(combinedList);
            child2.addAll(combinedList2);
            //Remove duplicate genes
            ArrayList<Integer> newChild1 = new ArrayList<>(), newChild2 = new ArrayList<>();
            for (int a: child1) {
                //Except 0
                if (!newChild1.contains(a) || a == 0) {
                    newChild1.add(a);
                } else {
                    //Fill in gaps
                    newChild1.add(0);
                }
            }
            for (int b: child2) {
                //Except 0
                if (!newChild2.contains(b) || b == 0) {
                    newChild2.add(b);
                } else {
                    newChild2.add(0);
                }
            }
            //System.out.println("New child: " + newChild1.toString() + hasDuplicates(newChild1) + "\n" + newChild2.toString() + hasDuplicates(newChild2));
            //Add children to population
            population.add(newChild1);
            population.add(newChild2);
            //System.out.println("Children added");
        }
    }

    private ArrayList<ArrayList<Integer>> getBest() {
        ArrayList<ArrayList<Integer>> best = new ArrayList<>();
        double avgFitness = getAvgFitness(population);
        for (ArrayList<Integer> specimen: population) {
            double specimenFitness = getFitness(specimen);
            if (specimenFitness > avgFitness) {
                //System.out.println("Specimen: " + specimen.toString() + " fitness = " + specimenFitness + " (avg = " + avgFitness + ")");
                //Add to best
                best.add(specimen);
            }
        }
        return best;
    }

    private double getAvgFitness(ArrayList<ArrayList<Integer>> population) {
        double sumFitness = 0.0;
        for (ArrayList<Integer> specimen: population) {
            sumFitness = sumFitness + getFitness(specimen);
        }
        double avgFitness = sumFitness / population.size();
        //System.out.println("AVG Fitness: " + avgFitness);
        return avgFitness;
    }

    private double getFitness(ArrayList<Integer> specimen) {
        //Want a low SD (well spread out) -> use negative offset score
        //3.0 would be the poorest distribution
        double fitness = 3.0;
        //Reward for higher standard deviation of lesson frequency (S.D of zeros per day)
        //Divide into days
        int [] frequency = new int[5];
        for (int i = 0; i < 5; i++) {
            //Get frequency of 0 per day
            List<Integer> day = specimen.subList(i * 6, (i * 6) + 6);
            //System.out.println("Day: " + i + " -> " + day.toString());
            for (int j = 0; j < day.size(); j++) {
                if (day.get(j) == 0) {
                    frequency[i] = frequency[i]  + 1;
                }
            }
            //System.out.println();
        }
        //Use SD formula
        int sumFrequency = Arrays.stream(frequency).sum();
        double mean = sumFrequency / 5.0;
        double numSum = 0.0;
        for (int b = 0; b < 5; b++) {
            numSum = numSum + Math.pow(frequency[b] - mean, 2);
        }
        double sd = Math.sqrt(numSum / 5.0);
        //System.out.println("S.D: " + sd);
        fitness = fitness - sd;

        //If a student has 2 lessons per week, fitness = 0.0
        //Ignore 0
        //int[] studentNumbers = IntStream.range(1, noOfStudents + 1).toArray();
        /*boolean isUnique = Stream.of(IntStream.range(1, noOfStudents + 1).toArray())
                .collect(Collectors.groupingBy(
                        Function.identity(), Collectors.counting()))
                .entrySet().stream().anyMatch(e -> e.getValue() > 1);
        if (hasDuplicates(specimen)) {
            System.out.println(specimen.toString() + " has duplicates!");
            //Penalize
            fitness = 0.0;
        } else {
            System.out.println(specimen.toString() + " has no duplicates...");
        }*/
        return fitness;
    }

    private boolean hasDuplicates(ArrayList<Integer> input) {
        boolean duplicates = false;
        for (int i = 0; i < input.size(); i++) {
            for (int j = i + 1; j < input.size() - i - 1; j++) {
                if (input.get(i) == input.get(j) && input.get(i) != 0) {
                    duplicates = true;
                }
            }
        }
        return duplicates;
    }

    private void spawnPopulation(int size) {
        for (int a = 0; a < size; a++) {
            //Generate population
            PseudoRandom ps = new PseudoRandom();
            ArrayList<Integer> individual = new ArrayList<>();
            individual.addAll(ps.calculateBasicTimetable(noOfStudents));
            //System.out.println("Individual to add: " + individual.toString());
            population.add(individual);
        }
    }

}
