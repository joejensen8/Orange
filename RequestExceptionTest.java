package orange;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by Joe on 9/24/2015.
 */
public class RequestExceptionTest
{

    @Test
    public void testGetErrorCode() throws Exception
    {
        Opad myOpad = new Opad(new SerialNumber(new BigInteger("5")), Optional.empty());
        RequestException myRE = new RequestException(RequestException.ErrorCode.EXCHANGE_FAILED);

        assertEquals(RequestException.ErrorCode.EXCHANGE_FAILED, myRE.getErrorCode());
    }
}