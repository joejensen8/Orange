package orange;

import jdk.nashorn.internal.codegen.CompilerConstants;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;

/**
 * Created by Joe on 9/5/2015.
 */
public abstract class AbstractProduct implements Product
{
    private SerialNumber serialNumber;
    private Optional<Set<String>> description;

    AbstractProduct(SerialNumber serialNumber, Optional<Set<String>> description)
    {
        this.serialNumber = serialNumber;
        this.description = description;
    }

    // returns serial number of the product
    public SerialNumber getSerialNumber()
    {
        return serialNumber;
    }

    // returns the product name
    public String getProductName()
    {
        return getProductType().getName();
    }

    // returns the optional set description of the product
    public Optional<Set<String>> getDescription()
    {
        return description;
    }

    // override of the equals method: only compares serial numbers
    @Override
    public boolean equals(Object other)
    {
        if (other != null && other instanceof Product)
        {
            Product myP = (Product) other;
            SerialNumber mySN = ((Product) other).getSerialNumber();
            return (serialNumber.getSerialNumber().equals(mySN.getSerialNumber()));
        }
        else
        {
            return false;
        }
    }

    // override of the hashcode method: uses int value of the serial number
    @Override
    public int hashCode()
    {
        return serialNumber.getSerialNumber().intValue();
    }

    // override of the toString method:
    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder();
        output.append(getProductName());
        output.append("\n");
        output.append(serialNumber.getSerialNumber());
        output.append("\n");

        description.get().forEach(s -> {
            output.append(toProperCase(s));
            output.append("\n");
        });

        return output.toString();
    }

    // this method assists the toString method by inputted a string and making the first letter upper case
    public static String toProperCase(String input)
    {
        switch (input.length())
        {
            case 0:
            {
                return "";
            }
            case 1:
            {
                return input.substring(0,1).toUpperCase();
            }
            default:
            {
                return input.substring(0,1).toUpperCase() + input.substring(1);
            }
        }
    }

    /**
     * helper method for exchange process. It returns a set with the serial numbers greater than the Ophones SN
     * @param compProducts the set of compatable products that will be narrowed down
     * @return greaterThanSN is the TreeSet of compatable products greater than the Ophone's Serial Number
     */
    public static TreeSet<SerialNumber> getBiggerSerialNumbers(TreeSet<SerialNumber> compProducts, Product product)
    {
        TreeSet<SerialNumber> greaterThanSN = new TreeSet<>();
        for (SerialNumber elem: compProducts)
        {
            if (elem.compareTo(product.getSerialNumber()) > 0)
            {
                greaterThanSN.add(elem);
            }
        }
        return greaterThanSN;
    }

    /**
     * helper for getReplacementOphone. Finds the average of the given tree set of SerialNumbers
     * @param set the set to find the average of
     * @return BigInteger the average of the set
     */
    public static BigInteger getSetAverage(TreeSet<SerialNumber> set)
    {
        BigInteger sum = new BigInteger("0");
        String setSize = "" + set.size();
        for(SerialNumber elem: set)
        {
            sum = sum.add(elem.getSerialNumber());
        }
        if (set.size() == 0)
        {
            return new BigInteger("-1");
        }
        else
        {
            return (sum.divide(new BigInteger(setSize)));
        }
    }

    @FunctionalInterface
    private interface testValidSN
    {
        Boolean test(SerialNumber serialNumber);
    }

    @FunctionalInterface
    private interface construct
    {
        Product createObject (SerialNumber serialNumber, Optional<Set<String>> description);
    }

    // Note: The initialized HashMap "serialNumberMap" and "constructorMap" have been declared with a capacity of 5. When adding a product, increase those values by 1
    private static HashMap<ProductType, construct> constructorMap = new HashMap<ProductType, construct>(5)
    {{
        // When adding a product, one must add the product to serialNumberMap
        put(ProductType.OPAD, (serialNumber, description) -> {return new Opad(serialNumber, description);});
        put(ProductType.OPHONE, (serialNumber, description) -> {return new Ophone(serialNumber, description);});
        put(ProductType.OPOD, (serialNumber, description) -> {return new Opod(serialNumber, description);});
        put(ProductType.OTV, (serialNumber, description) -> {return new Otv(serialNumber, description);});
        put(ProductType.OWATCH, (serialNumber, description) -> {return new Owatch(serialNumber, description);});
    }};

    private static HashMap<ProductType, testValidSN> serialNumberMap = new HashMap<ProductType, testValidSN>(5)
    {{
        // When adding a product, one must add the product to serialNumberMap
        put(ProductType.OPAD, (serialNumber) ->  {return Opad.isValidSerialNumber(serialNumber);});
        put(ProductType.OPHONE, (serialNumber) -> {return Ophone.isValidSerialNumber(serialNumber);});
        put(ProductType.OPOD, (serialNumber) -> {return Opod.isValidSerialNumber(serialNumber);});
        put(ProductType.OTV, (serialNumber) -> {return Otv.isValidSerialNumber(serialNumber);});
        put(ProductType.OWATCH, (serialNumber) -> {return Owatch.isValidSerialNumber(serialNumber);});
    }};

    public static Product make(ProductType productType, SerialNumber serialNumber, Optional<Set<String>> description) throws ProductException
    {
        if (serialNumberMap.containsKey(productType))
        {
            return ifValidGetProduct(productType, serialNumber, description);
        }
        else
        {
            throw new ProductException(productType, serialNumber, ProductException.ErrorCode.INVALID_PRODUCT_TYPE);
        }
    }

    // getProduct returns the product for the factory method construct if the serial number is valid, else an exception is thrown.
    private static Product ifValidGetProduct(ProductType productType, SerialNumber serialNumber, Optional<Set<String>> description) throws ProductException
    {
        if (serialNumberMap.get(productType).test(serialNumber))
        {
            return constructorMap.get(productType).createObject(serialNumber, description);
        }
        else
        {
            throw new ProductException(productType, serialNumber, ProductException.ErrorCode.INVALID_SERIAL_NUMBER);
        }
    }
}