package com.messenger.chatservice.repository;

import com.messenger.chatservice.models.entity.Message;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author shashidhar
 */
@Repository
public interface MessagesRepository extends CassandraRepository<Message, String>{

    @Query("SELECT * FROM message WHERE uuid IN :messageIds")
    List<Message> findAllByMessageUuids(List<UUID> messageIds);
}
