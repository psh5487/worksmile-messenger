/* 초기 상태 선언 */
export const initialState = {
    userInfo: {},
};

/* 액션 타입 선언 */
const INIT_USER = "user/INIT_USER";
const LOGIN_USER = "user/LOGIN_USER";
const LOGOUT_USER = "user/LOGOUT_USER";
const SETTING_USER = "user/SETTING_USER";

/* 액션 생성 함수 */
export const init = () => ({
    type: INIT_USER,
});

export const login = (user) => ({
    type: LOGIN_USER,
    data: user,
});

export const logout = () => ({
    type: LOGOUT_USER,
});

export const updateSetting = (user) => ({
    type: SETTING_USER,
    data: user,
});

/* 리듀서 선언 */
const userReducer = (state = initialState, action) => {
    switch (action.type) {
        case INIT_USER:
            return { userInfo: {} };

        case LOGIN_USER:
            return { userInfo: action.data };

        case LOGOUT_USER:
            return { userInfo: {} };

        case SETTING_USER:
            return { userInfo: action.data };

        default:
            return { ...state };
    }
};

export default userReducer;
