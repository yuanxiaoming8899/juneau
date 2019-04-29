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
package org.apache.juneau.jsonschema.annotation;

import static org.apache.juneau.jsonschema.JsonSchemaGenerator.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.*;

import org.apache.juneau.*;
import org.apache.juneau.annotation.*;
import org.apache.juneau.jsonschema.*;

/**
 * Annotation for specifying config properties defined in {@link JsonSchemaGenerator}.
 *
 * <p>
 * Used primarily for specifying bean configuration properties on REST classes and methods.
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Inherited
public @interface JsonSchemaConfig {

	//-------------------------------------------------------------------------------------------------------------------
	// JsonSchemaGenerator
	//-------------------------------------------------------------------------------------------------------------------

	/**
	 * Configuration property:  Add descriptions to types.
	 *
	 * <p>
	 * Identifies which categories of types that descriptions should be automatically added to generated schemas.
	 *
	 * <p>
	 * The description is the result of calling {@link ClassMeta#getFullName()}.
	 *
	 * <p>
	 * The format is a comma-delimited list of any of the following values:
	 *
	 * <ul class='doctree'>
	 * 	<li><js>"BEAN"</js>
	 * 	<li><js>"COLLECTION"</js>
	 * 	<li><js>"ARRAY"</js>
	 * 	<li><js>"MAP"</js>
	 * 	<li><js>"STRING"</js>
	 * 	<li><js>"NUMBER"</js>
	 * 	<li><js>"BOOLEAN"</js>
	 * 	<li><js>"ANY"</js>
	 * 	<li><js>"OTHER"</js>
	 * </ul>
	 *
	 * <h5 class='section'>See Also:</h5>
	 * <ul>
	 * 	<li class='jf'>{@link JsonSchemaGenerator#JSONSCHEMA_addDescriptionsTo}
	 * </ul>
	 */
	String addDescriptionsTo() default "";

	/**
	 * Configuration property:  Add examples.
	 *
	 * <p>
	 * Identifies which categories of types that examples should be automatically added to generated schemas.
	 * <p>
	 * The examples come from calling {@link ClassMeta#getExample(BeanSession)} which in turn gets examples
	 * from the following:
	 * <ul class='doctree'>
	 * 	<li class='ja'>{@link Example}
	 * 	<li class='jf'>{@link BeanContext#BEAN_examples}
	 * </ul>
	 *
	 * <p>
	 * The format is a comma-delimited list of any of the following values:
	 *
	 * <ul class='doctree'>
	 * 	<li><js>"BEAN"</js>
	 * 	<li><js>"COLLECTION"</js>
	 * 	<li><js>"ARRAY"</js>
	 * 	<li><js>"MAP"</js>
	 * 	<li><js>"STRING"</js>
	 * 	<li><js>"NUMBER"</js>
	 * 	<li><js>"BOOLEAN"</js>
	 * 	<li><js>"ANY"</js>
	 * 	<li><js>"OTHER"</js>
	 * </ul>
	 *
	 * <h5 class='section'>See Also:</h5>
	 * <ul>
	 * 	<li class='jf'>{@link JsonSchemaGenerator#JSONSCHEMA_addDescriptionsTo}
	 * </ul>
	 */
	String addExamplesTo() default "";

	/**
	 * Configuration property:  Allow nested descriptions.
	 *
	 * <p>
	 * Identifies whether nested descriptions are allowed in schema definitions.
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
	 * 	<li class='jf'>{@link JsonSchemaGenerator#JSONSCHEMA_allowNestedDescriptions}
	 * </ul>
	 */
	String allowNestedDescriptions() default "";

	/**
	 * Configuration property:  Allow nested examples.
	 *
	 * <p>
	 * Identifies whether nested examples are allowed in schema definitions.
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
	 * 	<li class='jf'>{@link JsonSchemaGenerator#JSONSCHEMA_allowNestedExamples}
	 * </ul>
	 */
	String allowNestedExamples() default "";

	/**
	 * Configuration property:  Bean schema definition mapper.
	 *
	 * <p>
	 * Interface to use for converting Bean classes to definition IDs and URIs.
	 *
	 * <p>
	 * Used primarily for defining common definition sections for beans in Swagger JSON.
	 *
	 * <p>
	 * This setting is ignored if {@link #JSONSCHEMA_useBeanDefs} is not enabled.
	 *
	 * <h5 class='section'>See Also:</h5>
	 * <ul>
	 * 	<li class='jf'>{@link JsonSchemaGenerator#JSONSCHEMA_beanDefMapper}
	 * </ul>
	 */
	Class<? extends BeanDefMapper> beanDefMapper() default BeanDefMapper.Null.class;

	/**
	 * Configuration property:  Default schemas.
	 *
	 * <p>
	 * Allows you to override or provide custom schema information for particular class types.
	 *
	 * <p>
	 * Keys are the class.
	 * <br>Values are Simple-JSON objects.
	 *
	 * <h5 class='section'>See Also:</h5>
	 * <ul>
	 * 	<li class='jf'>{@link JsonSchemaGenerator#JSONSCHEMA_defaultSchemas}
	 * </ul>
	 */
	CSEntry[] defaultSchemas() default {};

	/**
	 * Configuration property:  Ignore types from schema definitions.
	 *
	 * <p>
	 * Defines class name patterns that should be ignored when generating schema definitions in the generated
	 * Swagger documentation.
	 *
	 * <h5 class='section'>See Also:</h5>
	 * <ul>
	 * 	<li class='jf'>{@link JsonSchemaGenerator#JSONSCHEMA_ignoreTypes}
	 * </ul>
	 */
	String ignoreTypes() default "";

	/**
	 * Configuration property:  Use bean definitions.
	 *
	 * <p>
	 * When enabled, schemas on beans will be serialized as the following:
	 * <p class='bcode w800'>
	 * 	{
	 * 		type: <js>'object'</js>,
	 * 		<js>'$ref'</js>: <js>'#/definitions/TypeId'</js>
	 * 	}
	 * </p>
	 *
	 * <p>
	 * The definitions can then be retrieved from the session using {@link JsonSchemaGeneratorSession#getBeanDefs()}.
	 *
	 * <p>
	 * Definitions can also be added programmatically using {@link JsonSchemaGeneratorSession#addBeanDef(String, ObjectMap)}.
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
	 * 	<li class='jf'>{@link JsonSchemaGenerator#JSONSCHEMA_useBeanDefs}
	 * </ul>
	 */
	String useBeanDefs() default "";
}
