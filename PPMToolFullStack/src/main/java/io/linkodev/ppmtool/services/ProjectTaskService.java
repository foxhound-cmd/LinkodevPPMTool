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
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

        Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();

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

    }

    public Iterable<ProjectTask> findBacklogById(String backlog_id, String username) {

        projectService.findProjectByIdentifier(backlog_id, username);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username) {

       projectService.findProjectByIdentifier(backlog_id, username);

        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if (projectTask == null) {
            throw new ProjectNotFoundException("Project Task '" + pt_id
                    + " 'does not exist");
        }
        if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
            throw new ProjectNotFoundException("Project Task '" + pt_id
                    + " 'does not exist in project : " + backlog_id.toUpperCase());
        }
        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username) {

        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);

    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id, String username) {
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
        projectTaskRepository.delete(projectTask);
    }
}
