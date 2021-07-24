import React from "react";
import { Dropdown } from "react-bootstrap";
import { useSelector } from "react-redux";

function RoomSettingDropdown({ room }) {
    return (
        <>
            <Dropdown>
                <Dropdown.Toggle id="setting-dropdown">
                    <span className="material-icons light">
                        keyboard_arrow_right
                    </span>
                </Dropdown.Toggle>

                <Dropdown.Menu>
                    <Dropdown.Item className="pointer">
                        <span>
                            Push 알림
                            {room.pushNotice === "on" ? " OFF" : " ON"}
                        </span>
                    </Dropdown.Item>
                    <Dropdown.Item className="pointer">
                        <span>
                            즐겨찾기
                            {room.favoriteType === "on" ? " OFF" : " ON"}
                        </span>
                    </Dropdown.Item>
                    <Dropdown.Item className="pointer">
                        읽음 표시하기
                    </Dropdown.Item>
                    <Dropdown.Item className="pointer">
                        채팅방 이름 변경
                    </Dropdown.Item>
                    <Dropdown.Item className="pointer">
                        채팅방 나가기
                    </Dropdown.Item>
                </Dropdown.Menu>
            </Dropdown>
        </>
    );
}

export default RoomSettingDropdown;
