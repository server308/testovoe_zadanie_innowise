package com.example.testvoe_zadanie.Contorllers;

import com.example.testvoe_zadanie.DTO.ManagerDTO;
import com.example.testvoe_zadanie.Services.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping
    public ResponseEntity<ManagerDTO> createManager(@RequestBody ManagerDTO managerDTO) {
        ManagerDTO createdManager = managerService.createManager(managerDTO);
        return new ResponseEntity<>(createdManager, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ManagerDTO>> getAllManagers() {
        List<ManagerDTO> managers = managerService.getAllManagers();
        return new ResponseEntity<>(managers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagerDTO> getManagerById(@PathVariable Long id) {
        ManagerDTO manager = managerService.getManagerById(id);
        return new ResponseEntity<>(manager, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManagerDTO> updateManager(@PathVariable Long id, @RequestBody ManagerDTO managerDTO) {
        ManagerDTO updatedManager = managerService.updateManager(id, managerDTO);
        return new ResponseEntity<>(updatedManager, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManager(@PathVariable Long id) {
        managerService.deleteManager(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
