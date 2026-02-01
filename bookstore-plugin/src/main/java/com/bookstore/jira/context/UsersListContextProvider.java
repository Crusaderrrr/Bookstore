package com.bookstore.jira.context;

import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.JiraUrlCodec;
import com.bookstore.jira.service.ProjectUsersService;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class UsersListContextProvider extends AbstractJiraContextProvider {
    private final ProjectUsersService projectUsersService;

    @Inject
    public UsersListContextProvider(ProjectUsersService projectUsersService) {
        this.projectUsersService = projectUsersService;
    }

    @Override
    public Map<String, Object> getContextMap(ApplicationUser user, JiraHelper jiraHelper) {
        Map<String, Object> ctx = new HashMap<>();

        // Try to get the current project from the project-centric context
        Project project = (jiraHelper != null) ? jiraHelper.getProject() : null;

        // Fallback: allow passing ?projectKey=KEY (useful while debugging)
        String projectKey = null;
        if (project != null) {
            projectKey = project.getKey();
        } else if (jiraHelper != null && jiraHelper.getRequest() != null) {
            projectKey = jiraHelper.getRequest().getParameter("projectKey");
            if (projectKey != null) {
                projectKey = JiraUrlCodec.decode(projectKey);
            }
        }

        if (projectKey == null || projectKey.trim().isEmpty()) {
            // Populate minimal values so the template doesn't NPE
            ctx.put("projectKey", "");
            ctx.put("allUsersList", java.util.Collections.emptyList());
            ctx.put("canEdit", false);

            // Optional: show an error banner in Jira UI (works if your decorator renders messages)
            // (No hard dependency on JiraWebActionSupport; it's just a helper for message keys)
            // ctx.put("errors", java.util.Collections.singletonList("Missing project key"));
            return ctx;
        }

        ctx.put("contextPath", jiraHelper.getRequest().getContextPath());

        // Build the model (projectKey, allUsersList, canEdit) used by your VM
        ctx.putAll(projectUsersService.buildModel(projectKey, user));

        return ctx;
    }
}
