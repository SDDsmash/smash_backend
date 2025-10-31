package SDD.smash.Job.Batch.Runner;

import SDD.smash.Config.SeedProperties;
import SDD.smash.Util.BatchGuard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobCodeTopBatchRunner {
    private final JobLauncher jobLauncher;
    private final Job jcTopJob;
    private final BatchGuard guard;
    private final SeedProperties seedProperties;

    private final String SEED_VERSION;

    public JobCodeTopBatchRunner(JobLauncher jobLauncher, @Qualifier("jcTopJob") Job jcTopJob,
                                 BatchGuard guard, SeedProperties seedProperties) {
        this.jobLauncher = jobLauncher;
        this.jcTopJob = jcTopJob;
        this.guard = guard;
        this.seedProperties = seedProperties;
        this.SEED_VERSION = seedProperties.getVersion();
    }

    @Async
    @EventListener(ApplicationEvent.class)
    @Order(3)
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
