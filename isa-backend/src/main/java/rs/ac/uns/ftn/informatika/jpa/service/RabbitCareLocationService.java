package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.model.RabbitCareLocation;
import rs.ac.uns.ftn.informatika.jpa.repository.RabbitCareLocationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RabbitCareLocationService {

    private final RabbitCareLocationRepository repository;

    public RabbitCareLocationService(RabbitCareLocationRepository repository) {
        this.repository = repository;
    }

    public RabbitCareLocation saveLocation(RabbitCareLocation location) {
        return repository.save(location);
    }

    public Optional<RabbitCareLocation> getLocationById(String id) {
        return repository.findById(id);
    }

    public List<RabbitCareLocation> getAllLocations() {
        return repository.findAll();
    }

    public void deleteLocationById(String id) {
        repository.deleteById(id);
    }
}
