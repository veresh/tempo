package org.intalio.tempo.security.ws;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.intalio.tempo.security.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OMParser {
    private static final Logger LOG = LoggerFactory.getLogger(OMParser.class);

    private OMElement _element;

    public OMParser(OMElement element) {
        _element = element;
        _element.build();
        if (_element.getParent() != null) _element.detach();
    }

    public String getRequiredString(QName parameter) {
        OMElement e = _element.getFirstChildWithName(parameter);
        if (e == null)
            throw new IllegalArgumentException("Missing parameter: " + parameter);
        String text = e.getText();
        if (text == null || text.trim().length() == 0)
            throw new IllegalArgumentException("Empty parameter: " + parameter);
        if (LOG.isDebugEnabled())
            LOG.debug("Parameter " + parameter + ": " + text);
        return text;
    }

    public Property[] getProperties(QName parameter) {
        OMElement e = _element.getFirstChildWithName(parameter);
        if (e == null)
            throw new IllegalArgumentException("Missing properties parameter: " + parameter);
        Iterator<OMElement> iter = e.getChildElements();
        ArrayList<Property> props = new ArrayList<Property>();
        while (iter.hasNext()) {
            OMElement prop = iter.next();
            OMElement name = prop.getFirstChildWithName(Constants.NAME);
            if (e == null)
                throw new IllegalArgumentException("Missing property name: " + prop);
            OMElement value = prop.getFirstChildWithName(Constants.VALUE);
            if (e == null)
                throw new IllegalArgumentException("Missing property value: " + prop);
            props.add(new Property(name.getText(), value.getText()));
        }
        return props.toArray(new Property[props.size()]);
    }
}
