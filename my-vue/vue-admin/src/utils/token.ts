import Cookies from "js-cookie";

const TokenKey: string = "Token";

const domain: string = ".cloverkitty.love";

// 前缀
export let token_prefix = "Bearer ";

export function getToken(){
    return Cookies.get(TokenKey);
}

export function setToken(token: string){
    return Cookies.set(TokenKey, token)
    // return Cookies.set(TokenKey, token, {domain:domain})
}

export function removeToken(){
    return Cookies.remove(TokenKey)
    // return Cookies.remove(TokenKey, {domain:domain})
}
