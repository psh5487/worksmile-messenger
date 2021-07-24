import "./ChatMain.css";
import React, { useEffect, useState } from "react";
import { withRouter } from "react-router-dom";
import ChatList from "../components/ChatList";
import { useSelector } from "react-redux";
import Header from "../components/header";

function ChatMain(props) {
    const user = useSelector((state) => state.user.userInfo);

    // 로그인 여부 검사
    useEffect(() => {
        if (JSON.stringify(user) === "{}") {
            alert("잘못된 접근입니다.");
            props.history.push("/");
        }
    }, []);

    return (
        <>
            <Header props={props} />
            <div className="content-wrapper">
                <ChatList />
                <div className="waiting-container">
                    <div className="content-container">
                        <div className="img-container">
                            <img
                                alt="worksmile"
                                src="/imgs/worksmile_logo.png"
                                width="200px"
                            />
                        </div>
                        <div className="title">대화를 시작하세요.</div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default withRouter(ChatMain);
