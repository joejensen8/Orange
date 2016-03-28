package orange;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Optional;
import java.util.TreeSet;

import static org.junit.Assert.*;

/**
 * Created by Joe on 9/24/2015.
 */
public class ExchangeTest
{

    @Test
    public void testProcess() throws Exception
    {
        try
        {
            Opad myOpad = new Opad(new SerialNumber(new BigInteger("5")), Optional.empty());
            Exchange myE;
            Exchange.Builder builder = new Exchange.Builder();

            SerialNumber s1 = new SerialNumber(new BigInteger("5"));
            SerialNumber s2 = new SerialNumber(new BigInteger("9"));
            SerialNumber s3 = new SerialNumber(new BigInteger("7"));

            builder.addCompatible(s1).addCompatible(s2);
            myE = builder.build();
            builder.addCompatible(s3); // shows immutable exchange object

            assertNotEquals(myE.getCompatibleProducts(), builder.getCompatibleProducts());

            myE.process(myOpad, new RequestStatus());
        }catch(RequestException exception)
        {
            assertEquals(RequestException.ErrorCode.EXCHANGE_FAILED, exception.getErrorCode());
        }

    }

    @Test
    public void testGetCompatibleProducts() throws Exception
    {
        Opad myOpad = new Opad(new SerialNumber(new BigInteger("5")), Optional.empty());
        Exchange myE;
        Exchange.Builder builder = new Exchange.Builder();

        SerialNumber s1 = new SerialNumber(new BigInteger("5"));
        SerialNumber s2 = new SerialNumber(new BigInteger("9"));

        builder.addCompatible(s1).addCompatible(s2);
        myE = builder.build();

        TreeSet<SerialNumber> mySet = new TreeSet<>();
        mySet.add(s1);
        mySet.add(s2);

        assertEquals(mySet, myE.getCompatibleProducts());
    }
}