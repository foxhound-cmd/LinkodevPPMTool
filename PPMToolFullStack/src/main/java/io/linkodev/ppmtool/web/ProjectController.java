package io.linkodev.ppmtool.web;

import io.linkodev.ppmtool.domain.Project;
import io.linkodev.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("")
    public ResponseEntity<?> createdNewProject(@Valid @RequestBody Project project, BindingResult result) {

        if (result.hasErrors()) {

            Map<String, String> errorMap = new HashMap<>();

            result.getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));

            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }

        Project project1 = projectService.saveOrUpdateProject(project);
        return new ResponseEntity<>(project1, HttpStatus.CREATED);
    }
}
