package de.ohnes;
import java.io.IOException;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.ohnes.AlgorithmicComponents.Algorithm;
import de.ohnes.AlgorithmicComponents.Approximation.Approximation;
import de.ohnes.AlgorithmicComponents.Approximation.TwoApproximation;
import de.ohnes.AlgorithmicComponents.FPTAS.CompressionApproach;
import de.ohnes.AlgorithmicComponents.FPTAS.DoubleCompressionApproach;
import de.ohnes.AlgorithmicComponents.Shelves.FelixApproach;
import de.ohnes.AlgorithmicComponents.Shelves.KilianApproach;
import de.ohnes.logger.DrawSchedule;
import de.ohnes.logger.MyElasticsearchClient;
import de.ohnes.logger.printSchedule;
import de.ohnes.util.Instance;
import de.ohnes.util.MyMath;
import de.ohnes.util.TestResult;

public class App {

    private static String rand;
    private static String minJobs;
    private static String maxJobs;
    private static String minMachines;
    private static String maxMachines;
    private static String ESHost;
    private static String loop;

    private static String algo;

    public static void main(String[] args) throws Exception {
        
        rand = System.getenv("INSTANCE_RANDOM");
        minJobs = System.getenv("INSTANCE_MINJOBS");
        maxJobs = System.getenv("INSTNACE_MAXJOBS");
        minMachines = System.getenv("INSTANCE_MINMACHINES");
        maxMachines = System.getenv("INSTANCE_MAXMACHINES");
        ESHost = System.getenv("ELASTICSEARCH_HOST");
        loop = System.getenv("LOOP");
        algo = System.getenv("ALGO");
        
        MyElasticsearchClient.makeConnection(ESHost);
        if(loop != null) {
            while(true) {
                for(int i = 0; i < 10; i++) {
                    MyElasticsearchClient.addData(runTest());
                }
                MyElasticsearchClient.pushData("testdata-" + java.time.LocalDate.now().toString());

            }
        } else {
            for(int i = 0; i < 10; i++) {
                MyElasticsearchClient.addData(runTest());
            }
            MyElasticsearchClient.pushData("testdata-" + java.time.LocalDate.now().toString());
        }

    }

    private static TestResult runTest() {
        Instance I = new Instance();
        if(rand == null) {
            // Instance I = mapper.readValue(Paths.get("TestInstance copy 3.json").toFile(), Instance.class);
            try {
                // ObjectMapper mapper = new ObjectMapper();
                // SimpleModule module = new SimpleModule();
                // module.addDeserializer(Instance.class, new InstanceDeserializer());
                // mapper.registerModule(module);
                // I = mapper.readValue(Paths.get("TestInstance copy 3.json").toFile(), Instance.class);
                I = new ObjectMapper().readValue(Paths.get("TestInstances/TestInstance copy 3.json").toFile(), Instance.class);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            I.generateRandomInstance(Integer.parseInt(minJobs), Integer.parseInt(maxJobs), Integer.parseInt(minMachines), Integer.parseInt(maxMachines));
        }


        DualApproximationFramework dF;
        // if(algo == null) {
        //     return null; //algo not specified.
        // }
        if(algo == null || algo.equals("Kilian")) {
            Algorithm algo = new KilianApproach();
            Algorithm fptas = new DoubleCompressionApproach();
            Approximation approx = new TwoApproximation();
            dF = new DualApproximationFramework(fptas, algo, approx, I);
        } else if(algo.equals("Felix")) {
            Algorithm algo = new FelixApproach();
            Algorithm fptas = new CompressionApproach();
            Approximation approx = new TwoApproximation();
            dF = new DualApproximationFramework(fptas, algo, approx, I);
        } else {
            return null;
        }


        long startTime = System.currentTimeMillis();
        double d = dF.start(0.1);
        long endTime = System.currentTimeMillis();
        System.out.println("Ran Instance with " + I.getM() + " Machines and " + I.getN() + " Jobs in " + (endTime - startTime) + " Milliseconds...");

// ############################################## DEBUG ##################################################################################################################
        // System.out.println(String.format("-".repeat(70) + "%04.2f" + "-".repeat(70), d));
        // System.out.println(printSchedule.printMachines(I.getMachines()));
        // System.out.println(String.format("-".repeat(70) + "%04.2f" + "-".repeat(70), d));
// ############################################## DEBUG ##################################################################################################################
        // System.out.println("That took " + (endTime - startTime) + " milliseconds");
        // DrawSchedule.drawSchedule(I);

        TestResult tr = new TestResult();
        tr.setApproximation(dF.getApproximationName());
        tr.setFptas(dF.getFPTASName());
        tr.setShelvesAlgo(dF.getShelvesAlgoName());
        tr.setAchivedMakespan(I.getMakespan());
        tr.setEstimatedOptimum(d);
        tr.setJobs(I.getN());
        tr.setMachines(I.getM());
        tr.setMilliseconds((endTime - startTime));
        tr.setBigJobs(MyMath.findBigJobs(I, d).length);
        tr.setSmallJobs(MyMath.findSmallJobs(I, d).length);
        // tr.setProcessingTimes(I.getJobs());

        return tr;
    }

}
