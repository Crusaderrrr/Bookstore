package com.bookstore.jira.service;

import com.atlassian.jira.bc.user.search.UserSearchParams;
import com.atlassian.jira.bc.user.search.UserSearchService;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ProjectUsersService {
    private final PermissionManager permissionManager;
    private final ProjectManager projectManager;
    private final UserSearchService userSearchService;
    private final GroupManager groupManager;

    @Inject
    public ProjectUsersService(@ComponentImport PermissionManager permissionManager,
                               @ComponentImport ProjectManager projectManager,
                               @ComponentImport UserSearchService userSearchService,
                               @ComponentImport GroupManager groupManager) {

        this.permissionManager = permissionManager;
        this.projectManager = projectManager;
        this.userSearchService = userSearchService;
        this.groupManager = groupManager;
    }

    public HashMap<String, Object> buildModel(String projectKey, ApplicationUser currentUser) {
        HashMap<String, Object> context = new HashMap<>();

        context.put("projectKey", projectKey);

        Project project = projectManager.getProjectObjByKey(projectKey);
        List<Map<String, Object>> allUsersList = new ArrayList<>();

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
                row.put("groups", groupNames);
                allUsersList.add(row);
            }
        }

        context.put("allUsersList", allUsersList);

        context.put("canEdit", false);
        if (permissionManager.hasPermission(ProjectPermissions.ADMINISTER_PROJECTS, project,
                currentUser)) {
            context.put("canEdit", true);
        }

        return context;
    }

}