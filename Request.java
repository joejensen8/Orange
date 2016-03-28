package orange;

/**
 * Created by Joe on 9/20/2015.
 */
public interface Request
{
    public void process(Product product, RequestStatus status) throws RequestException;
}
