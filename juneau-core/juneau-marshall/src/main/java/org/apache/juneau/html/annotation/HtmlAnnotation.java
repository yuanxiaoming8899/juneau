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
package org.apache.juneau.html.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static org.apache.juneau.BeanContext.*;

import java.lang.annotation.*;
import java.lang.reflect.*;

import org.apache.juneau.*;
import org.apache.juneau.annotation.*;
import org.apache.juneau.html.*;
import org.apache.juneau.reflect.*;
import org.apache.juneau.svl.*;

/**
 * Utility classes and methods for the {@link Html @Html} annotation.
 */
@SuppressWarnings("rawtypes")
public class HtmlAnnotation {

	/** Default value */
	public static final Html DEFAULT = create().build();

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
	public static Html copy(Html a, VarResolverSession r) {
		return
			create()
			.anchorText(r.resolve(a.anchorText()))
			.format(a.format())
			.link(r.resolve(a.link()))
			.noTableHeaders(a.noTableHeaders())
			.noTables(a.noTables())
			.on(r.resolve(a.on()))
			.onClass(a.onClass())
			.render(a.render())
			.build();
	}

	/**
	 * Builder class for the {@link Html} annotation.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jm'>{@link BeanContextBuilder#annotations(Annotation...)}
	 * </ul>
	 */
	public static class Builder extends TargetedAnnotationTMFBuilder {

		String anchorText="", link="";
		HtmlFormat format=HtmlFormat.HTML;
		boolean noTableHeaders, noTables;
		Class<? extends HtmlRender> render=HtmlRender.class;

		/**
		 * Constructor.
		 */
		public Builder() {
			super(Html.class);
		}

		/**
		 * Instantiates a new {@link Html @Html} object initialized with this builder.
		 *
		 * @return A new {@link Html @Html} object.
		 */
		public Html build() {
			return new Impl(this);
		}

		/**
		 * Sets the {@link Html#anchorText()} property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder anchorText(String value) {
			this.anchorText = value;
			return this;
		}

		/**
		 * Sets the {@link Html#format()} property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder format(HtmlFormat value) {
			this.format = value;
			return this;
		}

		/**
		 * Sets the {@link Html#link()} property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder link(String value) {
			this.link = value;
			return this;
		}

		/**
		 * Sets the {@link Html#noTableHeaders()} property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder noTableHeaders(boolean value) {
			this.noTableHeaders = value;
			return this;
		}

		/**
		 * Sets the {@link Html#noTables()} property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder noTables(boolean value) {
			this.noTables = value;
			return this;
		}

		/**
		 * Sets the {@link Html#render()} property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder render(Class<? extends HtmlRender> value) {
			this.render = value;
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

	private static class Impl extends TargetedAnnotationTImpl implements Html {

		private boolean noTableHeaders, noTables;
		private Class<? extends HtmlRender> render;
		private final String anchorText, link;
		private HtmlFormat format;

		Impl(Builder b) {
			super(b);
			this.anchorText = b.anchorText;
			this.format = b.format;
			this.link = b.link;
			this.noTableHeaders = b.noTableHeaders;
			this.noTables = b.noTables;
			this.render = b.render;
			postConstruct();
		}

		@Override /* Html */
		public String anchorText() {
			return anchorText;
		}

		@Override /* Html */
		public HtmlFormat format() {
			return format;
		}

		@Override /* Html */
		public String link() {
			return link;
		}

		@Override /* Html */
		public boolean noTableHeaders() {
			return noTableHeaders;
		}

		@Override /* Html */
		public boolean noTables() {
			return noTables;
		}

		@Override /* Html */
		public Class<? extends HtmlRender> render() {
			return render;
		}
	}

	/**
	 * Applies targeted {@link Html} annotations to a {@link PropertyStoreBuilder}.
	 */
	public static class Apply extends ConfigApply<Html> {

		/**
		 * Constructor.
		 *
		 * @param c The annotation class.
		 * @param r The resolver for resolving values in annotations.
		 */
		public Apply(Class<Html> c, VarResolverSession r) {
			super(c, r);
		}

		@Override
		public void apply(AnnotationInfo<Html> ai, PropertyStoreBuilder psb) {
			Html a = ai.getAnnotation();

			if (isEmpty(a.on()) && isEmpty(a.onClass()))
				return;

			psb.prependTo(BEAN_annotations, copy(a, getVarResolver()));
		}
	}

	/**
	 * A collection of {@link Html @Html annotations}.
	 */
	@Documented
	@Target({METHOD,TYPE})
	@Retention(RUNTIME)
	@Inherited
	public static @interface Array {

		/**
		 * The child annotations.
		 */
		Html[] value();
	}
}