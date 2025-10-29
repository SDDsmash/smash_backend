package SDD.smash.Infra.Batch.Runner;

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
public class JobCodeTopBatchRunner {
    private final JobLauncher jobLauncher;
    private final Job jcTopJob;
    private final BatchGuard guard;

    private static final String SEED_VERSION = "v3";

    @Async
    @EventListener(ApplicationEvent.class)
    @Order(1)
    public void runjcTopJobAfterStartup() throws Exception {
        if(guard.alreadyDone("jcTopJob",SEED_VERSION)){
            log.info("jcTopJob already Done");
            return;
        }

        jobLauncher.run(
                jcTopJob,
                new JobParametersBuilder()
                        .addString("seedVersion", SEED_VERSION)
                        .toJobParameters()
        );

    }

}
