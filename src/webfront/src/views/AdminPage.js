import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { withRouter } from "react-router-dom";

import { getCompanys, getDepartments, getPositions } from "../reducers/admin";
import SuperAdminPage from "../components/SuperAdminPage";
import CompanyAdminPage from "../components/CompanyAdminPage";
import RootCompanyAdminPage from "../components/RootCompanyAdminPage";
import Header from "../components/header";
import { unsubscribeOnChat, unsubscribeOnRoomList } from "../utils/websocket";
import { USER_URL } from "../utils/constant";

import axios from "axios";
import Cookies from "js-cookie";

import "./AdminPage.css";

function AdminPage(props) {
    const dispatch = useDispatch();

    const user = useSelector((state) => state.user.userInfo);
    const stompClient = useSelector((state) => state.stomp.stompClient);
    const onChat = useSelector((state) => state.roomconn.onChat);
    const onRoomList = useSelector((state) => state.roomconn.onRoomList);

    // const [role, setRole] = useState();
    let role = "";
    // const [isAdmin, setIsAdmin] = useState(false);
    let isAdmin = false;
    const pathVar = props.match.params.option;
    switch (pathVar) {
        case "super":
            role = "ROLE_SUPER_ADMIN";
            // setRole("ROLE_SUPER_ADMIN");
            break;
        case "company":
            role = "ROLE_COMPANY_ADMIN";
            // setRole("ROLE_COMPANY_ADMIN");
            break;
        case "root-company":
            role = "ROLE_ROOT_COMPANY_ADMIN";
            // setRole("ROLE_ROOT_COMPANY_ADMIN");
            break;
    }

    if (user.role === role) {
        // setIsAdmin(true);
        isAdmin = true;
    }

    useEffect(() => {
        if (onChat !== null && onChat !== undefined) {
            // unsubscribeOnChat(dispatch, onChat, roomUserInfo, user, stompClient);
        }
        if (onRoomList !== null && onRoomList !== undefined) {
            unsubscribeOnRoomList(dispatch, onRoomList);
        }

        if (!isAdmin) {
            alert("접근 권한이 없습니다.");
            props.history.push("/");
        }

        // 회사 데이터 받아오기
        axios({
            method: "get",
            url: USER_URL + "/api/user/companylist/subroot",
        })
            .then((res) => {
                console.log("회사 데이터 결과 : ", res);
                dispatch(getCompanys(res.data.data));
            })
            .catch((err) => {
                alert(err);
            });

        // 부서 데이터 받아오기
        axios({
            method: "get",
            url: USER_URL + "/api/user/companylist/not-subroot",
        })
            .then((res) => {
                console.log("부서 데이터 결과 : ", res);
                dispatch(getDepartments(res.data.data));
            })
            .catch((err) => {
                alert(err);
            });

        // 직책 데이터
        axios({
            method: "get",
            url: USER_URL + "/api/user/positionlist",
        })
            .then((res) => {
                console.log("직책 데이터 결과 : ", res);
                dispatch(getPositions(res.data.data));
            })
            .catch((err) => {
                alert(err);
            });
    }, []);

    return (
        <>
            <Header props={props} />
            {role === "ROLE_COMPANY_ADMIN" ? (
                <CompanyAdminPage isAdmin={isAdmin} props={props} />
            ) : role === "ROLE_SUPER_ADMIN" ? (
                <SuperAdminPage isAdmin={isAdmin} props={props} />
            ) : (
                <RootCompanyAdminPage isAdmin={isAdmin} props={props} />
            )}
        </>
    );
}

export default withRouter(AdminPage);
