package org.example.Broadcast.Broadcasting;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "type") @JsonSubTypes({
        @JsonSubTypes.Type(value = TcpBroadcast.class, name = "TcpBroadcast"),
})
public interface Broadcast {

    /**
     * Sends a message to all consumers subscribed to the specified topic.
     *
     * @param topic the topic to send the message to
     * @param message the message to send
     */
    void sendToAllConsumers(Topic topic, Object message);



    /**
     * Sends a message to a specific consumer subscribed to the specified topic.
     *
     * @param topic the topic to send the message to
     * @param message the message to send
     * @param containerId the ID of the consumer container to send the message to
     */
    void sendToConsumer(Topic topic, Object message, Integer containerId);
}
