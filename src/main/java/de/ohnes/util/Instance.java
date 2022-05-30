package de.ohnes.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;


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
            processingTimes[0] = MyMath.getRandomNumber(1, 100);
            for(int j = 1; j < this.m; j++) {
                processingTimes[j] = MyMath.getRandomNumber((j * processingTimes[j - 1]) / (j + 1), processingTimes[j - 1]); //linearity??
            }
            this.jobs[i] = new Job(i, processingTimes);
        }

    }

    /**
     * find out the cononical number of machines for a job @param i with max. execution time @param h.
     * using binary search
     * O(log m)
     * @return -1 if the job cant be executed in time h
     */
    public int canonicalNumberMachines(long i, double h) {
        int[] processingTimes = this.jobs[(int) i].getProcessingTimes();
        int r = processingTimes.length - 1;
        int l = 0;
        while (r >= l) {
            int mid = l + (r - l) / 2;
            if (processingTimes[mid] == h) return mid;
            if (processingTimes[mid] < h) r = mid - 1;
            if (processingTimes[mid] > h) l = mid + 1;
        }
        return processingTimes[this.m - 1] > h ? this.m + 1 : l + 1; //TODO: wrong if processing time is exactly h!!
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
    
}
