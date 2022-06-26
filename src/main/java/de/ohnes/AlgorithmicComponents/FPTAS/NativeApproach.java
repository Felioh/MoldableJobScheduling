package de.ohnes.AlgorithmicComponents.FPTAS;

import de.ohnes.AlgorithmicComponents.Algorithm;
import de.ohnes.util.Instance;
import de.ohnes.util.Job;

public class NativeApproach implements Algorithm {

    /**
     * Schedule all Jobs in parallel
     * Time: O(n log(m)) -> cannonicalNumerMachines takes O(log(m))
     * @param I
     * @param d
     * @param epsilon
     * @return
     */
    @Override
    public boolean solve(Instance I, double d, double epsilon) {
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
    
}
