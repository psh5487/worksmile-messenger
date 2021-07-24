import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { withRouter, Link, Router, Switch, Route } from "react-router-dom";
import { Container, Row, Col, Form, Button } from "react-bootstrap";

import { login, init } from "../reducers/user";
import { AUTH_URL } from "../utils/constant";
import { connect } from "../reducers/stomp";
import Header from "../components/header";

import axios from "axios";
import Cookies from "js-cookie";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

import "./auth.css";

function LoginPage(props) {
    const dispatch = useDispatch();

    const [uid, setUid] = useState("");
    const [pwd, setPwd] = useState("");

    useEffect(() => {
        dispatch(init());
    }, []);

    const onUidHandler = (e) => {
        setUid(e.currentTarget.value);
    };

    const onPasswordHanlder = (e) => {
        setPwd(e.currentTarget.value);
    };

    // 로그인 처리 로직
    const onSubmitHandler = (e) => {
        e.preventDefault();

        const reqBody = {
            uid: uid,
            pwd: pwd,
        };

        axios({
            method: "post",
            url: AUTH_URL + "/api/auth/login",
            data: reqBody,
        })
            .then((res) => {
                console.log("로그인 결과 ", res);
                if (res.data.status === 200) {
                    const user = res.data.data.user;

                    // login action 생성, Store 갱신
                    dispatch(login(user));

                    Cookies.set("X-Auth-Token", res.data.data["X-Auth-Token"]);

                    // Web Socket CONN
                    // const socket = new SockJS("http://localhost:8086/ws");
                    const socket = new SockJS("http://52.198.41.19:8080/ws");
                    const stompClient = Stomp.over(socket);
                    const token = Cookies.get("X-Auth-Token");
                    stompClient.connect(
                        {
                            "X-Auth-Token": token,
                            uid: user.uid,
                        },
                        function (data) {
                            console.log("Connected: ", data);
                            dispatch(connect(stompClient));
                            props.history.push("/chat/rooms/user");
                        }
                    );
                } else {
                    alert("로그인 정보가 올바르지 않습니다.");
                    props.history.push("/");
                }
            })
            .catch((err) => {
                alert(err);
                props.history.push("/");
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
                    <b>로그인</b>
                </h2>
                <br />
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
                    <Form.Group controlId="formPassword">
                        <Form.Control
                            type="password"
                            value={pwd}
                            onChange={onPasswordHanlder}
                            placeholder="비밀번호"
                            required
                        />
                    </Form.Group>
                    <Button type="submit">로그인</Button>
                </Form>
                <br />
            </div>

            <Container>
                <Row className="justify-content-md-center">
                    <Col md="auto">
                        <Link
                            to="/user/help/find/id"
                            style={{ textDecoration: "none" }}
                        >
                            아이디 찾기
                        </Link>{" "}
                        |<Link to="/user/help/find/pw"> 비밀번호 찾기</Link> |
                        <Link to="/user/join/emp"> 일반 회원가입</Link> |
                        <Link to="/user/join/exec"> 임원 회원가입</Link>
                    </Col>
                </Row>
            </Container>
        </>
    );
}

export default withRouter(LoginPage);
