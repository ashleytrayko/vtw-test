package com.example.test.batch;

import com.example.test.domain.VtwBatchBoard;
import com.example.test.domain.VtwBoard;
import com.example.test.domain.VtwUser;
import com.example.test.dto.VtwBatchDTO;
import com.example.test.dto.VtwBoardDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.io.Writer;
import java.sql.Timestamp;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class VtwJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final KafkaProperties kafkaProperties;

    private final int chunkSize = 5;


    @Bean
    public Job vtwJob_builder(){
        return jobBuilderFactory.get("VtwJobConfig")
                .start(vtwJob_step1())
//                .start(vtwJob_step2())
                .build();
    }
    @Bean Job vtwJob_Builder3(){
        return jobBuilderFactory.get("vtwJobConfig")
                .start(vtwJob_step3())
                .build();
    }

    // Step 1 DB -> CSV
    @Bean
    public Step vtwJob_step1(){
        return stepBuilderFactory.get("vtwJob_step1()")
                .<VtwBoard,VtwBoard>chunk(chunkSize)
                .reader(vtwJob_ItemReader())
                .writer(vtwJob_CsvWriter(new FileSystemResource("output/vtwBoardList.csv")))
                .build();
    }

    // Step 2 CSV -> Kafka
//    @Bean
//    public Step vtwJob_step2(){
//        return stepBuilderFactory.get("vtwJob_step2")
//                .<>chunk(chunkSize)
//                .reader()
//                .writer()
//                .build();
//    }

    // Step 3 Kafka(CSV) -> DB
    @Bean
    public Step vtwJob_step3(){
        return stepBuilderFactory.get("vtwJob_step3()")
                .<VtwBatchDTO, VtwBatchBoard>chunk(chunkSize)
                .reader(vtwJob_CsvReader())
                .processor(csvToJpa_processor())
                .writer(vtwJob_ItemWriter())
                .build();
    }

//    @Bean
//    public KafkaItemReader<byte[], byte[]> kafkaItemReader(){
//        Properties consumerProperties = new Properties();
//        consumerProperties.putAll(kafkaProperties.buildConsumerProperties());
//
//        return new KafkaItemReaderBuilder<byte[], byte[]>()
//                .name("kafkaItemReader")
//                .topic("stream-test")
//                .partitions(1)
//                .partitionOffsets(new HashMap<>())
//                .consumerProperties(consumerProperties)
//                .build();
//    }
//
//    @Bean
//    public KafkaItemWriter<byte[], byte[]> kafkaItemWriter(){
//        Properties producerProperties = new Properties();
//        producerProperties.putAll(kafkaProperties.buildProducerProperties());
//
//        return new KafkaItemWriterBuilder<byte[], byte[]>().
//
//    }

    @Bean
    public JpaPagingItemReader<VtwBoard> vtwJob_ItemReader(){
        return new JpaPagingItemReaderBuilder<VtwBoard>()
                .name("vtwJob_ItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
//                .queryString("SELECT d from VtwBoard d order by d.boardNo asc")
                .queryString("SELECT d from VtwBoard d order by d.boardNo asc")
                .build();
    }

    @Bean
    public JpaItemWriter<VtwBatchBoard> vtwJob_ItemWriter(){
        JpaItemWriter<VtwBatchBoard> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

    @Bean
    public FlatFileItemReader<VtwBatchDTO> vtwJob_CsvReader(){
        FlatFileItemReader<VtwBatchDTO> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new PathResource("output/vtwBoardList.csv"));
        flatFileItemReader.setLinesToSkip(1);

        DefaultLineMapper<VtwBatchDTO> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames(new String[]{"boardNo","subject", "contents", "vtwUser", "creationDate", "updateDate"});
        delimitedLineTokenizer.setDelimiter(";");

        BeanWrapperFieldSetMapper<VtwBatchDTO> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(VtwBatchDTO.class);

        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
        flatFileItemReader.setLineMapper(defaultLineMapper);

        return flatFileItemReader;
    }

    @Bean
    public FlatFileItemWriter<VtwBoard> vtwJob_CsvWriter(Resource resource){
        BeanWrapperFieldExtractor<VtwBoard> vtwBoardBeanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        vtwBoardBeanWrapperFieldExtractor.setNames(new String[]{"boardNo","subject", "contents", "vtwUser", "creationDate", "updateDate"});
        vtwBoardBeanWrapperFieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<VtwBoard> delimitedLineAggregator = new DelimitedLineAggregator<>();
        delimitedLineAggregator.setDelimiter(";");
        delimitedLineAggregator.setFieldExtractor(vtwBoardBeanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<VtwBoard>().name("vtwJob_CsvWriter")
                .resource(resource)
                .lineAggregator(delimitedLineAggregator)
                .headerCallback(new FlatFileHeaderCallback() {
                    @Override
                    public void writeHeader(Writer writer) throws IOException {
                        writer.write("boardNo; subject; contents; vtwUser; creationDate; updateDate");
                    }
                })
                .shouldDeleteIfEmpty(true)
                .shouldDeleteIfExists(true)
                .build();
    }

    @Bean
    public ItemProcessor<VtwBatchDTO, VtwBatchBoard> csvToJpa_processor(){
        StringToTimestamp stringToTimestamp = new StringToTimestamp();
        return VtwBatchDTO -> new VtwBatchBoard(Long.parseLong(VtwBatchDTO.getBoardNo()),VtwBatchDTO.getSubject(), VtwBatchDTO.getContents(),
                new VtwUser(VtwBatchDTO.getVtwUser().getUserId(),VtwBatchDTO.getVtwUser().getUsername(),VtwBatchDTO.getVtwUser().getPassword(),VtwBatchDTO.getVtwUser().getRole(),VtwBatchDTO.getVtwUser().getCreationDate(),VtwBatchDTO.getVtwUser().getUpdateDate())
                ,stringToTimestamp.convert(VtwBatchDTO.getCreationDate()), stringToTimestamp.convert(VtwBatchDTO.getUpdateDate()));
    }

}
