

import AlgorithmicComponents.KnapsackSolver;
import logger.printSchedule;
import util.Instance;
import util.Job;
import util.MyMath;

public class Algo {

    public static void findTwoShelves(Instance I, double d) {
        //"forget about small jobs"
        Job[] bigJobs = MyMath.findBigJobs(I, d);

        //all the tasks are initially allotted to their canonical number of processors to respect the d/2 threshold
        for(Job job : bigJobs) {
            job.setAllotedMachines(I.canonicalNumberMachines(job.getId(), d/2));
        }

        System.out.println(printSchedule.printTwoShelves(bigJobs, (int) d));

        //transform to knapsack problem
        int[] profit = new int[bigJobs.length];
        int[] weight = new int[bigJobs.length];
        // int C = I.getM() - cap;
        for(int i = 0; i < bigJobs.length; i++) {

            int dAllotment = I.canonicalNumberMachines(bigJobs[i].getId(), d); //Note: Can not be -1. Since the has to exost a schedule with makespan d.
            int dHalfAllotment = bigJobs[i].getAllotedMachines();

            weight[i] = dAllotment;
            
            if (dHalfAllotment != -1) {
                //profit of an item-task will correspond to the work saving obtained by executing the task just to respect the threshold d instead of d/2
                //w_{i, y{i, d/2} - w_{i, y{i, d}}
                profit[i] = (dHalfAllotment * bigJobs[i].getProcessingTime(dHalfAllotment)) - (dAllotment * bigJobs[i].getProcessingTime(dAllotment));
                //weight of an item-task will be its canonical number of processors needed to respect the threshold d
            } else {
                profit[i] = (int) Math.round(I.getM() * d);     //really big.
            }

        }

        // bigJobs = MyMath.dynamicKnapsack(bigJobs, weight, profit, bigJobs.length, I.getM(), I, d);
        Job[] selectedJobs = KnapsackSolver.knapsackConvolution(bigJobs, weight, profit, bigJobs.length, I.getM());
        for(Job selectedJob : selectedJobs) {
            selectedJob.setAllotedMachines(I.canonicalNumberMachines(selectedJob.getId(), d));
        }


        System.out.println();
        // System.out.println(printSchedule.printTwoShelves(bigJobs, (int) d));
        System.out.println(printSchedule.printTwoShelves(bigJobs, (int) d));

    }

}
