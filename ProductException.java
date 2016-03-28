package orange;

/**
 * Created by Joe on 9/13/2015.
 */
public class ProductException extends Exception
{
    public enum ErrorCode
    {
        INVALID_SERIAL_NUMBER,
        INVALID_PRODUCT_TYPE,
        UNSUPPORTED_OPERATION;
    }

    ProductType productType;
    SerialNumber serialNumber;
    ErrorCode errorCode;

    public ProductException (ProductType productType, SerialNumber serialNumber, ErrorCode errorCode)
    {
        this.productType = productType;
        this.serialNumber = serialNumber;
        this.errorCode = errorCode;
    }

    public ProductType getProductType()
    {
        return productType;
    }

    public String getProductName()
    {
        return productType.getName();
    }

    public SerialNumber getSerialNumber()
    {
        return serialNumber;
    }

    public ErrorCode getErrorCode()
    {
        return errorCode;
    }

}
