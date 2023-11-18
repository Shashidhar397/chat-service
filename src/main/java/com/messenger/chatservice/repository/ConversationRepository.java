package com.messenger.chatservice.repository;

import com.messenger.chatservice.models.entity.Conversation;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author shashidhar
 */
@Repository
public interface ConversationRepository extends CassandraRepository<Conversation, String> {
    @Query("SELECT * FROM conversation WHERE users_involved CONTAINS :userUuid allow filtering")
    List<Conversation> findAllByUsersInvolvedContains(String userUuid);

    @Query("SELECT * FROM conversation WHERE uuid = :uuid")
    Conversation findByUuid(UUID uuid);

}
