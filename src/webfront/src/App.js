import "./reset.css";
import React from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import LoginPage from "./views/LoginPage";
import HelpId from "./views/HelpId";
import HelpPw from "./views/HelpPw";
import JoinPage from "./views/JoinPage";
import ChatMain from "./views/ChatMain";
import ChatRoom from "./views/ChatRoom";
import AdminPage from "./views/AdminPage";
import OrganizationPage from "./views/OrganizationPage";

function App() {
    return (
        <Router>
            <Switch>
                {/* option =>  null: 아무나 접근 가능, true: 로그인한 유저만 접근 가능, false: 로그인한 유저는 접근 불가능 */}
                <Route exact path="/" component={LoginPage} />
                {/* component={Auth(UserPage, true) */}
                <Route exact path="/admin/:option" component={AdminPage} />
                {/* component={Auth(AdminPage, true) */}
                <Route exact path="/user/login" component={LoginPage} />
                {/* component={Auth(LoginPage, false) */}

                <Route exact path="/user/join/:type" component={JoinPage} />

                <Route exact path="/user/help/find/id" component={HelpId} />
                <Route exact path="/user/help/find/pw" component={HelpPw} />
                {/* <Route exact path="/db" component={ApiTest} /> */}

                <Route exact path="/messages/room/" component={ChatRoom} />

                <Route
                    exact
                    path="/chat/rooms/user"
                    component={ChatMain}
                ></Route>

                <Route
                    exact
                    path="/organization/root-company"
                    component={OrganizationPage}
                ></Route>

                <Route
                    render={({ location }) => (
                        <div>
                            <h2>NOT FOUND 404</h2>
                        </div>
                    )}
                />
            </Switch>
        </Router>
    );
}

export default App;
