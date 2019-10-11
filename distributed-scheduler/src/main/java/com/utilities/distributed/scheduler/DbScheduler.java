package com.utilities.distributed.scheduler;

import com.github.kagkarlsson.scheduler.task.Task;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import com.github.kagkarlsson.scheduler.task.schedule.FixedDelay;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
@Profile("dbscheduler")
public class DbScheduler {
    private final AtomicLong count = new AtomicLong(0L);
    DataSource dataSource;

    /*private void scheduler(){
        RecurringTask<Void> hourlyTask = Tasks.recurring("my-sec-task", FixedDelay.ofSeconds(2))
                .execute((inst, ctx) -> {
                    System.out.println("Executed!");
                });

        final Scheduler schedule = Scheduler
                .create(dataSource)
                .startTasks(hourlyTask)
                .threads(5)
                .build();

        // hourlyTask is automatically scheduled on startup if not already started (i.e. exists in the db)
        schedule.start();
    }*/


    @Bean
    Task<Void> recurringSampleTask() {
        return Tasks
                .recurring("recurring-sample-task", FixedDelay.ofSeconds(2))
                .execute((instance, ctx) -> {
                    //log.info("Running recurring-simple-task. Instance: {}, ctx: {}", instance, ctx);
                    //counter.increase();
                    System.out.println("Executed!");
                    count.incrementAndGet();
                });
    }

/*
* Script to be executed for db-scheduler
*
* create table scheduled_tasks (
                                 task_name text not null,
                                 task_instance text not null,
                                 task_data bytea,
                                 execution_time timestamp with time zone not null,
                                 picked BOOLEAN not null,
                                 picked_by text,
                                 last_success timestamp with time zone,
                                 last_failure timestamp with time zone,
                                 consecutive_failures INT,
                                 last_heartbeat timestamp with time zone,
                                 version BIGINT not null,
                                 PRIMARY KEY (task_name, task_instance)
);
* */

}
