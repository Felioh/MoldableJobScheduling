package util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {
    
    private long id;
    private int[] processingTimes;
    private int startingTime;
    private int allotedMachines;

    public Job(int id, int[] processingTimes) {
        this.id = id;
        this.processingTimes = processingTimes;

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Job && ((Job) obj).getId() == this.getId();
    }

    /**
     * 
     * @param i the amount of Machines [1..]
     * @return the execution time
     */
    public int getProcessingTime(int i) {
        return this.processingTimes[i - 1];
    }
    
}
