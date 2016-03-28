package orange;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.text.html.Option;
import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Joe on 9/5/2015.
 */
public final class Opad extends AbstractProduct
{
    Opad(SerialNumber serialNumber, Optional<Set<String>> description)
    {
        super(serialNumber, description);
    }

    // returns the producty type
    public ProductType getProductType()
    {
        return ProductType.OPAD;
    }

    // returns true if the serial number is valid and false otherwise
    public static Boolean isValidSerialNumber(SerialNumber serialNumber)
    {
        Boolean isEven = serialNumber.isEven();
        Boolean thirdBitSet = serialNumber.testBit(3);
        return (isEven && thirdBitSet);
    }

    public void process(Exchange request, RequestStatus status) throws ProductException
    {
        /*
        An oPad is exchanged with the product that has the largest
        compatible serial number that is strictly less than the original oPad
        serial number. If no such compatible product exists, the exchange fails.
        Otherwise, the status is set to OK and the result is set to the serial
        number of the new oPad.
         */

        TreeSet<SerialNumber> compProducts = new TreeSet<>(request.getCompatibleProducts());
        SerialNumber replacement = findReplacement(compProducts);

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
    }

    // This method finds the replacement serial number for an exchange
    // according to the exchange process specifications
    private SerialNumber findReplacement(TreeSet<SerialNumber> compProducts)
    {
        SerialNumber replacement = null;
        for (SerialNumber element: compProducts)
        {
            if (element.compareTo(this.getSerialNumber()) == -1) //replacement is null on first call
                // if element is less than original serial number and greater than replacement
            {
                if (replacement == null)
                {
                    replacement = element;
                }
                else if (element.getSerialNumber().compareTo(replacement.getSerialNumber()) == 1)
                {
                    replacement = element;
                }
            }
        }
        return replacement;
    }


    public void process(Refund request, RequestStatus status) throws ProductException
    {
        BigInteger gcd = request.getRma().gcd(this.getSerialNumber().getSerialNumber());
        if (gcd.compareTo(new BigInteger("12")) >= 0) // can't convert to long... chops value off
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
