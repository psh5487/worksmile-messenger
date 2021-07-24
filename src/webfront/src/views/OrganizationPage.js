import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { withRouter } from "react-router-dom";

import ChatList from "../components/ChatList";
import Header from "../components/header";
import { USER_URL } from "../utils/constant";
import { unsubscribeOnChat } from "../utils/websocket";

import axios from "axios";

import "./organizationPage.css";

function OrganizationElement({ cid, cname }) {
    return (
        <>
            <div>{cname}</div>
        </>
    );
}

function OrganizationData({ rootCid, companys }) {
    const [organiList, setOrganiList] = useState();
    const onChat = useSelector((state) => state.roomconn.onChat);
    const dispatch = useDispatch();

    useEffect(() => {
        if (onChat !== null && onChat !== undefined) {
            unsubscribeOnChat(dispatch, onChat);
        }

        console.log(rootCid);
        console.log(companys);
        console.log(companys[rootCid]);
        console.log(companys[rootCid].cname);
    }, []);

    return (
        <>
            <div>
                <div></div>
                {/* {companys[rootCid].companys.map((res, index) => (
                    <OrganizationElement
                        cid={res.cid}
                        cname={res.cname}
                        key={index}
                    />
                ))} */}
            </div>
        </>
    );
}

function OrganizationPage(props) {
    const user = useSelector((state) => state.user.userInfo);
    const rootCid = user.root_cid;
    const [organizationData, setOrganizationData] = useState();

    useEffect(() => {
        console.log("조직도 이펙트 " + rootCid);
        axios({
            method: "get",
            url: USER_URL + "/api/user/organization/root-company/" + rootCid,
        })
            .then((res) => {
                console.log(res.data.data);
                if (res.data.status === 200) {
                    setOrganizationData(res.data.data);
                }
            })
            .catch((err) => {
                alert(err);
            });
    }, []);

    return (
        <>
            <Header props={props} />
            <div className="content-wrapper">
                <ChatList />
                <div className="organization-wrapper">
                    <div className="organization-tree">
                        <div>
                            {organizationData !== undefined
                                ? organizationData[rootCid].cname
                                : ""}
                        </div>
                        {organizationData !== undefined ? (
                            <OrganizationData
                                rootCid={rootCid}
                                companys={organizationData}
                            />
                        ) : (
                            <div></div>
                        )}
                    </div>
                    <div className="organization-content">dd</div>
                </div>
            </div>
        </>
    );
}

export default withRouter(OrganizationPage);
