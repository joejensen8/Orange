package orange;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigInteger;

/**
 * Created by Joe on 9/20/2015.
 */
public final class Refund implements Request
{
    private BigInteger RMA;

    private Refund(Builder builder)
    {
        RMA = builder.getRma(); // BigInteger is immutable already
    }

    public void process(Product product, RequestStatus status) throws RequestException
    {
        try
        {
            product.process(this, status);
        }catch(ProductException exception)
        {
            throw new RequestException(RequestException.ErrorCode.REFUND_FAILED);
        }
    }

    public BigInteger getRma()
    {
        return RMA;
    }

    public static class Builder
    {
        private BigInteger RMA;

        public Builder setRMA(BigInteger rma)
        {
            RMA = rma;
            return this;
        }

        public BigInteger getRma()
        {
            return RMA;
        }

        public Refund build() throws RequestException
        {
            if (RMA != null)
            {
                return new Refund(this);
            }
            else
            {
                throw new RequestException(RequestException.ErrorCode.INVALID_RMA);
            }
        }
    }

}
