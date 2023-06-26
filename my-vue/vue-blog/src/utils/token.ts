import Cookies from "js-cookie";

const TokenKey: string = "Token";

const domain: string = ".cloverkitty.love";

// token前缀
export let token_prefix = "Bearer ";

export function getToken() {
  return Cookies.get(TokenKey);
}

// 本地运行记得删除domain
export function setToken(token: string) {
  return Cookies.set(TokenKey, token);
  // return Cookies.set(TokenKey, token, { domain: domain });
}

export function removeToken() {
  return Cookies.remove(TokenKey);
  // return Cookies.remove(TokenKey, { domain: domain });
}
