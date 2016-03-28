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
public class OpodTest
{

    @Test
    public void testGetProductType() throws Exception
    {
        Opod myOpod = new Opod(new SerialNumber(new BigInteger("8")), null);
        ProductType myPT = myOpod.getProductType();
        assertEquals(ProductType.OPOD, myPT);
    }

    @Test
    public void testIsValidSerialNumber() throws Exception
    {
        // Correct Serial Number
        Opod myOpod = new Opod(new SerialNumber(new BigInteger("4")), null);
        assertTrue(Opod.isValidSerialNumber(myOpod.getSerialNumber()));

        // Is even but third bit is set
        Opod myOpod2 = new Opod(new SerialNumber(new BigInteger("8")), null);
        assertFalse(Opod.isValidSerialNumber(myOpod2.getSerialNumber()));

        // Is not even and third bit not set
        Opod myOpod3 = new Opod(new SerialNumber(new BigInteger("3")), null);
        assertFalse(Opod.isValidSerialNumber(myOpod3.getSerialNumber()));

        // Is not even and third bit is set
        Opod myOpod4 = new Opod(new SerialNumber(new BigInteger("9")), null);
        assertFalse(Opod.isValidSerialNumber(myOpod4.getSerialNumber()));
    }

    @Test
    public void testGetSerialNumber() throws Exception
    {
        Opod myOpod = new Opod(new SerialNumber(new BigInteger("4")), null);
        SerialNumber mySerialNumber = myOpod.getSerialNumber();
        assertEquals(4, mySerialNumber.getSerialNumber().intValue());
    }

    @Test
    public void testGetProductName() throws Exception
    {
        Opod myOpod = new Opod(new SerialNumber(new BigInteger("4")), null);
        assertEquals("oPod", myOpod.getProductName());
    }

    @Test
    public void testGetDescription() throws Exception
    {
        ArrayList<String> myArr = new ArrayList<>();
        Set<String> mySet = new HashSet<>(myArr);
        Opod myOpod = new Opod(new SerialNumber(new BigInteger("4")), Optional.of(mySet));

        // Empty
        assertEquals(Optional.of(mySet), myOpod.getDescription());

        // One element
        myArr.add("OPOD");
        mySet = new HashSet<>(myArr);
        myOpod = new Opod(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals(Optional.of(mySet), myOpod.getDescription());

        // Multiple Elements
        myArr.add("is");
        myArr.add("the");
        myArr.add("Product");
        mySet = new HashSet<>(myArr);
        myOpod = new Opod(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals(Optional.of(mySet), myOpod.getDescription());
    }

    @Test
    public void testEquals() throws Exception
    {
        Opod myOpod = new Opod(new SerialNumber(new BigInteger("4")), null);
        Opod myOpod1 = new Opod(new SerialNumber(new BigInteger("4")), null);
        assertTrue(myOpod.equals(myOpod1));

        Opod myOpod2 = new Opod(new SerialNumber(new BigInteger("5")), null);
        assertFalse(myOpod.equals(myOpod2));
    }

    @Test
    public void testHashCode() throws Exception
    {
        Opod myOpod = new Opod(new SerialNumber(new BigInteger("4")), null);
        assertEquals(4, myOpod.hashCode());
    }

    @Test
    public void testToString() throws Exception
    {
        ArrayList<String> myArr = new ArrayList<>();
        Set<String> mySet = new HashSet<>(myArr);
        Opod myOpod = new Opod(new SerialNumber(new BigInteger("4")), Optional.of(mySet));

        assertEquals("oPod\n4\n", myOpod.toString());

        myArr.add("this is a product");
        mySet = new HashSet<>(myArr);
        myOpod = new Opod(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals("oPod\n4\nThis is a product\n", myOpod.toString());

        myArr.add("and it's an MP3 Player");
        mySet = new HashSet<>(myArr);
        myOpod = new Opod(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals("oPod\n4\nThis is a product\nAnd it's an MP3 Player\n", myOpod.toString());
    }

    @Test
    public void testProcessExchange() throws Exception
    {
        // Test status OK and Result correct
        Exchange.Builder b = new Exchange.Builder();
        RequestStatus status = new RequestStatus();
        b.addCompatible(new SerialNumber(new BigInteger("13"))).addCompatible(new SerialNumber(new BigInteger("16")));
        Opod myOpod = new Opod(new SerialNumber(new BigInteger("15")), Optional.empty());
        try
        {
            Exchange exchange = b.build();
            myOpod.process(exchange, status);
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
            myOpod.process(exchange, status);
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
        Opod myOpod = new Opod(new SerialNumber(new BigInteger("408")), Optional.empty());
        try
        {
            Refund refund = b.build();
            myOpod.process(refund, status);
            assertEquals(Optional.empty(), status.getResult());
            assertEquals(RequestStatus.StatusCode.OK, status.getStatusCode());
        }catch(Exception e)
        {
            System.out.println("exception: " + e);
        }

        // Test a FAIL
        myOpod = new Opod(new SerialNumber(new BigInteger("112")), Optional.empty());
        try
        {
            Refund refund = b.build();
            myOpod.process(refund, status);
            assertEquals(Optional.empty(), status.getResult());
            assertEquals(RequestStatus.StatusCode.FAIL, status.getStatusCode());
        }catch (Exception e)
        {
            System.out.println("exception: " + e);
        }
    }
}