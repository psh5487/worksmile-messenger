/* 초기 상태 선언 */
export const initialStomp = {
    stompClient: {},
};

/* 액션 타입 선언 */
const STOMP_CONN = "stomp/STOMP_CONN";

/* 액션 생성 함수 */
export const connect = (stompClient) => ({
    type: STOMP_CONN,
    data: stompClient,
});

/* 리듀서 선언 */
const stompReducer = (state = initialStomp, action) => {
    switch (action.type) {
        case STOMP_CONN:
            return { stompClient: action.data };

        default:
            return { ...state };
    }
};

export default stompReducer;
