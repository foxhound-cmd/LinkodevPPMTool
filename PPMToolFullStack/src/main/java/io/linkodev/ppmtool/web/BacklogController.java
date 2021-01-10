package io.linkodev.ppmtool.web;


import io.linkodev.ppmtool.domain.ProjectTask;
import io.linkodev.ppmtool.services.MapValidationErrorService;
import io.linkodev.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {


    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask,
                                            BindingResult result, @PathVariable String backlog_id) {
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if (errorMap != null) {
            return errorMap;
        }

        ProjectTask projectTaskCRUD = projectTaskService.addProjectTask(backlog_id, projectTask);

        return new ResponseEntity<>(projectTaskCRUD, HttpStatus.CREATED);
    }
}