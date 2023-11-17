package com.messenger.chatservice;

import com.messenger.chatservice.models.entity.Message;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

/**
 * @author shashidhar
 */
@Repository
public interface MessagesRepository extends CassandraRepository<Message, String>{
}
