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
package org.apache.juneau.http.exception;

import static org.junit.runners.MethodSorters.*;

import org.apache.juneau.rest.annotation.*;
import org.apache.juneau.rest.client.*;
import org.apache.juneau.rest.mock.*;
import org.junit.*;

@FixMethodOrder(NAME_ASCENDING)
public class PreconditionRequired_Test {

	private static final int CODE = PreconditionRequired.CODE;
	private static final String MESSAGE = PreconditionRequired.MESSAGE;

	@Rest
	public static class A {
		@RestGet
		public void f1() throws PreconditionRequired {
			throw new PreconditionRequired();
		}
		@RestGet
		public void f2() throws PreconditionRequired {
			throw new PreconditionRequired("foo {0}", "bar");
		}
		@RestGet
		public void f3() throws PreconditionRequired {
			throw new PreconditionRequired(new RuntimeException("baz"));
		}
		@RestGet
		public void f4() throws PreconditionRequired {
			throw new PreconditionRequired(new RuntimeException("baz"), "foo {0}", "bar");
		}
		@RestGet
		public void f5() throws PreconditionRequired {
			throw new PreconditionRequired().header("Foo", "bar");
		}
		@RestGet
		public void f6() throws PreconditionRequired {
			throw new PreconditionRequired("foo");
		}
	}

	@Test
	public void a01_basic() throws Exception {
		RestClient c = MockRestClient.create(A.class).ignoreErrors().noLog().build();

		c.get("/f1").run()
			.assertStatus().code().is(CODE)
			.assertBody().is(MESSAGE);
		c.get("/f2").run()
			.assertStatus().code().is(CODE)
			.assertBody().is("foo bar");
		c.get("/f3").run()
			.assertStatus().code().is(CODE)
			.assertBody().is("baz");
		c.get("/f4").run()
			.assertStatus().code().is(CODE)
			.assertBody().is("foo bar");
		c.get("/f5").run()
			.assertStatus().code().is(CODE)
			.assertBody().is(MESSAGE)
			.assertStringHeader("Foo").is("bar");
		c.get("/f6").run()
			.assertStatus().code().is(CODE)
			.assertBody().is("foo");
	}
}
