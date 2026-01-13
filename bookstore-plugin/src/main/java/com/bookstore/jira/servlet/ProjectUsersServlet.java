package com.bookstore.jira.servlet;

import com.atlassian.crowd.embedded.api.CrowdService;
import com.atlassian.crowd.embedded.api.Group;
import com.atlassian.crowd.embedded.api.User;
import com.atlassian.crowd.exception.OperationNotPermittedException;
import com.atlassian.jira.bc.user.search.UserSearchParams;
import com.atlassian.jira.bc.user.search.UserSearchService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.templaterenderer.TemplateRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Named
public class ProjectUsersServlet extends HttpServlet {

    private final TemplateRenderer templateRenderer;
    private final PermissionManager permissionManager;
    private final ProjectManager projectManager;
    private final UserSearchService userSearchService;

    @Inject
    public ProjectUsersServlet(@ComponentImport TemplateRenderer templateRenderer,
                               @ComponentImport PermissionManager permissionManager,
                               @ComponentImport ProjectManager projectManager,
                               @ComponentImport UserSearchService userSearchService) {
        this.templateRenderer = templateRenderer;
        this.permissionManager = permissionManager;
        this.projectManager = projectManager;
        this.userSearchService = userSearchService;
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

        Project project = projectManager.getProjectObjByKey(projectKey);
        if (project == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setContentType("text/plain;charset=utf-8");
            resp.getWriter().write("Unknown projectKey: " + projectKey);
            return;
        }

        ApplicationUser currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        boolean canEdit = currentUser != null
                && permissionManager.hasPermission(ProjectPermissions.ADMINISTER_PROJECTS, project, currentUser);

        int limit = 200;
        UserSearchParams params = UserSearchParams.builder()
                .allowEmptyQuery(true)
                .includeActive(true)
                .includeInactive(false)
                .maxResults(limit)
                .build();

        List<ApplicationUser> possibleUsers = userSearchService.findUsers("", params);

        List<Map<String, Object>> browseUsers = new ArrayList<>();
        GroupManager  groupManager = ComponentAccessor.getGroupManager();

        Collection<Group> allGroupObjects = groupManager.getAllGroups();
        List<String> allGroups = new ArrayList<>();
        for (Group group : allGroupObjects) {
            allGroups.add(group.getName());
        }

        for (ApplicationUser u : possibleUsers) {
            if (permissionManager.hasPermission(ProjectPermissions.BROWSE_PROJECTS, project, u)) {
                Collection<String> groupNames = groupManager.getGroupNamesForUser(u);

                HashMap<String, Object> row = new HashMap<>();
                row.put("displayName",  u.getDisplayName());
                row.put("groups", groupNames);
                row.put("username",  u.getUsername());
                browseUsers.add(row);
            }
        }


        Map<String, Object> context = new HashMap<>();
        context.put("projectKey", project.getKey());
        context.put("projectName", project.getName());
        context.put("allUsersList", browseUsers);
        context.put("allGroups", allGroups);
        context.put("canEdit", canEdit);


        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=utf-8");
        templateRenderer.render("templates/project-users.vm", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ApplicationUser currentUser =
                ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();

        String projectKey = req.getParameter("projectKey");
        if (projectKey == null || projectKey.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing projectKey");
            return;
        }

        Project project = projectManager.getProjectObjByKey(projectKey);
        if (project == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown projectKey: " + projectKey);
            return;
        }

        if (currentUser == null ||
                !permissionManager.hasPermission(ProjectPermissions.ADMINISTER_PROJECTS, project,
                        currentUser)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "You are not allowed to edit project permissions");
            return;
        }

        String[] userNames = req.getParameterValues("user");
        if (userNames == null || userNames.length == 0) {
            resp.sendRedirect(req.getContextPath()
                    + "/plugins/servlet/projectusersservlet?projectKey=" + projectKey);
            return;
        }

        GroupManager groupManager = ComponentAccessor.getGroupManager();
        CrowdService crowdService = ComponentAccessor.getCrowdService();

        for (String username : userNames) {
            ApplicationUser targetUser = ComponentAccessor.getUserManager().getUserByName(username);
            if (targetUser == null) {
                continue;
            }

            User crowdUser = crowdService.getUser(targetUser.getName());


            String paramName = "group_" + username;
            String[] selectedGroups = req.getParameterValues(paramName);

            Set<String> selected = new HashSet<>();
            if (selectedGroups != null) {
                selected.addAll(Arrays.asList(selectedGroups));
            }
            Collection<String> currentGroups = groupManager.getGroupNamesForUser(targetUser);

            for (String current : currentGroups) {
                if (!selected.contains(current)) {
                    Group crowdGroup = crowdService.getGroup(current);
                    if (crowdGroup != null) {
                        try {
                            crowdService.removeUserFromGroup(crowdUser, crowdGroup);
                        } catch (OperationNotPermittedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            for (String desired : selected) {
                if (!currentGroups.contains(desired)) {
                    Group crowdGroup = crowdService.getGroup(desired);
                    if (crowdGroup != null) {
                        try {
                            crowdService.addUserToGroup(crowdUser, crowdGroup);
                        } catch (OperationNotPermittedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        resp.sendRedirect(req.getContextPath()
                + "/plugins/servlet/projectusersservlet?projectKey=" + projectKey);
    }
}