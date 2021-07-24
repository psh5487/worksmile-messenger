import "./ChatHome.css";
import React from "react";

function ChatHome() {
    return (
        <>
            <div className="right-container">
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
        </>
    );
}

export default ChatHome;
