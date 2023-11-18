package com.messenger.chatservice.models.entity;

import com.messenger.chatservice.models.ChatMessageRequestModel;
import com.messenger.chatservice.models.MessageStatus;
import com.messenger.chatservice.models.MessageType;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * @author shashidhar
 */
@Table(value = "message")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @CassandraType(type = CassandraType.Name.TEXT)
    private String content;

    @Column("recipient_uuid")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String recipientUuid;

    @Column("sender_uuid")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String senderUuid;

    @Column("message_type")
    @CassandraType(type = CassandraType.Name.TEXT)
    private MessageType messageType;

    @Column("message_status")
    @CassandraType(type = CassandraType.Name.TEXT)
    private MessageStatus messageStatus;

    @Column("created_at")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    private Timestamp createdAt;

    @CassandraType(type = CassandraType.Name.UUID)
    @Id
    @PrimaryKeyColumn(name = "message_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID uuid;

    public Message(ChatMessageRequestModel chatMessageRequestModel) {
        this.content = chatMessageRequestModel.getContent();
        this.recipientUuid = chatMessageRequestModel.getRecipientUuid();
        this.senderUuid = chatMessageRequestModel.getSenderUuid();
        this.messageType = chatMessageRequestModel.getMessageType();
        this.messageStatus = chatMessageRequestModel.getMessageStatus();
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.uuid = UUID.randomUUID();
    }
}
