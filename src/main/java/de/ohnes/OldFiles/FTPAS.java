package de.ohnes.OldFiles;

import de.ohnes.util.*;
/**
 * suitable for scheduling moldable jobs on a large number of machines.
 */
public class FTPAS {

    /**
     * Schedule all Jobs in parallel
     * Time: O(n log(m)) -> cannonicalNumerMachines takes O(log(m))
     * @param I
     * @param d
     * @param epsilon
     * @return
     */
    public static boolean simpleApproach(Instance I, double d, double epsilon) {
        int allotedMachines = 0;
        for(Job job : I.getJobs()) {
            int neededMachines = I.canonicalNumberMachines(job.getId(), (1 + epsilon) * d);
            if (neededMachines == -1) {
                return false;       //there exists no schedule if a task cant be scheduled in (1 + epsilon) * d time
            }
            job.setAllotedMachines(neededMachines);
            allotedMachines += neededMachines;
        }

        if(allotedMachines > I.getM()) {
            return false;   //reject d
        }

        return true;

    }

    /**
     * Schedule Jobs to respect d threshold and then compress big jobs.
     * @param I
     * @param d
     * @param epsilon
     * @return
     */
    public static boolean compressionApproach(Instance I, double d, double epsilon) {
        int allotedMachines = 0;
        for(Job job : I.getJobs()) {
            int neededMachines = I.canonicalNumberMachines(job.getId(), d);
            if (neededMachines == -1) {
                return false;       //there exists no schedule if a task cant be scheduled in (1 + epsilon) * d time
            }
            if(neededMachines >= (4 / epsilon)) {   //compress big jobs
                //free (epsilon / 4) * neededMachines (compression)
                neededMachines = (int) Math.ceil((epsilon / 4) * neededMachines); //because of monotony the jobs should not take longer than (1 + epsilon)*d
            }
            job.setAllotedMachines(neededMachines);
            allotedMachines += neededMachines;
        }

        if(allotedMachines > I.getM()) {
            return false;   //reject d
        }

        return true;

    }
    
}
