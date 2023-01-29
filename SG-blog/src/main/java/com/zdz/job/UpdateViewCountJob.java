package com.zdz.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UpdateViewCountJob {

    @Scheduled(cron = "0/5 * * * * ?")
    public void updateViewCount(){

    }
}
