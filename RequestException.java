package orange;

/**
 * Created by Joe on 9/20/2015.
 */
public class RequestException extends Exception
{
    /*
    Complement of ProductException
    Left entirely up to me
    thrown in case of request processing errors
     */

    public enum ErrorCode
    {
        EXCHANGE_FAILED,
        REFUND_FAILED,
        INVALID_RMA;
    }

    ErrorCode errorCode;

    public RequestException (ErrorCode errorCode)
    {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode()
    {
        return errorCode;
    }
}
