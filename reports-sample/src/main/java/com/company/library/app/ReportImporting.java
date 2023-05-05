package com.company.library.app;

import com.company.library.entity.InitFlags;
import io.jmix.core.DataManager;
import io.jmix.core.Resources;
import io.jmix.core.security.Authenticated;
import io.jmix.reports.ReportImportExport;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.io.IOException;
import java.io.InputStream;

@Component
public class ReportImporting {

    private static final Logger log = LoggerFactory.getLogger(ReportImporting.class);

    @Autowired
    protected ReportImportExport reportImportExport;
    @Autowired
    private Resources resources;
    @Autowired
    private DataManager dataManager;

    private final static String REPORT_LOCATION = "com/company/library/reports/";

    @EventListener
    @Transactional
    @Authenticated
    public void importReports(ApplicationStartedEvent event) {
        InitFlags initFlags = dataManager.load(InitFlags.class)
                .id(1)
                .lockMode(LockModeType.PESSIMISTIC_WRITE)
                .optional()
                .orElseGet(() -> {
                    InitFlags entity = dataManager.create(InitFlags.class);
                    entity.setId(1);
                    return entity;
                });

        if (!Boolean.TRUE.equals(initFlags.getReportsInitialized())) {
            importReport("Book count.zip");
            importReport("Publication details.zip");
            importReport("Report for entity Book.zip");
            importReport("Book items location.zip");
            importReport("Recently added book items.zip");
            importReport("Book Record.zip");
            importReport("Publications grouped by types and books.zip");

            initFlags.setReportsInitialized(true);
            dataManager.save(initFlags);
        }
    }

    private void importReport(String reportFileName) {
        String location = REPORT_LOCATION + reportFileName;
        log.info("Initializing report from " + location);
        try (InputStream stream = resources.getResourceAsStream(location)) {
            if (stream != null) {
                reportImportExport.importReports(IOUtils.toByteArray(stream));
            } else {
                log.info("Not found: " + location);
            }
        } catch (IOException e) {
            log.error("Unable to initialize reports", e);
        }
    }
}
