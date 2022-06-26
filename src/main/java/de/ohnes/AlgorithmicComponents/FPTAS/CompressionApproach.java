package de.ohnes.AlgorithmicComponents.FPTAS;

import de.ohnes.AlgorithmicComponents.Algorithm;
import de.ohnes.util.Instance;
import de.ohnes.util.Job;

public class CompressionApproach implements Algorithm {

    /**
     * Schedule Jobs to respect d threshold and then compress big jobs.
     * @param I
     * @param d
     * @param epsilon
     * @return
     */
    @Override
    public boolean solve(Instance I, double d, double epsilon) {
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
