package orange;

import org.junit.Test;

import javax.swing.text.html.Option;
import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

/**
 * Created by Joe on 9/17/2015.
 */
public class AbstractProductTest
{

    @Test
    public void testMake() throws Exception
    {
        // Test 1: shows product can be made successfully
        ProductType myPT = ProductType.OPAD;
        SerialNumber mySN = new SerialNumber(new BigInteger("8"));
        Product myOpad = AbstractProduct.make(myPT, mySN, Optional.empty());
        Product myOpadTest = new Opad(mySN, Optional.empty());
        assertEquals(myOpadTest, myOpad);

        // Test 2: Shows invalid product type exception thrown
        try
        {
            Product myOpod = AbstractProduct.make(null, mySN, Optional.empty());
        }catch(ProductException productException){
            assertEquals(ProductException.ErrorCode.INVALID_PRODUCT_TYPE, productException.getErrorCode());
        }

        // Test 3: Shows invalid serial number exception thrown
        mySN = new SerialNumber(new BigInteger("4"));
        myPT = ProductType.OPAD;
        try
        {
            myOpad = AbstractProduct.make(myPT, mySN, Optional.empty());
        }catch(ProductException productException){
            assertEquals(ProductException.ErrorCode.INVALID_SERIAL_NUMBER, productException.getErrorCode());
        }
    }

    @Test
    public void testGetBiggerSerialNumbers() throws Exception
    {
        TreeSet<SerialNumber> mySet = new TreeSet<>();
        mySet.add(new SerialNumber(new BigInteger("5")));
        mySet.add(new SerialNumber(new BigInteger("6")));
        mySet.add(new SerialNumber(new BigInteger("7")));
        mySet.add(new SerialNumber(new BigInteger("4")));
        Opad myOpad = new Opad(new SerialNumber(new BigInteger("5")), Optional.empty());
        mySet = AbstractProduct.getBiggerSerialNumbers(mySet, myOpad);
        TreeSet<SerialNumber> test = new TreeSet<>();
        test.add(new SerialNumber(new BigInteger("6")));
        test.add(new SerialNumber(new BigInteger("7")));
        assertEquals(test, mySet);
    }

    @Test
    public void testGetSetAverage() throws Exception
    {
        TreeSet<SerialNumber> mySet = new TreeSet<>();
        mySet.add(new SerialNumber(new BigInteger("5")));
        mySet.add(new SerialNumber(new BigInteger("6")));
        mySet.add(new SerialNumber(new BigInteger("7")));
        mySet.add(new SerialNumber(new BigInteger("4")));
        mySet.add(new SerialNumber(new BigInteger("3")));
        BigInteger testAvg = new BigInteger("5");
        assertEquals(testAvg, AbstractProduct.getSetAverage(mySet));
    }
}
