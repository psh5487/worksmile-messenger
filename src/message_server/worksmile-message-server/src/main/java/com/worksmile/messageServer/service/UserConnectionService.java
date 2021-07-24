package com.worksmile.messageServer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worksmile.messageServer.model.user.ConnectedUser;
import com.worksmile.messageServer.model.user.RoomUser;
import com.worksmile.messageServer.model.user.User;
import com.worksmile.messageServer.repository.RedisRepository;
import com.worksmile.messageServer.repository.UserRepository;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserConnectionService {

    private final UserRepository userRepository;
    private final RedisRepository redisRepository;

    public UserConnectionService(UserRepository userRepository, RedisRepository redisRepository) {
        this.userRepository = userRepository;
        this.redisRepository = redisRepository;
    }

    @Transactional
    public void manageConnectedUser(String userId) {
        User user = userRepository.findByUid(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        ConnectedUser connectedUser = redisRepository.getConnectedUser(userId);

        if(connectedUser == null) { // 어느 기기로도 접속하지 않은 경우
            // ConnectedUser 정보 저장
            ConnectedUser newConnectedUser = ConnectedUser.builder()
                    .deviceCount(1)
                    .build();
            redisRepository.addConnectedUser(userId, newConnectedUser);

            // 사용자가 속한 방에 ConnectedUser 넣어주기
            List<RoomUser> roomUsers = user.getRoomUsers();

            for(RoomUser roomUser : roomUsers) {
                redisRepository.addOneConnectedUserToRoom(roomUser.getRuuid(), userId);
            }
        } else { // 1개 이상의 기기로 접속하고 있는 경우
            // ConnectedUser 정보 수정
            connectedUser.setDeviceCount(connectedUser.getDeviceCount() + 1);
            redisRepository.addConnectedUser(userId, connectedUser);
        }
    }

    @Transactional
    public void manageDisconnectedUser(String userId) {
        User user = userRepository.findByUid(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        ConnectedUser connectedUser = redisRepository.getConnectedUser(userId);

        if(connectedUser.getDeviceCount() == 1) { // 1개 기기로만 접속 중인 경우
            // ConnectedUser 정보 제거
            redisRepository.removeConnectedUser(userId);

            // 사용자가 속한 방들의 ConnectedUser 제거
            List<RoomUser> roomUsers = user.getRoomUsers();

            for(RoomUser roomUser : roomUsers) {
                redisRepository.removeConnectedUserFromRoom(roomUser.getRuuid(), userId);
            }
        } else { // 2개 이상의 기기로 접속하고 있는 경우
            connectedUser.setDeviceCount(connectedUser.getDeviceCount() - 1);
            redisRepository.addConnectedUser(userId, connectedUser);
        }
    }
}
