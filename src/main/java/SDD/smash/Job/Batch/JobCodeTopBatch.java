package SDD.smash.Job.Batch;

import SDD.smash.Job.Converter.JobConverter;
import SDD.smash.Job.Dto.JobCodeTopDTO;
import SDD.smash.Job.Entity.JobCodeTop;
import SDD.smash.Job.Repository.JobCodeTopRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class JobCodeTopBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final JobCodeTopRepository jobCodeTopRepository;

    public JobCodeTopBatch(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, JobCodeTopRepository jobCodeTopRepository) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.jobCodeTopRepository = jobCodeTopRepository;
    }

    @Value("${jobCodeTop.filePath}")
    private String filePath;

    @Bean
    public Job jcTopJob(){
        return new JobBuilder("jcTopJob", jobRepository)
                .start(jcTopStep())
                .build();
    }

    @Bean
    public Step jcTopStep(){
        return new StepBuilder("jcTopStep", jobRepository)
                .<JobCodeTopDTO, JobCodeTop> chunk(10, platformTransactionManager)
                .reader(jcTopCsvReader())
                .processor(jcTopProcessor())
                .writer(jcTopWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<JobCodeTopDTO> jcTopCsvReader(){

        return new FlatFileItemReaderBuilder<JobCodeTopDTO>()
                .name("jcTopCsvReader")
                .resource(new FileSystemResource(filePath))
                .encoding("MS949")
                .linesToSkip(1)
                .skippedLinesCallback(line -> log.info("Skip header : {}", line))
                .strict(true)
                .delimited()
                .delimiter(",")
                .quoteCharacter('\0')
                .names("jobCode", "name")
                .fieldSetMapper(fieldSet -> {
                    JobCodeTopDTO dto = new JobCodeTopDTO();
                    dto.setCode(fieldSet.readString(0).trim());
                    dto.setName(fieldSet.readString(1).trim());

                    return dto;
                })
                .build();
    }

    @Bean
    public ItemProcessor<JobCodeTopDTO, JobCodeTop> jcTopProcessor(){
        return JobConverter::topToEntity;
    }

    @Bean
    public RepositoryItemWriter<JobCodeTop> jcTopWriter(){
        return new RepositoryItemWriterBuilder<JobCodeTop>()
                .repository(jobCodeTopRepository)
                .methodName("save")
                .build();
    }

}
