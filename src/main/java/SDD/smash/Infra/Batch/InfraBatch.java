package SDD.smash.Infra.Batch;


import SDD.smash.Address.Entity.Sigungu;
import SDD.smash.Address.Repository.SigunguRepository;
import SDD.smash.Infra.Dto.InfraDTO;
import SDD.smash.Infra.Entity.Industry;
import SDD.smash.Infra.Entity.Infra;
import SDD.smash.Infra.Repository.IndustryRepository;
import SDD.smash.Infra.Repository.InfraRepository;
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

import static SDD.smash.Infra.Converter.InfraConverter.infraToEntity;
import static SDD.smash.Util.BatchTextUtil.*;


@Configuration
@Slf4j
public class InfraBatch {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final IndustryRepository industryRepository;
    private final InfraRepository infraRepository;
    private final SigunguRepository sigunguRepository;

    public InfraBatch(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, IndustryRepository industryRepository, InfraRepository infraRepository, SigunguRepository sigunguRepository) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.industryRepository = industryRepository;
        this.infraRepository = infraRepository;
        this.sigunguRepository = sigunguRepository;
    }

    private Map<String, Sigungu> sigunguCache  = null;
    private Map<String, Industry> industryCache = null;

    private Sigungu resolveSigungu(String sigunguCode) {
        if (sigunguCache == null) {
            sigunguCache = new HashMap<>();
            for (Sigungu sigungu : sigunguRepository.findAll()) {
                sigunguCache.put(sigungu.getSigunguCode(), sigungu);
            }
        }
        Sigungu sigungu = sigunguCache.get(sigunguCode);
        if (sigungu == null) {
            return null; // 혹은 예외 throw
        }

        return sigunguCache.get(sigunguCode);
    }

    private Industry resolveIndustry(String industryCode) {
        if (industryCache == null) {
            industryCache = new HashMap<>();
            for (Industry industry : industryRepository.findAll()) {
                industryCache.put(industry.getCode(), industry);
            }
        }
        Industry industry = industryCache.get(industryCode);
        if (industry == null) {
            log.warn("❗ Unknown industry code: {}", industryCode);
            return null; // 혹은 예외 throw
        }
        return industryCache.get(industryCode);
    }

    @Value("${infra.filePath}")
    private String filePath;

    @Bean
    public Job infraJob(){
        return new JobBuilder("infraJob", jobRepository)
                .start(infraStep())
                .build();
    }

    @Bean
    public Step infraStep() {

        return new StepBuilder("infraStep", jobRepository)
                .<InfraDTO, Infra> chunk(500, platformTransactionManager)
                .reader(infraCsvReader())
                .processor(infraCsvProfessor())
                .writer(infraWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<InfraDTO> infraCsvReader() {

        return new FlatFileItemReaderBuilder<InfraDTO>()
                .name("infraCsvReader")
                .resource(new FileSystemResource(filePath))
                .encoding("MS949")
                .linesToSkip(1)
                .strict(true)
                .delimited()
                .delimiter(",")
                .quoteCharacter('\0')
                .names("sigungu_code", "opnSvcId","num")
                .fieldSetMapper(fieldSet -> {
                    String rawSigunguCode = normalize(fieldSet.readString(0));
                    String rawIndustryCode = normalize(fieldSet.readString(1));
                    String rawInfraName = normalize(fieldSet.readString(2));

                    return new InfraDTO(rawSigunguCode, rawIndustryCode, rawInfraName);
                })
                .build();
    }

    @Bean
    public ItemProcessor<InfraDTO, Infra> infraCsvProfessor(){
        return dto -> {
            String sigunguKey = dto.getSigungu_code();
            String industryCode = dto.getOpenSvcId();
            if (isBlank(sigunguKey)) {
                log.warn("❗ Empty sigungu key. Skip row.");
                return null; // 스킵
            } else if (isBlank(industryCode)) {
                log.warn("❗ Empty industry key. Skip row.");
                return null;
            }
            Sigungu sigungu = resolveSigungu(sigunguKey);
            Industry industry = resolveIndustry(industryCode);

            return infraToEntity(dto, sigungu,industry);
        };
    }
    @Bean
    public RepositoryItemWriter<Infra> infraWriter() {

        return new RepositoryItemWriterBuilder<Infra>()
                .repository(infraRepository)
                .methodName("save")
                .build();
    }

}
