package SDD.smash.Job.Repository;

import SDD.smash.Job.Entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
