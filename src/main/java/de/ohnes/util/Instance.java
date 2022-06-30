package de.ohnes.util;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;


@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Instance {

    @JsonProperty("number_jobs")
    private int n;
    @JsonProperty("machines")
    private int m;
    // @JsonDeserialize(as = Job[].class)
    @JsonProperty("jobs")
    private Job[] jobs;

    @Setter
    private Machine[] machines;


    public Instance(int n, int m, Job[] jobs) {
        this.n = n;
        this.m = m;
        this.jobs = jobs;
    }

    
    /** 
     * @param minJobs
     * @param maxJobs
     * @param minMachines
     * @param maxMachines
     */
    public void generateRandomInstance(int minJobs, int maxJobs, int minMachines, int maxMachines) {

        this.m = MyMath.getRandomNumber(minMachines, maxMachines);
        this.n = MyMath.getRandomNumber(minJobs, maxJobs);
        this.jobs = new Job[this.n];
        
        for(int i = 0; i < this.n; i++) {
            int[] processingTimes = new int[this.m];
            processingTimes[0] = MyMath.getRandomNumber(20, 100);
            for(int j = 1; j < this.m; j++) {
                processingTimes[j] = MyMath.getRandomNumber((j * processingTimes[j - 1]) / (j + 1), processingTimes[j - 1]); //linearity??
            }
            this.jobs[i] = new Job(i, processingTimes);
        }

    }

    @Override
    public String toString() {
        String result = "";
        result += "Machines: " + this.m + "\n";
        result += "Jobs:\n";
        for(Job j : this.jobs) {
            result += j.getId();
            result += "\t";
            for(double p : j.getProcessingTimes()) {
                result += p + ", ";
            }
            result += "\n";
        }
        return result;
    }
    
    public void addMachines(List<Machine> machines) {
        machines.addAll(Arrays.asList(this.machines));
        this.machines = machines.toArray(Machine[] :: new);
    }

    public double getMakespan() {
        double maxMakespan = 0;
        for(Job job : this.jobs) {
            double finishTime = job.getStartingTime() + job.getProcessingTime(job.getAllotedMachines());
            if(finishTime > maxMakespan) {
                maxMakespan = finishTime;
            }
        }
        return maxMakespan;
    }

    /**
     * only for debugging
     * @return
     */
    public double getMakespanBigJobs(double d) {
        double maxMakespan = 0;
        for(Job job : MyMath.findBigJobs(this, d)) {
            double finishTime = job.getStartingTime() + job.getProcessingTime(job.getAllotedMachines());
            if(finishTime > maxMakespan) {
                maxMakespan = finishTime;
            }
        }
        return maxMakespan;
    }

    public void resetInstance() {
        Arrays.asList(jobs).stream().forEach(j -> j.reset());
        this.machines = null;
    }
    
}
