package br.com.ryoshino.service;

import br.com.ryoshino.model.ClienteKafka;
import br.com.ryoshino.repository.ClienteKafkaRepository;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Service
@EnableScheduling
public class CcsService {

    private final long MINUTOS = (1000 * 60);
    private final CcsKafka kafkaProperties = new CcsKafka();
    private final String topic = "cliente_ccs";
    @Autowired
    private ClienteKafkaRepository clienteKafkaRepository;

    public CcsService(ClienteKafkaRepository clienteKafkaRepository) {
        this.clienteKafkaRepository = clienteKafkaRepository;
    }

    @Scheduled(fixedDelay = MINUTOS)
    public void consumirPeloKafkaAssincrono() throws IOException {
        Logger logger = LoggerFactory.getLogger(CcsService.class.getName());

        // create consumer configs
        kafkaProperties.configurationKafka().setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        kafkaProperties.configurationKafka().setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "10");
        // create consumer
        KafkaConsumer<String, ClienteKafka> consumer = new KafkaConsumer<>(kafkaProperties.configurationKafka());

        // subscribe consumer to our topic(s)
        consumer.subscribe(Arrays.asList(topic));

        ConsumerRecords<String, ClienteKafka> records;
        records = consumer.poll(Duration.ofMinutes(1)); // new in Kafka 2.0.0
        logger.info("Received: " + records.count());


        for (ConsumerRecord<String, ClienteKafka> record : records) {
            logger.info("Value: " + record.value());
            logger.info("Valor:" + record.toString().toUpperCase());
            clienteKafkaRepository.save(record.value());
        }
        // poll for new data
        consumer.commitSync();

        consumer.close();
    }

    public void consumirSincrono() throws IOException {
        Logger logger = LoggerFactory.getLogger(CcsService.class.getName());

        // create consumer
        KafkaConsumer<String, ClienteKafka> consumer = new KafkaConsumer<>(kafkaProperties.configurationKafka());

        // subscribe consumer to our topic(s)
        consumer.subscribe(Arrays.asList(topic));

        // poll for new data
        ConsumerRecords<String, ClienteKafka> records;

        records = consumer.poll(Duration.ofMinutes(1)); // new in Kafka 2.0.0
        logger.info("Received: " + records.count());

        for (ConsumerRecord<String, ClienteKafka> record : records) {
            logger.info("Value: " + record.value());
            logger.info("Valor:" + record.toString().toUpperCase());
            clienteKafkaRepository.save(record.value());
        }
        consumer.close();
    }

    private BufferedWriter getBufferedWriter(Logger logger, KafkaConsumer<String, ClienteKafka> consumer) throws IOException {
        FileWriter arquivo = new FileWriter("src/relatorioTransacao_Kafka.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(arquivo);
        bufferedWriter.write("CPF;NOME");
        ConsumerRecords<String, ClienteKafka> records;

        records = consumer.poll(Duration.ofMinutes(1)); // new in Kafka 2.0.0
        logger.info("Received: " + records.count());

        for (ConsumerRecord<String, ClienteKafka> record : records) {
            bufferedWriter.flush();
            logger.info("Value: " + record.value());
            logger.info("Valor:" + record.toString().toUpperCase());
            bufferedWriter.newLine();
            bufferedWriter.write(record.value().getIdCliente() + ", " + record.value().getNome() + ";");
            clienteKafkaRepository.save(record.value());
        }
        return bufferedWriter;
    }

    public void salvarCliente(ClienteKafka clienteKafka) {
        clienteKafkaRepository.save(clienteKafka);
    }

    public List<ClienteKafka> listarClientes() {
        return clienteKafkaRepository.findAll();
    }
}
