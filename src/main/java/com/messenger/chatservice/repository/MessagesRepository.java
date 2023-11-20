package com.messenger.chatservice.repository;

import com.messenger.chatservice.models.MessageStatus;
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

    @Query("SELECT * FROM message WHERE sender_uuid IN :userUuids AND recipient_uuid IN :userUuids ALLOW FILTERING")
    List<Message> findAllBySenderUuidInAndRecipientUuidIn(List<String> userUuids);

    @Query("UPDATE message SET message_status = :messageStatus WHERE uuid = :uuid")
    void updateMessageStatusByUuid(UUID uuid, MessageStatus messageStatus);
}
