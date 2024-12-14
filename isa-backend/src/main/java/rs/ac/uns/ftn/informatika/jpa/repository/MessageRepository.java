package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.informatika.jpa.model.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId AND m.timestamp < :joinedAt " +
            "ORDER BY m.timestamp DESC")
    Page<Message> findLastMessagesBeforeJoin(@Param("chatId") int chatId,
                                             @Param("joinedAt") LocalDateTime joinedAt,
                                             org.springframework.data.domain.Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId AND m.timestamp >= :joinedAt " +
            "ORDER BY m.timestamp DESC")
    Set<Message> findMessagesAfterJoin(@Param("chatId") int chatId,
                                             @Param("joinedAt") LocalDateTime joinedAt);

}
