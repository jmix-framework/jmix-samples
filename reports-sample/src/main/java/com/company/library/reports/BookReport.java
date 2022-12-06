package com.company.library.reports;

import com.company.library.entity.Book;
import com.haulmont.yarg.formatters.CustomReport;
import com.haulmont.yarg.structure.BandData;
import com.haulmont.yarg.structure.Report;

import java.util.Map;

public class BookReport implements CustomReport {
    @Override
    public byte[] createReport(Report report, BandData rootBand, Map<String, Object> params) {
        Book book = (Book) params.get("book");
        String html = "<html><body>";
        html += "<p>Name: " + book.getName() + "</p>";
        html += "</body></html>";
        return html.getBytes();
    }
}
