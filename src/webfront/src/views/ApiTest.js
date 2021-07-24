import axios from "axios";
import React, { useState } from "react";

function ApiTest() {
    const [message1, setMessage1] = useState("");
    const [message2, setMessage2] = useState({
        cid: 0,
        cname: "",
    });

    const onClickHandler = () => {
        const reqBody = {
            name: "yslimc",
            age: "30",
            phone: "01000000000",
        };

        axios({
            method: "post",
            url: "http://localhost:3000/api/db",
            data: reqBody,
        }).then((res) => {
            console.log(res);
            console.log(res.data);
            setMessage1(res.data);
        });
    };

    const onClickHandler2 = () => {
        const reqBody2 = {
            cid: 2,
        };

        axios({
            method: "post",
            url: "http://localhost:3000/api/cp",
            data: reqBody2,
        }).then((res) => {
            console.log(res);
            console.log(res.data);
            setMessage2({
                cid: res.data.cid,
                cname: res.data.cname,
            });
        });
    };

    return (
        <>
            <button onClick={onClickHandler}>CLICK</button>
            <h1 className="App-title">{message1}</h1>

            <button onClick={onClickHandler2}>CLICK2</button>
            <h1 className="App-title">
                {message2.cid} {message2.cname}
            </h1>
        </>
    );
}

export default ApiTest;
