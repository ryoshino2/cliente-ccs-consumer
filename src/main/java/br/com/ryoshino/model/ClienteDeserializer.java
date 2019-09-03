package br.com.ryoshino.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class ClienteDeserializer implements Deserializer<ClienteKafka> {


    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    public ClienteKafka deserialize(String arg0, byte[] arg1) {
        ObjectMapper mapper = new ObjectMapper();
        ClienteKafka cliente = null;
        try {
            cliente = mapper.readValue(arg1, ClienteKafka.class);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return cliente;
    }

    @Override
    public void close() {

    }
}
