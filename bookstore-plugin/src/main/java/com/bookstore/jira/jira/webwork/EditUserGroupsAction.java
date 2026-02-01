package com.bookstore.jira.jira.webwork;

import com.atlassian.crowd.embedded.api.Group;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.security.request.RequestMethod;
import com.atlassian.jira.security.request.SupportedMethods;
import com.atlassian.jira.security.xsrf.RequiresXsrfCheck;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.user.util.UserUtil;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditUserGroupsAction extends JiraWebActionSupport {
    private static final Logger log = LoggerFactory.getLogger(EditUserGroupsAction.class);

    private final PermissionManager permissionManager;
    private final ProjectManager projectManager;
    private final GroupManager groupManager;
    private final UserManager userManager;
    private final UserUtil userUtil;

    private String projectKey;
    private String username;
    private boolean canEdit;

    private Collection<String> currentGroups;
    private Collection<String> allGroups;

    public EditUserGroupsAction(@ComponentImport PermissionManager permissionManager,
                                @ComponentImport ProjectManager projectManager,
                                @ComponentImport GroupManager groupManager,
                                @ComponentImport UserManager userManager,
                                @ComponentImport UserUtil userUtil) {
        this.permissionManager = permissionManager;
        this.projectManager = projectManager;
        this.groupManager = groupManager;
        this.userManager = userManager;
        this.userUtil = userUtil;
    }

    @SupportedMethods({RequestMethod.GET})
    public String doDefault() throws Exception {
        Project project = projectManager.getProjectObjByKey(projectKey);
        if (project == null) {
            addErrorMessage("Project was not found");
            return ERROR;
        }

        ApplicationUser currentUser = getLoggedInUser();

        canEdit = permissionManager.hasPermission(ProjectPermissions.ADMINISTER_PROJECTS, project,
                currentUser);

        allGroups = groupManager.getAllGroupNames();

        ApplicationUser targetUser = userManager.getUserByName(username);
        if (targetUser == null) {
            addErrorMessage("Unknown user: " + username);
            return ERROR;
        }
        currentGroups = groupManager.getGroupNamesForUser(targetUser);

        return SUCCESS;
    }

    @SupportedMethods({RequestMethod.POST})
    @RequiresXsrfCheck
    public String doUpdate() throws Exception {
        final HttpServletRequest req = getHttpRequest();

        final ApplicationUser currentUser = getLoggedInUser();
        final Project project = projectManager.getProjectObjByKey(projectKey);

        if (!permissionManager.hasPermission(ProjectPermissions.ADMINISTER_PROJECTS, project,
                currentUser)) {
            addErrorMessage("You don't have permission to edit groups for this project");
            return ERROR;
        }

        ApplicationUser targetUser = userManager.getUserByName(username);
        if (targetUser == null) {
            addErrorMessage("Unknown user: " + username);
            return ERROR;
        }

        currentGroups = groupManager.getGroupNamesForUser(targetUser);

        String[] selected = req.getParameterValues("groups");
        Set<String> selectedGroups = selected == null
                ? new HashSet<>()
                : new HashSet<>(Arrays.asList(selected));

        Set<String> currentGroups = new HashSet<>(groupManager.getGroupNamesForUser(targetUser));

        Set<String> toAdd = new HashSet<>(selectedGroups);
        toAdd.removeAll(currentGroups);

        Set<String> toRemove = new HashSet<>(currentGroups);
        toRemove.removeAll(selectedGroups);

        for (String g : toAdd) {
            Group group = groupManager.getGroup(g);
            if (group != null) {
                userUtil.addUserToGroup(group, targetUser);
            }
        }
        for (String g : toRemove) {
            Group group = groupManager.getGroup(g);
            if (group != null) {
                userUtil.removeUserFromGroup(group, targetUser);
            }
        }


        return getRedirect("/secure/ProjectUsersAction!default.jspa?projectKey=" + projectKey);
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<String> getCurrentGroups() {
        return currentGroups;
    }

    public Collection<String> getAllGroups() {
        return allGroups;
    }
}
