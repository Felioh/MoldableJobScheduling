package de.ohnes.AlgorithmicComponents.Shelves;

import de.ohnes.AlgorithmicComponents.Algorithm;
import de.ohnes.AlgorithmicComponents.Knapsack.ConvolutionKnapsack;
import de.ohnes.AlgorithmicComponents.Knapsack.KnapsackSolver;
import de.ohnes.logger.printSchedule;
import de.ohnes.util.Instance;
import de.ohnes.util.Job;
import de.ohnes.util.MyMath;

public class FrenchApproach implements Algorithm {

    public boolean solve(Instance I, double d, double epsilon) {
        return false;
    };

    /**
     * finds a two shelf shedule for the instance I with deadline d, if a schedule of length d exists.
     * @param I The instance I. Objects will be altered, so there is no instance returned.
     * @param d the deadline
     * @return true if a schedule of length d exists, false if none exists.
     */
    private static boolean findTwoShelves(Instance I, double d) {
        //"forget about small jobs"
        Job[] bigJobs = MyMath.findBigJobs(I, d);

        Job[] smallJobs = MyMath.findSmallJobs(I, d);
        //minimal work of small jobs
        double Ws = 0;
        double WShelf1 = 0;
        for(Job job : smallJobs) {
            Ws += job.getProcessingTime(1);
        }

        //all the tasks are initially allotted to their canonical number of processors to respect the d/2 threshold
        for(Job job : bigJobs) {
            job.setAllotedMachines(I.canonicalNumberMachines(job.getId(), d/2));
        }

        // System.out.println(printSchedule.printTwoShelves(bigJobs, (int) d));

        //transform to knapsack problem
        int[] profit = new int[bigJobs.length];
        int[] weight = new int[bigJobs.length];
        // int C = I.getM() - cap;
        for(int i = 0; i < bigJobs.length; i++) {
            int dAllotment = I.canonicalNumberMachines(bigJobs[i].getId(), d); //Note: Can not be -1. Since the has to exost a schedule with makespan d.
            int dHalfAllotment = bigJobs[i].getAllotedMachines();


            if(dAllotment == -1) {  //there cant exists a schedule of legnth d if any job cant be scheduled in d time.
                return false;
            }


            weight[i] = dAllotment;
            
            if (dHalfAllotment != -1) {
                //profit of an item-task will correspond to the work saving obtained by executing the task just to respect the threshold d instead of d/2
                //w_{i, y{i, d/2} - w_{i, y{i, d}}
                profit[i] = (dHalfAllotment * bigJobs[i].getProcessingTime(dHalfAllotment)) - (dAllotment * bigJobs[i].getProcessingTime(dAllotment));
                //weight of an item-task will be its canonical number of processors needed to respect the threshold d
            } else {
                //TODO remove from knapsack and schedule on shelf 1.
                profit[i] = (int) Math.round(I.getM() * d);     //really big.
            }

        }
        

        // bigJobs = MyMath.dynamicKnapsack(bigJobs, weight, profit, bigJobs.length, I.getM(), I, d);
        KnapsackSolver kS = new ConvolutionKnapsack();
        Job[] shelf1 = kS.solve(bigJobs, weight, profit, bigJobs.length, I.getM());
        for(Job selectedJob : shelf1) {
            selectedJob.setAllotedMachines(I.canonicalNumberMachines(selectedJob.getId(), d));

            //update WShelf1
            WShelf1 += selectedJob.getAllotedMachines() * selectedJob.getProcessingTime(selectedJob.getAllotedMachines());
        }
        
        if(WShelf1 > I.getM() * d - Ws) {   //there cant exists a schedule of with makespan (s. Thesis Felix S. 76)
            return false;
        }

        System.out.println();
        // System.out.println(printSchedule.printTwoShelves(bigJobs, (int) d));
        System.out.println(printSchedule.printTwoShelves(bigJobs, (int) d));

        return true;

    }

    /**
     * converts a two shelf schedule to a feasible three schelves schedule.
     * @param I an Instance. A two Shelves schedule has to aleady been build.
     * @param d the deadline (also of the two shelves Schedule)
     * @return true if there exists a feasible schedule, false if not
     */
    private static boolean findThreeShelvesSchedule(Instance I, double d) {

        Job[] jobs = I.getJobs();
        Job[] bigJobs = MyMath.findBigJobs(I, d);
        Job[] shelf1 = MyMath.findShelf1(I, d);
        Job[] shelf2 = MyMath.findShelf2(I, d);

        int p0 = 0;     //processors required by S0.
        int p1 = I.getM();     //processors required by S1.  TODO: not correct!!!! (s. proof Lemma 5.8)

        Job singleSmallJob = null;
        for(Job job : shelf1) {
            int allotedMachines = job.getAllotedMachines();
            int pTime = job.getProcessingTime(allotedMachines);
            
            if (pTime <= 3/4 * d && allotedMachines > 1) {
                p1 -= job.getAllotedMachines();
                job.setAllotedMachines(allotedMachines - 1);        //assign to shelf 0.
                p0 += job.getAllotedMachines();
            } else if(pTime <= 3/4 * d && allotedMachines == 1) {
                if(singleSmallJob == null) {
                    singleSmallJob = job;
                } else {
                    p1 -= job.getAllotedMachines();     // == 1
                    p1 -= singleSmallJob.getAllotedMachines();
                    p0 += job.getAllotedMachines();
                    singleSmallJob.setStartingTime(pTime);         //assign both to shelf 0.
                    singleSmallJob = null;
                }
            }
        }

        for(Job job : shelf2) {
            int q = I.getM() - (p1 + p0);
            if(q > 0 && job.getProcessingTime(q) <= 3/2 * d) {
                int p = I.canonicalNumberMachines(job.getId(), 3/2 * d);
                //TODO: if moved to shelf1: apply rules 1 and 2!!!
                job.setAllotedMachines(p);      //either S0 or S1. TODO S0 wenn y(i, p) > d. wenn y(i, d) <= d S1
                if(job.getProcessingTime(p) > d) {
                    p0 += p;
                } else {
                    p1 += p;
                }
            }
        }

        



        return true;

    }

}
