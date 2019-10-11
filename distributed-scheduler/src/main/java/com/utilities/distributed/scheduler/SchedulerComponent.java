package com.utilities.distributed.scheduler;

import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile("shedlock")
public class SchedulerComponent {

    @Scheduled(cron = "0/2 * * * * *")
    @SchedulerLock(name = "TaskScheduler_scheduledTask",
            lockAtLeastForString = "PT5M", lockAtMostForString = "PT14M")
    public void scheduledTask() {
        System.out.println("Running scheduledTask");
    }

    //lockAtLeastForString = "PT5M", this means this will run after every 5 mins as it keeps the lock for 5 mins
    //lockAtMostForString = "PT14M",  this means it will hold the lock for 14 mins
}
