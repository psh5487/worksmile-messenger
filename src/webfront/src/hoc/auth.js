import React, { useEffect } from "react";
import { useDispatch } from "react-redux";
import { login } from "../reducers/user";
import axios from "axios";
import { BASE_URL } from "../utils/constant";
import Cookies from "js-cookie";

function Auth(Component, option, props) {
    // dispatch 사용하기
    const dispatch = useDispatch();

    function AuthCheck(props) {
        // Access Token 쿠키에서 읽고, 인증 요청 헤더에 넣기
        const jwtToken = Cookies.get("x-auth-token");

        useEffect(() => {
            axios({
                method: "get",
                // url: BASE_URL + "/api/loggedInUser/myInfo",
                url: "/api/loggedInUser/myInfo",
                headers: {
                    "x-auth-token": jwtToken,
                },
            })
                .then((res) => {
                    console.log("Auth check");
                    const user = res.data;
                    console.log(user);

                    // 자동 로그인. user state 변화주기
                    dispatch(login(user));

                    // Cookie에 토큰 저장
                    Cookies.set("x-auth-token", res.headers["x-auth-token"]);
                })
                .catch((err) => {
                    console.log(err);
                    props.history.push("/login");
                });
        }, []);

        return <Component />;
    }

    return AuthCheck;
}

export default Auth;
