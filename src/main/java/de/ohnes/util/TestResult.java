package de.ohnes.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Setter;

@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@SuppressWarnings("unused")
public class TestResult {

    private int jobs;
    private int machines;
    private int[][] processingTimes;
    private double estimatedOptimum;
    private double achivedMakespan;
    private long milliseconds;
    private String approximation;
    private String fptas;
    private String shelvesAlgo;
    private Integer bigJobs;
    private Integer smallJobs;

    public void setProcessingTimes(Job[] jobs) {
        this.processingTimes = new int[jobs.length][jobs[0].getProcessingTimes().length];
        for(int i = 0; i < jobs.length; i++) {
            this.processingTimes[i] = jobs[i].getProcessingTimes();
        }
    }
}
