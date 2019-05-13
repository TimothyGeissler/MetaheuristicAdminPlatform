package CalculateTimetable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PseudoRandom2 {

    private ArrayList<ArrayList<Integer>> population = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> blacklist = new ArrayList<>();
    private int numStudents;

    public ArrayList<ArrayList<Integer>> calculateSixWeekTimetable(int noOfStudents) {
        this.numStudents = noOfStudents;
        //Init blacklist
        for (int a = 0; a < numStudents; a++) {
            blacklist.add(new ArrayList<>());
        }

        ArrayList<ArrayList<Integer>> timetable = new ArrayList<>();
        //Iterate through 6-week cycle
        for (int week = 1; week <= 6; week++) {
            //Get timetable for each week
            ArrayList<Integer> weeklyTimetable = calculateTimetable(noOfStudents);
            addToBlacklist(weeklyTimetable);
            timetable.add(weeklyTimetable);
            population.clear();
        }

        return timetable;
    }

    private void addToBlacklist(ArrayList<Integer> input) {
        if (blacklist.get(0).size() > 0) {
            //Update
            //System.out.println("Update");
            for (int student = 1; student <= numStudents; student++) {
                for (int i = 0; i < input.size(); i++) {
                    if (input.get(i) == student) {
                        blacklist.get(student - 1).remove(i);
                        blacklist.get(student - 1).add(i, student);
                    }
                }
            }
        } else {
            //System.out.println("Generate");
            //Generate
            for (int student = 1; student <= numStudents; student++) {
                for (int i = 0; i < input.size(); i++) {
                    if (input.get(i) == student) {
                        blacklist.get(student - 1).add(student);
                    } else {
                        blacklist.get(student - 1).add(0);
                    }
                }
            }
        }
        /*for (ArrayList<Integer> list: blacklist) {
            System.out.println("Blacklist: " + list.toString());
        }*/
    }

    private boolean fitsBlacklist(ArrayList<Integer> chromosome) {
        boolean fits = true;
        if (blacklist.get(0).size() > 0) {
            //Check
            for (int student = 1; student <= numStudents; student++) {
                for (int i = 0; i < chromosome.size(); i++) {
                    if (chromosome.get(i).equals(blacklist.get(student - 1).get(i)) && chromosome.get(i) == student) {
                        fits = false;
                    }
                }
            }
            //System.out.println(chromosome.toString() + " - > " + blacklist + " " + fits);
        }
        return fits;
    }

    private ArrayList<Integer> calculateTimetable(int noOfStudents) {
        for (int x = 0; x < 100; x++) {
            ArrayList<Integer> specimen = new ArrayList<>(generateChromosome(noOfStudents));
            if (fitsBlacklist(specimen)) {
                population.add(specimen);
                /*
                if (x > 25 && x < 50) {
                    System.out.print("25%");
                } else if (x > 50 && x < 75) {
                    System.out.print("50%");
                } else if (x > 75 && x < 90) {
                    System.out.print("75%");
                } else if (x > 90) {
                    System.out.print("90%");
                }*/
                //System.out.println("Adding: " + specimen.toString() + " - " + fitsBlacklist(specimen));
            } else {
                //System.out.println("Going back a step");
                x = x - 1;
            }
        }
        ArrayList<Integer> bestSpecimen = getBestSpecimen(population);
        System.out.println("Perfect fitness found: " + bestSpecimen.toString() + " @ " + getFitness(bestSpecimen) + "\nBlacklist: ");
        for (ArrayList<Integer> list: blacklist) {
            System.out.println(list.toString());
        }
        //Use blacklist to compare fitness, 6 weeks UI update
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
        fitness = fitness - sd;

        return fitness;
    }

    private List<Integer> generateChromosome(int noOfStudents) {
        int scheduleSize = 30;
        List<Integer> shuffledList = IntStream.range(1, noOfStudents + 1).boxed().collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(shuffledList);
        //Add in 0 (free slots) to fill timetable
        while (shuffledList.size() < scheduleSize) {
            int randomIndex = (int) (Math.random() * shuffledList.size());
            shuffledList.add(randomIndex, 0);
        }/*
        for (int a = 0; a < shuffledList.size() - 1; a++) {
            result[a] = shuffledList.get(a);
        }
        System.out.println("Skeleton List: " + shuffledList.toString() + shuffledList.size());
        System.out.println("Skeleton Array 1: " + Arrays.toString(result) + result.length);*/
        Collections.shuffle(shuffledList);
        return shuffledList;
    }
}
