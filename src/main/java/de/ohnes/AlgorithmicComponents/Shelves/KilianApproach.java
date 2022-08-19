package de.ohnes.AlgorithmicComponents.Shelves;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.ohnes.AlgorithmicComponents.GeometricalRounding;
import de.ohnes.AlgorithmicComponents.Knapsack.ConvolutionKnapsack;
import de.ohnes.AlgorithmicComponents.Knapsack.KnapsackSolver;
import de.ohnes.logger.printSchedule;
import de.ohnes.util.Job;
import de.ohnes.util.Machine;
import de.ohnes.util.MyMath;

public class KilianApproach extends FelixApproach {

    public KilianApproach() {
        super();
    }

    @Override
    public boolean solve(double d, double epsilon) {

        //parameters
        double roh = (1 / 4.0) * epsilon;
        double b = 1 / roh;
        double d_quote = (1 + 4 * epsilon) * d;


        //"forget about small jobs"
        List<Job> shelf2 = new ArrayList<>(Arrays.asList(MyMath.findBigJobs(I, d)));
        List<Job> smallJobs = new ArrayList<>(Arrays.asList(MyMath.findSmallJobs(I, d)));

        //minimal work of small jobs
        double Ws = 0;
        double WShelf1 = 0;
        double WShelf2 = 0;
        for(Job job : smallJobs) {
            Ws += job.getProcessingTime(1);
        }

        //all the tasks are initially allotted to their canonical number of processors to respect the d/2 threshold
        for(Job job : shelf2) {
            job.setAllotedMachines(job.canonicalNumberMachines(d/2));
            if(job.getAllotedMachines() != -1) {
                WShelf2 += job.getAllotedMachines() * job.getProcessingTime(job.getAllotedMachines()); //update the work of shelf2
            }
        }

        //transform to knapsack problem
        int[] profit = new int[shelf2.size()];
        int[] weight = new int[shelf2.size()];

        // int C = I.getM() - cap;
        for(int i = 0; i < shelf2.size(); i++) {
            Job job = shelf2.get(i);
            int dAllotment = gammaPrime(job, d, roh, b);
            int dHalfAllotment = gammaPrime(job, d / 2, roh, b); //TODO check -1 possibility. (should not happen.)


            if(dAllotment == -1) {  //there cant exists a schedule of legnth d if any job cant be scheduled in d time.
                return false;
            }

            //weight of an item-task will be its canonical number of processors needed to respect the threshold d
            weight[i] = dAllotment;

            
            if (dHalfAllotment != -1) {
                //profit of an item-task will correspond to the work saving obtained by executing the task just to respect the threshold d instead of d/2
                //w_{i, y{i, d/2} - w_{i, y{i, d}}
                profit[i] = (dHalfAllotment * job.getProcessingTime(dHalfAllotment)) - (dAllotment * job.getProcessingTime(dAllotment)); //TODO: is not the original profit (p. 89 Thesis Felix)
                if(dHalfAllotment < b && dAllotment < b) { //both sizes are small
                    //round the original profit down to the next multiple of epsilon * d
                    int j = 1;
                    while(Math.floor(j * epsilon * d) <= profit[i]) {
                        j++;
                        if(j > (2 / Math.pow(epsilon, 2))) { // j \in N <= 2/ e^2
                            break;
                        }
                    }
                    profit[i] = (int) Math.floor((j - 1) * epsilon * d);    //TODO use double profits.
                } else if(dHalfAllotment >= b && dAllotment >= b) { //both sizes are big
                    double t_PrimeD = (1 / (1 + 4 * d));
                    double t_PrimeDHalf = (1 / (1 + 4 * (d / 2.0)));
                    profit[i] = (int) Math.floor(dHalfAllotment * t_PrimeDHalf - dAllotment * t_PrimeD);
                } else { //jobs where dHalfAllotment >= b and dAllotment < b
                    double t_PrimeDHalf = (1 / (1 + 4 * (d / 2.0)));
                    int w_dHalf = (int) Math.floor(t_PrimeDHalf * dHalfAllotment);
                    int w_d = (int) Math.floor(dAllotment * job.getProcessingTime(dAllotment));
                    //round w(j, d) down to the next multiple of epsilon * d
                    int j = 1;
                    while(Math.floor(j * epsilon * d) <= profit[i]) {
                        j++;
                        if(j > (4 / Math.pow(epsilon, 2))) { // j \in N <= 4/ e^2
                            break;
                        }
                    }
                    profit[i] = w_dHalf - (int) Math.floor((j - 1) * epsilon * d);
                }
                
            } else { //job has to be scheduled on s1.
                //TODO remove from knapsack and schedule on shelf 1.
                profit[i] = (int) Math.round(I.getM() * d);     //really big.
            }

        }
        

        KnapsackSolver kS = new ConvolutionKnapsack();
        List<Job> shelf1 = kS.solve(shelf2, weight, profit, shelf2.size(), I.getM());
        shelf2.removeAll(shelf1); //update shelf2
        int p1 = 0;     //processors required by S1.
        for(Job selectedJob : shelf1) {
            //update WShelf2
            if(selectedJob.getAllotedMachines() != -1) {
                WShelf2 -= selectedJob.getAllotedMachines() * selectedJob.getProcessingTime(selectedJob.getAllotedMachines());
            }
            //"move job to shelf1"
            int dAllotment = selectedJob.canonicalNumberMachines(d);
            if(dAllotment > b) { //rounding
                dAllotment = (int) GeometricalRounding.gFloor(dAllotment, b, I.getM(), 1 + roh); //TODO check rounding by integer casting.
            }
            selectedJob.setAllotedMachines(dAllotment);
            p1 += dAllotment; //keep track of the number of machines used by s1

            //update WShelf1
            WShelf1 += selectedJob.getAllotedMachines() * selectedJob.getProcessingTime(selectedJob.getAllotedMachines());
        }
        
        if(WShelf1 + WShelf2 > I.getM() * d - Ws) {   //there cant exists a schedule of with makespan d (s. Thesis Felix S. 76)
            return false;
        }
        
// ############################################## DEBUG ##################################################################################################################
        // System.out.println();
        // // System.out.println(printSchedule.printTwoShelves(bigJobs, (int) d));
        // System.out.println(printSchedule.printTwoShelves(MyMath.findBigJobs(I, d), (int) d));
// ############################################## DEBUG ##################################################################################################################
        
        List<Job> shelf0 = applyTransformationRules(d, shelf1, shelf2, p1);

        
        
// ############################################## DEBUG ##################################################################################################################
        // System.out.println();
        // // System.out.println(printSchedule.printTwoShelves(bigJobs, (int) d));
        // System.out.println(printSchedule.printThreeShelves(shelf0, shelf1, shelf2));
// ############################################## DEBUG ##################################################################################################################
        addSmallJobs(shelf1, shelf2, smallJobs, d, I.getM());

        List<Machine> machinesS0 = new ArrayList<>();
        double startTime = -1;
        for(Job job : shelf0) {
            if(job.getStartingTime() != startTime) {
                Machine m = new Machine(0);
                m.addJob(job);
                machinesS0.add(m);
                startTime = job.getProcessingTime(job.getAllotedMachines());
            } else {
                machinesS0.get(machinesS0.size() - 1).addJob(job);
                startTime += job.getProcessingTime(job.getAllotedMachines());
            }
        }
        I.addMachines(machinesS0);

// ############################################## DEBUG ##################################################################################################################
        // System.out.println(String.format("-".repeat(70) + "%04.2f" + "-".repeat(70), d));
        // System.out.println(printSchedule.printMachines(I.getMachines()));
        // System.out.println(String.format("-".repeat(70) + "%04.2f" + "-".repeat(70), d));
// ############################################## DEBUG ##################################################################################################################

        return true;
    }

    private int gammaPrime(Job job, double d, double roh, double b) {
        int gamma = job.canonicalNumberMachines(d);
        if(gamma == -1) {
            return -1;
        }
        int gamma_prime = gamma;
        while(Math.ceil((1 - roh) * (gamma_prime + 1)) <= gamma) {      //TODO 1- roh correct??
            gamma_prime++;
        }
        return (int) Math.ceil((1 - roh) * gamma_prime); //TODO maybe floor.
    }

}
