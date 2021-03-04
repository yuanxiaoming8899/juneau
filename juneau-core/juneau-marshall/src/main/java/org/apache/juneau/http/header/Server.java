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
package org.apache.juneau.http.header;

import java.util.function.*;

import org.apache.juneau.http.annotation.*;

/**
 * Represents a parsed <l>Server</l> HTTP response header.
 *
 * <p>
 * A name for the server.
 *
 * <h5 class='figure'>Example</h5>
 * <p class='bcode w800'>
 * 	Server: Apache/2.4.1 (Unix)
 * </p>
 *
 * <h5 class='topic'>RFC2616 Specification</h5>
 *
 * The Server response-header field contains information about the software used by the origin server to handle the
 * request.
 * The field can contain multiple product tokens (section 3.8) and comments identifying the server and any significant
 * sub-products.
 * The product tokens are listed in order of their significance for identifying the application.
 *
 * <p class='bcode w800'>
 * 	Server         = "Server" ":" 1*( product | comment )
 * </p>
 *
 * <p>
 * Example:
 * <p class='bcode w800'>
 * 	Server: CERN/3.0 libwww/2.17
 * </p>
 *
 * <p>
 * If the response is being forwarded through a proxy, the proxy application MUST NOT modify the Server response-header.
 * Instead, it SHOULD include a Via field (as described in section 14.45).
 *
 * <p>
 * Note: Revealing the specific software version of the server might allow the server machine to become more vulnerable
 * to attacks against software that is known to contain security holes.
 * Server implementors are encouraged to make this field a configurable option.
 *
 * <ul class='seealso'>
 * 	<li class='extlink'>{@doc ExtRFC2616}
 * </ul>
 */
@Header("Server")
public class Server extends BasicStringHeader {

	private static final long serialVersionUID = 1L;

	/**
	 * Convenience creator.
	 *
	 * @param value
	 * 	The header value.
	 * 	<br>Can be any of the following:
	 * 	<ul>
	 * 		<li>{@link String}
	 * 		<li>Anything else - Converted to <c>String</c> using {@link Object#toString()} and then parsed.
	 * 	</ul>
	 * @return A new {@link Server} object.
	 */
	public static Server of(Object value) {
		if (value == null)
			return null;
		return new Server(value);
	}

	/**
	 * Convenience creator using supplier.
	 *
	 * <p>
	 * Header value is re-evaluated on each call to {@link #getValue()}.
	 *
	 * @param value
	 * 	The header value supplier.
	 * 	<br>Can be any of the following:
	 * 	<ul>
	 * 		<li>{@link String}
	 * 		<li>Anything else - Converted to <c>String</c> using {@link Object#toString()} and then parsed.
	 * 	</ul>
	 * @return A new {@link Server} object.
	 */
	public static Server of(Supplier<?> value) {
		if (value == null)
			return null;
		return new Server(value);
	}

	/**
	 * Constructor.
	 *
	 * @param value
	 * 	The header value.
	 * 	<br>Can be any of the following:
	 * 	<ul>
	 * 		<li>{@link String}
	 * 		<li>Anything else - Converted to <c>String</c> using {@link Object#toString()} and then parsed.
	 * 		<li>A {@link Supplier} of anything on this list.
	 * 	</ul>
	 */
	public Server(Object value) {
		super("Server", value);
	}

	/**
	 * Constructor
	 *
	 * @param value
	 * 	The header value.
	 */
	public Server(String value) {
		this((Object)value);
	}
}
