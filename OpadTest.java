package orange;

import org.junit.Test;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Joe on 9/9/2015.
 */
public class OpadTest
{

    @Test
    public void testGetProductType() throws Exception
    {
        Opad myOpad = new Opad(new SerialNumber(new BigInteger("8")), null);
        ProductType myPT = myOpad.getProductType();
        assertEquals(ProductType.OPAD, myPT);
    }

    @Test
    public void testIsValidSerialNumber() throws Exception
    {
        // Correct Serial Number
        Opad myOpad = new Opad(new SerialNumber(new BigInteger("8")), null);
        assertTrue(Opad.isValidSerialNumber(myOpad.getSerialNumber()));

        // Is even but third bit is not set
        Opad myOpad2 = new Opad(new SerialNumber(new BigInteger("4")), null);
        assertFalse(Opad.isValidSerialNumber(myOpad2.getSerialNumber()));

        // Is not even and third bit is set
        Opad myOpad3 = new Opad(new SerialNumber(new BigInteger("9")), null);
        assertFalse(Opad.isValidSerialNumber(myOpad3.getSerialNumber()));

        // Is not even and third bit is not set
        Opad myOpad4 = new Opad(new SerialNumber(new BigInteger("3")), null);
        assertFalse(Opad.isValidSerialNumber(myOpad4.getSerialNumber()));
    }

    @Test
    public void testGetSerialNumber() throws Exception
    {
        Opad myOpad = new Opad(new SerialNumber(new BigInteger("4")), null);
        SerialNumber mySerialNumber = myOpad.getSerialNumber();
        assertEquals(4, mySerialNumber.getSerialNumber().intValue());
    }

    @Test
    public void testGetProductName() throws Exception
    {
        Opad myOpad = new Opad(new SerialNumber(new BigInteger("4")), null);
        assertEquals("oPad", myOpad.getProductName());
    }

    @Test
    public void testGetDescription() throws Exception
    {
        ArrayList<String> myArr = new ArrayList<>();
        Set<String> mySet = new HashSet<>(myArr);
        Opad myOpad = new Opad(new SerialNumber(new BigInteger("4")), Optional.of(mySet));

        // Empty
        assertEquals(Optional.of(mySet), myOpad.getDescription());

        // One element
        myArr.add("OPAD");
        mySet = new HashSet<>(myArr);
        myOpad = new Opad(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals(Optional.of(mySet), myOpad.getDescription());

        // Multiple Elements
        myArr.add("is");
        myArr.add("the");
        myArr.add("Product");
        mySet = new HashSet<>(myArr);
        myOpad = new Opad(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals(Optional.of(mySet), myOpad.getDescription());
    }

    @Test
    public void testEquals() throws Exception
    {
        Opad myOpad = new Opad(new SerialNumber(new BigInteger("4")), null);
        Opad myOpad1 = new Opad(new SerialNumber(new BigInteger("4")), null);
        assertTrue(myOpad.equals(myOpad1));

        Opad myOpad2 = new Opad(new SerialNumber(new BigInteger("5")), null);
        assertFalse(myOpad.equals(myOpad2));
    }

    @Test
    public void testHashCode() throws Exception
    {
        Opad myOpad = new Opad(new SerialNumber(new BigInteger("4")), null);
        assertEquals(4, myOpad.hashCode());
    }

    @Test
    public void testToString() throws Exception
    {
        ArrayList<String> myArr = new ArrayList<>();
        Set<String> mySet = new HashSet<>(myArr);
        Opad myOpad = new Opad(new SerialNumber(new BigInteger("4")), Optional.of(mySet));

        assertEquals("oPad\n4\n", myOpad.toString());

        myArr.add("this is a product");
        mySet = new HashSet<>(myArr);
        myOpad = new Opad(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals("oPad\n4\nThis is a product\n", myOpad.toString());

        myArr.add("and it's a tablet");
        mySet = new HashSet<>(myArr);
        myOpad = new Opad(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals("oPad\n4\nThis is a product\nAnd it's a tablet\n", myOpad.toString());
    }

    @Test
    public void testToProperCase() throws Exception
    {
        String s = "hello";
        assertEquals("Hello", AbstractProduct.toProperCase(s));

        String s1 = "Hello";
        assertEquals("Hello", AbstractProduct.toProperCase(s1));
    }


    @Test
    public void testProcessExchange() throws Exception
    {
        // Test status OK and Result correct
        Exchange.Builder b = new Exchange.Builder();
        RequestStatus status = new RequestStatus();
        b.addCompatible(new SerialNumber(new BigInteger("13"))).addCompatible(new SerialNumber(new BigInteger("16")));
        Opad myOpad = new Opad(new SerialNumber(new BigInteger("15")), Optional.empty());
        try
        {
            Exchange exchange = b.build();
            myOpad.process(exchange, status);
            assertEquals(RequestStatus.StatusCode.OK, status.getStatusCode());
            assertEquals(Optional.of(new BigInteger("13")), status.getResult());
        }catch(Exception e)
        {
            System.out.println("exception: " + e);
        }
        // Test status FAIL and result empty
        Exchange.Builder builder = new Exchange.Builder();
        try
        {
            Exchange exchange = builder.build();
            myOpad.process(exchange, status);
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
            myOpad.process(exchange, status);
            assertEquals(RequestStatus.StatusCode.FAIL, status.getStatusCode());
            assertEquals(Optional.empty(), status.getResult());
        }catch (Exception e)
        {
            System.out.println("exception: " + e);
        }

        // The following is the JUNIT test required in the SPEC
        Exchange.Builder myBuilder = new Exchange.Builder();
        myBuilder.addCompatible(new SerialNumber(new BigInteger("1032"))).addCompatible(new SerialNumber(new BigInteger("1244")));
        myOpad = new Opad(new SerialNumber(new BigInteger("1048")), Optional.empty());
        try
        {
            Exchange exchange = myBuilder.build();
            myOpad.process(exchange, status);
            assertEquals(RequestStatus.StatusCode.OK, status.getStatusCode());
            assertEquals(Optional.of(new BigInteger("1032")), status.getResult());
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
        Opad myOpad = new Opad(new SerialNumber(new BigInteger("408")), Optional.empty());
        try
        {
            Refund refund = b.build();
            myOpad.process(refund, status);
            assertEquals(Optional.empty(), status.getResult());
            assertEquals(RequestStatus.StatusCode.OK, status.getStatusCode());
        }catch(Exception e)
        {
            System.out.println("exception: " + e);
        }

        // Test a FAIL
        myOpad = new Opad(new SerialNumber(new BigInteger("112")), Optional.empty());
        try
        {
            Refund refund = b.build();
            myOpad.process(refund, status);
            assertEquals(Optional.empty(), status.getResult());
            assertEquals(RequestStatus.StatusCode.FAIL, status.getStatusCode());
        }catch (Exception e)
        {
            System.out.println("exception: " + e);
        }
    }
}
