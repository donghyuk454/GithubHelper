package edu.skku.cs.pa3.model.user;

import java.util.List;

import edu.skku.cs.pa3.model.BaseModel;
import edu.skku.cs.pa3.model.github.GithubInfo;
import edu.skku.cs.pa3.model.github.project.Project;

public class UserModel extends BaseModel {
    private String user_id;
    private String github_id;
    private String[] friends;
    private GithubInfo github;
    private List<Project> projects;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGithub_id() {
        return github_id;
    }

    public void setGithub_id(String github_id) {
        this.github_id = github_id;
    }

    public String[] getFriends() {
        return friends;
    }

    public void setFriends(String[] friends) {
        this.friends = friends;
    }

    public GithubInfo getGithub() {
        return github;
    }

    public void setGithub(GithubInfo github) {
        this.github = github;
    }

    @Override
    public boolean isSuccess() {
        return super.isSuccess();
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
