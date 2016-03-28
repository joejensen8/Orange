package orange;

/**
 * Created by Joe on 9/5/2015.
 */
public enum ProductType
{
    OPOD    ("oPod"),
    OPAD    ("oPad"),
    OPHONE  ("oPhone"),
    OWATCH  ("oWatch"),
    OTV     ("oTv");

    // returns the name of the product
    private final String name;
    ProductType(String name)
    {
        this.name = name;
    }

    // returns the name of the product
    public String getName()
    {
        return this.name;
    }
}
