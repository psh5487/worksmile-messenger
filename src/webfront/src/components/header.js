import { Navbar } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";

import { unsubscribeOnChat, unsubscribeOnRoomList } from "../utils/websocket";

import "./header.css";

const Header = ({ props, roomUserInfo }) => {
    const dispatch = useDispatch();

    const user = useSelector((state) => state.user.userInfo);
    const stompClient = useSelector((state) => state.stomp.stompClient);
    const onChat = useSelector((state) => state.roomconn.onChat);
    const onRoomList = useSelector((state) => state.roomconn.onRoomList);

    const clickHeader = (e) => {
        // admin page에서 클릭 시, 메인 화면
        if (props.match.path === "/admin/:option") {
            props.history.push("/chat/rooms/user");
        } else {
            // 우측 채팅방을 보고 있다면 해당 채팅방 구독 해지
            if (onChat !== null && onChat !== undefined) {
                unsubscribeOnChat(
                    dispatch,
                    onChat,
                    roomUserInfo,
                    user,
                    stompClient
                );
            }

            // 로그인된 유저 정보가 존재하지 않으면,
            if (JSON.stringify(user) === "{}") {
                unsubscribeOnRoomList(dispatch, onRoomList);
                props.history.push("/user/login");
            } else {
                props.history.push("/chat/rooms/user");
            }
        }
    };

    return (
        <>
            <Navbar bg="dark" variant="dark">
                <span className="header-title pointer" onClick={clickHeader}>
                    <img
                        alt="worksmile"
                        src="/imgs/worksmile_logo.png"
                        width="30"
                        height="30"
                        className="d-inline-block align-top"
                    />{" "}
                    <b>WorkSmile</b>
                </span>
            </Navbar>
        </>
    );
};

export default Header;
