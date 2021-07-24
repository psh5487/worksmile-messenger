import React, { useEffect, useState } from "react";
import { withRouter } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { logout } from "../reducers/user";
import axios from "axios";
import Cookies from "js-cookie";
import { BASE_URL } from "../utils/constant";
import { Button, Table } from "react-bootstrap";
import Header from "../components/header";

function UserPage(props) {
    // useSelector로 현재 사용자 state 조회
    const userInfo = useSelector((state) => state.user.userInfo);

    // dispatch 사용하기
    const dispatch = useDispatch();

    /* 로그아웃 처리 */
    const onClickHandler = () => {
        const reqBody = {
            userId: userInfo.userId,
        };

        axios({
            method: "post",
            url: BASE_URL + "/api/user/logout",
            data: reqBody,
        })
            .then((res) => {
                // Cookie에서 토큰 제거하기
                Cookies.remove("access-token");
                Cookies.remove("refresh-token");

                // user state 변화주기
                dispatch(logout());

                // 로그인 페이지로 이동
                props.history.push("/login");
            })
            .catch((err) => {
                console.log(err);
            });
    };

    return (
        <>
            <Header />
            <h3>User Page</h3>
            <Button variant="warning" type="submit" onClick={onClickHandler}>
                로그아웃
            </Button>
            <br />
            <br />

            <h3>My Info</h3>
            {userInfo && (
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>My Id</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Role</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>{userInfo.userId}</td>
                            <td>{userInfo.name}</td>
                            <td>{userInfo.email}</td>
                            <td>{userInfo.role}</td>
                        </tr>
                    </tbody>
                </Table>
            )}
            <br />
        </>
    );
}

export default withRouter(UserPage);
