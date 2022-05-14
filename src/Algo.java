

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

        //find all jobs that cant be scheduled in d/2 time.
        // int cap = Arrays.stream(bigJobs).filter(j -> j.getAllotedMachines() == -1).mapToInt(j -> I.canonicalNumberMachines(j.getId(), d)).reduce(0, Integer::sum); //reduce(0, (a, b) -> I.canonicalNumberMachines(a.getId(), d) + I.canonicalNumberMachines(b.getId(), d));
        //..and then remove them from the knapsack problem.
        // bigJobs = Arrays.stream(bigJobs).filter(j -> j.getAllotedMachines() != -1).toArray(Job[] :: new);

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

        bigJobs = dynamicKnapsack(bigJobs, weight, profit, bigJobs.length, I.getM(), I, d);


        System.out.println();
        System.out.println(printSchedule.printTwoShelves(bigJobs, (int) d));

    }

    private static Job[] dynamicKnapsack(Job[] jobs, int[] wt, int[] val, int n, int W, Instance I, double d) {
        int i, w;
        int K[][] = new int[n + 1][W + 1];
 
        // Build table K[][] in bottom up manner
        for (i = 0; i <= n; i++) {
            for (w = 0; w <= W; w++) {
                if (i == 0 || w == 0)
                    K[i][w] = 0;
                else if (wt[i - 1] <= w)
                    K[i][w] = Math.max(val[i - 1] +
                              K[i - 1][w - wt[i - 1]], K[i - 1][w]);
                else
                    K[i][w] = K[i - 1][w];
            }
        }
 
        // stores the result of Knapsack
        int res = K[n][W];
 
        w = W;
        for (i = n; i > 0 && res > 0; i--) {
 
            // either the result comes from the top
            // (K[i-1][w]) or from (val[i-1] + K[i-1]
            // [w-wt[i-1]]) as in Knapsack table. If
            // it comes from the latter one/ it means
            // the item is included.
            if (res == K[i - 1][w])
                continue;
            else {
 
                // This item is included.
                jobs[i - 1].setAllotedMachines(I.canonicalNumberMachines(jobs[i - 1].getId(), d));    //allot job to machines respecting d as a threshold
 
                // Since this weight is included its
                // value is deducted
                res = res - val[i - 1];
                w = w - wt[i - 1];
            }
        }
        return jobs;
    }
    
}
