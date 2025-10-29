package SDD.smash.Address.Batch.Runner;

import SDD.smash.Util.BatchGuard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SidoBatchRunner {
    private final JobLauncher jobLauncher;
    private final Job SidoJob;
    private final BatchGuard guard;

    private static final String SEED_VERSION = "v1";

    @Async
    @EventListener(ApplicationEvent.class)
    public void runSidoJobAfterStartup() throws Exception {
        if(guard.alreadyDone("SidoJob",SEED_VERSION)){
            log.info("SidoJob already completed for seedVersion = " + SEED_VERSION);
            return;
        }

        jobLauncher.run(
                SidoJob,
                new JobParametersBuilder()
                        .addString("seedVersion", SEED_VERSION)
                        .toJobParameters()
        );
        log.info("SidoBatch Job 실행 완료");

    }
}
