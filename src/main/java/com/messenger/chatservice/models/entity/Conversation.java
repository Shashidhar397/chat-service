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

import java.util.List;
import java.util.UUID;

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

    @Column("message_id")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String messageId;

    @Column("users_involved")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> usersInvolved;

    @Column("created_at")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    private Long createdAt;

}
