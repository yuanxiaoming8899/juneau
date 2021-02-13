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
package org.apache.juneau.rest.client;

import static org.apache.juneau.assertions.Assertions.*;

import java.io.*;
import java.net.*;

import org.apache.http.*;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.*;
import org.apache.juneau.http.response.*;
import org.apache.juneau.rest.*;
import org.apache.juneau.rest.annotation.*;
import org.apache.juneau.rest.mock.*;
import org.junit.*;

public class BasicHttpRequestRetryHandler_Test {

	@Rest
	public static class A extends BasicRestObject {
		@RestGet
		public Ok get() {
			return Ok.OK;
		}
	}

	public static class A1 extends HttpRequestExecutor {
		@Override
		public HttpResponse execute(HttpRequest request, HttpClientConnection conn, HttpContext context) throws IOException, HttpException {
			throw new UnknownHostException("foo");
		}
	}

	@Test
	public void a01_basic() throws Exception {
		RestClient x = MockRestClient.create(A.class).retryHandler(new BasicHttpRequestRetryHandler(1, 1, true)).requestExecutor(new A1()).build();
		assertThrown(()->x.get().run()).contains("foo");
	}
}
