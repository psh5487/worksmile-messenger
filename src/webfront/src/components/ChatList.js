import React, { useEffect, useState } from "react";
import { Col, Container, Form, InputGroup, Row } from "react-bootstrap";
import { Link, withRouter } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";

import Room from "../components/Room";
import CreateRoom from "./CreateRoom";
import SearchPublicRoom from "./SearchPublicRoom";
import SettingDropdown from "./SettingDropdown";
import { update } from "../reducers/forbidden";
import { initRooms, updateLastMsg, updateRooms } from "../reducers/roominfo";
import { subscribeOnRoomList } from "../utils/websocket";

import axios from "axios";
import Cookies from "js-cookie";

import "./ChatList.css";

function ChatList(props) {
    const dispatch = useDispatch();

    const user = useSelector((state) => state.user.userInfo);
    const stompClient = useSelector((state) => state.stomp.stompClient);
    const onRoomList = useSelector((state) => state.roomconn.onRoomList);
    const rooms = useSelector((state) => state.roomInfo.rooms);
    const lastMsgs = useSelector((state) => state.roomInfo.lastMsgs);

    const [rname, setRname] = useState("");

    const token = Cookies.get("x-auth-token");
    const organizationUrl = "/organization/root-company";

    useEffect(() => {
        // 본인이 포함된 채팅방 목록 + 해당 채팅방의 최근 메시지 정보
        axios({
            method: "get",
            url: "/api/chat/rooms/cur-user/" + user.uid,
            headers: { "X-Auth-Token": token },
        })
            .then((res) => {
                console.log("채팅방 목록 + 메시지 정보 : ", res);
                if (res.data.status === 200) {
                    dispatch(initRooms(res.data.data.rooms));
                    dispatch(updateLastMsg(res.data.data.last_messages));

                    if (onRoomList === null || onRoomList === undefined) {
                        subscribeOnRoomList(
                            dispatch,
                            onRoomList,
                            stompClient,
                            user.uid,
                            res.data.data.last_messages
                        );
                    }
                }
            })
            .catch((err) => {
                console.log(err);
            });

        // 비속어 리스트
        axios({
            method: "get",
            url: "/api/chat/forbidden-words",
        })
            .then((res) => {
                console.log("비속어 리스트 : ", res);
                if (res.data.status === 200) {
                    dispatch(update(res.data.data.forbidden_words));
                }
            })
            .catch((err) => {
                alert(err);
            });
    }, []);

    const onRoomListHandler = (e) => {
        setRname(e.currentTarget.value);
    };

    const doSearchRoom = (e) => {
        if (e.keyCode === 13) {
            const keyword = e.currentTarget.value;

            // 채팅방 및 사용자 찾는 API 요청
            axios({
                method: "GET",
                url:
                    "/api/chat/search/cur-user/" +
                    user.uid +
                    "?keyword=" +
                    keyword,
                headers: { "X-Auth-Token": token },
            }).then((res) => {
                console.log("채팅방 검색 결과 : ", res);
                if (res.data.status === 200) {
                    // 방 순서 시간순으로 맞추고, 최근 메시지도 필요함
                    // dispatch(updateRooms(res.data.data.rooms));
                }
            });
        }
    };

    return (
        <>
            <div className="list-container">
                <Row className="userinfo">
                    <Col xs={3} className="col-img">
                        <img
                            alt="worksmile"
                            src="/imgs/worksmile_logo.png"
                            width="100%"
                            text-align="left"
                        />
                    </Col>
                    <Col xs={7} className="col user-profile">
                        {user.uname} {user.pname} <br />
                        {user.subroot_cname} <br />
                        {user.cname}
                    </Col>
                    {user.role === "ROLE_ROOT_COMPANY_ADMIN" ? (
                        <Col xs={2} className="col">
                            <Link to="/admin/root-company">
                                <span className="material-icons light">
                                    admin_panel_settings
                                </span>
                            </Link>
                        </Col>
                    ) : user.role === "ROLE_COMPANY_ADMIN" ? (
                        <Col xs={2} className="col">
                            <Link to="/admin/company">
                                <span className="material-icons light">
                                    admin_panel_settings
                                </span>
                            </Link>
                        </Col>
                    ) : user.role === "ROLE_SUPER_ADMIN" ? (
                        <Col xs={2} className="col">
                            <Link to="/admin/super">
                                <span className="material-icons light">
                                    admin_panel_settings
                                </span>
                            </Link>
                        </Col>
                    ) : (
                        <Col xs={2} className="col"></Col>
                    )}
                </Row>
                <Row className="iconlist">
                    <Col>
                        <Link to={organizationUrl}>
                            <span className="material-icons light">
                                business
                            </span>
                        </Link>
                    </Col>
                    <Col>
                        <CreateRoom user={user} />
                    </Col>
                    <Col>
                        <SettingDropdown props={props} />
                    </Col>
                    <Col>
                        <SearchPublicRoom />
                    </Col>
                    <Col>
                        <span className="material-icons light">
                            event_available
                        </span>
                    </Col>
                </Row>
                <div className="room-search">
                    <InputGroup>
                        <Form.Control
                            type="text"
                            value={rname}
                            onChange={onRoomListHandler}
                            onKeyUp={doSearchRoom}
                            placeholder="채팅방 및 사용자 검색"
                        />
                    </InputGroup>
                </div>
                <div className="favorite-roomlist">
                    <div className="room-type">즐겨찾기</div>
                    {Array.isArray(rooms)
                        ? // && rooms.length !== 0
                          rooms
                              .filter((room) => {
                                  return room.favorite_type === "on";
                              })
                              .map((room, index) => (
                                  <Room
                                      key={index}
                                      room={room}
                                      lastMsg={lastMsgs[index]}
                                      props={props}
                                  />
                              ))
                        : ""}
                </div>
                <div className="general-roomlist">
                    <div className="room-type">최근</div>
                    {Array.isArray(rooms)
                        ? // && rooms.length !== 0
                          rooms
                              .filter((room) => {
                                  return room.favorite_type === "off";
                              })
                              .map((room, index) => (
                                  <Room
                                      key={index}
                                      room={room}
                                      lastMsg={lastMsgs[index]}
                                      props={props}
                                  />
                              ))
                        : ""}
                </div>
            </div>
        </>
    );
}

export default withRouter(ChatList);
