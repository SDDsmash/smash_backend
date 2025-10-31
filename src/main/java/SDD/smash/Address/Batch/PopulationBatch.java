package SDD.smash.Address.Batch;

import SDD.smash.Address.Dto.PopulationDTO;
import SDD.smash.Address.Entity.Population;
import SDD.smash.Address.Entity.Sigungu;
import SDD.smash.Address.Repository.PopulationRepository;
import SDD.smash.Address.Repository.SigunguRepository;
import SDD.smash.Util.BatchTextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
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

import java.util.HashMap;
import java.util.Map;

import static SDD.smash.Address.Converter.AddressConverter.*;
import static SDD.smash.Util.BatchTextUtil.*;


@Configuration
@Slf4j
public class PopulationBatch {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final PopulationRepository populationRepository;
    private final SigunguRepository sigunguRepository;

    public PopulationBatch(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, PopulationRepository populationRepository, SigunguRepository sigunguRepository) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.populationRepository = populationRepository;
        this.sigunguRepository = sigunguRepository;
    }

    private Map<String, Sigungu> sigunguCache  = null;

    private Sigungu resolveSigungu(String sigunguCode) {
        if (sigunguCache == null) {
            sigunguCache = new HashMap<>();
            for (Sigungu sigungu : sigunguRepository.findAll()) {
                sigunguCache.put(sigungu.getSigunguCode(), sigungu);
            }
        }
        Sigungu sigungu = sigunguCache.get(sigunguCode);
        if (sigungu == null) {
            log.warn("❗ Unknown sigungu code: {}", sigunguCode);
            return null; // 혹은 예외 throw
        }
        String sigunguCodeTest = sigungu.getSigunguCode();
        return sigunguCache.get(sigunguCode);
    }

    @Value("${population.filePath}")
    private String filePath;

    @Bean
    public Job PopulationJob(){
        return new JobBuilder("PopulationJob", jobRepository)
                .start(populationStep())
                .build();
    }

    @Bean
    public Step populationStep() {

        return new StepBuilder("populationStep", jobRepository)
                .<PopulationDTO, Population> chunk(100, platformTransactionManager)
                .reader(populationCsvReader())
                .processor(populationCsvProcessor())
                .writer(populationWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<PopulationDTO> populationCsvReader() {

        return new FlatFileItemReaderBuilder<PopulationDTO>()
                .name("populationCsvReader")
                .resource(new FileSystemResource(filePath))
                .encoding("MS949")
                .linesToSkip(1)
                .strict(true)
                .delimited()
                .delimiter(",")
                .quoteCharacter('\0')
                .names("sigungu_code", "population")
                .fieldSetMapper(fieldSet -> {
                    String sigunguCode = normalize(fieldSet.readString(0));
                    String pop = digitsOnly(fieldSet.readString(1));

                    return new PopulationDTO(sigunguCode,pop);
                })
                .build();
    }

    @Bean
    public ItemProcessor<PopulationDTO, Population> populationCsvProcessor(){
        return dto -> {
            String sigunguKey = dto.getSigungu_code();
            if (BatchTextUtil.isBlank(sigunguKey)) {
                log.warn("❗ Empty sigungu key. Skip row.");
                return null; // 스킵
            }
            Sigungu sigungu = resolveSigungu(sigunguKey);

            return populationToEntity(dto, sigungu);
        };
    }
    @Bean
    public RepositoryItemWriter<Population> populationWriter() {

        return new RepositoryItemWriterBuilder<Population>()
                .repository(populationRepository)
                .methodName("save")
                .build();
    }

}
