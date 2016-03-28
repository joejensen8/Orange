package orange;

import org.junit.Test;

import java.math.BigInteger;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Joe on 9/9/2015.
 */
public class OphoneTest
{

    @Test
    public void testGetProductType() throws Exception
    {
        Ophone myOphone = new Ophone(new SerialNumber(new BigInteger("8")), null);
        ProductType myPT = myOphone.getProductType();
        assertEquals(ProductType.OPHONE, myPT);
    }

    @Test
    public void testIsValidSerialNumber() throws Exception
    {
        // Correct Serial Number
        Ophone myOphone = new Ophone(new SerialNumber(new BigInteger("63")), null);
        assertTrue(Ophone.isValidSerialNumber(myOphone.getSerialNumber()));

        // Is odd but gcd is incorrect
        Ophone myOphone2 = new Ophone(new SerialNumber(new BigInteger("3")), null);
        assertFalse(Ophone.isValidSerialNumber(myOphone2.getSerialNumber()));

        // Is not odd and gcd is correct
        Ophone myOphone3 = new Ophone(new SerialNumber(new BigInteger("35280")), null);
        assertFalse(Ophone.isValidSerialNumber(myOphone3.getSerialNumber()));

        // Is not odd and gcd is incorrect
        Ophone myOphone4 = new Ophone(new SerialNumber(new BigInteger("84")), null);
        assertFalse(Ophone.isValidSerialNumber(myOphone4.getSerialNumber()));
    }

    @Test
    public void testGetSerialNumber() throws Exception
    {
        Ophone myOphone = new Ophone(new SerialNumber(new BigInteger("4")), null);
        SerialNumber mySerialNumber = myOphone.getSerialNumber();
        assertEquals(4, mySerialNumber.getSerialNumber().intValue());
    }

    @Test
    public void testGetProductName() throws Exception
    {
        Ophone myOphone = new Ophone(new SerialNumber(new BigInteger("4")), null);
        assertEquals("oPhone", myOphone.getProductName());
    }

    @Test
    public void testGetDescription() throws Exception
    {
        ArrayList<String> myArr = new ArrayList<>();
        Set<String> mySet = new HashSet<>(myArr);
        Ophone myOphone = new Ophone(new SerialNumber(new BigInteger("4")), Optional.of(mySet));

        // Empty
        assertEquals(Optional.of(mySet), myOphone.getDescription());

        // One element
        myArr.add("OPHONE");
        mySet = new HashSet<>(myArr);
        myOphone = new Ophone(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals(Optional.of(mySet), myOphone.getDescription());

        // Multiple Elements
        myArr.add("is");
        myArr.add("the");
        myArr.add("Product");
        mySet = new HashSet<>(myArr);
        myOphone = new Ophone(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals(Optional.of(mySet), myOphone.getDescription());
    }

    @Test
    public void testEquals() throws Exception
    {
        Ophone myOphone = new Ophone(new SerialNumber(new BigInteger("4")), null);
        Ophone myOphone1 = new Ophone(new SerialNumber(new BigInteger("4")), null);
        assertTrue(myOphone.equals(myOphone1));

        Ophone myOphone2 = new Ophone(new SerialNumber(new BigInteger("5")), null);
        assertFalse(myOphone.equals(myOphone2));
    }

    @Test
    public void testHashCode() throws Exception
    {
        Ophone myOphone = new Ophone(new SerialNumber(new BigInteger("4")), null);
        assertEquals(4, myOphone.hashCode());
    }

    @Test
    public void testToString() throws Exception
    {
        ArrayList<String> myArr = new ArrayList<>();
        Set<String> mySet = new HashSet<>(myArr);
        Ophone myOphone = new Ophone(new SerialNumber(new BigInteger("4")), Optional.of(mySet));

        assertEquals("oPhone\n4\n", myOphone.toString());

        myArr.add("this is a product");
        mySet = new HashSet<>(myArr);
        myOphone = new Ophone(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals("oPhone\n4\nThis is a product\n", myOphone.toString());

        myArr.add("and it's a phone");
        mySet = new HashSet<>(myArr);
        myOphone = new Ophone(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals("oPhone\n4\nThis is a product\nAnd it's a phone\n", myOphone.toString());
    }

    @Test
    public void testProcessExchange() throws Exception
    {
        // Test status OK and Result correct
        Exchange.Builder b = new Exchange.Builder();
        RequestStatus status = new RequestStatus();
        b.addCompatible(new SerialNumber(new BigInteger("13"))).addCompatible(new SerialNumber(new BigInteger("16")));
        b.addCompatible(new SerialNumber(new BigInteger("18")));
        Ophone myOphone = new Ophone(new SerialNumber(new BigInteger("15")), Optional.empty());
        try
        {
            Exchange exchange = b.build();
            myOphone.process(exchange, status);
            assertEquals(RequestStatus.StatusCode.OK, status.getStatusCode());
            assertEquals(Optional.of(new BigInteger("16")), status.getResult());
        }catch(Exception e)
        {
            System.out.println("exception: " + e);
        }
        // Test status FAIL and result empty
        Exchange.Builder builder = new Exchange.Builder();
        try
        {
            Exchange exchange = builder.build();
            myOphone.process(exchange, status);
            assertEquals(RequestStatus.StatusCode.FAIL, status.getStatusCode());
            assertEquals(Optional.empty(), status.getResult());
        }catch (Exception e)
        {
            System.out.println("exception: " + e);
        }
        builder.addCompatible(new SerialNumber(new BigInteger("17"))).addCompatible(new SerialNumber(new BigInteger("201")));
        try
        {
            Exchange exchange = builder.build();
            myOphone.process(exchange, status);
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
        b.setRMA(new BigInteger("16"));
        RequestStatus status = new RequestStatus();
        Ophone myOphone = new Ophone(new SerialNumber(new BigInteger("8")), Optional.empty());
        try
        {
            Refund refund = b.build();
            myOphone.process(refund, status);
            assertEquals(Optional.empty(), status.getResult());
            assertEquals(RequestStatus.StatusCode.OK, status.getStatusCode());
        }catch(Exception e)
        {
            System.out.println("exception: " + e);
        }

        // Test a FAIL
        myOphone = new Ophone(new SerialNumber(new BigInteger("7")), Optional.empty());
        try
        {
            Refund refund = b.build();
            myOphone.process(refund, status);
            assertEquals(Optional.empty(), status.getResult());
            assertEquals(RequestStatus.StatusCode.FAIL, status.getStatusCode());
        }catch (Exception e)
        {
            System.out.println("exception: " + e);
        }
        b.setRMA(new BigInteger("9"));
        try
        {
            Refund refund = b.build();
            myOphone.process(refund, status);
            assertEquals(Optional.empty(), status.getResult());
            assertEquals(RequestStatus.StatusCode.FAIL, status.getStatusCode());
        }catch (Exception e)
        {
            System.out.println("exception: " + e);
        }

    }
}