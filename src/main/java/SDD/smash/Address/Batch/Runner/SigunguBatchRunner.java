package SDD.smash.Address.Batch.Runner;

import SDD.smash.Config.SeedProperties;
import SDD.smash.Util.BatchGuard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SigunguBatchRunner {
    private final JobLauncher jobLauncher;
    private final Job SigunguJob;
    private final BatchGuard guard;
    private final SeedProperties seedProperties;

    private final String SEED_VERSION = seedProperties.getVersion();

    @Order(2)
    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void runSigunguJobAfterStartup() throws Exception{
        if(guard.alreadyDone("SigunguJob",SEED_VERSION)){
            log.info("SidoJob has already done");
            return;
        }

        jobLauncher.run(
                SigunguJob,
                new JobParametersBuilder()
                        .addString("seedVersion", SEED_VERSION)
                        .toJobParameters()
        );
    }
}
