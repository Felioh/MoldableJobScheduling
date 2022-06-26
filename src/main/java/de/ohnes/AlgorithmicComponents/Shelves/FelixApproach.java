package de.ohnes.AlgorithmicComponents.Shelves;

import java.util.ArrayList;
import java.util.List;

import de.ohnes.AlgorithmicComponents.Algorithm;
import de.ohnes.AlgorithmicComponents.GeometricalRounding;
import de.ohnes.AlgorithmicComponents.Knapsack.ConvolutionKnapsack;
import de.ohnes.AlgorithmicComponents.Knapsack.KnapsackSolver;
import de.ohnes.logger.printSchedule;
import de.ohnes.util.*;

public class FelixApproach implements Algorithm {

    public boolean solve(Instance I, double d, double epsilon) {

        //parameters
        double delta = (1 / 5.0) * epsilon;
        double roh = (1 / 4.0) * (Math.sqrt(1 + delta) - 1);
        double b = 1 / (2 * roh - Math.pow(roh, 2));
        double d_quote = Math.pow((1 + roh), 2) * d;


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

        //transform to knapsack problem
        int[] profit = new int[bigJobs.length];
        int[] weight = new int[bigJobs.length];
        // int C = I.getM() - cap;
        for(int i = 0; i < bigJobs.length; i++) {
            int dAllotment = I.canonicalNumberMachines(bigJobs[i].getId(), d); //Note: Can not be -1. Since the has to exost a schedule with makespan d.
            int dHalfAllotment = bigJobs[i].getAllotedMachines();

            if(dAllotment > b) { //rounding
                dAllotment = (int) GeometricalRounding.gFloor(dAllotment, (int) b, I.getM(), 1 + roh); //TODO: chcek Rounding by integer casting
            }
            if(dHalfAllotment > b) { //rounding
                dHalfAllotment = (int) GeometricalRounding.gFloor(dHalfAllotment, (int) b, I.getM(), 1 + roh); //TODO: chcek Rounding by integer casting
            }


            if(dAllotment == -1) {  //there cant exists a schedule of legnth d if any job cant be scheduled in d time.
                return false;
            }

            //weight of an item-task will be its canonical number of processors needed to respect the threshold d
            weight[i] = dAllotment;

            
            if (dHalfAllotment != -1) {
                //profit of an item-task will correspond to the work saving obtained by executing the task just to respect the threshold d instead of d/2
                //w_{i, y{i, d/2} - w_{i, y{i, d}}
                profit[i] = (dHalfAllotment * bigJobs[i].getProcessingTime(dHalfAllotment)) - (dAllotment * bigJobs[i].getProcessingTime(dAllotment)); //TODO: is not the original profit (p. 89 Thesis Felix)
                if(dHalfAllotment < b) { //this means the job has been compressed.
                    if(profit[i] < (delta / 2) * d) {
                        profit[i] = 0;
                    }else {
                        profit[i] = (int) GeometricalRounding.gCeil(profit[i],(int) ((delta / 2) * d),(int) ((b / 2) * d), 1 + (delta / b)); //TODO: chcek Rounding by integer casting
                    }
                } else { //not compressed job
                    double dHalfTime = GeometricalRounding.gFloor(bigJobs[i].getProcessingTime(I.canonicalNumberMachines(bigJobs[i].getId(), d / 2)), (int) (d / 4), (int) (d / 2), 1 + (delta / b));
                    double dTime = GeometricalRounding.gFloor(bigJobs[i].getProcessingTime(I.canonicalNumberMachines(bigJobs[i].getId(), d)), (int) (d / 2), (int) d, 1 + (delta / b));

                    profit[i] = (int) ((dHalfTime * dHalfAllotment) - (dTime * dAllotment));
                }
                
            } else { //job has to be scheduled on s1.
                //TODO remove from knapsack and schedule on shelf 1.
                profit[i] = (int) Math.round(I.getM() * d);     //really big.
            }

        }
        

        KnapsackSolver kS = new ConvolutionKnapsack();
        Job[] shelf1 = kS.solve(bigJobs, weight, profit, bigJobs.length, I.getM());
        int p1 = 0;     //processors required by S1.
        for(Job selectedJob : shelf1) {
            int dAllotment = I.canonicalNumberMachines(selectedJob.getId(), d);
            if(dAllotment > b) { //rounding
                dAllotment = (int) GeometricalRounding.gFloor(dAllotment, (int) b, I.getM(), 1 + roh); //TODO: chcek Rounding by integer casting
            }
            selectedJob.setAllotedMachines(dAllotment);
            p1 += dAllotment; //keep track of the number of machines used by s1

            //update WShelf1
            WShelf1 += selectedJob.getAllotedMachines() * selectedJob.getProcessingTime(selectedJob.getAllotedMachines());
        }
        
        if(WShelf1 > I.getM() * d - Ws) {   //there cant exists a schedule of with makespan d (s. Thesis Felix S. 76)
            return false;
        }
        
// ############################################## DEBUG ##################################################################################################################
        System.out.println();
        // System.out.println(printSchedule.printTwoShelves(bigJobs, (int) d));
        System.out.println(printSchedule.printTwoShelves(bigJobs, (int) d));
// ############################################## DEBUG ##################################################################################################################

        //three shelves
        Job[] shelf2 =  MyMath.findShelf2(I, d);
        int p0 = 0;     //processors required by S0.

        //Note: below transformations dont increase the total work, so WShelf1 < I.getM() * d - Ws still holds.

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

// ############################################## DEBUG ##################################################################################################################
        System.out.println();
        // System.out.println(printSchedule.printTwoShelves(bigJobs, (int) d));
        System.out.println(printSchedule.printTwoShelves(bigJobs, (int) d));
// ############################################## DEBUG ##################################################################################################################
        return true;
    }

}
