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
public final class Otv extends AbstractProduct
{
    Otv(SerialNumber serialNumber, Optional<Set<String>> description)
    {
        super(serialNumber, description);
    }

    // returns the product type
    public ProductType getProductType()
    {
        return ProductType.OTV;
    }

    // returns true if the serial number is valid and false otherwise
    public static Boolean isValidSerialNumber(SerialNumber serialNumber)
    {
        Boolean isOdd = serialNumber.isOdd();
        Boolean gcdLessThanEqual14 = isGCDCorrect(serialNumber);
        return (isOdd && gcdLessThanEqual14);
    }

    // returns true if the GCD is less than or equal to 14
    private static boolean isGCDCorrect(SerialNumber serialNumber)
    {
        SerialNumber mySN = new SerialNumber(new BigInteger("630"));
        BigInteger gcd = serialNumber.gcd(mySN);
        int myGCD = gcd.intValue();
        return (myGCD <= 14);
    }

    // this process method deals with exchanges
    public void process(Exchange request, RequestStatus status) throws ProductException
    {
        /*
        As for exchanges, consider the compatible serial numbers that are
        strictly greater than the oTv’s and less than or equal than the oTv’s plus
        1024. Then, take their average. Then, exchange the oTv with the largest
        compatible serial number that is strictly greater than the oPhone’s and
        strictly less than the average. If no such compatible product exists, then
        the exchange fails.
        */

        TreeSet<SerialNumber> biggerSerialNumbers = AbstractProduct.getBiggerSerialNumbers(new TreeSet<SerialNumber>(request.getCompatibleProducts()), this);
        SerialNumber replacement = findReplacementSerialNumber(biggerSerialNumbers);
        if (replacement != null) // if findReplacementSerialNumbers finds no replacement serial number, the exchange fails
        {
            status.setStatusCode(RequestStatus.StatusCode.OK);
            status.setResult(Optional.of(replacement.getSerialNumber()));
        }
        else
        {
            status.setStatusCode(RequestStatus.StatusCode.FAIL);
            status.setResult(Optional.empty());
        }
    }

    // this method finds the serial number to replace the original serial number
    // If no such serial number exists, null is returned
    private SerialNumber findReplacementSerialNumber(TreeSet<SerialNumber> set)
    {
        TreeSet<SerialNumber> mySet = new TreeSet<>(set);
        BigInteger upperLimit = new BigInteger("1024");
        upperLimit = upperLimit.add(this.getSerialNumber().getSerialNumber());
        mySet = removeElementsAbove(mySet, upperLimit);
        BigInteger setAverage = AbstractProduct.getSetAverage(mySet);
        if (setAverage.compareTo(new BigInteger("-1")) == 0 || mySet.size() == 1)
        {
            return null; // returning null because no valid replacement serial number exists
        }
        mySet = removeElementsAbove(mySet, setAverage);
        return mySet.last();
    }

    // This method takes in a TreeSet and returns a new TreeSet containing all elements strictly bellow the upperLimit
    private TreeSet<SerialNumber> removeElementsAbove(TreeSet<SerialNumber> set, BigInteger upperLimit)
    {
        TreeSet<SerialNumber> mySet = new TreeSet<>();
        for (SerialNumber elem: set)
        {
            if (elem.getSerialNumber().compareTo(upperLimit) < 0)
            {
                mySet.add(elem);
            }
        }
        return mySet;
    }

    public void process(Refund request, RequestStatus status) throws ProductException
    {
        // A refund succeeds only if the RMA is positive.

        BigInteger rma = request.getRma();
        if (rma.compareTo(new BigInteger("0")) >= 0)
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
