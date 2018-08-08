package org.eurekaclinical.casmock.servlet;

/*-
 * #%L
 * CAS Mock
 * %%
 * Copyright (C) 2016 - 2018 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class XmlService {

    private static final VelocityEngine VELOCITY_ENGINE = new VelocityEngine();

    private static final Map<String, Template> TEMPLATE_MAP = new HashMap();

    private static final String[] XML_FILES = {
        "xml/proxyResponse.xml",
        "xml/proxyValidateResponse.xml",
        "xml/proxyValidateResponseWithPGTIOU.xml",
        "xml/serviceResponse.xml",
        "xml/serviceResponseWithPGTIOU.xml"};

    public XmlService() {
        VELOCITY_ENGINE.setProperty(
                RuntimeConstants.RESOURCE_LOADER, 
                "classpath");
        VELOCITY_ENGINE.setProperty(
                "classpath.resource.loader.class", 
                ClasspathResourceLoader.class.getName());
        VELOCITY_ENGINE.init();
        Template t;
        for (String xmlFile : XML_FILES) {
            t = VELOCITY_ENGINE.getTemplate(xmlFile);
            TEMPLATE_MAP.put(xmlFile, t);
        }
    }

    public String getResponse(final String responseXml) {
        StringWriter writer = new StringWriter();
        TEMPLATE_MAP.get(responseXml).merge(getVelocityContext(), writer);
        return writer.toString();
    }

    private VelocityContext getVelocityContext() {
        VelocityContext vc = new VelocityContext();
        vc.put("casUser", "superuser");
        return vc;
    }

}
