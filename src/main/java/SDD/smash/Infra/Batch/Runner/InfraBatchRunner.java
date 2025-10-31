package SDD.smash.Infra.Batch.Runner;

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
public class InfraBatchRunner {
    private final JobLauncher jobLauncher;
    private final Job infraJob;
    private final BatchGuard guard;
    private final SeedProperties seedProperties;

    private final String SEED_VERSION;

    public InfraBatchRunner(JobLauncher jobLauncher, @Qualifier("infraJob") Job infraJob,
                            BatchGuard guard, SeedProperties seedProperties) {
        this.jobLauncher = jobLauncher;
        this.infraJob = infraJob;
        this.guard = guard;
        this.seedProperties = seedProperties;
        this.SEED_VERSION = seedProperties.getVersion();
    }

    @Order(7)
    @Async
    @EventListener(ApplicationEvent.class)
    public void runInfraAfterStartup() throws Exception {
        if(guard.alreadyDone("infraJob",SEED_VERSION)){
            return;
        }

        jobLauncher.run(
                infraJob,
                new JobParametersBuilder()
                        .addString("seedVersion", SEED_VERSION)
                        .toJobParameters()
        );

    }
}
