package com.example.vtw.batch;

import com.example.vtw.domain.Log;
import com.example.vtw.domain.Board;
import com.example.vtw.dto.LogDTO;
import com.example.vtw.dto.BoardDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.id.IncrementGenerator;
import org.hibernate.id.UUIDGenerator;
import org.hibernate.id.factory.internal.DefaultIdentifierGeneratorFactory;
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
import org.springframework.batch.item.kafka.KafkaItemWriter;
import org.springframework.batch.item.kafka.builder.KafkaItemWriterBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.kafka.core.KafkaTemplate;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final KafkaTemplate<LogDTO, LogDTO> kafkaTemplate;

    // 5개 단위로 읽고 커밋
    private final int chunkSize = 5;

    
    // 배치 잡 생성
    // DB SELECT -> DB(이력) INSERT -> CSV 파일 생성 -> 카프카 전송
    @Bean
    public Job batchJob_builder(){
        return jobBuilderFactory.get("BatchJobConfig")
                .start(boardTableToLogTable())
                .next(createCsvFile())
//                .next(csvToKafka())
                .build();
    }


    // Step 1 Board Table SELECT -> Log Table INSERT-- 일단 성공.. 일시는 추후 추가..
    @Bean Step boardTableToLogTable(){
        return stepBuilderFactory.get("boardTableToLogTable()")
                .<Board, Log> chunk(chunkSize)
                .reader(boardItemReader())
                .processor(BoardToLogProcessor())
                .writer(LogItemWriter())
                .build();
    }

    // Step 2 Create CSV FILE.. <- Step 1과 동시에 가능 할 것 같은데 안되서 스텝 나눔..
    @Bean Step createCsvFile(){
        return stepBuilderFactory.get("boardTableToLogTable()")
                .<Board, Log> chunk(chunkSize)
                .reader(boardItemReader())
                .processor(BoardToLogProcessor())
                .writer(board_CsvFileWriter(new PathResource("output/BoardList.csv"))) //일단은 log로 저장
                .build();
    }

//     Step 3 CSV FILE -> Kafka
//    @Bean
//    public Step csvToKafka(){
//        return stepBuilderFactory.get("csvToKafka")
//                .<LogDTO, LogDTO>chunk(chunkSize)
//                .reader(logCsvFileReader())
//                .processor(objectToString())
//                .writer(kafkaItemWriter())
//                .build();
//    }



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

    
    
    // DB 데이터 읽어오기(JPA)
    @Bean
    public JpaPagingItemReader<Board> boardItemReader(){
        return new JpaPagingItemReaderBuilder<Board>()
                .name("boardItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT d from Board d order by d.boardNo asc")
                .build();
    }
    
    // 읽어온 데이터 DB 저장(JPA)
    @Bean
    public JpaItemWriter<Log> LogItemWriter(){
        JpaItemWriter<Log> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

    
//     읽어온 데이터 CSV 파일로 작성
    @Bean
    public FlatFileItemWriter<Log> board_CsvFileWriter(Resource resource){
        BeanWrapperFieldExtractor<Log> vtwBoardBeanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        vtwBoardBeanWrapperFieldExtractor.setNames(new String[]{"contents", "user", "creationDate"});
        vtwBoardBeanWrapperFieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<Log> delimitedLineAggregator = new DelimitedLineAggregator<>();
        delimitedLineAggregator.setDelimiter(";");
        delimitedLineAggregator.setFieldExtractor(vtwBoardBeanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<Log>().name("board_CsvFileWriter")
                .resource(resource)
                .lineAggregator(delimitedLineAggregator)
                .headerCallback(new FlatFileHeaderCallback() {
                    @Override
                    public void writeHeader(Writer writer) throws IOException {
                        writer.write("contents; user; creationDate");
                    }
                })
                .shouldDeleteIfEmpty(true)
                .shouldDeleteIfExists(true)
                .build();
    }


    // BoardDTO를 Log 로 변환
    @Bean
    public ItemProcessor<Board, Log> BoardToLogProcessor(){

        return BoardDTO -> new Log(UUID.randomUUID(),BoardDTO.getContents(), BoardDTO.getUser()
                ,BoardDTO.getCreationDate(), new Timestamp(System.currentTimeMillis()));
    }


    // CSV파일 읽어오기
    @Bean
    public FlatFileItemReader<LogDTO> logCsvFileReader(){
        FlatFileItemReader<LogDTO> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new PathResource("output/BoardList.csv"));
        flatFileItemReader.setLinesToSkip(1);

        DefaultLineMapper<LogDTO> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames(new String[]{"contents", "vtwUser", "creationDate"});
        delimitedLineTokenizer.setDelimiter(";");

        BeanWrapperFieldSetMapper<LogDTO> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(LogDTO.class);

        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
        flatFileItemReader.setLineMapper(defaultLineMapper);

        return flatFileItemReader;
    }

    @Bean
    public KafkaItemWriter<LogDTO, LogDTO> kafkaItemWriter(){
        return new KafkaItemWriterBuilder<LogDTO, LogDTO>().kafkaTemplate(kafkaTemplate).itemKeyMapper(LogDTO::getLogNumber).delete(false).build();
    }

    @Bean
    public ItemProcessor<LogDTO, LogDTO> objectToString(){
        return LogDTO -> new LogDTO(LogDTO.getContents().toString(), LogDTO.getUser().toString(), LogDTO.getCreationDate().toString());
    }

}
