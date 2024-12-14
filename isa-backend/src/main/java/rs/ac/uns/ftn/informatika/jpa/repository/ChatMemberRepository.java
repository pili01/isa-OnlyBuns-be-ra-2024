package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.informatika.jpa.model.Chat;
import rs.ac.uns.ftn.informatika.jpa.model.ChatMember;

import java.util.Optional;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Integer> {

    @Query("SELECT cm FROM ChatMember cm WHERE cm.chat.id = :chatId AND cm.user.id=:userId")
    Optional<ChatMember> findByChatIdAndUserId(int chatId, int userId);
}
