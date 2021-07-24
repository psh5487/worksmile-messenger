import React, { useRef, useState } from "react";
import { Button, Form, InputGroup, ListGroup, Modal } from "react-bootstrap";

import { useSelector } from "react-redux";

import axios from "axios";
import Cookies from "js-cookie";

import "./iconList.css";

function PublicRoom({ rooms, user }) {
    const token = Cookies.get("X-Auth-Token");
    const addUser = (room) => {
        // 해당 채팅방에 입장 -- 미구현
        // const reqBody = {
        //     roomId: room.ruuid,
        //     roomType: room.roomType,
        //     users: [user.uid],
        // };
        // console.log(reqBody);
        // axios({
        //     method: "post",
        //     url: "/api/chat/user",
        //     data: reqBody,
        //     headers: { "X-Auth-Token": token },
        // })
        //     .then((res) => {
        //         console.log(res);
        //     })
        //     .catch((err) => {
        //         console.log(err);
        //     });
    };

    return (
        <ListGroup>
            {Array.isArray(rooms) ? (
                rooms.map((room, index) => (
                    <ListGroup.Item
                        key={index}
                        className="pointer"
                        onClick={() => addUser(room)}
                    >
                        <div>
                            {room.init_name} {room.memcnt}명
                        </div>
                        <div>
                            방장 : <span>{room.leader_id}</span>
                        </div>
                    </ListGroup.Item>
                ))
            ) : (
                <ListGroup.Item>결과없음</ListGroup.Item>
            )}
        </ListGroup>
    );
}

function SearchPublicRoom() {
    const user = useSelector((state) => state.user.userInfo);

    const [show, setShow] = useState(false);
    const [rname, setRname] = useState("");
    const [publicRooms, setPublicRooms] = useState([]);

    const inputRef = useRef();

    const handleClose = () => {
        setShow(false);
        setRname("");
        setPublicRooms([]);
    };

    const handleShow = () => {
        setShow(true);
        setRname("");

        // 해당하는 subrootCid의 오픈 채팅방을 전부 요청
        axios({
            method: "get",
            url: "/api/chat/rooms/public/company/" + user.subroot_cid,
        })
            .then((res) => {
                console.log("오픈 채팅방 초기값 결과 : ", res);
                if (res.data.status === 200) {
                    setPublicRooms(res.data.data.open_rooms);
                    inputRef.current.focus();
                }
            })
            .catch((err) => {
                alert(err);
            });
    };

    const onChangeRname = (e) => {
        setRname(e.currentTarget.value);
    };

    const searchRoom = () => {
        // rname으로 해당 오픈 채팅방 검색
        axios({
            method: "get",
            url:
                "/api/chat/search/public-room/cur-user/" +
                user.uid +
                "?rname=" +
                rname,
        })
            .then((res) => {
                console.log("오픈 채팅방 검색 결과 : ", res);
                if (res.data.status === 200) {
                    setPublicRooms(res.data.data.open_rooms);
                }
            })
            .catch((err) => {
                alert(err);
            });
    };

    const onKeyUp = (e) => {
        if (e.keyCode === 27) {
            handleClose();
        } else if (e.keyCode === 13) {
            searchRoom();
        }
    };

    return (
        <>
            <span className="material-icons light pointer" onClick={handleShow}>
                search
            </span>

            <Modal
                show={show}
                onHide={handleClose}
                backdrop="static"
                keyboard={false}
            >
                <Modal.Header closeButton>
                    <Modal.Title>공개 대화방 검색하기</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <InputGroup>
                        <Form.Control
                            type="text"
                            value={rname}
                            onChange={onChangeRname}
                            onKeyUp={onKeyUp}
                            ref={inputRef}
                            placeholder="대화방명을 입력하세요."
                        />
                    </InputGroup>
                    <div className="room-list">
                        <PublicRoom rooms={publicRooms} user={user} />
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button
                        variant="primary"
                        className="orange-btn"
                        onClick={searchRoom}
                    >
                        검색
                    </Button>
                    <Button variant="secondary" onClick={handleClose}>
                        취소
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default SearchPublicRoom;
