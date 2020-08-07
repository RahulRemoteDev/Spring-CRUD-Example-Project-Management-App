package com.mihneacristian.project_tracker.Controller;

import com.mihneacristian.project_tracker.DTO.ProjectDTO;
import com.mihneacristian.project_tracker.Entities.Project;
import com.mihneacristian.project_tracker.Repositories.ProjectRepository;
import com.mihneacristian.project_tracker.Services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects/v1")
public class ProjectController {

    ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Autowired
    ProjectService projectService;

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Integer projectId) {

        Project projectById = projectService.getProjectById(projectId);
        if (projectById != null) {
            Project projectEntity = projectById;
            ProjectDTO projectDTO = new ProjectDTO(projectEntity);
            return new ResponseEntity<ProjectDTO>(projectDTO, HttpStatus.OK);

            //todo catch exception here.
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No project with the id " + projectId + " was found.");
        }
    }

    @GetMapping(value = "/projects", produces = "application/json")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {

        List<ProjectDTO> allProjects = projectService.getAllProjects();
        return new ResponseEntity<List<ProjectDTO>>(allProjects, HttpStatus.OK);
    }

    @PostMapping(value = "/add-project", consumes = "application/json")
    public ResponseEntity<Project> createProject(@RequestBody ProjectDTO projectDTO) {

        Project project = projectService.saveProject(projectDTO);
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }

    @PutMapping(value = "/project/{projectId}", consumes = "application/json")
    public ResponseEntity<Project> updateProjectById(@PathVariable(name = "projectId") Integer projectId, @RequestBody ProjectDTO projectDTO) {

        Project p = projectService.getProjectById(projectId);
        if (p != null) {
            Project project = projectService.updateProjectById(projectId, projectDTO);//todo save
            return new ResponseEntity<Project>(project, HttpStatus.OK);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find project with the id: " + projectId);
        }
    }

    @DeleteMapping("/project/{projectId}")
    public void deleteProjectById(@PathVariable Integer projectId) {

        if (projectService.isProjectIdPresent(projectId)) {
            projectService.deleteProjectById(projectId);

            //TODO DO NOT THROW EXCEPTION DIRECTLY. RETURN AN EXCEPTION DTO
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No project with the id " + projectId + " was found.");
        }
    }
}
