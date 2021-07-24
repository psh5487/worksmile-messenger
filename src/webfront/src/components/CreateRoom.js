import React, { useEffect, useRef, useState } from "react";
import { Button, Form, InputGroup, ListGroup, Modal } from "react-bootstrap";

import axios from "axios";
import Cookies from "js-cookie";

import "./iconList.css";

function CreateRoom({ user }) {
    const [show, setShow] = useState(false);
    const [memcnt, setMemcnt] = useState(1);
    const [userName, setUserName] = useState("");
    const [resultUserList, setResultUserList] = useState([]);
    const [userList, setUserList] = useState([user.uid]);
    const [roomName, setRoomName] = useState("1:1 채팅방");
    const [disclose, setDisclose] = useState("private");

    const inputRef = useRef();
    const token = Cookies.get("X-Auth-Token");
    let chatRoomListRealTime = null;

    const handleClose = () => {
        console.log(chatRoomListRealTime);
        setShow(false);
        setMemcnt(1);
        setUserName("");
        setResultUserList([]);
        setUserList([user.uid]);
        setRoomName();
        setDisclose("private");
    };

    const handleShow = () => {
        setShow(true);
    };

    const onChangeUserName = (e) => {
        setUserName(e.currentTarget.value);
    };

    const onChangeRoomName = (e) => {
        setRoomName(e.currentTarget.value);
    };

    const setPrivate = (e) => {
        setDisclose("private");
    };

    const setPublic = (e) => {
        setDisclose("public");
    };

    const addUserList = (e) => {
        const uid = e.currentTarget.id;
        console.log(uid);
        setResultUserList([]);
        setMemcnt(memcnt + 1);
        setUserList([...userList, uid]);
        inputRef.current.focus();
    };

    const onKeyUp = (e) => {
        // ESC 입력
        if (e.keyCode === 27) {
            handleClose();
        }

        // Enter 입력
        else if (e.keyCode === 13) {
            console.log(userName);

            axios({
                method: "get",
                url:
                    "/api/chat/search/user/cur-user/" +
                    user.uid +
                    "?keyword=" +
                    userName,
            })
                .then((res) => {
                    console.log("사용자 찾기 결과 : ", res);
                    if (res.data.status === 200) {
                        setResultUserList(res.data.data.users);
                    }
                })
                .catch((err) => {
                    alert(err);
                });
        }
    };

    const createRoom = (e) => {
        // 채팅방 생성 API 요청
        const reqBody = {
            room_leader: user.uid,
            company_name: user.subroot_cname,
            userlist: userList,
            room_type: memcnt > 2 ? disclose : "",
            room_name: roomName,
            memcnt: memcnt,
        };
        axios({
            method: "post",
            url: "/api/chat/room/",
            data: reqBody,
            header: { "X-Auth-Token": token },
        })
            .then((res) => {
                console.log("채팅방 생성 결과 : ", res);
                if (res.data.status === 200) {
                    /**
                     * 방 생성하고 만든 방의 정보를 state에 저장
                     */
                    // dispatch(updateRooms(res.data.data.room));
                }
                handleClose();
            })
            .catch((err) => {
                alert(err);
            });
    };

    return (
        <>
            <span className="material-icons light pointer" onClick={handleShow}>
                add_comment
            </span>

            <Modal
                show={show}
                onHide={handleClose}
                backdrop="static"
                keyboard={false}
            >
                <Modal.Header closeButton>
                    <Modal.Title>새 채팅방 만들기</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className="modal-subtitle">
                        <span>사용자 초대</span> (<span>{memcnt - 1}</span>)
                    </div>
                    <InputGroup>
                        <Form.Control
                            type="text"
                            value={userName}
                            onChange={onChangeUserName}
                            onKeyUp={onKeyUp}
                            ref={inputRef}
                            placeholder="사용자명을 입력하세요."
                        />
                    </InputGroup>
                    <div className="search-result">
                        <ListGroup>
                            {resultUserList.length === 0 ? (
                                <ListGroup.Item>결과없음</ListGroup.Item>
                            ) : (
                                resultUserList
                                    .filter((u) => u.uid !== user.uid)
                                    .map((u) => (
                                        <ListGroup.Item>
                                            <div
                                                className="pointer"
                                                id={u.uid}
                                                onClick={addUserList}
                                            >
                                                <span>{u.uname}</span> (
                                                <span>{u.uid}</span>){" "}
                                                <span>{u.pname}</span>
                                            </div>
                                        </ListGroup.Item>
                                    ))
                            )}
                        </ListGroup>
                    </div>
                    <div className="user-list">{userList}</div>
                    {memcnt > 2 ? (
                        <>
                            <div className="room-title">
                                <InputGroup>
                                    <Form.Control
                                        type="text"
                                        value={roomName}
                                        onChange={onChangeRoomName}
                                        placeholder="대화방명을 입력하세요."
                                    />
                                </InputGroup>
                            </div>
                            <div className="disclose">
                                <Form.Check
                                    type="radio"
                                    name="disclose"
                                    id="private"
                                    label="비공개 대화방"
                                    defaultChecked
                                    onClick={setPrivate}
                                />
                                <Form.Check
                                    type="radio"
                                    name="disclose"
                                    label="공개 대화방"
                                    id="public"
                                    onClick={setPublic}
                                />
                            </div>
                        </>
                    ) : (
                        <div></div>
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <Button
                        variant="primary"
                        className="orange-btn"
                        onClick={createRoom}
                    >
                        만들기
                    </Button>
                    <Button variant="secondary" onClick={handleClose}>
                        취소
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default CreateRoom;
