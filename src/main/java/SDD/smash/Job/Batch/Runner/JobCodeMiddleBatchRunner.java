package SDD.smash.Job.Batch.Runner;

import SDD.smash.Config.SeedProperties;
import SDD.smash.Util.BatchGuard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobCodeMiddleBatchRunner {
    private final JobLauncher jobLauncher;
    private final Job jcMiddleJob;
    private final BatchGuard guard;
    private final SeedProperties seedProperties;

    private final String SEED_VERSION = seedProperties.getVersion();

    @Async
    @EventListener(ApplicationEvent.class)
    @Order(4)
    public void runjcToMiddleJobAfterStartup() throws Exception {
        if(guard.alreadyDone("jcMiddleJob",SEED_VERSION)){
            log.info("jcMiddleJob already Done");
            return;
        }

        jobLauncher.run(
                jcMiddleJob,
                new JobParametersBuilder()
                        .addString("seedVersion", SEED_VERSION)
                        .toJobParameters()
        );

    }

}
