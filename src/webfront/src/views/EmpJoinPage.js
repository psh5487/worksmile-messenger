import React, { useState } from "react";
import { Button } from "react-bootstrap";
import { withRouter } from "react-router-dom";
import JoinPage from "./JoinPage";

function EmpJoinPage() {
    return <JoinPage type="일반" />;
}

export default withRouter(EmpJoinPage);
