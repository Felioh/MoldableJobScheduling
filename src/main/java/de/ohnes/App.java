package de.ohnes;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.ohnes.AlgorithmicComponents.Algorithm;
import de.ohnes.AlgorithmicComponents.FPTAS.CompressionApproach;
import de.ohnes.AlgorithmicComponents.Shelves.FelixApproach;
import de.ohnes.logger.InstanceDeserializer;
import de.ohnes.util.Instance;

public class App {
    public static void main(String[] args) throws Exception {
        
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Instance.class, new InstanceDeserializer());
        mapper.registerModule(module);

        Instance I = mapper.readValue(Paths.get("TestInstance copy 3.json").toFile(), Instance.class);

        // Instance I = new Instance();

        // I.generateRandomInstance(8, 10, 2, 5);
        
        System.out.println(I);
        Algorithm algo = new FelixApproach();
        Algorithm fptas = new CompressionApproach();
        DualApproximationFramework dF = new DualApproximationFramework(fptas, algo);
        // algo.setInstance(I);
        // algo.solve(100, 0.5);
        double d = dF.start(I, 0.1);
        System.out.println(d);
    }
}
