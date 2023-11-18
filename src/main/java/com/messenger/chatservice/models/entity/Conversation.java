package com.messenger.chatservice.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author shashidhar
 */
@Table(value = "conversation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {


    @Id
    @PrimaryKeyColumn(name = "conversation_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID uuid;

    @Column("messages")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.UUID)
    private List<UUID> messages;

    @Column("users_involved")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> usersInvolved;

    @Column("created_at")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    private Timestamp createdAt;

    public Conversation(Message message) {
        this.messages = new ArrayList<>();
        this.messages.add(message.getUuid());
        this.usersInvolved = Arrays.asList(message.getRecipientUuid(), message.getSenderUuid());
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.uuid = UUID.randomUUID();
    }
}
