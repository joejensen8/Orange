package orange;

import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Joe on 9/9/2015.
 */
public class OtvTest
{

    @Test
    public void testGetProductType() throws Exception
    {
        Otv myOtv = new Otv(new SerialNumber(new BigInteger("8")), null);
        ProductType myPT = myOtv.getProductType();
        assertEquals(ProductType.OTV, myPT);
    }

    @Test
    public void testIsValidSerialNumber() throws Exception
    {
        // Correct Serial Number
        Otv myOtv = new Otv(new SerialNumber(new BigInteger("3")), null);
        assertTrue(Otv.isValidSerialNumber(myOtv.getSerialNumber()));

        // Is odd but gcd is incorrect
        Otv myOtv2 = new Otv(new SerialNumber(new BigInteger("63")), null);
        assertFalse(Otv.isValidSerialNumber(myOtv2.getSerialNumber()));

        // Is not odd and gcd is correct
        Otv myOtv3 = new Otv(new SerialNumber(new BigInteger("4")), null);
        assertFalse(Otv.isValidSerialNumber(myOtv3.getSerialNumber()));

        // Is not odd and gcd is incorrect
        Otv myOtv4 = new Otv(new SerialNumber(new BigInteger("84")), null);
        assertFalse(Otv.isValidSerialNumber(myOtv4.getSerialNumber()));
    }

    @Test
    public void testGetSerialNumber() throws Exception
    {
        Otv myOtv = new Otv(new SerialNumber(new BigInteger("4")), null);
        SerialNumber mySerialNumber = myOtv.getSerialNumber();
        assertEquals(4, mySerialNumber.getSerialNumber().intValue());
    }

    @Test
    public void testGetProductName() throws Exception
    {
        Otv myOtv = new Otv(new SerialNumber(new BigInteger("4")), null);
        assertEquals("oTv", myOtv.getProductName());
    }

    @Test
    public void testGetDescription() throws Exception
    {
        ArrayList<String> myArr = new ArrayList<>();
        Set<String> mySet = new HashSet<>(myArr);
        Otv myOtv = new Otv(new SerialNumber(new BigInteger("4")), Optional.of(mySet));

        // Empty
        assertEquals(Optional.of(mySet), myOtv.getDescription());

        // One element
        myArr.add("OTV");
        mySet = new HashSet<>(myArr);
        myOtv = new Otv(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals(Optional.of(mySet), myOtv.getDescription());

        // Multiple Elements
        myArr.add("is");
        myArr.add("the");
        myArr.add("Product");
        mySet = new HashSet<>(myArr);
        myOtv = new Otv(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals(Optional.of(mySet), myOtv.getDescription());
    }

    @Test
    public void testEquals() throws Exception
    {
        Otv myOtv = new Otv(new SerialNumber(new BigInteger("4")), null);
        Otv myOtv1 = new Otv(new SerialNumber(new BigInteger("4")), null);
        assertTrue(myOtv.equals(myOtv1));

        Otv myOtv2 = new Otv(new SerialNumber(new BigInteger("5")), null);
        assertFalse(myOtv.equals(myOtv2));
    }

    @Test
    public void testHashCode() throws Exception
    {
        Otv myOtv = new Otv(new SerialNumber(new BigInteger("4")), null);
        assertEquals(4, myOtv.hashCode());
    }

    @Test
    public void testToString() throws Exception
    {
        ArrayList<String> myArr = new ArrayList<>();
        Set<String> mySet = new HashSet<>(myArr);
        Otv myOtv = new Otv(new SerialNumber(new BigInteger("4")), Optional.of(mySet));

        assertEquals("oTv\n4\n", myOtv.toString());

        myArr.add("this is a product");
        mySet = new HashSet<>(myArr);
        myOtv = new Otv(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals("oTv\n4\nThis is a product\n", myOtv.toString());

        myArr.add("and it's a TV");
        mySet = new HashSet<>(myArr);
        myOtv = new Otv(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals("oTv\n4\nThis is a product\nAnd it's a TV\n", myOtv.toString());
    }

    @Test
    public void testProcessExchange() throws Exception
    {
        // Test status OK and Result correct
        Exchange.Builder b = new Exchange.Builder();
        RequestStatus status = new RequestStatus();
        b.addCompatible(new SerialNumber(new BigInteger("1023"))).addCompatible(new SerialNumber(new BigInteger("1025")));
        b.addCompatible(new SerialNumber(new BigInteger("2025")));
        Otv myOtv = new Otv(new SerialNumber(new BigInteger("1000")), Optional.empty());
        try
        {
            Exchange exchange = b.build();
            myOtv.process(exchange, status);
            assertEquals(RequestStatus.StatusCode.OK, status.getStatusCode());
            assertEquals(Optional.of(new BigInteger("1023")), status.getResult());
        }catch(Exception e)
        {
            System.out.println("exception: " + e);
        }
        // Test status FAIL and result empty
        Exchange.Builder builder = new Exchange.Builder();
        try
        {
            Exchange exchange = builder.build();
            myOtv.process(exchange, status);
            assertEquals(RequestStatus.StatusCode.FAIL, status.getStatusCode());
            assertEquals(Optional.empty(), status.getResult());
        }catch (Exception e)
        {
            System.out.println("exception: " + e);
        }
        builder.addCompatible(new SerialNumber(new BigInteger("1030"))).addCompatible(new SerialNumber(new BigInteger("999")));
        try
        {
            Exchange exchange = builder.build();
            myOtv.process(exchange, status);
            assertEquals(RequestStatus.StatusCode.FAIL, status.getStatusCode());
            assertEquals(Optional.empty(), status.getResult());
        }catch (Exception e)
        {
            System.out.println("exception: " + e);
        }
    }

    @Test
    public void testProcessRefund() throws Exception
    {
        // Test an OK
        Refund.Builder b = new Refund.Builder();
        b.setRMA(new BigInteger("312"));
        RequestStatus status = new RequestStatus();
        Otv myOtv = new Otv(new SerialNumber(new BigInteger("408")), Optional.empty());
        try
        {
            Refund refund = b.build();
            myOtv.process(refund, status);
            assertEquals(Optional.empty(), status.getResult());
            assertEquals(RequestStatus.StatusCode.OK, status.getStatusCode());
        }catch(Exception e)
        {
            System.out.println("exception: " + e);
        }

        // Test a FAIL
        Refund.Builder builder = new Refund.Builder();
        b.setRMA(new BigInteger("-1"));
        try
        {
            Refund refund = b.build();
            myOtv.process(refund, status);
            assertEquals(Optional.empty(), status.getResult());
            assertEquals(RequestStatus.StatusCode.FAIL, status.getStatusCode());
        }catch (Exception e)
        {
            System.out.println("exception: " + e);
        }
    }
}