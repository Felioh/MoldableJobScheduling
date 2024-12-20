package de.ohnes.AlgorithmicComponents.Shelves;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.ohnes.AlgorithmicComponents.Algorithm;
import de.ohnes.AlgorithmicComponents.Knapsack.MCKnapsack;
import de.ohnes.util.Instance;
import de.ohnes.util.Job;
import de.ohnes.util.Machine;
import de.ohnes.util.MyMath;

public class OhnesorgeApproach implements Algorithm {

    private Instance I;

    /**
     * finds a shedule for the instance I with deadline d, if a schedule of
     * length d exists.
     * 
     * @param d       the deadline (makespan guess)
     * @param epsilon the "the small error"
     * @return true if a schedule of length d exists, false if none exists.
     */
    @Override
    public boolean solve(double d, double epsilon) {
        double t = 3 * d / 7;
        // forget about small jobs
        List<Job> bigJobs = new ArrayList<>(Arrays.asList(MyMath.findBigJobs(I, t)));
        List<Job> smallJobs = new ArrayList<>(Arrays.asList(MyMath.findSmallJobs(I, t)));

        // minimal work of small jobs
        int Ws = smallJobs.stream().mapToInt(j -> j.getProcessingTime(1)).sum();

        // solve the knapsack problem
        List<Job> shelf1 = new ArrayList<>();
        List<Job> shelf0 = new ArrayList<>();
        List<Job> shelf2 = new ArrayList<>();
        MCKnapsack knapsack = new MCKnapsack();
        knapsack.solve(bigJobs, 0, shelf1, shelf0, shelf2, d);

        // check work constraint
        int WShelf1 = shelf1.stream()
                .mapToInt(j -> j.getAllotedMachines() * j.getProcessingTime(j.getAllotedMachines()))
                .sum();
        int WShelf2 = shelf2.stream()
                .mapToInt(j -> j.getAllotedMachines() * j.getProcessingTime(j.getAllotedMachines()))
                .sum();
        int WShelf0 = shelf0.stream()
                .mapToInt(j -> j.getAllotedMachines() * j.getProcessingTime(j.getAllotedMachines()))
                .sum();

        if (WShelf1 + WShelf2 + WShelf0 > I.getM() * d - Ws) {
            return false;
        }

        // adjust jobs chosen in c2, i.e. shelf0
        Job j1 = null;
        Job j3 = null;
        for (Job job : shelf0) {
            int allotedMachines = job.getAllotedMachines();
            switch (allotedMachines) {
                case 1:
                    if (j1 == null) {
                        j1 = job;
                    } else {
                        // schedule these jobs after each other
                        j1.setStartingTime(job.getProcessingTime(1));
                        j1 = null;
                    }
                    break;
                case 3:
                    if (j3 == null) {
                        j3 = job;
                    } else {
                        // schedule these jobs after each other
                        j3.setStartingTime(job.getProcessingTime(3));
                        j3 = null;
                    }
                    break;
                default:
                    // schedule job in \gamma(, 10/7d)
                    job.setAllotedMachines(job.canonicalNumberMachines(10 * d / 7));
                    break;
            }
        }
        if (j3 != null && j1 != null) {
            // this job can be artificially split, since it will not be altered during the
            // algorithm in any way.
            // TODO: do we need to exchange this job in the instance?
            int pTime = j3.getProcessingTime(2);
            shelf0.remove(j3);
            Job virtualJob1 = new Job(j3.getId(), new int[] { pTime, pTime });
            virtualJob1.setAllotedMachines(1);
            shelf0.add(virtualJob1);
            j1.setStartingTime(pTime);
            shelf1.add(virtualJob1);
        } else {
            if (j3 != null) {
                // schedule job in \gamma(, 10/7d)
                int allottedMachines = j3.canonicalNumberMachines(10 * d / 7);
                j3.setAllotedMachines(allottedMachines);
                if (j3.getProcessingTime(allottedMachines) <= d) {
                    shelf1.add(j3);
                    shelf0.remove(j3);
                }
            }
            if (j1 != null) {
                shelf1.add(j1);
                shelf0.remove(j1);
            }
        }

        int m2 = shelf2.stream().mapToInt(Job::getAllotedMachines).sum();
        int m1 = shelf1.stream().mapToInt(Job::getAllotedMachines).sum();
        int m0 = shelf0.stream().filter(j -> j.getStartingTime() == 0).mapToInt(Job::getAllotedMachines).sum();

        // shelf 0 and shelf 1 should not use more than m machines
        assert (m0 + m1 <= I.getM());

        if (m2 <= I.getM() - m0) {
            return true; // done. the schedule should be feasible.
        }

        // apply transformation rules with increasing \lambda
        double lambdad = 10 * d / 7;
        applyTransformationRules(lambdad, d, shelf0, shelf1, shelf2, I.getM() - m0 - m1);

        m2 = shelf2.stream().mapToInt(Job::getAllotedMachines).sum();
        m1 = shelf1.stream().mapToInt(Job::getAllotedMachines).sum();
        m0 = shelf0.stream().filter(j -> j.getStartingTime() == 0).mapToInt(Job::getAllotedMachines).sum();

        if (m2 <= I.getM() - m0) {
            return true; // done. the schedule should be feasible.
        }

        // if q = 0, we can find a feasible schedule
        if (I.getM() - m0 - m1 == 0) {
            algorithm3(shelf1, shelf2, m2, m1);
            return true;
        }

        lambdad = 13 * d / 9;
        applyTransformationRules(lambdad, d, shelf0, shelf1, shelf2, I.getM() - m0 - m1);

        m2 = shelf2.stream().mapToInt(Job::getAllotedMachines).sum();
        m1 = shelf1.stream().mapToInt(Job::getAllotedMachines).sum();
        m0 = shelf0.stream().filter(j -> j.getStartingTime() == 0).mapToInt(Job::getAllotedMachines).sum();

        if (m2 <= I.getM() - m0) {
            return true; // done. the schedule should be feasible.
        }

        // for \lambda \leq m_1/6, we can find a feasible schedule
        if (I.getM() - m0 - m1 <= m1 / 6) {
            algorithm3(shelf1, shelf2, m2, m1);
            return true;
        }

        lambdad = 73 * d / 50;
        applyTransformationRules(lambdad, d, shelf0, shelf1, shelf2, I.getM() - m0 - m1);

        m2 = shelf2.stream().mapToInt(Job::getAllotedMachines).sum();
        m1 = shelf1.stream().mapToInt(Job::getAllotedMachines).sum();
        m0 = shelf0.stream().filter(j -> j.getStartingTime() == 0).mapToInt(Job::getAllotedMachines).sum();

        if (m2 <= I.getM() - m0) {
            return true; // done. the schedule should be feasible.
        }

        // we can find a feasible schedule with either algorithm 3 or 4
        if (I.getM() - m0 - m1 <= m1 / 6) {
            algorithm3(shelf1, shelf2, m2, m1);
            return true;
        } else {
            assert (shelf2.size() == 1); // Observation 3.5
            algorithm4(lambdad, shelf1, shelf2.get(0), m1);
            return true;
        }

    }

    protected boolean placeJobs(List<Job> shelf0, List<Job> shelf1, List<Job> shelf2, Instance I, double d) {
        Machine[] machines = I.getMachines();

        int i = 0;
        // S0
        List<Machine> partiallyFilledMachines = new ArrayList<>();
        for (Job job : shelf0.stream().filter(j -> j.getStartingTime() == 0).toArray(Job[]::new)) {
            int allotedMachines = job.getAllotedMachines();
            boolean small = job.getProcessingTime(allotedMachines) <= d;
            for (; i < i + allotedMachines; allotedMachines--, i++) {
                machines[i].addJob(job);
                if (small) {
                    partiallyFilledMachines.add(machines[i]);
                }
            }
        }
        for (Job job : shelf0.stream().filter(j -> j.getStartingTime() != 0).toArray(Job[]::new)) {
            int allotedMachines = job.getAllotedMachines();
            for (Machine machine : partiallyFilledMachines.stream()
                    .filter(m -> m.getFirstFreeTime() == job.getStartingTime()).toArray(Machine[]::new)) {
                machine.addJob(job);
                allotedMachines--;
                if (allotedMachines == 0) {
                    break;
                }
            }
            assert (allotedMachines == 0); // all jobs should be placed.
        }

        // TODO: place shelf1 and shelf2.
        return true;
    }

    protected void algorithm3(List<Job> shelf1, List<Job> shelf2, int m2, int m1) {
        // sort jobs in shelf2 by their processing time
        shelf2.sort((j1, j2) -> Integer.compare(j1.getProcessingTime(j1.getAllotedMachines()),
                j2.getProcessingTime(j2.getAllotedMachines())));
        while (m2 > m1) {
            // get the smallest job in shelf2
            Job job = shelf2.get(0);
            job.setAllotedMachines(job.getAllotedMachines() - 1);
            // resort the list
            // TODO: this can be done smarter
            shelf2.sort((j1, j2) -> Integer.compare(j1.getProcessingTime(j1.getAllotedMachines()),
                    j2.getProcessingTime(j2.getAllotedMachines())));
        }
    }

    protected void algorithm4(double lambdad, List<Job> shelf1, Job j0, int m1) {
        // sort jobs in shelf1 by their processing time (ascending!)
        shelf1.sort((j1, j2) -> Integer.compare(j2.getProcessingTime(j1.getAllotedMachines()),
                j1.getProcessingTime(j2.getAllotedMachines())));

        int i = shelf1.size() - 1;
        int w = shelf1.get(i).getAllotedMachines();
        for (int x = m1; x > 0; x--) {
            // check, if j0 fits
            if (j0.getProcessingTime(x)
                    + shelf1.get(i).getProcessingTime(shelf1.get(i).getAllotedMachines()) <= lambdad) {
                // schedule j0 like this
                j0.setAllotedMachines(x);
                return;
            }
            w--;
            if (w == 0) {
                i--;
                w = shelf1.get(i).getAllotedMachines();
            }
        }
        assert (false); // this should never happen

    }

    // TODO: combine sequential small jobs that remain on shelf1 to single bigger
    // job for simplicity
    protected void applyTransformationRules(double lambdad, double d, List<Job> shelf0, List<Job> shelf1,
            List<Job> shelf2, int q) {
        Job j1 = null;
        for (Job job : shelf1) {
            if (job.getAllotedMachines() == 1) {
                // T2
                if (job.getProcessingTime(1) > lambdad / 2) {
                    continue; // ignore jobs that are big enough
                }
                if (j1 == null) {
                    j1 = job;
                } else {
                    // schedule these jobs after each other
                    shelf0.add(j1);
                    shelf0.add(job);
                    shelf1.remove(j1);
                    shelf1.remove(job);
                    j1.setStartingTime(job.getProcessingTime(1));
                    j1 = null;
                }
                continue;
            }

            // T1
            if (job.canonicalNumberMachines(lambdad) < job.getAllotedMachines()) {
                job.setAllotedMachines(job.canonicalNumberMachines(lambdad));
                shelf0.add(job);
                shelf1.remove(job);
            }
        }

        // T3
        for (Job job : shelf2) {
            if (job.canonicalNumberMachines(lambdad) <= q) {
                job.setAllotedMachines(job.canonicalNumberMachines(lambdad));
                if (job.getProcessingTime(job.getAllotedMachines()) <= d) {
                    shelf1.add(job);
                    shelf2.remove(job);
                }
            }
        }
    }

    @Override
    public void setInstance(Instance I) {
        this.I = I;
    }

}
