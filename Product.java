package orange;

import java.util.Optional;
import java.util.Set;

/**
 * Created by Joe on 9/5/2015.
 */
public interface Product
{
    /**
     * This method returns the serial number of the product
     * @return SerialNumber the serial number of the product
     */
    public SerialNumber getSerialNumber();

    /**
     * returns the enum ProductType of the product
     * @return ProductType
     */
    public ProductType getProductType();

    /**
     * returns the ProductName of the product
     * @return String the product name
     */
    public String getProductName();

    /**
     * returns the set of strings containting the description of the product
     * @return Optional<Set<String>> the set containting the description
     */
    public Optional<Set<String>> getDescription();

    /**
     * Process processes a request to exchange a product for another product
     * @param request the exchange request
     * @param status the status of the exchange
     * @throws ProductException
     */
    public void process(Exchange request, RequestStatus status) throws ProductException;

    /**
     * Process process a request to get a refund for a purchased product
     * @param request the refund request
     * @param status the status of the refund
     * @throws ProductException
     */
    public void process(Refund request, RequestStatus status) throws ProductException;
}
