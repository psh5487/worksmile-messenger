import axios from "axios";
import React, { useEffect, useState } from "react";
import { Button, Col, Form, Modal, Row } from "react-bootstrap";
import { useSelector } from "react-redux";
import { ROLE_LIST, USER_URL } from "../utils/constant";
import DeleteModal from "./DeleteModal";

function UserData({ props, user }) {
    const companys = useSelector((state) => state.admin.companys);
    const departments = useSelector((state) => state.admin.departments);
    const positions = useSelector((state) => state.admin.positions);

    const [company, setCompany] = useState(user.rootCid);
    const [department, setDepartment] = useState(user.subrootCid);
    const [position, setPosition] = useState(user.pid);
    const [role, setRole] = useState(user.role);

    const onCampanyHandler = (e) => {
        console.log(e.currentTarget.value);
        setCompany(e.currentTarget.value);
    };

    const onDepartmentHandler = (e) => {
        console.log(e.currentTarget.value);
        setDepartment(e.currentTarget.value);
    };

    const onPositionHandler = (e) => {
        console.log(e.currentTarget.value);
        setPosition(e.currentTarget.value);
    };

    const onRoleHandler = (e) => {
        console.log(e.currentTarget.value);
        setRole(e.currentTarget.value);
    };

    const userUpdate = () => {
        const reqBody = {
            uid: user.uid,
            subrootCid: company,
            cid: department,
            pid: position,
            role: role,
        };
        axios({
            method: "put",
            url: USER_URL + "/api/admin/user",
            data: reqBody,
        })
            .then((res) => {
                console.log(res.data);
                const data = res.data.data;
                // console.log(props.history.location.pathname);
                alert("사용자 정보가 수정되었습니다.");
                setCompany(data.subrootCid);
                setDepartment(data.cid);
                setPosition(data.pid);
                setRole(data.role);
            })
            .catch((err) => {
                alert(err);
            });
    };

    return (
        <tr key={user.uid}>
            <td className="admin-table">{user.uid}</td>
            <td className="admin-table">{user.uname}</td>
            <td className="admin-table">
                <Form.Group controlId="formCompany">
                    <Form.Control
                        as="select"
                        onChange={onCampanyHandler}
                        defaultValue={user.subrootCid}
                    >
                        {companys.map((company, index) => (
                            <option key={index} value={company.cid}>
                                {company.cname}
                            </option>
                        ))}
                    </Form.Control>
                </Form.Group>
            </td>
            <td className="admin-table">
                <Form.Group controlId="formDepartment">
                    <Form.Control
                        as="select"
                        onChange={onDepartmentHandler}
                        defaultValue={user.cid}
                    >
                        {departments
                            .filter((department) => {
                                return department.cid !== 0;
                            })
                            .map((department, index) => (
                                <option key={index} value={department.cid}>
                                    {department.cname}
                                </option>
                            ))}
                    </Form.Control>
                </Form.Group>
            </td>
            <td className="admin-table">
                <Form.Group controlId="formPosition">
                    <Form.Control
                        as="select"
                        onChange={onPositionHandler}
                        defaultValue={user.pid}
                    >
                        {positions
                            .filter((position) => {
                                return position.pid !== 0;
                            })
                            .map((position, index) => (
                                <option key={index} value={position.pid}>
                                    {position.pname}
                                </option>
                            ))}
                    </Form.Control>
                </Form.Group>
            </td>
            <td className="admin-table">
                <Form.Group controlId="formRole">
                    <Form.Control
                        as="select"
                        onChange={onRoleHandler}
                        defaultValue={user.role}
                    >
                        {ROLE_LIST.map((role, index) => (
                            <option key={index} value={role}>
                                {role}
                            </option>
                        ))}
                    </Form.Control>
                </Form.Group>
            </td>
            <td className="admin-table">{user.email}</td>
            <td className="admin-table">{user.phone}</td>
            <td className="admin-table">{user.registerAt}</td>
            <td className="admin-table">
                <Row className="no-border">
                    <Col>
                        <Button onClick={userUpdate}>수정</Button>
                    </Col>
                    <Col>
                        <DeleteModal user={user} />
                    </Col>
                </Row>
            </td>
        </tr>
    );
}

export default UserData;
