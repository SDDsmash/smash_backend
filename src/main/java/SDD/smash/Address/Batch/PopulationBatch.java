package SDD.smash.Address.Batch;

import SDD.smash.Address.Dto.PopulationDTO;
import SDD.smash.Address.Dto.PopulationUpsertDTO;
import SDD.smash.Address.Entity.Sigungu;
import SDD.smash.Address.Repository.SigunguRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Set;
import java.util.stream.Collectors;

import static SDD.smash.Util.BatchTextUtil.*;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class PopulationBatch {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final SigunguRepository sigunguRepository;
    private final @Qualifier("dataDBSource") DataSource dataDataSource;


    private Set<String> sigunguCodeCache = null;

    private boolean isKnownSigunguCode(String code) {
        if (sigunguCodeCache == null) {
            sigunguCodeCache = sigunguRepository.findAll()
                    .stream()
                    .map(Sigungu::getSigunguCode)
                    .collect(Collectors.toSet());
        }
        return sigunguCodeCache.contains(code);
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
                .<PopulationDTO, PopulationUpsertDTO> chunk(100, platformTransactionManager)
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
    public ItemProcessor<PopulationDTO, PopulationUpsertDTO> populationCsvProcessor(){
        return dto -> {
            String sigunguCode = dto.getSigungu_code();
            if (isBlank(sigunguCode) || !isKnownSigunguCode(sigunguCode)) return null;
            return PopulationUpsertDTO.builder()
                    .sigunguCode(sigunguCode)
                    .population(dto.getPopulation())
                    .build();
        };
    }
    @Bean
    public JdbcBatchItemWriter<PopulationUpsertDTO> populationWriter() {
        String upsertSql = """
        INSERT INTO Population (sigungu_code, population_count)
        VALUES (:sigunguCode, :population)
        ON DUPLICATE KEY UPDATE population_count = VALUES(population_count)
            """;

        return new JdbcBatchItemWriterBuilder<PopulationUpsertDTO>()
                .dataSource(dataDataSource)
                .sql(upsertSql)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .assertUpdates(false)
                .build();
    }

}
