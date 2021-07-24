import React from "react";
import { Table } from "react-bootstrap";
import { useSelector } from "react-redux";
import UserData from "./UserData";

import "./element.css";

function UserDataList({ role, userList, props }) {
    const user = useSelector((state) => state.user.userInfo);

    return (
        <Table striped bordered hover>
            <thead>
                <tr>
                    <th>아이디</th>
                    <th>이름</th>
                    <th>회사</th>
                    <th>부서</th>
                    <th>직책</th>
                    <th>ROLE</th>
                    <th>이메일</th>
                    <th>연락처</th>
                    <th>가입날짜</th>
                    <th>버튼</th>
                </tr>
            </thead>
            <tbody>
                {userList.length === 0 ? (
                    <tr></tr>
                ) : (
                    userList
                        .filter((u) => {
                            return u.role === role;
                        })
                        .filter((u) => {
                            return u.uid !== user.uid;
                        })
                        .map((u) => (
                            <UserData key={u.uid} user={u} props={props} />
                        ))
                )}
            </tbody>
        </Table>
    );
}

export default UserDataList;
