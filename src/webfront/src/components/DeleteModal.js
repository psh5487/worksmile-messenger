import React, { useState } from "react";
import { Button, Modal } from "react-bootstrap";

import { USER_URL } from "../utils/constant";

import axios from "axios";

function DeleteModal({ user }) {
    const [show, setShow] = useState(false);

    const userDelete = () => {
        const reqBody = {
            uid: user.uid,
            subrootCid: -1,
            cid: -1,
            pid: -1,
            role: "",
        };
        axios({
            method: "delete",
            url: USER_URL + "/api/admin/user",
            data: reqBody,
        })
            .then((res) => {
                console.log("유저 삭제 결과 : ", res);
                if (res.data.status === 200) {
                    alert("사용자 정보가 삭제되었습니다.");
                } else {
                    alert("삭제 중 문제가 발생했습니다.");
                }
            })
            .catch((err) => {
                alert(err);
            });
        handleClose();
    };
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    return (
        <>
            <Button onClick={handleShow}>삭제</Button>

            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>사용자 삭제</Modal.Title>
                </Modal.Header>
                <Modal.Body>사용자가 영구적으로 삭제됩니다.</Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={userDelete}>
                        삭제하기
                    </Button>
                    <Button variant="secondary" onClick={handleClose}>
                        닫기
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default DeleteModal;
