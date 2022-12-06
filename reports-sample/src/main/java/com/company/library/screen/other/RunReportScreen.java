package com.company.library.screen.other;

import com.company.library.entity.BookPublication;
import com.company.library.entity.LiteratureType;
import com.haulmont.yarg.reporting.ReportOutputDocument;
import io.jmix.core.DataManager;
import io.jmix.localfs.LocalFileStorageProperties;
import io.jmix.reports.ReportImportExport;
import io.jmix.reports.entity.Report;
import io.jmix.reports.entity.ReportOutputType;
import io.jmix.reports.entity.ReportTemplate;
import io.jmix.reports.exception.ReportingException;
import io.jmix.reports.runner.ReportRunContext;
import io.jmix.reports.runner.ReportRunner;
import io.jmix.reports.util.ReportZipUtils;
import io.jmix.reportsui.runner.ParametersDialogShowMode;
import io.jmix.reportsui.runner.UiReportRunContext;
import io.jmix.reportsui.runner.UiReportRunner;
import io.jmix.ui.component.Button;
import io.jmix.ui.download.DownloadFormat;
import io.jmix.ui.download.Downloader;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

@UiController("jmxrpr_RunReportScreen")
@UiDescriptor("run-report-screen.xml")
public class RunReportScreen extends Screen {
    @Autowired
    protected DataManager dataManager;
    // tag::inject-report-runner[]
    @Autowired
    protected ReportRunner reportRunner;

    // end::inject-report-runner[]
// tag::inject-ui-report-runner[]
    @Autowired
    protected UiReportRunner uiReportRunner;

// end::inject-ui-report-runner[]

    @Autowired
    protected ReportZipUtils reportZipUtils;
    @Autowired
    protected Downloader downloader;
    @Autowired
    protected ReportImportExport reportImportExport;
    @Autowired
    protected LocalFileStorageProperties localFileStorageProperties;

    private Report getReportByCode(String code){
        return dataManager.load(Report.class)
                .query("select c from report_Report c where c.code = :code")
                .parameter("code", code)
                .one();
    }
    @Subscribe("rrcBtn1")
    protected void onRrcBtn1Click(Button.ClickEvent event) {
        Report report = getReportByCode("BOOKS");
        LiteratureType type = dataManager.load(LiteratureType.class)
                .query("select c from jmxrpr_LiteratureType c where c.name = :name")
                .parameter("name", "Art")
                .one();
        // tag::report-run-context-v1[]
        ReportRunContext context = new ReportRunContext(report)
                .addParam("type", type)
                .setOutputNamePattern("Books");
        // end::report-run-context-v1[]
        ReportOutputDocument document = reportRunner.run(context);
        List<ReportOutputDocument> documents = new ArrayList<>();
        documents.add(document);
        // tag::report-zip-utils[]
        byte[] zipArchiveContent = reportZipUtils.createZipArchive(documents);
        downloader.download(zipArchiveContent, "Reports.zip", DownloadFormat.ZIP);
        // end::report-zip-utils[]
    }
    // tag::rrc-btn2-start[]
    @Subscribe("rrcBtn2")
    protected void onRrcBtn2Click(Button.ClickEvent event) {
        // end::rrc-btn2-start[]
        Report report = getReportByCode("BOOKS");
        LiteratureType type = dataManager.load(LiteratureType.class)
                .query("select c from jmxrpr_LiteratureType c where c.name = :name")
                .parameter("name", "Art")
                .one();

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("type", type);
        ReportTemplate template = dataManager.load(ReportTemplate.class)
                .query("select c from report_ReportTemplate c where c.code = :code and c.report = :report")
                .parameter("code", "DEFAULT")
                .parameter("report", report)
                .one();
        // tag::report-run-context-v2[]

        ReportRunContext context = new ReportRunContext(report)
                .setReportTemplate(template)
                .setOutputType(ReportOutputType.PDF)
                .setParams(paramsMap);
        // end::report-run-context-v2[]
        // tag::report-runner-v1[]
        ReportOutputDocument document = reportRunner
                .run(new ReportRunContext(report).setParams(paramsMap)); // <1>
        // end::report-runner-v1[]
        //ReportOutputDocument document = reportRunner.run(context);
        // tag::rrc-btn2-end[]
    }
    // end::rrc-btn2-end[]
// tag::rr-btn1-start[]
    @Subscribe("rrBtn1")
    protected void onRrBtn1Click(Button.ClickEvent event) {
        // end::rr-btn1-start[]
        LiteratureType type = dataManager.load(LiteratureType.class)
                .query("select c from jmxrpr_LiteratureType c where c.name = :name")
                .parameter("name", "Art")
                .one();
        // tag::report-runner-v2[]
        ReportOutputDocument document = reportRunner.byReportCode("BOOKS") // <1>
                .addParam("type", type) // <2>
                .withTemplateCode("books-template") // <3>
                .run(); // <4>
        // end::report-runner-v2[]
        // tag::rr-btn1-end[]
    }
// end::rr-btn1-end[]

    // tag::rr-btn2-start[]
    @Subscribe("rrBtn2")
    protected void onRrBtn2Click(Button.ClickEvent event) {
        // end::rr-btn2-start[]
        Report report = getReportByCode("BOOKS");
        LiteratureType type = dataManager.load(LiteratureType.class)
                .query("select c from jmxrpr_LiteratureType c where c.name = :name")
                .parameter("name", "Art")
                .one();
        // tag::report-runner-v3[]
        ReportOutputDocument document = reportRunner.byReportEntity(report)
                .addParam("type", type)
                .withOutputType(ReportOutputType.PDF) // <1>
                .withOutputNamePattern("Books") // <2>
                .run();
        // end::report-runner-v3[]
        // tag::get-content[]
        String documentName = document.getDocumentName();

        byte[] content = document.getContent();
        // end::get-content[]
        // tag::rr-btn2-end[]
    }
// end::rr-btn2-end[]

    @Subscribe("urrBtn1")
    protected void onUrrBtn1Click(Button.ClickEvent event) {
        Report report = getReportByCode("BOOK_COUNT");
        // tag::run-and-show[]
        uiReportRunner.runAndShow(new UiReportRunContext(report));
        // end::run-and-show[]
    }

    @Subscribe("urrBtn2")
    protected void onUrrBtn2Click(Button.ClickEvent event) {
        Report report = getReportByCode("BOOK_COUNT");
        // tag::in-background[]
        uiReportRunner.byReportEntity(report)
                .withParametersDialogShowMode(ParametersDialogShowMode.IF_REQUIRED)
                .inBackground(RunReportScreen.this)
                .runAndShow();
        // end::in-background[]
    }

    @Subscribe("urrBtn3")
    protected void onUrrBtn3Click(Button.ClickEvent event) {
        Report report = getReportByCode("PUBL");
        List<BookPublication> publicationList = dataManager.load(BookPublication.class).all().list();
        // tag::run-multiple-reports[]
        uiReportRunner.byReportEntity(report)
                .withOutputType(ReportOutputType.PDF)
                .withTemplateCode("publication-template")
                .withParametersDialogShowMode(ParametersDialogShowMode.NO)
                .runMultipleReports("entity", publicationList);
        // end::run-multiple-reports[]
    }

    @Subscribe("importBtn")
    protected void onImportBtnClick(Button.ClickEvent event) {
        File tempFile = new File(localFileStorageProperties
                .getStorageDir() + "Book count.zip");
        byte[] bytes;
        try {
            bytes = FileUtils.readFileToByteArray(tempFile);
        } catch (IOException e) {
            throw new ReportingException(format("Error while importing"), e);
        }
        reportImportExport.importReportsWithResult(bytes,null);
    }
}