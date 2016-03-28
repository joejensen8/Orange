package orange;

import org.omg.CORBA.UNKNOWN;

import java.math.BigInteger;
import java.util.Optional;

/**
 * Created by Joe on 9/20/2015.
 */
public class RequestStatus
{
    public enum StatusCode
    {
        UNKNOWN,
        OK,
        FAIL;
    }

    // The current status
    private StatusCode statusCode;
    //
    private Optional<BigInteger> result;

    public RequestStatus()
    {
        statusCode = StatusCode.UNKNOWN;
        result = Optional.empty();
    }

    public StatusCode getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode)
    {
        this.statusCode = statusCode;
    }

    public Optional<BigInteger> getResult()
    {
        return result;
    }

    public void setResult(Optional<BigInteger> result)
    {
        this.result = result;
    }
}
