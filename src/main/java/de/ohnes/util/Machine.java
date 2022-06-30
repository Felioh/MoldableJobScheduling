package de.ohnes.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Machine {
    
    private int id;
    private double usedTime;    //maybe not needed. ?
    private List<Job> jobs;

    public Machine(int id) {
        this.id = id;
        this.usedTime = 0;
        this.jobs = new ArrayList<>();
    }

    public void addJob(Job job) {
        this.jobs.add(job);
        this.jobs = this.jobs.stream().sorted(Comparator.comparing(Job::getStartingTime)).collect(Collectors.toList()); //TODO anders lösen
        this.usedTime += job.getProcessingTime(job.getAllotedMachines());
    }

    public void removeJob(Job job) {
        this.jobs.remove(job);
        this.usedTime -= job.getProcessingTime(job.getAllotedMachines());
    }

    public double getFirstFreeTime() {
        double t = 0;
        for(Job job : jobs) {
            if(job.getStartingTime() == t) {    //TODO deal with possibility that more than two jobs are alloted.
                t = job.getProcessingTime(job.getAllotedMachines());
            }
        }
        return t;
    }

}
