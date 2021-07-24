import React, { useState } from "react";
import { withRouter } from "react-router-dom";
import { Form, Button } from "react-bootstrap";
import JoinPage from "./JoinPage";

function ExecJoinPage() {
    return <JoinPage type="임원" />;
}

export default withRouter(ExecJoinPage);
