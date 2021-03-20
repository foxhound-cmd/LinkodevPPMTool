package io.linkodev.ppmtool.services;

import io.linkodev.ppmtool.domain.Backlog;
import io.linkodev.ppmtool.domain.Project;
import io.linkodev.ppmtool.domain.User;
import io.linkodev.ppmtool.exceptions.ProjectIdException;
import io.linkodev.ppmtool.repositories.BacklogRepository;
import io.linkodev.ppmtool.repositories.ProjectRepository;
import io.linkodev.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username) {


        try {

            User user = userRepository.findByUsername(username);

            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            String projectIdentifier = project.getProjectIdentifier().toUpperCase();


            project.setProjectIdentifier(projectIdentifier);

            if (project.getId() == null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(projectIdentifier);
            } else {
                project.setBacklog(backlogRepository.findByProjectIdentifier(projectIdentifier));
            }


            return projectRepository.save(project);

        } catch (Exception e) {
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase()
                    + "' already exist");
        }
    }

    public Project findProjectByIdentifier(String projectId) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Project ID '" + projectId.toUpperCase() + "' does not exist");
        }
        return project;
    }

    public Iterable<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Cannot delete project with ID '" + projectId.toUpperCase() +
                    "'. This project does not exist");
        }

        projectRepository.delete(project);
    }
}
