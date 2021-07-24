import React, { useState } from "react";
import { Button, Form } from "react-bootstrap";
import { Link, withRouter } from "react-router-dom";

import { AUTH_URL, BASE_URL } from "../utils/constant";
import Header from "../components/header";

import axios from "axios";

import "./auth.css";

function HelpPw(props) {
    const [uid, setUid] = useState("");

    const onUidHandler = (e) => {
        setUid(e.currentTarget.value);
    };

    const onSubmitHandler = (e) => {
        e.preventDefault();
        const reqBody = {
            uid: uid,
        };

        // 입력된 아이디로 RDB worksmile_users 테이블 조회
        axios({
            method: "POST",
            url: AUTH_URL + "/api/auth/help/pwd/email",
            data: reqBody,
        })
            .then((res) => {
                console.log("PW 찾기 결과 : ", res);
                if (res.data.status === 200) {
                    alert(
                        "비밀번호 초기화. \n가입된 이메일을 확인해주시기 바랍니다."
                    );
                } else {
                    alert("입력한 사용자가 존재하지 않습니다.");
                }
            })
            .catch((err) => {
                alert(err);
            });
    };

    return (
        <>
            <Header props={props} />
            <div className="myform">
                <img
                    alt="worksmile"
                    src="/imgs/worksmile_logo.png"
                    width="150"
                    height="150"
                />
                <br />
                <h2>
                    <b>비밀번호 찾기</b>
                </h2>
                <br />
                <p>회원 정보에 등록한 아이디를 입력해주세요</p>
                <Form onSubmit={onSubmitHandler}>
                    <Form.Group controlId="formUserId">
                        <Form.Control
                            type="text"
                            value={uid}
                            onChange={onUidHandler}
                            placeholder="아이디"
                            required
                        />
                    </Form.Group>
                    <Button type="submit">전송</Button>
                </Form>
                <Link to="/user/help/find/id">
                    <Button>아이디 찾기</Button>
                </Link>
            </div>
        </>
    );
}

export default withRouter(HelpPw);
