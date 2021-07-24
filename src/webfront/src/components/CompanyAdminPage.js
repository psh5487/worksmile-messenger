import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";

import { USER_URL } from "../utils/constant";
import UserDataList from "./UserDataList";

import Cookies from "js-cookie";
import axios from "axios";

function SuperAdminPage({ props, isAdmin }) {
    // useSelector로 현재 사용자 state 조회
    const user = useSelector((state) => state.user.userInfo);

    // 전체 Worksmile 사용자 리스트
    const [userList, setUserList] = useState([]);

    /* 회원 표 그리기 */
    useEffect(() => {
        const jwtToken = Cookies.get("access-token");

        // 사용자 리스트 받기
        axios({
            method: "get",
            url:
                USER_URL +
                "/api/admin/company/users/company/" +
                user.subroot_cid,
            headers: {
                "access-token": jwtToken,
            },
        })
            .then((res) => {
                console.log("company admin 사용자 결과 : ", res);
                if (res.data.status === 200) {
                    setUserList(res.data.data);
                }
            })
            .catch((err) => {
                console.log(err);
            });
    }, []);

    return (
        <>
            <h3>Company Admin Page</h3>
            <br />

            <h5>Not Permitted User</h5>
            {isAdmin && (
                <UserDataList
                    role={"ROLE_NOT_PERMITTED_USER"}
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
