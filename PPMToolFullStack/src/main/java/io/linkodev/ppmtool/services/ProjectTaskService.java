package io.linkodev.ppmtool.services;


import io.linkodev.ppmtool.domain.Backlog;
import io.linkodev.ppmtool.domain.Project;
import io.linkodev.ppmtool.domain.ProjectTask;
import io.linkodev.ppmtool.exceptions.ProjectNotFoundException;
import io.linkodev.ppmtool.repositories.BacklogRepository;
import io.linkodev.ppmtool.repositories.ProjectRepository;
import io.linkodev.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
        try {

            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);

            projectTask.setBacklog(backlog);
            Integer backlogSequence = backlog.getPTSequence();
            projectTask.setProjectSequence(projectIdentifier + "-" + (++backlogSequence));
            projectTask.setProjectIdentifier(projectIdentifier);
            backlog.setPTSequence(backlogSequence);

            if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
                projectTask.setPriority(3);
            }

            if (projectTask.getStatus() == null || projectTask.getStatus().equals("")) {
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);
        } catch (Exception e) {
            throw new ProjectNotFoundException("Project not Found with identifier " + projectIdentifier.toUpperCase());
        }
    }

     public Iterable<ProjectTask> findBacklogById(String backlog_id) {

        Project project = projectRepository.findByProjectIdentifier(backlog_id);
        if (project == null) {
            throw new ProjectNotFoundException("Project with ID : '" + backlog_id.toUpperCase() + "'does not exist");
        }

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
    }
}
