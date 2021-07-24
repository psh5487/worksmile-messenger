/* 초기 상태 선언 */
export const initialRoomConn = {
    // 좌측 채팅방 목록을 바라보고 있을 때, 채팅방 리스트의 채팅방들 구독 정보
    onRoomList: null,
    // 우층 채팅창을 바라보고 있을 때, 채팅방 구독 정보
    onChat: null,
    // 이전 채팅방 구독 정보
    beforeOnChat: null,
};

/* 액션 타입 선언 */
const ON_ROOM_CONN = "roomconn/ON_ROOM_CONN";
const ON_CHAT_CONN = "roomconn/ON_CHAT_CONN";
const OFF_ROOM_CONN = "roomconn/OFF_ROOM_CONN";
const OFF_CHAT_CONN = "roomconn/OFF_CHAT_CONN";

/* 액션 생성 함수 */
export const createRoomConn = (roomConn) => ({
    type: ON_ROOM_CONN,
    data: roomConn,
});

export const createChatConn = (chatConn) => ({
    type: ON_CHAT_CONN,
    data: chatConn,
});

export const deleteRoomConn = () => ({
    type: OFF_ROOM_CONN,
});

export const deleteChatConn = () => ({
    type: OFF_CHAT_CONN,
});

/* 리듀서 선언 */
const roomConnReducer = (state = initialRoomConn, action) => {
    switch (action.type) {
        case ON_ROOM_CONN:
            return { ...state, onRoomList: action.data };

        case ON_CHAT_CONN:
            return {
                ...state,
                beforeOnChat: state.onChat,
                onChat: action.data,
            };

        case OFF_ROOM_CONN:
            return { ...state, onRoomList: null };

        case OFF_CHAT_CONN:
            return { ...state, beforeOnChat: state.onChat, onChat: null };

        default:
            return { ...state };
    }
};

export default roomConnReducer;
