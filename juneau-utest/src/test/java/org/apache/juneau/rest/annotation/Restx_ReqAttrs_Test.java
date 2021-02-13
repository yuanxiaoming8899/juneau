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
package org.apache.juneau.rest.annotation;

import static org.junit.runners.MethodSorters.*;

import java.util.*;

import org.apache.juneau.collections.*;
import org.apache.juneau.http.annotation.HasQuery;
import org.apache.juneau.rest.*;
import org.apache.juneau.rest.client.*;
import org.apache.juneau.rest.mock.*;
import org.junit.*;

@FixMethodOrder(NAME_ASCENDING)
public class Restx_ReqAttrs_Test {

	//------------------------------------------------------------------------------------------------------------------
	// Test properties inheritance.
	//------------------------------------------------------------------------------------------------------------------

	@Rest(defaultRequestAttributes={"p1:v1","p2:v2"})
	public static class A {}

	@Rest(defaultRequestAttributes={"p2:v2a","p3:v3","p4:v4"})
	public static class A1 extends A {}

	@Rest
	public static class A2 extends A1 {
		@RestOp
		public OMap a1(RequestAttributes attrs) {
			// Should show {p1:'v1',p2:'v2a',p3:'v3',p4:'v4'}
			return transform(attrs);
		}
		@RestOp(defaultRequestAttributes={"p4:v4a","p5:v5"})
		public OMap a2(RequestAttributes attrs, @HasQuery("override") boolean override) {
			// Should show {p1:'v1',p2:'v2a',p3:'v3',p4:'v4a',p5:'v5'} when override is false.
			// Should show {p1:'x',p2:'x',p3:'x',p4:'x',p5:'x'} when override is true.
			if (override) {
				attrs.put("p1", "x");
				attrs.put("p2", "x");
				attrs.put("p3", "x");
				attrs.put("p4", "x");
				attrs.put("p5", "x");
			}
			return transform(attrs);
		}

		@RestGet
		public OMap b1(RequestAttributes attrs) {
			// Should show {p1:'v1',p2:'v2a',p3:'v3',p4:'v4'}
			return transform(attrs);
		}
		@RestGet(defaultRequestAttributes={"p4:v4a","p5:v5"})
		public OMap b2(RequestAttributes attrs, @HasQuery("override") boolean override) {
			// Should show {p1:'v1',p2:'v2a',p3:'v3',p4:'v4a',p5:'v5'} when override is false.
			// Should show {p1:'x',p2:'x',p3:'x',p4:'x',p5:'x'} when override is true.
			if (override) {
				attrs.put("p1", "x");
				attrs.put("p2", "x");
				attrs.put("p3", "x");
				attrs.put("p4", "x");
				attrs.put("p5", "x");
			}
			return transform(attrs);
		}

		@RestPut
		public OMap c1(RequestAttributes attrs) {
			// Should show {p1:'v1',p2:'v2a',p3:'v3',p4:'v4'}
			return transform(attrs);
		}
		@RestPut(defaultRequestAttributes={"p4:v4a","p5:v5"})
		public OMap c2(RequestAttributes attrs, @HasQuery("override") boolean override) {
			// Should show {p1:'v1',p2:'v2a',p3:'v3',p4:'v4a',p5:'v5'} when override is false.
			// Should show {p1:'x',p2:'x',p3:'x',p4:'x',p5:'x'} when override is true.
			if (override) {
				attrs.put("p1", "x");
				attrs.put("p2", "x");
				attrs.put("p3", "x");
				attrs.put("p4", "x");
				attrs.put("p5", "x");
			}
			return transform(attrs);
		}

		@RestPost
		public OMap d1(RequestAttributes attrs) {
			// Should show {p1:'v1',p2:'v2a',p3:'v3',p4:'v4'}
			return transform(attrs);
		}
		@RestPost(defaultRequestAttributes={"p4:v4a","p5:v5"})
		public OMap d2(RequestAttributes attrs, @HasQuery("override") boolean override) {
			// Should show {p1:'v1',p2:'v2a',p3:'v3',p4:'v4a',p5:'v5'} when override is false.
			// Should show {p1:'x',p2:'x',p3:'x',p4:'x',p5:'x'} when override is true.
			if (override) {
				attrs.put("p1", "x");
				attrs.put("p2", "x");
				attrs.put("p3", "x");
				attrs.put("p4", "x");
				attrs.put("p5", "x");
			}
			return transform(attrs);
		}

		@RestDelete
		public OMap e1(RequestAttributes attrs) {
			// Should show {p1:'v1',p2:'v2a',p3:'v3',p4:'v4'}
			return transform(attrs);
		}
		@RestDelete(defaultRequestAttributes={"p4:v4a","p5:v5"})
		public OMap e2(RequestAttributes attrs, @HasQuery("override") boolean override) {
			// Should show {p1:'v1',p2:'v2a',p3:'v3',p4:'v4a',p5:'v5'} when override is false.
			// Should show {p1:'x',p2:'x',p3:'x',p4:'x',p5:'x'} when override is true.
			if (override) {
				attrs.put("p1", "x");
				attrs.put("p2", "x");
				attrs.put("p3", "x");
				attrs.put("p4", "x");
				attrs.put("p5", "x");
			}
			return transform(attrs);
		}

		private OMap transform(RequestAttributes attrs) {
			OMap m = new OMap();
			for (Map.Entry<String,Object> e : attrs.entrySet()) {
				if (e.getKey().startsWith("p"))
					m.put(e.getKey(), e.getValue());
			}
			return m;
		}
	}

	@Test
	public void a01_basic() throws Exception {
		RestClient a = MockRestClient.build(A2.class);

		a.get("/a1").run().assertBody().is("{p1:'v1',p2:'v2a',p3:'v3',p4:'v4'}");
		a.get("/a2").run().assertBody().is("{p1:'v1',p2:'v2a',p3:'v3',p4:'v4a',p5:'v5'}");
		a.get("/a2?override").run().assertBody().is("{p1:'x',p2:'x',p3:'x',p4:'x',p5:'x'}");

		a.get("/b1").run().assertBody().is("{p1:'v1',p2:'v2a',p3:'v3',p4:'v4'}");
		a.get("/b2").run().assertBody().is("{p1:'v1',p2:'v2a',p3:'v3',p4:'v4a',p5:'v5'}");
		a.get("/b2?override").run().assertBody().is("{p1:'x',p2:'x',p3:'x',p4:'x',p5:'x'}");

		a.put("/c1").run().assertBody().is("{p1:'v1',p2:'v2a',p3:'v3',p4:'v4'}");
		a.put("/c2").run().assertBody().is("{p1:'v1',p2:'v2a',p3:'v3',p4:'v4a',p5:'v5'}");
		a.put("/c2?override").run().assertBody().is("{p1:'x',p2:'x',p3:'x',p4:'x',p5:'x'}");

		a.post("/d1").run().assertBody().is("{p1:'v1',p2:'v2a',p3:'v3',p4:'v4'}");
		a.post("/d2").run().assertBody().is("{p1:'v1',p2:'v2a',p3:'v3',p4:'v4a',p5:'v5'}");
		a.post("/d2?override").run().assertBody().is("{p1:'x',p2:'x',p3:'x',p4:'x',p5:'x'}");

		a.delete("/e1").run().assertBody().is("{p1:'v1',p2:'v2a',p3:'v3',p4:'v4'}");
		a.delete("/e2").run().assertBody().is("{p1:'v1',p2:'v2a',p3:'v3',p4:'v4a',p5:'v5'}");
		a.delete("/e2?override").run().assertBody().is("{p1:'x',p2:'x',p3:'x',p4:'x',p5:'x'}");
	}
}
