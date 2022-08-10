package gateway.secure.model;

public enum UserStatusCodeEnum implements ResultStatusCode {

    ACCOUNT_LOCKED(5002, "账号已被锁定"),

    ACCOUNT_CREDENTIAL_EXPIRED(5003, "账号凭证已过期"),

    ACCOUNT_DISABLE(5004, "账号已被禁用"),

    PERMISSION_DENIED(5005, "账号没有权限"),

    USER_UNAUTHORIZED(5006, "账号未认证");


    private final int code;

    private final String msg;

    UserStatusCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}

