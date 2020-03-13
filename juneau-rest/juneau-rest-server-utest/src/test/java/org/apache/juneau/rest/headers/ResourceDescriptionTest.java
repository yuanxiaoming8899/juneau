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
package org.apache.juneau.rest.headers;

import static org.junit.Assert.*;

import org.apache.juneau.marshall.*;
import org.apache.juneau.rest.helper.*;
import org.junit.*;
import org.junit.runners.*;

/**
 * Validates the handling of the Accept-Charset header.
 */
@SuppressWarnings({})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ResourceDescriptionTest {

	@Test
	public void a01_basic() throws Exception {
		ResourceDescription rd = new ResourceDescription("a","b?c=d&e=f","g");
		assertEquals("<table><tr><td>name</td><td><a href=\"/b?c=d&amp;e=f\">a</a></td></tr><tr><td>description</td><td>g</td></tr></table>", Html.DEFAULT.toString(rd));
		assertEquals("{name:'a',description:'g'}", SimpleJson.DEFAULT.toString(rd));
	}
}