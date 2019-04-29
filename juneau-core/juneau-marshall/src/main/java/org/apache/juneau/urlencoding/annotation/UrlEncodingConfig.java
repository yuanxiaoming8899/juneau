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
package org.apache.juneau.urlencoding.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.*;

import org.apache.juneau.urlencoding.*;

/**
 * Annotation for specifying config properties defined in {@link UrlEncodingSerializer} and {@link UrlEncodingParser}.
 *
 * <p>
 * Used primarily for specifying bean configuration properties on REST classes and methods.
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Inherited
public @interface UrlEncodingConfig {

	//-------------------------------------------------------------------------------------------------------------------
	// UrlEncodingSerializer
	//-------------------------------------------------------------------------------------------------------------------

	/**
	 * Configuration property:  Parser bean property collections/arrays as separate key/value pairs.
	 *
	 * <p>
	 * This is the parser-side equivalent of the {@link UrlEncodingSerializer#URLENC_expandedParams} setting.
	 *
	 * <p>
	 * If <js>"false"</js>, serializing the array <code>[1,2,3]</code> results in <code>?key=$a(1,2,3)</code>.
	 * <br>If <js>"true"</js>, serializing the same array results in <code>?key=1&amp;key=2&amp;key=3</code>.
	 *
	 * <p>
	 * This option only applies to beans.
	 *
	 * <h5 class='section'>Notes:</h5>
	 * <ul class='spaced-list'>
	 * 	<li>
	 * 		If parsing multi-part parameters, it's highly recommended to use Collections or Lists
	 * 		as bean property types instead of arrays since arrays have to be recreated from scratch every time a value
	 * 		is added to it.
	 * </ul>
	 *
	 * <p>
	 * Possible values:
	 * <ul>
	 * 	<li><js>"true"</js>
	 * 	<li><js>"false"</js> (default)
	 * </ul>
	 *
	 * <h5 class='section'>See Also:</h5>
	 * <ul>
	 * 	<li class='jf'>{@link UrlEncodingSerializer#URLENC_expandedParams}
	 * </ul>
	 */
	String expandedParams() default "";
}
