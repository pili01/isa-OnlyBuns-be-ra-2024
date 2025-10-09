package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.informatika.jpa.model.Location;
import rs.ac.uns.ftn.informatika.jpa.model.Post;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    @Query("select l from Location l where l.id in (:locationIds)")
    List<Location> findAllPostLocations(List<Integer> locationIds);

    @Query("select l from Location l where l.id = :locationId")
    Location findUserLocation(Integer locationId);
}
