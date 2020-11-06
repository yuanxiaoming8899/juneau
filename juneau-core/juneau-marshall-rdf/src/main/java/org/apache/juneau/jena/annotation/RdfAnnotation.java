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
package org.apache.juneau.jena.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static org.apache.juneau.BeanContext.*;

import java.lang.annotation.*;
import java.lang.reflect.*;

import org.apache.juneau.*;
import org.apache.juneau.annotation.*;
import org.apache.juneau.jena.*;
import org.apache.juneau.reflect.*;
import org.apache.juneau.svl.*;

/**
 * Utility classes and methods for the {@link Rdf @Rdf} annotation.
 */
public class RdfAnnotation {

	/** Default value */
	public static final Rdf DEFAULT = create().build();

	/**
	 * Instantiates a new builder for this class.
	 *
	 * @return A new builder object.
	 */
	public static Builder create() {
		return new Builder();
	}

	/**
	 * Instantiates a new builder for this class.
	 *
	 * @param on The targets this annotation applies to.
	 * @return A new builder object.
	 */
	public static Builder create(Class<?>...on) {
		return create().on(on);
	}

	/**
	 * Instantiates a new builder for this class.
	 *
	 * @param on The targets this annotation applies to.
	 * @return A new builder object.
	 */
	public static Builder create(String...on) {
		return create().on(on);
	}

	/**
	 * Creates a copy of the specified annotation.
	 *
	 * @param a The annotation to copy.s
	 * @param r The var resolver for resolving any variables.
	 * @return A copy of the specified annotation.
	 */
	public static Rdf copy(Rdf a, VarResolverSession r) {
		return
			create()
			.beanUri(r.resolve(a.beanUri()))
			.collectionFormat(a.collectionFormat())
			.namespace(r.resolve(a.namespace()))
			.on(r.resolve(a.on()))
			.onClass(a.onClass())
			.prefix(r.resolve(a.prefix()))
			.build();
	}

	/**
	 * Builder class for the {@link Rdf} annotation.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jm'>{@link BeanContextBuilder#annotations(Annotation...)}
	 * </ul>
	 */
	public static class Builder extends TargetedAnnotationTMFBuilder {

		String namespace="", prefix="";
		boolean	beanUri;
		RdfCollectionFormat collectionFormat=RdfCollectionFormat.DEFAULT;

		/**
		 * Constructor.
		 */
		public Builder() {
			super(Rdf.class);
		}

		/**
		 * Instantiates a new {@link Rdf @Rdf} object initialized with this builder.
		 *
		 * @return A new {@link Rdf @Rdf} object.
		 */
		public Rdf build() {
			return new Impl(this);
		}

		/**
		 * Sets the {@link Rdf#beanUri} property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder beanUri(boolean value) {
			this.beanUri = value;
			return this;
		}

		/**
		 * Sets the {@link Rdf#collectionFormat} property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder collectionFormat(RdfCollectionFormat value) {
			this.collectionFormat = value;
			return this;
		}

		/**
		 * Sets the {@link Rdf#namespace} property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder namespace(String value) {
			this.namespace = value;
			return this;
		}

		/**
		 * Sets the {@link Rdf#prefix} property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder prefix(String value) {
			this.prefix = value;
			return this;
		}

		// <FluentSetters>

		@Override /* GENERATED - TargetedAnnotationBuilder */
		public Builder on(String...values) {
			super.on(values);
			return this;
		}

		@Override /* GENERATED - TargetedAnnotationTBuilder */
		public Builder on(java.lang.Class<?>...value) {
			super.on(value);
			return this;
		}

		@Override /* GENERATED - TargetedAnnotationTBuilder */
		public Builder onClass(java.lang.Class<?>...value) {
			super.onClass(value);
			return this;
		}

		@Override /* GENERATED - TargetedAnnotationTMFBuilder */
		public Builder on(Field...value) {
			super.on(value);
			return this;
		}

		@Override /* GENERATED - TargetedAnnotationTMFBuilder */
		public Builder on(Method...value) {
			super.on(value);
			return this;
		}

		// </FluentSetters>
	}

	private static class Impl extends TargetedAnnotationTImpl implements Rdf {

		private final boolean beanUri;
		private final RdfCollectionFormat collectionFormat;
		private final String namespace, prefix;

		Impl(Builder b) {
			super(b);
			this.beanUri = b.beanUri;
			this.collectionFormat = b.collectionFormat;
			this.namespace = b.namespace;
			this.prefix = b.prefix;
			postConstruct();
		}

		@Override /* Rdf */
		public boolean beanUri() {
			return beanUri;
		}

		@Override /* Rdf */
		public RdfCollectionFormat collectionFormat() {
			return collectionFormat;
		}

		@Override /* Rdf */
		public String namespace() {
			return namespace;
		}

		@Override /* Rdf */
		public String prefix() {
			return prefix;
		}
	}

	/**
	 * Applies targeted {@link Rdf} annotations to a {@link PropertyStoreBuilder}.
	 */
	public static class Apply extends ConfigApply<Rdf> {

		/**
		 * Constructor.
		 *
		 * @param c The annotation class.
		 * @param r The resolver for resolving values in annotations.
		 */
		public Apply(Class<Rdf> c, VarResolverSession r) {
			super(c, r);
		}

		@Override
		public void apply(AnnotationInfo<Rdf> ai, PropertyStoreBuilder psb) {
			Rdf a = ai.getAnnotation();

			if (isEmpty(a.on()) && isEmpty(a.onClass()))
				return;

			psb.prependTo(BEAN_annotations, copy(a, getVarResolver()));
		}
	}

	/**
	 * A collection of {@link Rdf @Rdf annotations}.
	 */
	@Documented
	@Target({METHOD,TYPE})
	@Retention(RUNTIME)
	@Inherited
	public static @interface Array {

		/**
		 * The child annotations.
		 */
		Rdf[] value();
	}
}