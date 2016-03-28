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
public class OwatchTest
{

    @Test
    public void testGetProductType() throws Exception
    {
        Owatch myOwatch = new Owatch(new SerialNumber(new BigInteger("8")), null);
        ProductType myPT = myOwatch.getProductType();
        assertEquals(ProductType.OWATCH, myPT);
    }

    @Test
    public void testIsValidSerialNumber() throws Exception
    {
        // Correct Serial Number
        Owatch myOwatch = new Owatch(new SerialNumber(new BigInteger("8855")), null);
        assertTrue(Owatch.isValidSerialNumber(myOwatch.getSerialNumber()));

        // Is odd but gcd is incorrect
        Owatch myOwatch2 = new Owatch(new SerialNumber(new BigInteger("3")), null);
        assertFalse(Owatch.isValidSerialNumber(myOwatch2.getSerialNumber()));

        // Is not odd and gcd is correct
        Owatch myOwatch3 = new Owatch(new SerialNumber(new BigInteger("84")), null);
        assertFalse(Owatch.isValidSerialNumber(myOwatch3.getSerialNumber()));

        // Is not odd and gcd is incorrect
        Owatch myOwatch4 = new Owatch(new SerialNumber(new BigInteger("4")), null);
        assertFalse(Owatch.isValidSerialNumber(myOwatch4.getSerialNumber()));
    }

    @Test
    public void testGetSerialNumber() throws Exception
    {
        Owatch myOwatch = new Owatch(new SerialNumber(new BigInteger("4")), null);
        SerialNumber mySerialNumber = myOwatch.getSerialNumber();
        assertEquals(4, mySerialNumber.getSerialNumber().intValue());
    }

    @Test
    public void testGetProductName() throws Exception
    {
        Owatch myOwatch = new Owatch(new SerialNumber(new BigInteger("4")), null);
        assertEquals("oWatch", myOwatch.getProductName());
    }

    @Test
    public void testGetDescription() throws Exception
    {
        ArrayList<String> myArr = new ArrayList<>();
        Set<String> mySet = new HashSet<>(myArr);
        Owatch myOwatch = new Owatch(new SerialNumber(new BigInteger("4")), Optional.of(mySet));

        // Empty
        assertEquals(Optional.of(mySet), myOwatch.getDescription());

        // One element
        myArr.add("OWATCH");
        mySet = new HashSet<>(myArr);
        myOwatch = new Owatch(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals(Optional.of(mySet), myOwatch.getDescription());

        // Multiple Elements
        myArr.add("is");
        myArr.add("the");
        myArr.add("Product");
        mySet = new HashSet<>(myArr);
        myOwatch = new Owatch(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals(Optional.of(mySet), myOwatch.getDescription());
    }

    @Test
    public void testEquals() throws Exception
    {
        Owatch myOwatch = new Owatch(new SerialNumber(new BigInteger("4")), null);
        Owatch myOwatch1 = new Owatch(new SerialNumber(new BigInteger("4")), null);
        assertTrue(myOwatch.equals(myOwatch1));

        Owatch myOwatch2 = new Owatch(new SerialNumber(new BigInteger("5")), null);
        assertFalse(myOwatch.equals(myOwatch2));
    }

    @Test
    public void testHashCode() throws Exception
    {
        Owatch myOwatch = new Owatch(new SerialNumber(new BigInteger("4")), null);
        assertEquals(4, myOwatch.hashCode());
    }

    @Test
    public void testToString() throws Exception
    {
        ArrayList<String> myArr = new ArrayList<>();
        Set<String> mySet = new HashSet<>(myArr);
        Owatch myOwatch = new Owatch(new SerialNumber(new BigInteger("4")), Optional.of(mySet));

        assertEquals("oWatch\n4\n", myOwatch.toString());

        myArr.add("this is a product");
        mySet = new HashSet<>(myArr);
        myOwatch = new Owatch(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals("oWatch\n4\nThis is a product\n", myOwatch.toString());

        myArr.add("and it's a TV");
        mySet = new HashSet<>(myArr);
        myOwatch = new Owatch(new SerialNumber(new BigInteger("4")), Optional.of(mySet));
        assertEquals("oWatch\n4\nThis is a product\nAnd it's a TV\n", myOwatch.toString());
    }

    @Test
    public void testProcessExchange() throws Exception
    {
        // Test status OK and Result correct
        Exchange.Builder b = new Exchange.Builder();
        RequestStatus status = new RequestStatus();
        b.addCompatible(new SerialNumber(new BigInteger("13"))).addCompatible(new SerialNumber(new BigInteger("16")));
        Owatch myOwatch = new Owatch(new SerialNumber(new BigInteger("15")), Optional.empty());
        try
        {
            Exchange exchange = b.build();
            myOwatch.process(exchange, status);
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
            myOwatch.process(exchange, status);
            assertEquals(RequestStatus.StatusCode.FAIL, status.getStatusCode());
            assertEquals(Optional.empty(), status.getResult());
        }catch (Exception e)
        {
            System.out.println("exception: " + e);
        }
        builder.addCompatible(new SerialNumber(new BigInteger("13")));
        try
        {
            Exchange exchange = builder.build();
            myOwatch.process(exchange, status);
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
        Owatch myOwatch = new Owatch(new SerialNumber(new BigInteger("408")), Optional.empty());
        try
        {
            Refund refund = b.build();
            myOwatch.process(refund, status);
            assertEquals(Optional.empty(), status.getResult());
            assertEquals(RequestStatus.StatusCode.OK, status.getStatusCode());
        }catch(Exception e)
        {
            System.out.println("exception: " + e);
        }

        // Test a FAIL
        myOwatch = new Owatch(new SerialNumber(new BigInteger("313")), Optional.empty());
        try
        {
            Refund refund = b.build();
            myOwatch.process(refund, status);
            assertEquals(Optional.empty(), status.getResult());
            assertEquals(RequestStatus.StatusCode.FAIL, status.getStatusCode());
        }catch (Exception e)
        {
            System.out.println("exception: " + e);
        }
    }
}