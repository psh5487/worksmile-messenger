import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";

import { USER_URL } from "../utils/constant";
import UserDataList from "./UserDataList";
import ForbiddenList from "./ForbiddenList";

import Cookies from "js-cookie";

import axios from "axios";

function SuperAdminPage({ props, isAdmin }) {
    // ROLE_COMPANY_ADMIN + ROLE_ROOT_COMPANY_ADMIN 에 대한 유저
    const [userList, setUserList] = useState([]);

    /* 회원 표 그리기 */
    useEffect(() => {
        const jwtToken = Cookies.get("access-token");

        // 사용자 리스트 받기
        axios({
            method: "get",
            url: USER_URL + "/api/admin/super/users/company-admins",
            headers: {
                "access-token": jwtToken,
            },
        })
            .then((res) => {
                console.log("super admin 사용자 결과 : ", res);
                setUserList(res.data.data);
            })
            .catch((err) => {
                console.log(err);
            });
    }, []);

    return (
        <>
            <h3>Super Admin Page</h3>
            <br />
            <h5>Not Permitted Admin User</h5>
            {isAdmin && (
                <UserDataList
                    role={"ROLE_NOT_PERMITTED_ADMIN"}
                    props={props}
                    userList={userList}
                />
            )}
            <h5>Root Company Admin User</h5>
            {isAdmin && (
                <UserDataList
                    role={"ROLE_ROOT_COMPANY_ADMIN"}
                    props={props}
                    userList={userList}
                />
            )}
            <h5>Company Admin User</h5>
            {isAdmin && (
                <UserDataList
                    role={"ROLE_COMPANY_ADMIN"}
                    props={props}
                    userList={userList}
                />
            )}
            <h5>Forbidden Words List</h5>
            {isAdmin && <ForbiddenList />}
        </>
    );
}

export default SuperAdminPage;
