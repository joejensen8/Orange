package orange;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Joe on 9/5/2015.
 */
public final class Opod extends AbstractProduct
{
    Opod(SerialNumber serialNumber, Optional<Set<String>> description)
    {
        super(serialNumber, description);
    }

    // returns the product type
    public ProductType getProductType()
    {
        return ProductType.OPOD;
    }

    // returns true if the serial number is valid and false otherwise
    public static Boolean isValidSerialNumber(SerialNumber serialNumber)
    {
        Boolean isEven = serialNumber.isEven();
        Boolean thirdBitNotSet = !serialNumber.testBit(3);
        return (isEven && thirdBitNotSet);
    }

    public void process(Exchange request, RequestStatus status) throws ProductException
    {
       /*
       An oPod is exchanged with any product that has a compatible
       serial number. If no compatible product exists, the exchange fails.
       Otherwise, the status is set to OK and the result is set to the serial
       number of the new oPad.
        */
        TreeSet<SerialNumber> compProducts = new TreeSet<>(request.getCompatibleProducts());
        if (!compProducts.isEmpty())
        {
            status.setStatusCode(RequestStatus.StatusCode.OK);
            status.setResult(Optional.of(compProducts.first().getSerialNumber()));
        }
        else
        {
            status.setStatusCode(RequestStatus.StatusCode.FAIL);
            status.setResult(Optional.empty());
        }
    }

    public void process(Refund request, RequestStatus status) throws ProductException
    {
        /*
        An oPod refund succeeds if and only if the greatest common divisor of
        the RMA and the serial number is at least 24, in which case the status
        is set to OK and the result is set to undefined.
         */
        BigInteger gcd = request.getRma().gcd(this.getSerialNumber().getSerialNumber());
        if (gcd.compareTo(new BigInteger("24")) >= 0) // can't convert to long... chops value off
        {
            status.setStatusCode(RequestStatus.StatusCode.OK);
            status.setResult(Optional.empty()); // set to undefined
        }
        else
        {
            status.setStatusCode(RequestStatus.StatusCode.FAIL);
            status.setResult(Optional.empty());
        }
    }
}
