import React, { useState } from "react";
import { Button, Col, FormControl, InputGroup, Row } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";

import { USER_URL } from "../utils/constant";
import { update } from "../reducers/forbidden";

import axios from "axios";

import "./Forbidden.css";

function ForbiddenList() {
    const dispatch = useDispatch();

    const forbiddens = useSelector((state) => state.forbidden.forbiddens);

    const [word, setWord] = useState("");

    const wordDelete = (word) => {
        console.log(word);
        const reqBody = {
            wid: word.wid,
            word: word.word,
        };
        axios({
            method: "delete",
            url: USER_URL + "/api/admin/super/forbidden-words",
            data: reqBody,
        })
            .then((res) => {
                console.log("비속어 삭제 결과 : ", res);
                if (res.data.status === 200) {
                    dispatch(update(res.data.data));
                }
            })
            .catch((err) => {
                alert(err);
            });
    };

    const onWordHandler = (e) => {
        setWord(e.currentTarget.value);
    };

    const wordCreate = () => {
        if (word.trim().length !== word.length) {
            alert("문자의 앞과 뒤에 공백을 추가할 수 없습니다.");
            return false;
        }

        const reqBody = {
            word: word,
        };
        axios({
            method: "POST",
            url: USER_URL + "/api/admin/super/forbidden-words",
            data: reqBody,
        })
            .then((res) => {
                console.log("비속어 추가 결과 : ", res);
                if (res.data.status === 200) {
                    dispatch(update(res.data.data));
                }
            })
            .catch((err) => {
                alert(err);
            });
    };

    return (
        <>
            <div className="forbidden-list">
                {forbiddens.map((word, index) => (
                    <Row key={index} className="forbidden-row">
                        <Col>{word.wid}</Col>
                        <Col>{word.word}</Col>
                        <Col>
                            <Button onClick={() => wordDelete(word)}>
                                삭제
                            </Button>
                        </Col>
                    </Row>
                ))}
            </div>
            <div className="update-forbidden">
                <InputGroup className="mb-3">
                    <FormControl
                        placeholder="금지어 추가"
                        aria-label="Recipient's username"
                        aria-describedby="basic-addon2"
                        onChange={onWordHandler}
                    />
                    <InputGroup.Append>
                        <Button onClick={wordCreate}>추가</Button>
                    </InputGroup.Append>
                </InputGroup>
            </div>
        </>
    );
}

export default ForbiddenList;
