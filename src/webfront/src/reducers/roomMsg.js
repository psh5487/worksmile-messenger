/* 초기 상태 선언 */
export const initialRoomMsg = {
    // 채팅방 목록의 채팅방 접속 시, 가져올 메시지
    msgs: [],
};

/* 액션 타입 선언 */
const ADD_ROOM_MSG = "roomMsg/ADD_ROOM_MSG";
const UPDATE_ROOM_MSG = "roomMsg/UPDATE_ROOM_MSG";

/* 액션 생성 함수 */
export const addRoomMsgs = (roomMsgs) => ({
    type: ADD_ROOM_MSG,
    data: roomMsgs,
});

export const updateRoomMsgs = (roomMsgs) => ({
    type: UPDATE_ROOM_MSG,
    data: roomMsgs,
});

/* 리듀서 선언 */
const roomMsgReducer = (state = initialRoomMsg, action) => {
    const msgs = state["msgs"];
    switch (action.type) {
        case ADD_ROOM_MSG:
            return { msgs: ("msgs", msgs.concat(action.data)) };

        case UPDATE_ROOM_MSG:
            return { msgs: action.data };

        default:
            return { ...state };
    }
};

export default roomMsgReducer;
