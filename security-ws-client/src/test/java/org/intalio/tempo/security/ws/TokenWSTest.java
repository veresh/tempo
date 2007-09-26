package org.intalio.tempo.security.ws;

import static org.junit.Assert.fail;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.intalio.tempo.security.Property;
import org.intalio.tempo.security.authentication.AuthenticationException;
import org.intalio.tempo.security.rbac.RBACException;
import org.intalio.tempo.security.util.PropertyUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Integration tests for TokenWS: exercise the marshalling/unmarshalling between the client and server.
 * 
 * Uses direct in-VM invocation by default.  If the property "org.intalio.tempo.security.ws.endpoint"
 * is defined, it is used to make actual WS calls against a live instance.
 */
public class TokenWSTest {

    protected static TokenClient _client;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String config = TokenWSTest.class.getClassLoader().getResource("securityConfig.xml").getFile();
        String dir = new File(config).getParent();
        System.setProperty(Constants.CONFIG_DIR_PROPERTY, dir);

        String endpoint = System.getProperty("org.intalio.tempo.security.ws.endpoint");
        if (endpoint != null) {
            _client = new TokenClient(endpoint);
        } else {
            _client = new TokenClientMock();
        }
    }

    @Test
    public void testAuthenticateUser() throws AuthenticationException, RBACException, RemoteException {
        String token = _client.authenticateUser("exolab\\castor", "castor");
        if (token == null || token.length() < 10)
            fail("invalid token returned: " + token);

        try {
            // try invalid user
            _client.authenticateUser("exolab\\foo", "bar");
            fail("Invalid user should have raised AuthenticationException");
        } catch (AuthenticationException except) {
            // pass
        } catch (AxisFault except) {
            // pass
        }

        try {
            // try invalid password
            _client.authenticateUser("exolab\\castor", "bar");
            fail("Invalid password should have raised AuthenticationException");
        } catch (AuthenticationException except) {
            // pass
        } catch (AxisFault except) {
            // pass
        }
    }

    @Test
    public void testAuthenticateUserWithCredentials() throws AuthenticationException, RBACException, RemoteException {
        Property[] props = new Property[1];
        props[0] = new Property("password", "castor");
        _client.authenticateUser("exolab\\castor", props);
    }

    @Test
    public void testGetTokenProperties() throws AuthenticationException, RBACException, RemoteException {
        String token = _client.authenticateUser("exolab\\castor", "castor");
        if (token == null || token.length() < 10)
            fail("invalid token returned: " + token);

        Property[] props = _client.getTokenProperties(token);
        if (props == null || props.length < 1)
            fail("invalid properties returned: " + PropertyUtils.toMap(props));
    }

    static class TokenClientMock extends TokenClient {

        TokenConstants _tokenWS = new TokenConstants();

        public TokenClientMock() {
            super("internal://");
        }

        protected OMParser invoke(String action, OMElement request) throws AxisFault {
            try {
                Method method = _tokenWS.getClass().getMethod(action, OMElement.class);
                OMElement response = (OMElement) method.invoke(_tokenWS, request);
                return new OMParser(response);
            } catch (InvocationTargetException except) {
                throw AxisFault.makeFault(except.getTargetException());
            } catch (Exception except) {
                throw new RuntimeException(except);
            }
        }
    }
}
