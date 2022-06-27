package de.ohnes.AlgorithmicComponents.Shelves;

import de.ohnes.AlgorithmicComponents.Algorithm;
import de.ohnes.AlgorithmicComponents.Knapsack.ConvolutionKnapsack;
import de.ohnes.AlgorithmicComponents.Knapsack.KnapsackSolver;
import de.ohnes.logger.printSchedule;
import de.ohnes.util.Instance;
import de.ohnes.util.Job;
import de.ohnes.util.MyMath;

public class FrenchApproach implements Algorithm {

    protected Instance I;

    public FrenchApproach() {
        
    }

    /**
     * finds a two shelf shedule for the instance I with deadline d, if a schedule of length d exists.
     * @param d the deadline (makespan guess)
     * @param epsilon the "the small error"
     * @return true if a schedule of length d exists, false if none exists.
     */
    public boolean solve(double d, double epsilon) {
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
            job.setAllotedMachines(job.canonicalNumberMachines(d/2));
        }

        // System.out.println(printSchedule.printTwoShelves(bigJobs, (int) d));

        //transform to knapsack problem
        int[] profit = new int[bigJobs.length];
        int[] weight = new int[bigJobs.length];
        // int C = I.getM() - cap;
        for(int i = 0; i < bigJobs.length; i++) {
            int dAllotment = bigJobs[i].canonicalNumberMachines(d); //Note: Can not be -1. Since the has to exost a schedule with makespan d.
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
        int p1 = 0;
        Job[] shelf1 = kS.solve(bigJobs, weight, profit, bigJobs.length, I.getM());
        for(Job selectedJob : shelf1) {
            selectedJob.setAllotedMachines(selectedJob.canonicalNumberMachines(d));
            p1 += selectedJob.canonicalNumberMachines(d); //keep track of p1

            //update WShelf1
            WShelf1 += selectedJob.getAllotedMachines() * selectedJob.getProcessingTime(selectedJob.getAllotedMachines());
        }
        
        if(WShelf1 > I.getM() * d - Ws) {   //there cant exists a schedule of with makespan (s. Thesis Felix S. 76)
            return false;
        }

        System.out.println();
        // System.out.println(printSchedule.printTwoShelves(bigJobs, (int) d));
        System.out.println(printSchedule.printTwoShelves(bigJobs, (int) d));

        applyTransformationRules(d, bigJobs, shelf1, p1);

        return true;
    };

    /**
     * converts a two shelf schedule to a feasible three schelves schedule. (s. 5.3.1.3 Thesis Felix)
     * @param I an Instance. A two Shelves schedule has to aleady been build.
     * @param d the deadline (also of the two shelves Schedule)
     * @return true if there exists a feasible schedule, false if not
     */
    protected void applyTransformationRules(double d, Job[] bigJobs, Job[] shelf1, int p1) {

        Job[] shelf2 = MyMath.findShelf2(I, d);

        int p0 = 0;     //processors required by S0.

        Job singleSmallJob = null;
        for(Job job : shelf1) {
            int allotedMachines = job.getAllotedMachines();
            int pTime = job.getProcessingTime(allotedMachines);
            
            if (pTime <= (3/4.0) * d && allotedMachines > 1) {
                p1 -= job.getAllotedMachines();
                job.setAllotedMachines(allotedMachines - 1);        //assign to shelf 0.
                p0 += job.getAllotedMachines();
            } else if(pTime <= (3/4.0) * d && allotedMachines == 1) {
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
            if(q > 0 && job.getProcessingTime(q) <= (3/2.0) * d) {
                int p = job.canonicalNumberMachines((3/2.0) * d);
                //TODO: if moved to shelf1: apply rules 1 and 2!!!
                job.setAllotedMachines(p);      //either S0 or S1. TODO S0 wenn y(i, p) > d. wenn y(i, d) <= d S1
                if(job.getProcessingTime(p) > d) {
                    p0 += p;
                } else {
                    p1 += p;
                }
            }
        }

    }

    @Override
    public void setInstance(Instance I) {
        this.I = I;
        
    }

}
