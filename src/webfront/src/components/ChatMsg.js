import React from "react";

import "./ChatMsg.css";

function ChatMsg({ msg, uid, key }) {
    const msgId = "msg" + msg.midx;

    return (
        <>
            {msg.sender === uid ? (
                <div id={msgId} className="my-message" key={key}>
                    <span className="msg-time">{msg.created_at}</span>
                    <br />
                    <span className="msg-content">{msg.content}</span>
                </div>
            ) : (
                <div id={msgId} className="not-my-message" key={key}>
                    <span className="msg-uname">{msg.uname}</span>{" "}
                    <span className="msg-pname">{msg.pname}</span> {" / "}
                    <span className="msg-cname">{msg.cname}</span> {"  "}
                    <span className="msg-time">{msg.created_at}</span>
                    <br />
                    <span className="msg-content">{msg.content}</span>
                </div>
            )}
        </>
    );
}

export default ChatMsg;
