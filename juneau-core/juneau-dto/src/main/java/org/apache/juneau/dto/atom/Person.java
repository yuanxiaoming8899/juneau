// ***************************************************************************************************************************
// * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.  See the NOTICE file *
// * distributed with this work for additional information regarding copyright ownership.  The ASF licenses this file        *
// * to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance            *
// * with the License.  You may obtain a copy of the License at                                                              *
// *                                                                                                                         *
// *  http://www.apache.org/licenses/LICENSE-2.0                                                                             *
// *                                                                                                                         *
// * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an  *
// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the        *
// * specific language governing permissions and limitations under the License.                                              *
// ***************************************************************************************************************************
package org.apache.juneau.dto.atom;

import static org.apache.juneau.internal.CollectionUtils.*;
import static org.apache.juneau.internal.StringUtils.*;

import java.net.*;
import java.util.*;

import org.apache.juneau.*;

/**
 * Represents an <c>atomPersonConstruct</c> construct in the RFC4287 specification.
 *
 * <h5 class='figure'>Schema</h5>
 * <p class='bschema'>
 * 	atomPersonConstruct =
 * 		atomCommonAttributes,
 * 		(element atom:name { text }
 * 		&amp; element atom:uri { atomUri }?
 * 		&amp; element atom:email { atomEmailAddress }?
 * 		&amp; extensionElement*)
 * </p>
 *
 * <ul class='seealso'>
 * 	<li class='link'>{@doc jd.Atom}
 * 	<li class='jp'>{@doc package-summary.html#TOC}
 * 	<li class='extlink'>{@source}
 * </ul>
 */
public class Person extends Common {

	private String name;
	private URI uri;
	private String email;


	/**
	 * Normal constructor.
	 *
	 * @param name The name of the person.
	 */
	public Person(String name) {
		name(name);
	}

	/** Bean constructor. */
	public Person() {}


	//-----------------------------------------------------------------------------------------------------------------
	// Bean properties
	//-----------------------------------------------------------------------------------------------------------------

	/**
	 * Bean property getter:  <property>name</property>.
	 *
	 * <p>
	 * The name of the person.
	 *
	 * @return The property value, or <jk>null</jk> if it is not set.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Bean property setter:  <property>name</property>.
	 *
	 * <p>
	 * The name of the person.
	 *
	 * @param value
	 * 	The new value for this property.
	 * 	<br>Can be <jk>null</jk> to unset the property.
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Bean property fluent getter:  <property>name</property>.
	 *
	 * <p>
	 * The name of the person.
	 *
	 * @return The property value as an {@link Optional}.  Never <jk>null</jk>.
	 */
	public Optional<String> name() {
		return optional(name);
	}

	/**
	 * Bean property fluent setter:  <property>name</property>.
	 *
	 * <p>
	 * The name of the person.
	 *
	 * @param value
	 * 	The new value for this property.
	 * 	<br>Can be <jk>null</jk> to unset the property.
	 * @return This object.
	 */
	public Person name(String value) {
		setName(value);
		return this;
	}

	/**
	 * Bean property getter:  <property>uri</property>.
	 *
	 * <p>
	 * The URI of the person.
	 *
	 * @return The property value, or <jk>null</jk> if it is not set.
	 */
	public URI getUri() {
		return uri;
	}

	/**
	 * Bean property setter:  <property>uri</property>.
	 *
	 * <p>
	 * The URI of the person.
	 *
	 * <p>
	 * The value can be of any of the following types: {@link URI}, {@link URL}, {@link String}.
	 * Strings must be valid URIs.
	 *
	 * <p>
	 * URIs defined by {@link UriResolver} can be used for values.
	 *
	 * @param value
	 * 	The new value for this property.
	 * 	<br>Can be <jk>null</jk> to unset the property.
	 */
	public void setUri(Object value) {
		this.uri = toURI(value);
	}

	/**
	 * Bean property fluent getter:  <property>uri</property>.
	 *
	 * <p>
	 * The URI of the person.
	 *
	 * @return The property value as an {@link Optional}.  Never <jk>null</jk>.
	 */
	public Optional<URI> uri() {
		return optional(uri);
	}

	/**
	 * Bean property fluent setter:  <property>v</property>.
	 *
	 * <p>
	 * The URI of the person.
	 *
	 * @param value
	 * 	The new value for this property.
	 * 	<br>Can be <jk>null</jk> to unset the property.
	 * @return This object.
	 */
	public Person uri(Object value) {
		setUri(value);
		return this;
	}

	/**
	 * Bean property getter:  <property>email</property>.
	 *
	 * <p>
	 * The email address of the person.
	 *
	 * @return The property value, or <jk>null</jk> if it is not set.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Bean property setter:  <property>email</property>.
	 *
	 * <p>
	 * The email address of the person.
	 *
	 * @param value
	 * 	The new value for this property.
	 * 	<br>Can be <jk>null</jk> to unset the property.
	 */
	public void setEmail(String value) {
		this.email = value;
	}

	/**
	 * Bean property fluent getter:  <property>email</property>.
	 *
	 * <p>
	 * The email address of the person.
	 *
	 * @return The property value as an {@link Optional}.  Never <jk>null</jk>.
	 */
	public Optional<String> email() {
		return optional(email);
	}

	/**
	 * Bean property fluent setter:  <property>email</property>.
	 *
	 * <p>
	 * The email address of the person.
	 *
	 * @param value
	 * 	The new value for this property.
	 * 	<br>Can be <jk>null</jk> to unset the property.
	 * @return This object.
	 */
	public Person email(String value) {
		setEmail(value);
		return this;
	}


	//-----------------------------------------------------------------------------------------------------------------
	// Overridden setters (to simplify method chaining)
	//-----------------------------------------------------------------------------------------------------------------

	@Override /* Common */
	public Person base(Object base) {
		super.base(base);
		return this;
	}

	@Override /* Common */
	public Person lang(String lang) {
		super.lang(lang);
		return this;
	}
}
