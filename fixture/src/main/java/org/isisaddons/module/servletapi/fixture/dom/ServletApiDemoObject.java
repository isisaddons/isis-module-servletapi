/*
 *  Copyright 2013~2014 Dan Haywood
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.isisaddons.module.servletapi.fixture.dom;

import java.io.IOException;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.isisaddons.module.servletapi.dom.HttpServletRequestProvider;
import org.isisaddons.module.servletapi.dom.HttpServletResponseProvider;
import org.isisaddons.module.servletapi.dom.ServletContextProvider;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.util.ObjectContracts;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")
@DomainObject(
        objectType = "SERVLETAPI_DEMO_OBJECT"
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
public class ServletApiDemoObject implements Comparable<ServletApiDemoObject> {

    //region > name (property)

    private String name;

    @javax.jdo.annotations.Column(allowsNull="false")
    @Title(sequence="1")
    @MemberOrder(sequence="1")
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    //endregion

    //region > servlet context name (derived property)
    public String getServletContextName() {
        return servletContextProvider.getServletContext().getServletContextName();
    }
    //endregion

    //region > request locale name (derived property)
    public String getRequestLocale() {
        return httpServletRequestProvider.getServletRequest().getLocale().toString();
    }
    //endregion

    //region > cookie (action)
    @MemberOrder(sequence = "1")
    public ServletApiDemoObject addHeader(
            @ParameterLayout(named = "Header")
            final String header,
            @ParameterLayout(named = "Value")
            final String value) throws IOException {
        httpServletResponseProvider.getServletResponse().addHeader(header, value);
        return this;
    }

    public String default0AddHeader() {
        return "x-isisaddons-module-servletapi-demo";
    }
    public String default1AddHeader() {
        return "hello!";
    }
    //endregion


    //region > compareTo

    @Override
    public int compareTo(ServletApiDemoObject other) {
        return ObjectContracts.compare(this, other, "name");
    }

    //endregion

    //region > injected services

    @javax.inject.Inject
    @SuppressWarnings("unused")
    private DomainObjectContainer container;

    @javax.inject.Inject
    private ServletContextProvider servletContextProvider;

    @javax.inject.Inject
    private HttpServletRequestProvider httpServletRequestProvider;

    @javax.inject.Inject
    private HttpServletResponseProvider httpServletResponseProvider;
    //endregion

}
