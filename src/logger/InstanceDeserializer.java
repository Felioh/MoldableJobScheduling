package logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import util.Instance;
import util.Job;

public class InstanceDeserializer extends StdDeserializer<Instance> {

    public InstanceDeserializer() { 
        this(null); 
    }

    public InstanceDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Instance deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = jp.getCodec().readTree(jp);
        int m = (node.get("machines")).asInt();
        int n = (node.get("number_jobs")).asInt();
        List<Job> jobs = new ArrayList<>();
        for(JsonNode job : node.get("jobs")) {
            int jobId = job.get("id").asInt();
            int[] pTimes = new int[m];
            int i = 0;
            for(JsonNode pTime : job.get("processingTimes")) {
                pTimes[i++] = pTime.asInt();
            }
            jobs.add(new Job(jobId, pTimes));
        }

        return new Instance(n, m, jobs.toArray(Job[] :: new));
    }
    
}
