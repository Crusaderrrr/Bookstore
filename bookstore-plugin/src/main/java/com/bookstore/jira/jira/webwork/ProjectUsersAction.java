package com.bookstore.jira.jira.webwork;

import com.atlassian.jira.bc.user.search.UserSearchParams;
import com.atlassian.jira.bc.user.search.UserSearchService;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.security.request.RequestMethod;
import com.atlassian.jira.security.request.SupportedMethods;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ProjectUsersAction extends JiraWebActionSupport {

    private final PermissionManager permissionManager;
    private final ProjectManager projectManager;
    private final UserSearchService userSearchService;
    private final GroupManager groupManager;

    private String projectKey;

    private List<Map<String, Object>> allUsersList = new ArrayList<>();
    private boolean canEdit = false;

    @Inject
    public ProjectUsersAction(
            @ComponentImport PermissionManager permissionManager,
            @ComponentImport ProjectManager projectManager,
            @ComponentImport UserSearchService userSearchService,
            @ComponentImport GroupManager groupManager) {
        this.permissionManager = permissionManager;
        this.projectManager = projectManager;
        this.userSearchService = userSearchService;
        this.groupManager = groupManager;
    }

    @Override
    @SupportedMethods({RequestMethod.GET})
    public String doDefault() throws Exception {
        if (projectKey == null || projectKey.trim().isEmpty()) {
            addErrorMessage("Missing project key");
            return ERROR;
        }

        Project project = projectManager.getProjectObjByKey(projectKey);
        if (project == null) {
            addErrorMessage("Unknown project key: " + projectKey);
            return ERROR;
        }

        ApplicationUser currentUser = getLoggedInUser();

        canEdit = permissionManager.hasPermission(
                ProjectPermissions.ADMINISTER_PROJECTS,
                project,
                currentUser
        );

        UserSearchParams params = UserSearchParams.builder()
                .allowEmptyQuery(true)
                .includeActive(true)
                .includeInactive(false)
                .build();

        List<ApplicationUser> possibleUsers = userSearchService.findUsers("", params);

        for (ApplicationUser u : possibleUsers) {
            if (permissionManager.hasPermission(ProjectPermissions.BROWSE_PROJECTS, project, u)) {
                Collection<String> groupNames = groupManager.getGroupNamesForUser(u);

                HashMap<String, Object> row = new HashMap<>();
                row.put("name", u.getName());
                row.put("displayName", u.getDisplayName());
                row.put("emailAddress", u.getEmailAddress());
                row.put("groups", groupNames);
                allUsersList.add(row);
            }
        }

        return SUCCESS;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public List<Map<String, Object>> getAllUsersList() {
        return allUsersList;
    }

    public boolean isCanEdit() {
        return canEdit;
    }
}
