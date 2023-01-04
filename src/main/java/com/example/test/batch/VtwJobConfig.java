package com.example.test.batch;

import com.example.test.domain.VtwBatchBoard;
import com.example.test.domain.VtwBoard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.kafka.KafkaItemWriter;
import org.springframework.batch.item.kafka.builder.KafkaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.persistence.EntityManagerFactory;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class VtwJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private final int chunkSize = 5;


    @Bean
    public Job vtwJob_builder(){
        return jobBuilderFactory.get("VtwJobConfig")
                .start(vtwJob_step1())
//                .start(vtwJob_step2())
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
                .<VtwBoard,VtwBatchBoard>chunk(chunkSize)
                .reader(vtwJob_CsvReader())
                .writer(vtwJob_ItemWriter())
                .build();
    }

//    @Bean
//    public KafkaItemWriter<> kafkaItemWriter(){
//        return new KafkaItemWriterBuilder<>()
//                .kafkaTemplate(template)
//                .itemKeyMapper()
//                .build();
//    }

    @Bean
    public JpaPagingItemReader<VtwBoard> vtwJob_ItemReader(){
        return new JpaPagingItemReaderBuilder<VtwBoard>()
                .name("vtwJob_ItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
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
    public FlatFileItemReader<VtwBoard> vtwJob_CsvReader(){
        FlatFileItemReader<VtwBoard> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("output/vtwBoardList.csv"));

        DefaultLineMapper<VtwBoard> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames(new String[]{"boardNo", "contents", "creationDate", "subject", "updateDate", "vtwUser"});
        delimitedLineTokenizer.setDelimiter(",");

        BeanWrapperFieldSetMapper<VtwBoard> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(VtwBoard.class);

        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
        flatFileItemReader.setLineMapper(defaultLineMapper);

        return flatFileItemReader;
    }

    @Bean
    public FlatFileItemWriter<VtwBoard> vtwJob_CsvWriter(Resource resource){
        BeanWrapperFieldExtractor<VtwBoard> vtwBoardBeanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        vtwBoardBeanWrapperFieldExtractor.setNames(new String[]{"boardNo", "contents", "creationDate", "subject", "updateDate", "vtwUser"});
        vtwBoardBeanWrapperFieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<VtwBoard> delimitedLineAggregator = new DelimitedLineAggregator<>();
        delimitedLineAggregator.setDelimiter(",");
        delimitedLineAggregator.setFieldExtractor(vtwBoardBeanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<VtwBoard>().name("vtwJob_CsvWriter")
                .resource(resource)
                .lineAggregator(delimitedLineAggregator)
                .shouldDeleteIfEmpty(true)
                .shouldDeleteIfExists(true)
                .build();
    }
}
