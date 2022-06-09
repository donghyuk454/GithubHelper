package edu.skku.cs.pa3.model.github;

import java.util.List;

import edu.skku.cs.pa3.model.github.project.Project;

public class GithubInfo {
    private String html_url;
    private String avatar_url;
    private String name;
    private List<Project> projects;

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
