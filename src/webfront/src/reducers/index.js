import { combineReducers } from "redux";
import user from "./user";
import stomp from "./stomp";
import admin from "./admin";
import forbidden from "./forbidden";
import roomconn from "./roomconn";
import roomInfo from "./roominfo";
import roomMsg from "./roomMsg";

const rootReducer = combineReducers({
    user,
    stomp,
    admin,
    forbidden,
    roomconn,
    roomInfo,
    roomMsg,
});

export default rootReducer;
