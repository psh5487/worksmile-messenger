import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";

import { USER_URL } from "../utils/constant";
import UserDataList from "./UserDataList";

import axios from "axios";

function SuperAdminPage({ props, isAdmin }) {
    const user = useSelector((state) => state.user.userInfo);

    // 전체 Worksmile 사용자 리스트
    const [userList, setUserList] = useState([]);

    /* 회원 표 그리기 */
    useEffect(() => {
        // 사용자 리스트 받기
        axios({
            method: "get",
            url:
                USER_URL +
                "/api/admin/root-company/users/company/" +
                user.root_cid,
        })
            .then((res) => {
                console.log("root company admin 사용자 결과 : ", res);
                setUserList(res.data.data);
            })
            .catch((err) => {
                console.log(err);
            });
    }, []);

    return (
        <>
            <h3>Root Company Admin Page</h3>
            <br />

            <h5>Not Permitted User</h5>
            {isAdmin && (
                <UserDataList
                    role={"ROLE_NOT_PERMITTED_USER"}
                    userList={userList}
                    props={props}
                />
            )}
            <h5>Company Admin User</h5>
            {isAdmin && (
                <UserDataList
                    role={"ROLE_COMPANY_ADMIN"}
                    userList={userList}
                    props={props}
                />
            )}
            <h5>User</h5>
            {isAdmin && (
                <UserDataList
                    role={"ROLE_USER"}
                    userList={userList}
                    props={props}
                />
            )}
        </>
    );
}

export default SuperAdminPage;
