import { Dropdown } from "react-bootstrap";
import { logout, updateSetting } from "../reducers/user";
import { useDispatch, useSelector } from "react-redux";

import { deleteChatConn, deleteRoomConn } from "../reducers/roomconn";
import { updateLastMsg, initRooms } from "../reducers/roominfo";
import { update } from "../reducers/forbidden";
import { AUTH_URL, USER_URL } from "../utils/constant";

import axios from "axios";
import React from "react";
import Cookies from "js-cookie";

import "./iconList.css";

function SettingDropdown({ props }) {
    const dispatch = useDispatch();

    const user = useSelector((state) => state.user.userInfo);
    const stompClient = useSelector((state) => state.stomp.stompClient);

    const token = Cookies.get("X-Auth-Token");

    // 로그아웃
    const doLogout = (e) => {
        const reqBody = {
            uid: user.uid,
        };

        axios({
            method: "post",
            url: AUTH_URL + "/api/auth/logout",
            data: reqBody,
            headers: { "X-Auth-Token": token },
        })
            .then((res) => {
                console.log(res);
                if (res.data.status === 200) {
                    alert(res.data.message);

                    // Store 내 state 초기화
                    stompClient.disconnect(function () {}, { uid: user.uid });
                    dispatch(deleteRoomConn());
                    dispatch(deleteChatConn());
                    dispatch(update());
                    dispatch(initRooms());
                    dispatch(updateLastMsg());
                    dispatch(logout());
                    props.history.push("/");
                } else {
                    alert(
                        "로그아웃을 할 수 없습니다. 잠시 후 다시 시도해주세요."
                    );
                }
            })
            .catch((err) => {
                alert(err);
            });
    };

    // 전체 서비스 설정
    const allServiceSetting = (e) => {
        const reqBody = {
            allPushNotice: user.all_push_notice,
        };

        axios({
            method: "put",
            url: USER_URL + "/api/user/service-setting/user/" + user.uid,
            data: reqBody,
        })
            .then((res) => {
                console.log("전체 서비스 설정 결과 : ", res);
                if (res.data.status === 200) {
                    user.all_push_notice =
                        user.all_push_notice === "on" ? "off" : "on";
                    dispatch(updateSetting(user));
                }
            })
            .catch((err) => {
                alert(err);
            });
    };

    return (
        <>
            <Dropdown>
                <Dropdown.Toggle id="setting-dropdown">
                    <span className="material-icons light">settings</span>
                </Dropdown.Toggle>

                <Dropdown.Menu>
                    <Dropdown.Item onClick={allServiceSetting}>
                        전체 채팅방 알림
                        {user.all_push_notice === "on" ? " OFF" : " ON"}
                    </Dropdown.Item>
                    <Dropdown.Item>프로필 변경</Dropdown.Item>
                    <Dropdown.Item>개인 정보 변경</Dropdown.Item>
                    <Dropdown.Item onClick={doLogout}>로그아웃</Dropdown.Item>
                </Dropdown.Menu>
            </Dropdown>
        </>
    );
}

export default SettingDropdown;
