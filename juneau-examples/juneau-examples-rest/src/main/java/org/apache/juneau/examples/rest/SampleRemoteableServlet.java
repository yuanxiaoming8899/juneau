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
package org.apache.juneau.examples.rest;

import java.util.*;
import java.util.Map;

import org.apache.juneau.examples.addressbook.*;
import org.apache.juneau.rest.annotation.*;
import org.apache.juneau.rest.remoteable.*;

/**
 * Class showing the functionality of the RemoteableServlet class.
 */
@SuppressWarnings("serial")
@RestResource(
	path="/remoteable",
	messages="nls/SampleRemoteableServlet",
	title="Remoteable Service Proxy API",
	description="Sample class showing how to use remoteable proxies.",
	htmldoc=@HtmlDoc(
		navlinks={
			"up: request:/..",
			"options: servlet:/?method=OPTIONS",
			"source: $C{Source/gitHub}/org/apache/juneau/examples/rest/$R{servletClassSimple}.java"
		}
	),
	// Allow us to use method=POST from a browser.
	allowedMethodParams="*",
	swagger=@ResourceSwagger(
		contact="name:'Juneau Developer',email:'dev@juneau.apache.org'",
		license="name:'Apache 2.0',url:'http://www.apache.org/licenses/LICENSE-2.0.html'",
		version="2.0",
		termsOfService="You are on your own.",
		externalDocs="description:'Apache Juneau',url:'http://juneau.apache.org'"
	)
)
public class SampleRemoteableServlet extends RemoteableServlet {

	private final AddressBook addressBook;
	
	public SampleRemoteableServlet() {
		addressBook = new AddressBook();
		addressBook.init();
	}

	@Override /* RemoteableServlet */
	protected Map<Class<?>,Object> getServiceMap() throws Exception {
		Map<Class<?>,Object> m = new LinkedHashMap<>();

		// In this simplified example, we expose the same POJO service under two different interfaces.
		// One is IAddressBook which only exposes methods defined on that interface, and
		// the other is AddressBook itself which exposes all methods defined on the class itself.
		m.put(IAddressBook.class, addressBook);
		m.put(AddressBook.class, addressBook);
		return m;
	}
}
