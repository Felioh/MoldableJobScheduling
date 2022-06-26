package de.ohnes;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.ohnes.AlgorithmicComponents.Algorithm;
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
        Algorithm Algo = new FelixApproach();
        Algo.solve(I, 100, 0.5);
    }
}
