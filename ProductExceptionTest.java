package orange;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

/**
 * Created by Joe on 9/17/2015.
 */
public class ProductExceptionTest
{

    @Test
    public void testGetProductType() throws Exception
    {
        ProductType myProduct = ProductType.OPAD;
        SerialNumber mySN = new SerialNumber(new BigInteger("8"));
        ProductException.ErrorCode errorCode = ProductException.ErrorCode.INVALID_SERIAL_NUMBER;
        ProductException myPE = new ProductException(myProduct, mySN, errorCode);
        assertEquals(myProduct, myPE.getProductType());
    }

    @Test
    public void testGetProductName() throws Exception
    {
        ProductType myProduct = ProductType.OPAD;
        SerialNumber mySN = new SerialNumber(new BigInteger("8"));
        ProductException.ErrorCode errorCode = ProductException.ErrorCode.INVALID_SERIAL_NUMBER;
        ProductException myPE = new ProductException(myProduct, mySN, errorCode);
        assertEquals(myProduct.getName(), myPE.getProductName());
    }

    @Test
    public void testGetSerialNumber() throws Exception
    {
        ProductType myProduct = ProductType.OPAD;
        SerialNumber mySN = new SerialNumber(new BigInteger("8"));
        ProductException.ErrorCode errorCode = ProductException.ErrorCode.INVALID_SERIAL_NUMBER;
        ProductException myPE = new ProductException(myProduct, mySN, errorCode);
        assertEquals(mySN, myPE.getSerialNumber());
    }

    @Test
    public void testGetErrorCode() throws Exception
    {
        ProductType myProduct = ProductType.OPAD;
        SerialNumber mySN = new SerialNumber(new BigInteger("8"));
        ProductException.ErrorCode errorCode = ProductException.ErrorCode.INVALID_SERIAL_NUMBER;
        ProductException myPE = new ProductException(myProduct, mySN, errorCode);
        assertEquals(errorCode, myPE.getErrorCode());
    }
}