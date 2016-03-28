package orange;

import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Created by Joe on 9/20/2015.
 */
public final class Exchange implements Request
{
    private NavigableSet<SerialNumber> serialNumbers;

    private Exchange(Builder builder)
    {
        serialNumbers = new TreeSet<>(builder.getCompatibleProducts());
        // creates a new exchange
        // with the list of compatible products provided by the builder
    }

    public void process(Product product, RequestStatus status) throws RequestException
    {
        try
        {
            product.process(this, status);
        }catch(ProductException exception)
        {
            throw new RequestException(RequestException.ErrorCode.EXCHANGE_FAILED);
        }
    }

    public NavigableSet<SerialNumber> getCompatibleProducts()
    {
        return new TreeSet<>(serialNumbers);
        // returns the serial numbers of the products compatible with the customer request
    }

    public static class Builder
    {
        private Set<SerialNumber> compatibleSerNums = new TreeSet<>();

        public Builder addCompatible(SerialNumber serialNumber)
        {
            compatibleSerNums.add(serialNumber);
            return this;
        }

        public Set<SerialNumber> getCompatibleProducts()
        {
            return compatibleSerNums; // new?
        }

        public Exchange build()
        {
            return new Exchange(this);
            // returns an Exchange with the given list of compatible products
        }
    }

}

// NEED TO FIX:
// getCompatibleProducts is not immutable in Exchange class - doesn't need to check for immutability biginteger checks for it already
// if RMA is null, throws request exception
// FIX TESTS TO SHOW CHANGES & IMMUTABILITY