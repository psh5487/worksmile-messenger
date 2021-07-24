import React, { useEffect, useRef, useState } from "react";
import { withRouter } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";

import ChatList from "../components/ChatList";
import ChatMsg from "../components/ChatMsg";
import Header from "../components/header";
import { sendMsgType } from "../utils/websocket";

import axios from "axios";
import scrollIntoView from "scroll-into-view-if-needed";

import "./ChatRoom.css";

function ChatRoom(props) {
    const roomMsgs = useSelector((state) => state.roomMsg.msgs);
    const stompClient = useSelector((state) => state.stomp.stompClient);
    const user = useSelector((state) => state.user.userInfo);

    const [msg, setMsg] = useState("");

    const refMsgInput = useRef();
    const refSendBtn = useRef();

    const roomUserInfo = props.location.state.roomUser;
    const propsRoomMsg = props.location.state.propsRoomMsg;
    const lastIdx = roomUserInfo.last_read_idx;

    useEffect(() => {
        // 읽은 마지막 메시지로 스크롤 고정
        // console.log(lastIdx);
        const node = document.getElementById("msg" + lastIdx);
        if (node !== null) {
            scrollIntoView(node, {
                scrollMode: "if-needed",
                block: "nearest",
                inline: "nearest",
            });
        }

        // roomUser의 last_read_idx 갱신
        const lastIndex = roomMsgs[roomMsgs.length - 1].midx;
        console.log(lastIdx);

        const reqBody = {
            uid: user.uid,
            ruuid: roomUserInfo.ruuid,
            room_last_message_idx: lastIndex,
        };
        axios({
            method: "put",
            url: "/api/messages/msg/user-off",
            data: reqBody,
        })
            .then((res) => {
                console.log("LastReadIdx 갱신 : ", res);
                // dispatch(updateRoomIdx(res.data.data.user));
            })
            .catch((err) => {
                alert(err);
            });
    }, [roomUserInfo]);

    const sendMsg = (e) => {
        console.log("ChatRoom : sendMsg call");
        setMsg("");
        sendMsgType("TALK", msg, roomUserInfo, user, stompClient);
    };

    const onChatHandler = (e) => {
        setMsg(e.currentTarget.value);
    };

    const onKeyUp = (e) => {
        // Enter 입력 시
        if (e.keyCode === 13) {
            refSendBtn.current.click();
        }
    };

    return (
        <>
            <Header props={props} roomUserInfo={roomUserInfo} />
            <div className="content-wrapper">
                <ChatList />
                <div className="chatting-container">
                    <div className="partition" id="room-info">
                        {roomUserInfo.favorite_type === "on" ? (
                            <span className="material-icons md18 pointer">
                                star
                            </span>
                        ) : (
                            <span className="material-icons md18 pointer">
                                star_border
                            </span>
                        )}
                        <span className="room-title orange">
                            {roomUserInfo.rname}
                        </span>
                    </div>
                    <div className="partition" id="room-chat-list">
                        {Array.isArray(roomMsgs) && roomMsgs.length > 0
                            ? roomMsgs
                                  .filter((msg) => msg.type !== "CREATE_ROOM")
                                  .map((msg, index) => (
                                      <ChatMsg
                                          msg={msg}
                                          uid={user.uid}
                                          key={index + 1}
                                      />
                                  ))
                            : ""}
                    </div>
                    <div className="partition" id="room-chat-input">
                        <input
                            type="text"
                            id="text"
                            placeholder="메시지를 입력하세요."
                            onChange={onChatHandler}
                            value={msg}
                            onKeyUp={onKeyUp}
                            ref={refMsgInput}
                        />
                        <button
                            id="sendMessage"
                            onClick={sendMsg}
                            ref={refSendBtn}
                            disabled={msg === "" ? true : false}
                        >
                            전송
                        </button>
                    </div>
                </div>
            </div>
        </>
    );
}

export default withRouter(ChatRoom);
