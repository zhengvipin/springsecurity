const URl = "http://127.0.0.1:8086/api/";
const TOKEN_KEY = "jwtToken";

// 存储令牌
function setJwtToken(token) {
    localStorage.setItem(TOKEN_KEY, token);
}

// 获得令牌
function getJwtToken() {
    return localStorage.getItem(TOKEN_KEY);
}

// 删除令牌
function cleanJwtToken() {
    localStorage.removeItem(TOKEN_KEY);
}

// 生成令牌请求头
function createJwtAuthenticationToken() {
    let token = getJwtToken();
    if (token) {
        return {"Authorization": "Bearer " + token};
    } else {
        return {};
    }
}