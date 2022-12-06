package com.company.library.app;

import io.jmix.reports.ReportExecutionHistoryRecorder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// tag::quartz[]
@Component("jmxrpr_ReportHistoryCleaner")
public class ReportHistoryCleanJob implements Job {
    @Autowired
    protected ReportExecutionHistoryRecorder reportExecutionHistoryRecorder;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        reportExecutionHistoryRecorder.cleanupHistory();
    }
}
// end::quartz[]