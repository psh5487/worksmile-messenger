import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { withRouter } from "react-router-dom";
import { Col, Row } from "react-bootstrap";

import RoomSettingDropdown from "./RoomSettingDropdown";
import { createChatConn } from "../reducers/roomconn";
import { subscribeOnChat, unsubscribeOnChat } from "../utils/websocket";
import { addRoomMsgs, updateRoomMsgs } from "../reducers/roomMsg";

import axios from "axios";

import "./Room.css";
import { SHOW_MSG_AMOUNT } from "../utils/constant";

function Room({ props, room, lastMsg }) {
    const dispatch = useDispatch();

    const user = useSelector((state) => state.user.userInfo);
    const stompClient = useSelector((state) => state.stomp.stompClient);
    const onChat = useSelector((state) => state.roomconn.onChat);
    const beforeOnChat = useSelector((state) => state.roomconn.onChat);
    const allRoom = useSelector((state) => state.roomInfo.rooms);

    const [resContent, setResContent] = useState(
        lastMsg !== undefined ? lastMsg.msg[0].content : ""
    );
    const [resTime, setResTime] = useState(
        lastMsg !== undefined ? lastMsg.msg[0].created_at : ""
    );
    const [resCnt, setResCnt] = useState(
        lastMsg !== undefined ? lastMsg.msg[0].midx - room.last_read_idx : ""
    );

    const contentId = "content" + room.ruuid;
    const timeId = "time" + room.ruuid;

    let beforeRoom = "";
    if (beforeOnChat !== null) {
        beforeRoom = allRoom.filter(
            (each) => user.uid + "/" + each.ruuid === beforeOnChat.id
        );
    }

    useEffect(() => {
        setResContent(lastMsg !== undefined ? lastMsg.msg[0].content : "");
        setResTime(lastMsg !== undefined ? lastMsg.msg[0].created_at : "");
        setResCnt(
            lastMsg !== undefined
                ? lastMsg.msg[0].midx - room.last_read_idx
                : ""
        );
    });

    const roomClickHandler = (e) => {
        e.preventDefault();
        const ruuid = room.ruuid;

        axios({
            method: "get",
            url:
                "/api/messages/msg/room/" +
                ruuid +
                "?start=0" +
                // room.last_read_idx +
                "&num=" +
                SHOW_MSG_AMOUNT,
        })
            .then((res) => {
                console.log("채팅방 들어가기 결과 : ", res);

                if (res.data.status === 200) {
                    // 클릭한 채팅방의 메시지들 state 갱신
                    dispatch(updateRoomMsgs(res.data.data.messages));

                    // 채팅방(우측)에 대한 SUBSCRIBE
                    // console.log(onChat);
                    // console.log(beforeRoom);
                    const cur = onChat;
                    unsubscribeOnChat(
                        dispatch,
                        cur,
                        beforeRoom,
                        user,
                        stompClient
                    );

                    const oc = null;
                    subscribeOnChat(dispatch, oc, stompClient, user, room);
                    setResCnt(0);
                    props.history.push({
                        pathname: "/messages/room/",
                        state: {
                            roomUser: room,
                            propsRoomMsg: res.data.data.messages,
                        },
                    });
                }
            })
            .catch((err) => {
                alert(err);
            });
    };

    return (
        <Row className="user-room">
            <Col xs={2} className="col-img" onClick={roomClickHandler}>
                <img
                    alt="worksmile"
                    src="/imgs/worksmile_logo.png"
                    width="100%"
                    text-align="left"
                />
            </Col>
            <Col xs={8} className="col" onClick={roomClickHandler}>
                <span className="room-not-read">{resCnt}</span>{" "}
                <span className="room-title">{room.rname}</span>{" "}
                <span className="room-member">{room.memcnt}</span> <br />
                <span id={contentId} className="room-content">
                    {resContent}
                </span>
            </Col>
            <Col xs={2} className="col" onClick={roomClickHandler}>
                <span id={timeId} className="room-timestamp">
                    {resTime}
                </span>
            </Col>
            <div className="room-side pointer">
                <RoomSettingDropdown room={room} />
            </div>
        </Row>
    );
}

export default withRouter(Room);
