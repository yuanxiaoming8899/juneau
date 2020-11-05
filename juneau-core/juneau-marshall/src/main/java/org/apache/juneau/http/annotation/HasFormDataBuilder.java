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
package org.apache.juneau.http.annotation;

import java.lang.annotation.*;

import org.apache.juneau.*;
import org.apache.juneau.annotation.*;

/**
 * Builder class for the {@link HasFormData} annotation.
 *
 * <ul class='seealso'>
 * 	<li class='jm'>{@link BeanContextBuilder#annotations(Annotation...)}
 * </ul>
 */
public class HasFormDataBuilder extends AnnotationBuilder {

	/** Default value */
	public static final HasFormData DEFAULT = create().build();

	/**
	 * Instantiates a new builder for this class.
	 *
	 * @return A new builder object.
	 */
	public static HasFormDataBuilder create() {
		return new HasFormDataBuilder();
	}

	private static class Impl extends AnnotationImpl implements HasFormData {

		private final String n, name, value;

		Impl(HasFormDataBuilder b) {
			super(b);
			this.n = b.n;
			this.name = b.name;
			this.value = b.value;
			postConstruct();
		}

		@Override
		public String n() {
			return n;
		}

		@Override
		public String name() {
			return name;
		}

		@Override
		public String value() {
			return value;
		}
	}


	String n="", name="", value="";

	/**
	 * Constructor.
	 */
	public HasFormDataBuilder() {
		super(HasFormData.class);
	}

	/**
	 * Instantiates a new {@link HasFormData @HasFormData} object initialized with this builder.
	 *
	 * @return A new {@link HasFormData @HasFormData} object.
	 */
	public HasFormData build() {
		return new Impl(this);
	}

	/**
	 * Sets the {@link HasFormData#n} property on this annotation.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public HasFormDataBuilder n(String value) {
		this.n = value;
		return this;
	}

	/**
	 * Sets the {@link HasFormData#name} property on this annotation.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public HasFormDataBuilder name(String value) {
		this.name = value;
		return this;
	}

	/**
	 * Sets the {@link HasFormData#value} property on this annotation.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public HasFormDataBuilder value(String value) {
		this.value = value;
		return this;
	}

	// <FluentSetters>
	// </FluentSetters>
}