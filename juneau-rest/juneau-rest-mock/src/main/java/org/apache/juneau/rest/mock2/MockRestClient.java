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
package org.apache.juneau.rest.mock2;

import java.net.*;

import org.apache.http.*;
import org.apache.juneau.*;
import org.apache.juneau.rest.annotation.*;
import org.apache.juneau.rest.client2.*;

/**
 * Mocked {@link RestClient}.
 *
 * <ul class='seealso'>
 * 	<li class='link'>{@doc juneau-rest-mock}
 * </ul>
 */
public class MockRestClient extends RestClient {

	/**
	 * Constructor.
	 *
	 * @param ps
	 * 	The REST bean or bean class annotated with {@link Rest @Rest}.
	 * 	<br>If a class, it must have a no-arg constructor.
	 */
	public MockRestClient(PropertyStore ps) {
		super(ps);
		ps.getInstanceProperty("MockRestClient.MockHttpClientConnectionManager.o", MockHttpClientConnectionManager.class, null).init(this);
	}

	/**
	 * Creates a new {@link RestClientBuilder} configured with the specified REST implementation bean or bean class.
	 *
	 * @param impl
	 * 	The REST bean or bean class annotated with {@link Rest @Rest}.
	 * 	<br>If a class, it must have a no-arg constructor.
	 * @return A new builder.
	 */
	public static MockRestClientBuilder create(Object impl) {
		return new MockRestClientBuilder().restBean(impl);
	}

	/**
	 * Creates a new {@link RestClient} with no registered serializer or parser.
	 *
	 * <p>
	 * Equivalent to calling:
	 * <p class='bcode w800'>
	 * 	MockRestClient.create(impl).build();
	 * </p>
	 *
	 * @param impl
	 * 	The REST bean or bean class annotated with {@link Rest @Rest}.
	 * 	<br>If a class, it must have a no-arg constructor.
	 * @return A new builder.
	 */
	public static MockRestClient build(Object impl) {
		return create(impl).build();
	}

	/**
	 * Creates a new {@link RestClient} with JSON marshalling support.
	 *
	 * <p>
	 * Equivalent to calling:
	 * <p class='bcode w800'>
	 * 	MockRestClient.create(impl).json().build();
	 * </p>
	 *
	 * @param impl
	 * 	The REST bean or bean class annotated with {@link Rest @Rest}.
	 * 	<br>If a class, it must have a no-arg constructor.
	 * @return A new builder.
	 */
	public static MockRestClient buildJson(Object impl) {
		return create(impl).json().build(MockRestClient.class);
	}

	/**
	 * Creates a new {@link RestClient} with Simplified-JSON marshalling support.
	 *
	 * <p>
	 * Equivalent to calling:
	 * <p class='bcode w800'>
	 * 	MockRestClient.create(impl).json().build();
	 * </p>
	 *
	 * @param impl
	 * 	The REST bean or bean class annotated with {@link Rest @Rest}.
	 * 	<br>If a class, it must have a no-arg constructor.
	 * @return A new builder.
	 */
	public static MockRestClient buildSimpleJson(Object impl) {
		return create(impl).simpleJson().build(MockRestClient.class);
	}

	/**
	 * Creates a {@link RestRequest} object from the specified {@link HttpRequest} object.
	 *
	 * <p>
	 * Subclasses can override this method to provide their own specialized {@link RestRequest} objects.
	 *
	 * @param uri The target.
	 * @param method The HTTP method (uppercase).
	 * @param hasBody Whether this method has a request entity.
	 * @return A new {@link RestRequest} object.
	 * @throws RestCallException If an exception or non-200 response code occurred during the connection attempt.
	 */
	@Override
	protected MockRestRequest createRequest(URI uri, String method, boolean hasBody) throws RestCallException {
		return new MockRestRequest(this, uri, method, hasBody);
	}

	// <CONFIGURATION-PROPERTIES>

	// </CONFIGURATION-PROPERTIES>
}
