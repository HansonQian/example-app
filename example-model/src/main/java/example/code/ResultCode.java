package example.code;

/**
 * @className: ResultCode
 * @description: 结果码接口类
 * @author: Hanson
 * @create: 2020-01-28 14:08
 **/
public interface ResultCode {


    /**
     * 操作是否成功
     *
     * @return true 成功| false 失败
     */
    boolean isSuccess();

    /**
     * @return 操作代码
     */
    String getCode();

    /**
     * @return 提示信息
     */
    String getMessage();


}
