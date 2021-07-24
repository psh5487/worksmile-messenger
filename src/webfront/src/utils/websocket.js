import React from "react";
import { useSelector } from "react-redux";
import scrollIntoView from "scroll-into-view-if-needed";
import {
    createRoomConn,
    createChatConn,
    deleteChatConn,
    deleteRoomConn,
} from "../reducers/roomconn";
import { updateLastMsg, updateRooms } from "../reducers/roominfo";
import { addRoomMsgs } from "../reducers/roomMsg";

// onRoomList Subs
export const subscribeOnRoomList = (
    dispatch,
    chatRoomListRealTime,
    stompClient,
    uid,
    lastMsgs
) => {
    // const dispatch = useDispatch();

    // console.log(stompClient);
    // console.log(uid);
    console.log(lastMsgs);

    if (chatRoomListRealTime === null || chatRoomListRealTime === undefined) {
        chatRoomListRealTime = stompClient.subscribe(
            "/sub/msg/user/" + uid,
            function (data) {
                // console.log("ON_ROOM_LIST");
                // console.log(data);
                dispatch(createRoomConn(chatRoomListRealTime));

                // 여기서 lastMsgs 를 갱신해야함
                // lastMsgs = useSelector((state) => state.roomInfo.lastMsgs);
                // dispatch();

                showChatRoomListRealTime(
                    dispatch,
                    JSON.parse(data.body),
                    lastMsgs
                );
            },
            { id: uid }
        );
        // dispatch(createRoomConn(chatRoomListRealTime));
    }
};

// onChat Subs
export const subscribeOnChat = (
    dispatch,
    curChatRoom,
    stompClient,
    user,
    roomUserInfo
) => {
    // const dispatch = useDispatch();
    console.log(stompClient);
    console.log(user);
    console.log(roomUserInfo);

    if (curChatRoom === null || curChatRoom === undefined) {
        curChatRoom = stompClient.subscribe(
            "/sub/msg/room/" + roomUserInfo.ruuid,
            function (data) {
                // console.log("ON_CHAT");
                // console.log(data);
                showMessageOutput(dispatch, JSON.parse(data.body));
            },
            { id: user.uid + "/" + roomUserInfo.ruuid }
        );
        sendMsgType("ON", "", roomUserInfo, user, stompClient);
        console.log(curChatRoom);
        dispatch(createChatConn(curChatRoom));
    }
};

// onRoomList Msg Formatting
export const showChatRoomListRealTime = (dispatch, message, lastMsgs) => {
    console.log("onRoom 메시지 도착");
    // console.log(message);
    // console.log(message["type"]);
    if (message.type !== "ON" && message.type !== "OFF") {
        // dispatch(addRoomMsgs(message));
        const targetRuuid = message.ruuid;
        console.log(targetRuuid);
        console.log(lastMsgs);
        console.log(lastMsgs.length);
        for (let index = 0; index < lastMsgs.length; index++) {
            console.log(lastMsgs[index].ruuid);
            if (lastMsgs[index].ruuid === targetRuuid) {
                console.log("같은 인덱스는 " + index);
                lastMsgs[index] = {
                    _id: lastMsgs[index]._id,
                    ruuid: targetRuuid,
                    msg: [message],
                };
                console.log(lastMsgs);
                dispatch(updateLastMsg(lastMsgs));
                break;
            }
        }
    }
    // dispatch(updateLastMsg(lastMsgs));
};

// onChat Msg Formatting
export const showMessageOutput = (dispatch, message) => {
    console.log("onChat 메시지 도착");
    console.log(message);
    if (message.type !== "ON" && message.type !== "OFF") {
        console.log(message.type);

        dispatch(addRoomMsgs(message));

        const node = document.getElementById("msg" + message.midx);
        if (node !== null) {
            scrollIntoView(node, {
                scrollMode: "if-needed",
                block: "nearest",
                inline: "nearest",
            });
        }
    }
};

// onRoomList unsubs
export const unsubscribeOnRoomList = (dispatch, chatRoomListRealTime) => {
    // UNSUBSCRIBE
    if (chatRoomListRealTime !== null && chatRoomListRealTime !== undefined) {
        chatRoomListRealTime.unsubscribe();
        dispatch(deleteRoomConn());

        // DISCONNECT
        // stompClient.disconnect(function () {}, { uid: sender });
        // console.log("Disconnected");
    }
};

// onChat unsubs
export const unsubscribeOnChat = (
    dispatch,
    curChatRoom,
    roomUserInfo,
    // allRoom,
    // lastMsg,
    user,
    stompClient
) => {
    // console.log(roomUserInfo);
    // console.log(allRoom);
    // UNSUBSCRIBE
    if (curChatRoom !== null && curChatRoom !== undefined) {
        curChatRoom.unsubscribe();
        dispatch(deleteChatConn());

        // roomUserInfo[0].last_read_idx = lastMsg.msg[0].midx;
        // for (let index = 0; index < allRoom.length; index++) {
        //     console.log(allRoom[index].ruuid);
        //     console.log(roomUserInfo[0].ruuid);
        //     if (allRoom[index].ruuid === roomUserInfo[0].ruuid) {
        //         allRoom[index] = roomUserInfo[0];
        //     }
        // }
        // console.log(allRoom);
        // dispatch(updateRooms(allRoom));

        // SEND
        sendMsgType("OFF", "", roomUserInfo, user, stompClient);
    }
};

// Msg Send
export const sendMsgType = (type, msg, roomUserInfo, user, stompClient) => {
    console.log("websocket : sendMsgType call");
    let content = "";

    if (type === "TALK") {
        console.log("TALK MSG");
        content = msg;
    } else if (type === "OFF") {
        console.log("OFF MSG");
        const contentObj = { last_read_idx: roomUserInfo.last_read_idx }; // 현재 채팅방의 마지막 메시지 idx 넣을 것
        content = JSON.stringify(contentObj);
    } else if (type === "ENTER") {
        console.log("ENTER MSG");
        const userList = { users: ["a", "b", "c"] };
        content = JSON.stringify(userList);
    }

    const newMessage = {
        type: type,
        midx: null,
        parent_id: 0,
        content: content,
        sender: user.uid,
        uname: user.uname,
        cname: user.subroot_cname,
        pname: user.pname,
        ruuid: roomUserInfo.ruuid,
        device: "web",
        created_at: "",
        deleted_at: "",
    };

    // SEND
    stompClient.send("/pub/msg", {}, JSON.stringify(newMessage));
    // refMsgInput.current.value = "";
    // setMsg("");
    // refMsgInput.current.focus();
};
