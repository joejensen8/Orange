package orange;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sun.reflect.generics.tree.Tree;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Joe on 9/5/2015.
 */
public final class Owatch extends AbstractProduct
{
    Owatch(SerialNumber serialNumber, Optional<Set<String>> description)
    {
        super(serialNumber, description);
    }

    // returns the product type
    public ProductType getProductType()
    {
        return ProductType.OWATCH;
    }

    // returns true if the serial number is valid and false otherwise
    public static Boolean isValidSerialNumber(SerialNumber serialNumber)
    {
        Boolean isOdd = serialNumber.isOdd();
        Boolean gcdValueCorrect = isGCDCorrect(serialNumber);
        return (isOdd && gcdValueCorrect);
    }

    // helper method that returns true if the GCD value is in the desired range and false otherwise
    private static boolean isGCDCorrect(SerialNumber serialNumber)
    {
        SerialNumber mySN = new SerialNumber(new BigInteger("630"));
        BigInteger gcd = serialNumber.gcd(mySN);
        int myGCD = gcd.intValue();
        return (14 < myGCD && myGCD <= 42);
    }

    public void process(Exchange request, RequestStatus status) throws ProductException
    {
        /*
        As for exchanges, consider the smallest compatible serial number
        that is strictly greater than the original oWatch serial number. If no
        such compatible product exists, the exchange fails. Otherwise, the
        status is set to OK and the result is set to the serial number of the new
        oWatch
        */

        // biggerSerialNumbers stores all SN's from compatable products that are larger than this products SN
        TreeSet<SerialNumber> biggerSerialNumbers = AbstractProduct.getBiggerSerialNumbers(new TreeSet<SerialNumber>(request.getCompatibleProducts()), this);
        if (!biggerSerialNumbers.isEmpty())
        {
            status.setStatusCode(RequestStatus.StatusCode.OK);
            status.setResult(Optional.of(biggerSerialNumbers.first().getSerialNumber()));
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
        A refund succeeds if and only if the exclusive OR of the serial number
        and the RMA is greater than 14.
        */

        BigInteger xor = this.getSerialNumber().getSerialNumber().xor(request.getRma());
        if (xor.compareTo(new BigInteger("14")) > 0)
        {
            status.setStatusCode(RequestStatus.StatusCode.OK);
            status.setResult(Optional.empty());
        }
        else
        {
            status.setStatusCode(RequestStatus.StatusCode.FAIL);
            status.setResult(Optional.empty());
        }
    }
}
