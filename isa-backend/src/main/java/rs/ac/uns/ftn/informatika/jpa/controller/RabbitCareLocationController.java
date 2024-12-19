package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.jpa.model.RabbitCareLocation;
import rs.ac.uns.ftn.informatika.jpa.service.RabbitCareLocationService;

import java.util.List;

@RestController
@RequestMapping("/api/careLocations")
public class RabbitCareLocationController {

    private final RabbitCareLocationService service;

    public RabbitCareLocationController(RabbitCareLocationService service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<List<RabbitCareLocation>> getAllLocations() {
        List<RabbitCareLocation> locations = service.getAllLocations();
        return ResponseEntity.ok(locations);
    }
}
