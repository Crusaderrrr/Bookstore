package com.bookstore.jira.servlet;

import com.atlassian.jira.bc.user.search.UserSearchParams;
import com.atlassian.jira.bc.user.search.UserSearchService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.templaterenderer.TemplateRenderer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Named
public class ProjectUsersServlet extends HttpServlet {

    private final TemplateRenderer templateRenderer;

    @Inject
    public ProjectUsersServlet(@ComponentImport TemplateRenderer templateRenderer) {
        this.templateRenderer = templateRenderer;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String projectKey = req.getParameter("projectKey");
        if (projectKey == null || projectKey.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/plain;charset=utf-8");
            resp.getWriter().write("Missing projectKey");
            return;
        }

        Project project = ComponentAccessor.getProjectManager().getProjectObjByKey(projectKey);
        if (project == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setContentType("text/plain;charset=utf-8");
            resp.getWriter().write("Unknown projectKey: " + projectKey);
            return;
        }

        PermissionManager permissionManager = ComponentAccessor.getPermissionManager();
        UserSearchService userSearchService =
                ComponentAccessor.getComponent(UserSearchService.class);

        int limit = 200;
        UserSearchParams params = UserSearchParams.builder()
                .allowEmptyQuery(true)
                .includeActive(true)
                .includeInactive(false)
                .maxResults(limit)
                .build();

        List<ApplicationUser> possibleUsers = userSearchService.findUsers("", params);

        List<String> browseUsers = new ArrayList<>();
        for (ApplicationUser u : possibleUsers) {
            if (permissionManager.hasPermission(ProjectPermissions.BROWSE_PROJECTS, project, u)) {
                browseUsers.add(u.getName());
            }
        }

        Collections.sort(browseUsers);

        Map<String, Object> context = new HashMap<>();
        context.put("projectKey", project.getKey());
        context.put("projectName", project.getName());
        context.put("allUsersList", browseUsers);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=utf-8");
        templateRenderer.render("templates/project-users.vm", context, resp.getWriter());
    }
}
