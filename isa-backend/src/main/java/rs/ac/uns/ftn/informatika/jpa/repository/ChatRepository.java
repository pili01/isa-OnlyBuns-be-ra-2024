package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.informatika.jpa.model.Chat;
import rs.ac.uns.ftn.informatika.jpa.model.User;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat,Integer> {

    Optional<Chat> findByCode(String code);
}
