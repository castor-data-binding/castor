import java.io.FileWriter;
import java.io.IOException;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;

public class CastorTest {

    private String name;

    private CastorState state;

    public CastorTest() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the state.
     */
    public CastorState getState() {
        return state;
    }

    /**
     * @param state
     *            The state to set.
     */
    public void setState(CastorState state) {
        this.state = state;
    }

}
