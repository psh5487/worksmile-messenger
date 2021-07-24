import React, { useRef, useState } from "react";
import { Button, Form } from "react-bootstrap";
import { Link, withRouter } from "react-router-dom";

import { AUTH_URL, BASE_URL } from "../utils/constant";
import Header from "../components/header";

import axios from "axios";

import "./auth.css";

function HelpId(props) {
    const [email, setEmail] = useState("");
    const emailRef = useRef();

    const onEmailHandler = (e) => {
        setEmail(e.currentTarget.value);
    };

    const onSubmitHandler = (e) => {
        e.preventDefault();
        const reqBody = {
            email: email,
        };

        // 입력된 이메일로 RDB worksmile_users 테이블의 uid 조회
        axios({
            method: "POST",
            url: AUTH_URL + "/api/auth/help/find/id",
            data: reqBody,
        })
            .then((res) => {
                console.log("ID 찾기 결과 : ", res);
                if (res.data.status === 200) {
                    alert("아이디는 " + res.data.data.uid + " 입니다.");
                } else {
                    alert("ID를 찾을 수 없습니다.");
                    emailRef.current.focus();
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
                    <b>아이디 찾기</b>
                </h2>
                <br />
                <p>회원 정보에 등록한 이메일 주소를 입력해주세요</p>
                <Form onSubmit={onSubmitHandler}>
                    <Form.Group controlId="formUserId">
                        <Form.Control
                            type="email"
                            value={email}
                            onChange={onEmailHandler}
                            placeholder="이메일"
                            required
                            ref={emailRef}
                        />
                    </Form.Group>
                    <Button type="submit">찾기</Button>
                </Form>
                <Link to="/user/help/find/pw">
                    <Button>비밀번호 찾기</Button>
                </Link>
            </div>
        </>
    );
}

export default withRouter(HelpId);
