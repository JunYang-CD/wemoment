package gateway.secure.utils;

import gateway.secure.model.CommonStatusCodeEnum;
import gateway.secure.model.Result;
import gateway.secure.model.ResultStatusCode;

public class ResultUtil<T> {

    public static <T> Result<T> success() {
        return new Result<>(CommonStatusCodeEnum.SUCCESS.getCode(),
                CommonStatusCodeEnum.SUCCESS.getMsg());

    }

    public static <T> Result<T> success(T data) {
        return new Result<>(
                CommonStatusCodeEnum.SUCCESS.getCode(),
                CommonStatusCodeEnum.SUCCESS.getMsg(),
                data
        );
    }

    public static <T> Result<T> error() {
        return new Result<>(
                CommonStatusCodeEnum.SERVER_ERROR.getCode(),
                CommonStatusCodeEnum.SERVER_ERROR.getMsg()
        );
    }

    public static <T> Result<T> failed(ResultStatusCode resultStatusCode) {
        return new Result<>(resultStatusCode.getCode(), resultStatusCode.getMsg());
    }
}
