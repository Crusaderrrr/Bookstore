package com.bookstore.jira.jira.webwork;

import com.atlassian.jira.bc.user.search.UserSearchParams;
import com.atlassian.jira.bc.user.search.UserSearchService;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import webwork.action.ServletResponseAware;

public class DownloadUsersPdfAction extends JiraWebActionSupport
        implements ServletResponseAware {

    private HttpServletResponse response;
    private InputStream pdfStream;
    private final UserSearchService userSearchService;
    private final PermissionManager permissionManager;

    public DownloadUsersPdfAction(@ComponentImport UserSearchService userSearchService,
                                  @ComponentImport PermissionManager permissionManager) {
        this.userSearchService = userSearchService;
        this.permissionManager = permissionManager;
    }


    @Override
    public String doExecute() throws Exception {

        UserSearchParams params = UserSearchParams.builder()
                .allowEmptyQuery(true)
                .includeActive(true)
                .includeInactive(true)
                .sorted(true)
                .build();

        List<ApplicationUser> users = userSearchService.findUsers("", params);

        byte[] pdfBytes = buildPdf(users);

        this.pdfStream = new ByteArrayInputStream(pdfBytes);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"users.pdf\"");
        return "download";
    }

    private byte[] buildPdf(List<ApplicationUser> users) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document doc = new Document();
        PdfWriter.getInstance(doc, baos);
        doc.open();

        PdfPTable table = new PdfPTable(new float[] {4f, 6f, 6f});
        table.setWidthPercentage(100f); // fill available page width

        // Header row (simple version)
        table.addCell("Username");
        table.addCell("Display Name");
        table.addCell("Email");
        table.setHeaderRows(1); // repeat header if table spans pages

        for (ApplicationUser u : users) {
            table.addCell(nullSafe(u.getName()));
            table.addCell(nullSafe(u.getDisplayName()));
            table.addCell(nullSafe(u.getEmailAddress()));
        }

        doc.add(table);

        doc.close(); // also closes writer
        return baos.toByteArray();
    }

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }

    public InputStream getPdfStream() {
        return pdfStream;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }
}
