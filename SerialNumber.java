package orange;

import java.math.BigInteger;

/**
 * Created by Joe on 9/4/2015.
 */
public class SerialNumber implements Comparable<SerialNumber>
{
    private BigInteger serialNumber;

    public SerialNumber(BigInteger serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    // returns the greatest common denominator between the serial number and the input other
    public BigInteger gcd(SerialNumber other)
    {
        return this.serialNumber.gcd(other.getSerialNumber());
    }

    // returns the remainder of the division between this serial number and the input other
    public BigInteger mod(SerialNumber other)
    {
        return this.serialNumber.mod(other.getSerialNumber());
    }

    // returns true if the input "bit" is asserted and false otherwise
    public boolean testBit(int bit)
    {
        return this.serialNumber.testBit(bit);
    }

    // returns true if the serial number is even
    public boolean isEven()
    {
        return (!serialNumber.testBit(0));
    }

    // returns true if the serial number is odd
    public boolean isOdd()
    {
        return (!this.isEven());
    }

    // returns the serial number as a big integer
    public BigInteger getSerialNumber()
    {
        return serialNumber;
    }

    public int compareTo(SerialNumber other)
    {
        return this.serialNumber.compareTo(other.getSerialNumber());
    }
}