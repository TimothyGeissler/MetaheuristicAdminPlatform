package CalculateTimetable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PseudoRandom {

    //Will calculate timetable which will fill core hard duplication parameters
    public List<Integer> calculateBasicTimetable(int noOfStudents) {
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
        return shuffledList;
    }

}
