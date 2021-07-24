import React, { useEffect, useState } from "react";
import { withRouter } from "react-router-dom";
import { useSelector } from "react-redux";
import axios from "axios";
import Cookies from "js-cookie";
import { BASE_URL } from "../utils/constant";
import { Table } from "react-bootstrap";
import Header from "../components/header";

function AdminPage(props) {
    // useSelector로 현재 사용자 state 조회
    const userInfo = useSelector((state) => state.user.userInfo);
    let isAdmin = false;

    if (userInfo) {
        if (userInfo.role === "ROLE_SUPER_ADMIN") {
            console.log("role admin");
            isAdmin = true;
        }
    }

    // 전체 Worksmile 사용자 리스트
    const [userList, setUserList] = useState([]);

    /* 회원 표 그리기 */
    useEffect(() => {
        // Access Token 쿠키에서 읽고, 인증 요청 헤더에 넣기
        const jwtToken = Cookies.get("access-token");

        axios({
            method: "get",
            url: BASE_URL + "/api/super-admin/users/company-admins",
            headers: {
                "access-token": jwtToken,
            },
        })
            .then((res) => {
                setUserList(res.data);
            })
            .catch((err) => {
                console.log(err);
            });
    }, []);

    return (
        <>
            <Header />
            <h3>Admin Page</h3>
            <br />

            <h3>All User Info</h3>
            {isAdmin && (
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>User Id</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Role</th>
                        </tr>
                    </thead>
                    <tbody>
                        {userList.map((u) => (
                            <tr key={u.userId}>
                                <td>{u.userId}</td>
                                <td>{u.name}</td>
                                <td>{u.email}</td>
                                <td>{u.role}</td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            )}
        </>
    );
}

export default withRouter(AdminPage);
