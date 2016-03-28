package orange;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sun.reflect.generics.tree.Tree;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Joe on 9/5/2015.
 */
public final class Ophone extends AbstractProduct
{
    Ophone(SerialNumber serialNumber, Optional<Set<String>> description)
    {
        super(serialNumber, description);
    }

    // returns the product type
    public ProductType getProductType()
    {
        return ProductType.OPHONE;
    }

    // returns true if the serial number is valid and false otherwise
    public static Boolean isValidSerialNumber(SerialNumber serialNumber)
    {
        Boolean isOdd = serialNumber.isOdd();
        Boolean gcdGreaterThan42 = isGCDCorrect(serialNumber);
        return (isOdd && gcdGreaterThan42);
    }

    private static boolean isGCDCorrect(SerialNumber serialNumber)
    {
        SerialNumber mySN = new SerialNumber(new BigInteger("630"));
        BigInteger gcd = serialNumber.gcd(mySN);
        int myGCD = gcd.intValue();
        return (myGCD > 42);
    }

    public void process(Exchange request, RequestStatus status) throws ProductException
    {
        /*
        An oPhone exchange works as follows. Consider the compatible
        serial numbers that are strictly greater than the oPhone’s, and take their
        average. Then, exchange the oPhone with the largest compatible serial
        number that is strictly greater than the oPhone’s and strictly less than
        the average. If no such compatible product exists, then the exchange
        fails.
        */

        TreeSet<SerialNumber> compProducts = new TreeSet<>(request.getCompatibleProducts());
        TreeSet<SerialNumber> greaterThanSN = AbstractProduct.getBiggerSerialNumbers(compProducts, this);
        SerialNumber replacement = getReplacementOphone(greaterThanSN);
        if (replacement != null)
        {
            status.setStatusCode(RequestStatus.StatusCode.OK);
            status.setResult(Optional.of(replacement.getSerialNumber()));
        }
        else
        {
            status.setStatusCode(RequestStatus.StatusCode.FAIL);
            status.setResult(Optional.empty());
        }
        // Make sure to JUNIT getBiggerSerialNumbers in abstract product and setAverage
    }

    private SerialNumber getReplacementOphone(TreeSet<SerialNumber> set)
    {
        BigInteger setAverage = AbstractProduct.getSetAverage(set);
        SerialNumber replace = null;
        for (SerialNumber elem: set)
        {
            if (elem.getSerialNumber().compareTo(setAverage) < 0 && elem.compareTo(replace) > 0)
            {
                replace = elem;
            }
        }
        return replace;
    }

    public void process(Refund request, RequestStatus status) throws ProductException
    {
        /*
        An oPhone succeeds if and only if the serial number can be obtained
        by shifting to the left (changed to right via discussino) the RMA by 1, 2, or 3 bits.
         */

        BigInteger rma = request.getRma();
        boolean serialNumberFound = false;
        int timesShifted = 0;
        SERIAL_NUMBER_FOUND:
        while (timesShifted < 3)
        {
            rma = rma.shiftRight(1);
            if (rma.compareTo(this.getSerialNumber().getSerialNumber()) == 0)
            {
                serialNumberFound = true;
                break SERIAL_NUMBER_FOUND;
            }
            timesShifted++;
        }

        if (serialNumberFound)
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
