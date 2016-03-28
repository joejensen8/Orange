package orange;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by Joe on 9/24/2015.
 */
public class RefundTest
{

    @Test
    public void testProcess() throws Exception
    {
        try
        {
            Opad myOpad = new Opad(new SerialNumber(new BigInteger("5")), Optional.empty());
            Refund myR;
            Refund.Builder builder = new Refund.Builder();

            builder.setRMA(new BigInteger("5"));
            myR = builder.build();
            builder.setRMA(new BigInteger("7")); // shows immutable exchange object

            assertNotEquals(myR.getRma(), builder.getRma());

            myR.process(myOpad, new RequestStatus());
        }catch(RequestException exception)
        {
            assertEquals(RequestException.ErrorCode.REFUND_FAILED, exception.getErrorCode());
        }
    }

    @Test
    public void testGetRma() throws Exception
    {
        Opad myOpad = new Opad(new SerialNumber(new BigInteger("5")), Optional.empty());
        Refund myR;
        Refund.Builder builder = new Refund.Builder();

        builder.setRMA(new BigInteger("5"));
        myR = builder.build();

        assertEquals(new BigInteger("5"), myR.getRma());
    }
}