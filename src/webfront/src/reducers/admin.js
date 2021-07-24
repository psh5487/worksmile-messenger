/* 초기 상태 선언 */
export const initialAdmin = {
    companys: [],
    departments: [],
    positions: [],
};

/* 액션 타입 선언 */
const ADMIN_COMPANY = "admin/ADMIN_COMPANY";
const ADMIN_DEPARTMENT = "admin/ADMIN_DEPARTMENT";
const ADMIN_POSITION = "admin/ADMIN_POSITION";

/* 액션 생성 함수 */
export const getCompanys = (companys) => ({
    type: ADMIN_COMPANY,
    data: companys,
});

export const getDepartments = (departments) => ({
    type: ADMIN_DEPARTMENT,
    data: departments,
});

export const getPositions = (positions) => ({
    type: ADMIN_POSITION,
    data: positions,
});

/* 리듀서 선언 */
const adminReducer = (state = initialAdmin, action) => {
    switch (action.type) {
        case ADMIN_COMPANY:
            return { ...state, companys: action.data };

        case ADMIN_DEPARTMENT:
            return { ...state, departments: action.data };

        case ADMIN_POSITION:
            return { ...state, positions: action.data };

        default:
            return { ...state };
    }
};

export default adminReducer;
