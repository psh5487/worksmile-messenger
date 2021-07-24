/* 초기 상태 선언 */
export const initialForbiddens = {
    forbiddens: [],
};

/* 액션 타입 선언 */
const WORDS_UPDATE = "forbidden/WORDS_UPDATE";

/* 액션 생성 함수 */
export const update = (forbiddens) => ({
    type: WORDS_UPDATE,
    data: forbiddens,
});

/* 리듀서 선언 */
const wordsReducer = (state = initialForbiddens, action) => {
    switch (action.type) {
        case WORDS_UPDATE:
            return { ...state, forbiddens: action.data };

        default:
            return { ...state };
    }
};

export default wordsReducer;
