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
package org.apache.juneau.utils;

/**
 * Interface for resolving variables within strings.
 */
public interface StringResolver {

	/**
	 * NO-OP resolver.
	 */
	public static final StringResolver NOOP = new StringResolver() {
		@Override
		public String resolve(String input) {
			return input;
		}
	};

	/**
	 * Resolve any variables in the specified input string.
	 *
	 * @param input The string containing variables to resolve.
	 * @return A string with variables resolved.
	 */
	String resolve(String input);
}
