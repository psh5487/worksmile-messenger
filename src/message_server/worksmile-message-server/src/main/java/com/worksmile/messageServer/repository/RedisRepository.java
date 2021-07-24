package com.worksmile.messageServer.repository;

import com.worksmile.messageServer.model.user.ConnectedUser;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.*;

@Service
public class RedisRepository {

    // Redis CacheKeys
    private static final String CONNECTED_USERS = "CONNECTED_USERS";
    private static final String CONNECTED_USERS_OF_ROOM = "CONNECTED_USERS_OF_ROOM/";
    private static final String ON_USERS_OF_ROOM = "ON_USERS_OF_ROOM/";

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ConnectedUser> connectedUsers; // CONNECTED_USER

    @Resource(name = "redisTemplate")
    private SetOperations<String, String> connectedUsersOfRoom; // CONNECTED_USERS_OF_ROOM

    @Resource(name = "redisTemplate")
    private SetOperations<String, String> onUsersOfRoom; // ON_USERS_OF_ROOM

    private Map<String, ChannelTopic> topics; // Key: topicId, Val: channelTopic

    /** CONNECTED_USER 관련 */
    public ConnectedUser getConnectedUser(String userId) {
        return connectedUsers.get(CONNECTED_USERS, userId);
    }

    public void addConnectedUser(String userId, ConnectedUser connectedUser) {
        connectedUsers.put(CONNECTED_USERS, userId, connectedUser);
    }

    public void removeConnectedUser(String userId) {
        connectedUsers.delete(CONNECTED_USERS, userId);
    }

    /** CONNECTED_USERS_OF_ROOM 관련 */
    public Set<String> getConnectedUsersOfRoom(String roomId) {
        return Optional.ofNullable(connectedUsersOfRoom.members(CONNECTED_USERS_OF_ROOM + roomId))
                .orElse(new HashSet<>(){});
    }

    public void addOneConnectedUserToRoom(String roomId, String userId) {
        connectedUsersOfRoom.add(CONNECTED_USERS_OF_ROOM + roomId, userId);
    }

    public void addMultipleConnectedUsersToRoom(String roomId, String[] users) {
        connectedUsersOfRoom.add(CONNECTED_USERS_OF_ROOM + roomId, users);
    }

    public void removeConnectedUserFromRoom(String roomId, String userId) {
        connectedUsersOfRoom.remove(CONNECTED_USERS_OF_ROOM + roomId, userId);
    }

    /** ON_USERS_OF_ROOM 관련 */
    public Set<String> getOnUsersOfRoom(String roomId) {
        return Optional.ofNullable(onUsersOfRoom.members(ON_USERS_OF_ROOM + roomId))
                .orElse(new HashSet<>(){});
    }

    public void addOnUserToRoom(String roomId, String userId) {
        onUsersOfRoom.add(ON_USERS_OF_ROOM + roomId, userId);
    }

    public void removeOnUserFromRoom(String roomId, String userId) {
        onUsersOfRoom.remove(ON_USERS_OF_ROOM + roomId, userId);
    }

}
