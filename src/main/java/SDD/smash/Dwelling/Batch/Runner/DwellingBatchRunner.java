package SDD.smash.Dwelling.Batch.Runner;

import SDD.smash.Config.SeedProperties;
import SDD.smash.Exception.Exception.BusinessException;
import SDD.smash.Util.BatchGuard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class DwellingBatchRunner {
    private final Job dwellingJob;
    private final JobLauncher jobLauncher;
    private final BatchGuard guard;
    private final SeedProperties seedProperties;

    private final String SEED_VERSION;

    public DwellingBatchRunner(@Qualifier("dwellingJob") Job dwellingJob, JobLauncher jobLauncher,
                               BatchGuard guard, SeedProperties seedProperties) {
        this.dwellingJob = dwellingJob;
        this.jobLauncher = jobLauncher;
        this.guard = guard;
        this.seedProperties = seedProperties;
        this.SEED_VERSION = seedProperties.getVersion();
    }

    @Order(10)
    @EventListener(ApplicationReadyEvent.class)
    public void runOnceAfterStartup() throws Exception {
        try{

            if(guard.alreadyDone("dwellingJob",SEED_VERSION)){
                log.info("Already dwellingJob : " + SEED_VERSION );
                return;
            }

            String dealYmd = "202509";
            long months = 12L;
            String codes = null;

            JobParameters params = new JobParametersBuilder()
                    .addString("dealYmd", dealYmd)
                    .addLong("months", months)
                    .addString("seedVersion", SEED_VERSION)
                    .addLong("triggerTime", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution exec = jobLauncher.run(dwellingJob, params);
            log.info("dwellingJob started: id={}, params={}", exec.getId(), params);
        } catch (Exception e){
            log.error("Failed to run dwellingJob: {}", e.getMessage(), e);
        }
    }
}
