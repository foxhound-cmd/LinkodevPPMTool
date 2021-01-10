package io.linkodev.ppmtool.services;


import io.linkodev.ppmtool.domain.Backlog;
import io.linkodev.ppmtool.domain.ProjectTask;
import io.linkodev.ppmtool.repositories.BacklogRepository;
import io.linkodev.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);

        projectTask.setBacklog(backlog);
        Integer backlogSequence = backlog.getPTSequence();
        projectTask.setProjectSequence(projectIdentifier + "-" + (++backlogSequence));
        projectTask.setProjectIdentifier(projectIdentifier);

        if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
            projectTask.setPriority(3);
        }

        if (projectTask.getStatus() == null || projectTask.getStatus().equals("")) {
            projectTask.setStatus("TO_DO");
        }

        return projectTaskRepository.save(projectTask);
    }
}