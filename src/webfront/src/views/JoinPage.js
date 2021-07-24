import React, { useEffect, useRef, useState } from "react";
import { withRouter } from "react-router-dom";
import { Form, Button } from "react-bootstrap";

import { AUTH_URL, BASE_URL, USER_URL } from "../utils/constant";
import Header from "../components/header";

import axios from "axios";

import "./auth.css";

function JoinPage(props) {
    const [uid, setUid] = useState("");
    const [pwd, setPwd] = useState("");
    const [confirmPwd, setConfirmPwd] = useState("");
    const [uname, setUname] = useState("");
    const [email, setEmail] = useState("");
    const [phone, setPhone] = useState("");
    const [companyId, setCompanyId] = useState("");
    const [companyName, setCompanyName] = useState("");
    const [uniqueId, setUniqueId] = useState("fail");
    const [companys, setCompanys] = useState([]);

    const uidRef = useRef();

    // 회사 리스트 받아오기
    useEffect(() => {
        axios({
            method: "get",
            url: USER_URL + "/api/user/companylist/subroot",
        })
            .then((res) => {
                console.log("회사 리스트 : ", res.data);
                setCompanys(res.data.data);
            })
            .catch((err) => {
                alert("회사 리스트를 받아올 수 없습니다.");
            });
    }, []);

    const onUserIdHandler = (e) => {
        setUid(e.currentTarget.value);
    };

    const onPasswordHanlder = (e) => {
        setPwd(e.currentTarget.value);
    };

    const onConfirmPasswordHandler = (e) => {
        setConfirmPwd(e.currentTarget.value);
    };

    const onNameHandler = (e) => {
        setUname(e.currentTarget.value);
    };

    const onEmailHandler = (e) => {
        setEmail(e.currentTarget.value);
    };

    const onPhoneHandler = (e) => {
        setPhone(e.currentTarget.value);
    };

    const onCampanyIdHandler = (e) => {
        setCompanyId(e.currentTarget.value);

        const cid = e.currentTarget.value;
        const company = companys.find((c) => {
            return cid == c.cid;
        });
        setCompanyName(company.cname);
    };

    const onCampanyNameHandler = (e) => {
        setCompanyName(e.currentTarget.value);
        setCompanyId(0);
    };

    // userId 중복확인
    const onBlurUserId = (e) => {
        const reqBody = {
            uid: uid,
        };

        if (uid === "") {
            alert("ID를 입력해주세요");
            setUniqueId("fail");
            return false;
        }

        if (uid.replace(/(\s*)/g, "").length !== uid.length) {
            alert("ID에 공백을 넣을 수 없습니다.");
            setUniqueId("fail");
            return false;
        }

        if (checkIdPattern(uid)) {
            alert("ID에 특수문자는 들어갈 수 없습니다.");
            return false;
        }

        axios({
            method: "post",
            url: AUTH_URL + "/api/auth/id/unique",
            data: reqBody,
        })
            .then((res) => {
                console.log("ID 중복확인 결과 : ", res);
                if (res.data.status === 200) {
                    alert("사용 가능한 아이디입니다.");
                    setUniqueId("pass");
                } else {
                    alert("이미 사용 중인 아이디입니다.");
                    setUniqueId("fail");
                    // uidRef.current.focus();
                }
            })
            .catch((err) => {
                alert(err);
                setUniqueId("fail");
            });
    };

    // ID 유효성 검사 패턴
    const checkIdPattern = (uid) => {
        var pattern = /[~!@#$%^&*()_+|<>?:{}]/;
        if (pattern.test(uid)) {
            return true;
        }
        return false;
    };

    // Join 처리 로직
    const onSubmitHandler = (e) => {
        e.preventDefault();

        // 중복 검사
        if (uniqueId !== "pass") {
            alert("ID 중복검사가 완료되지 않았습니다.");
            return false;
        }

        // pwd 유효성 검사
        if (pwd !== confirmPwd) {
            alert("비밀번호가 일치하지 않습니다.");
            return false;
        }

        // uid 유효성 검사
        if (checkIdPattern(uid)) {
            alert("ID에 특수문자는 들어갈 수 없습니다.");
            return false;
        }

        const reqBody = {
            type:
                props.match.params.type === "emp"
                    ? "userRegister"
                    : "adminRegister",
            uid: uid,
            pwd: pwd,
            uname: uname,
            email: email,
            phone: phone,
            subroot_cid: companyId,
            subroot_cname: companyName,
        };

        axios({
            method: "post",
            url: AUTH_URL + "/api/auth/join",
            data: reqBody,
        })
            .then((res) => {
                console.log("회원가입 처리 결과 : ", res);
                if (res.data.status === 200) {
                    alert("회원가입이 요청되었습니다.");
                    props.history.push("/user/login");
                } else {
                    console.log("회원가입에 실패했습니다. 다시 요청해주세요.");
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
                    {props.match.params.type === "emp" ? (
                        <b> 회원가입</b>
                    ) : (
                        <b>임원 회원가입</b>
                    )}
                </h2>
                <br />
                <Form onSubmit={onSubmitHandler}>
                    <Form.Group controlId="formUserId">
                        <Form.Control
                            type="text"
                            value={uid}
                            onChange={onUserIdHandler}
                            onBlur={onBlurUserId}
                            placeholder="아이디"
                            ref={uidRef}
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
                    <Form.Group controlId="formConfirmPassword">
                        <Form.Control
                            type="password"
                            value={confirmPwd}
                            onChange={onConfirmPasswordHandler}
                            placeholder="비밀번호 확인"
                            required
                        />
                    </Form.Group>
                    <Form.Group controlId="formName">
                        <Form.Control
                            type="text"
                            value={uname}
                            onChange={onNameHandler}
                            placeholder="이름"
                            required
                        />
                    </Form.Group>
                    <Form.Group controlId="formEmail">
                        <Form.Control
                            type="email"
                            value={email}
                            onChange={onEmailHandler}
                            placeholder="이메일"
                            required
                        />
                    </Form.Group>
                    <Form.Group controlId="formPhone">
                        <Form.Control
                            type="text"
                            value={phone}
                            onChange={onPhoneHandler}
                            placeholder="휴대전화"
                            required
                        />
                    </Form.Group>
                    {/* 조건부 렌더링 -- 삼항연산자 사용 
                    링크 : https://velog.io/@hidaehyunlee/React-%EB%A6%AC%ED%8C%A9%ED%86%A0%EB%A7%81-2-JSX%EC%97%90%EC%84%9C-%EC%A1%B0%EA%B1%B4%EB%AC%B8-%EC%82%AC%EC%9A%A9%ED%95%B4-%EB%A0%8C%EB%8D%94%EB%A7%81%ED%95%98%EA%B8%B0*/}
                    {props.match.params.type === "emp" ? (
                        <Form.Group controlId="formCompany">
                            <Form.Control
                                as="select"
                                value={companyId}
                                onChange={onCampanyIdHandler}
                            >
                                {companys.map((company, index) => (
                                    <option key={index} value={company.cid}>
                                        {company.cname}
                                    </option>
                                ))}
                            </Form.Control>
                        </Form.Group>
                    ) : (
                        <Form.Group controlId="formCompany">
                            <Form.Control
                                type="text"
                                value={companyName}
                                onChange={onCampanyNameHandler}
                                placeholder="회사명"
                                required
                            />
                        </Form.Group>
                    )}
                    <Button type="submit">가입하기</Button>
                </Form>
            </div>
        </>
    );
}

export default withRouter(JoinPage);
