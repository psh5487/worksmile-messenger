/* 초기 상태 선언 */
export const initialRoomInfo = {
    // 채팅방 목록의 채팅방 정보
    rooms: [],
    // 채팅방 목록의 마지막 메시지 정보
    lastMsgs: [],
};

/* 액션 타입 선언 */
const UPDATE_ROOM = "roominfo/UPDATE_ROOM";
const UPDATE_LAST_MSG = "roominfo/UPDATE_LAST_MSG";
const INIT_ROOM = "roominfo/INIT_ROOM";

/* 액션 생성 함수 */
export const updateRooms = (rooms) => ({
    type: UPDATE_ROOM,
    data: rooms,
});

export const updateLastMsg = (lastMsgs) => ({
    type: UPDATE_LAST_MSG,
    data: lastMsgs,
});

export const initRooms = (rooms) => ({
    type: INIT_ROOM,
    data: rooms,
});

/* 리듀서 선언 */
const roomInfoReducer = (state = initialRoomInfo, action) => {
    switch (action.type) {
        case UPDATE_ROOM:
            return {
                ...state,
                rooms: ("rooms", state["rooms"].concat(action.data)),
            };

        case UPDATE_LAST_MSG:
            return { ...state, lastMsgs: action.data };

        case INIT_ROOM:
            return { ...state, rooms: action.data };

        default:
            return { ...state };
    }
};

export default roomInfoReducer;
