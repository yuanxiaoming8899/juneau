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
package org.apache.juneau.rest.config;

import org.apache.juneau.annotation.*;
import org.apache.juneau.html.*;
import org.apache.juneau.html.annotation.*;
import org.apache.juneau.json.*;
import org.apache.juneau.rest.annotation.*;
import org.apache.juneau.serializer.annotation.*;

/**
 * Basic configuration for a REST resource that supports JSON and HTML transport.
 *
 * <p>
 * 	Default settings defined:
 * </p>
 * <ul class='spaced-list'>
 * 	<li class='ja'>{@link Rest}:
 * 		<ul>
 * 			<li class='jma'>{@link Rest#serializers() serializers}:
 * 				<ul class='javatree'>
 * 					<li class='jc'>{@link JsonSerializer}
 * 					<li class='jc'>{@link SimpleJsonSerializer}
 * 					<li class='jc'>{@link HtmlDocSerializer}
 * 				</ul>
 * 			</li>
 * 			<li class='jma'>{@link Rest#parsers() parsers}:
 * 				<ul class='javatree'>
 * 					<li class='jc'>{@link JsonParser}
 * 					<li class='jc'>{@link SimpleJsonParser}
 * 					<li class='jc'>{@link HtmlParser}
 * 				</ul>
 * 			</li>
 * 			<li class='jma'>{@link Rest#defaultAccept() defaultAccept}:  <js>"text/json"</js>
 * 			<li class='jma'>{@link Rest#config() config}:  <js>"$S{juneau.configFile,SYSTEM_DEFAULT}"</js>
 *		</ul>
 *	</li>
 * 	<li class='ja'>{@link BeanConfig}:
 * 		<ul>
 * 			<li class='jma'>{@link BeanConfig#ignoreUnknownBeanProperties() ignoreUnknownBeanProperties}:  <js>"true"</js>
 * 		</ul>
 * 	</li>
 * 	<li class='ja'>{@link SerializerConfig}:
 * 		<ul>
 * 			<li class='jma'>{@link SerializerConfig#uriResolution() uriResolution}:  <js>"ROOT_RELATIVE"</js>
 * 		</ul>
 * 	</li>
 * 	<li class='ja'>{@link HtmlDocConfig}:
 * 		<ul>
 * 			<li class='jma'>{@link HtmlDocConfig#header() header}:  <js>"&lt;h1>$RS{title}&lt;/h1>&lt;h2>$RS{operationSummary,description}&lt;/h2>$C{REST/header}"</js>
 * 			<li class='jma'>{@link HtmlDocConfig#navlinks() navlinks}:  <js>"up: request:/.."</js>
 * 			<li class='jma'>{@link HtmlDocConfig#stylesheet() stylesheet}:  <js>"$C{REST/theme,servlet:/htdocs/themes/devops.css}"</js>
 * 			<li class='jma'>{@link HtmlDocConfig#head() head}:  <js>"$C{REST/head}"</js>
 * 			<li class='jma'>{@link HtmlDocConfig#footer() footer}:  <js>"$C{REST/footer}"</js>
 * 			<li class='jma'>{@link HtmlDocConfig#nowrap() nowrap}:  <js>"true"</js>
 * 		</ul>
 * 	</li>
 * </ul>
 *
 * <p>
 * 	This annotation can be applied to REST resource classes to define common JSON default configurations:
 * </p>
 * <p class='bcode w800'>
 * 	<jc>// Used on a top-level resource.</jc>
 * 	<ja>@Rest</ja>
 * 	<jk>public class</jk> MyResource <jk>extends</jk> RestServlet <jk>implements</jk> BasicJsonHtmlConfig { ... }
 * </p>
 * <p class='bcode w800'>
 * 	<jc>// Used on a child resource.</jc>
 * 	<ja>@Rest</ja>
 * 	<jk>public class</jk> MyResource <jk>extends</jk> RestObject <jk>implements</jk> BasicJsonHtmlConfig { ... }
 * </p>
 *
 * <p>
 * 	Note that the framework will aggregate annotations defined on all classes in the class hierarchy with
 * 	values defined on child classes overriding values defined on parent classes.  That allows any values defined
 * 	on this interface to be overridden by annotations defined on the implemented class.
 * </p>
 *
 * <ul class='seealso'>
 * 	<li class='link'>{@doc jrs.AnnotatedClasses}
 * 	<li class='extlink'>{@source}
 * </ul>
 */
@Rest(

	// Default serializers for all Java methods in the class.
	serializers={
		HtmlDocSerializer.class,
		JsonSerializer.class,
		SimpleJsonSerializer.class
	},

	// Default parsers for all Java methods in the class.
	parsers={
		HtmlParser.class,
		JsonParser.class,
		SimpleJsonParser.class
	},

	defaultAccept="text/json",

	// Optional external configuration file.
	config="$S{juneau.configFile,SYSTEM_DEFAULT}"
)
@BeanConfig(
	// When parsing generated beans, ignore unknown properties that may only exist as getters and not setters.
	ignoreUnknownBeanProperties="true"
)
@SerializerConfig(
	// Enable automatic resolution of URI objects to root-relative values.
	uriResolution="ROOT_RELATIVE"
)
@HtmlDocConfig(

	// Default page header contents.
	header={
		"<h1>$RS{title}</h1>",  // Use @Rest(title)
		"<h2>$RS{operationSummary,description}</h2>", // Use either @RestOp(summary) or @Rest(description)
		"$C{REST/header}"  // Extra header HTML defined in external config file.
	},

	// Basic page navigation links.
	navlinks={
		"up: request:/.."
	},

	// Default stylesheet to use for the page.
	// Can be overridden from external config file.
	// Default is DevOps look-and-feel (aka Depression look-and-feel).
	stylesheet="$C{REST/theme,servlet:/htdocs/themes/devops.css}",

	// Default contents to add to the <head> section of the HTML page.
	// Use it to add a favicon link to the page.
	head="$C{REST/head}",

	// No default page footer contents.
	// Can be overridden from external config file.
	footer="$C{REST/footer}",

	// By default, table cell contents should not wrap.
	nowrap="true"
)
public interface BasicJsonHtmlConfig {}
