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
package org.apache.juneau.dto.html5;

import org.apache.juneau.annotation.*;
import org.apache.juneau.internal.*;

/**
 * DTO for an HTML <a class="doclink" href="https://www.w3.org/TR/html5/forms.html#the-select-element">&lt;select&gt;</a>
 * element.
 * 
 * <h5 class='section'>See Also:</h5>
 * <ul class='doctree'>
 * 	<li class='link'><a class='doclink' href='../../../../../overview-summary.html#juneau-dto.HTML5'>Overview &gt; juneau-dto &gt; HTML5</a>
 * </ul>
 */
@Bean(typeName="select")
public class Select extends HtmlElementContainer {

	/**
	 * <a class="doclink" href="https://www.w3.org/TR/html5/forms.html#attr-fe-autofocus">autofocus</a> attribute.
	 * 
	 * <p>
	 * Automatically focus the form control when the page is loaded.
	 * 
	 * @param autofocus
	 * 	The new value for this attribute.
	 * 	Typically a {@link Boolean} or {@link String}.
	 * @return This object (for method chaining).
	 */
	public final Select autofocus(Object autofocus) {
		attr("autofocus", autofocus);
		return this;
	}

	/**
	 * <a class="doclink" href="https://www.w3.org/TR/html5/forms.html#attr-fe-disabled">disabled</a> attribute.
	 * 
	 * <p>
	 * Whether the form control is disabled.
	 * 
	 * @param disabled
	 * 	The new value for this attribute.
	 * 	Typically a {@link Boolean} or {@link String}.
	 * @return This object (for method chaining).
	 */
	public final Select disabled(Object disabled) {
		attr("disabled", disabled);
		return this;
	}

	/**
	 * <a class="doclink" href="https://www.w3.org/TR/html5/forms.html#attr-fae-form">form</a> attribute.
	 * 
	 * <p>
	 * Associates the control with a form element.
	 * 
	 * @param form The new value for this attribute.
	 * @return This object (for method chaining).
	 */
	public final Select form(String form) {
		attr("form", form);
		return this;
	}

	/**
	 * <a class="doclink" href="https://www.w3.org/TR/html5/forms.html#attr-select-multiple">multiple</a> attribute.
	 * 
	 * <p>
	 * Whether to allow multiple values.
	 * 
	 * @param multiple
	 * 	The new value for this attribute.
	 * 	Typically a {@link Boolean} or {@link String}.
	 * @return This object (for method chaining).
	 */
	public final Select multiple(Object multiple) {
		attr("multiple", multiple);
		return this;
	}

	/**
	 * <a class="doclink" href="https://www.w3.org/TR/html5/forms.html#attr-fe-name">name</a> attribute.
	 * 
	 * <p>
	 * Name of form control to use for form submission and in the form.elements API.
	 * 
	 * @param name The new value for this attribute.
	 * @return This object (for method chaining).
	 */
	public final Select name(String name) {
		attr("name", name);
		return this;
	}

	/**
	 * <a class="doclink" href="https://www.w3.org/TR/html5/forms.html#attr-select-required">required</a> attribute.
	 * 
	 * <p>
	 * Whether the control is required for form submission.
	 * 
	 * @param required
	 * 	The new value for this attribute.
	 * 	Typically a {@link Boolean} or {@link String}.
	 * @return This object (for method chaining).
	 */
	public final Select required(Object required) {
		attr("required", required);
		return this;
	}

	/**
	 * <a class="doclink" href="https://www.w3.org/TR/html5/forms.html#attr-select-size">size</a> attribute.
	 * 
	 * <p>
	 * Size of the control.
	 * 
	 * @param size
	 * 	The new value for this attribute.
	 * 	Typically a {@link Number} or {@link String}.
	 * @return This object (for method chaining).
	 */
	public final Select size(Object size) {
		attr("size", size);
		return this;
	}

	/**
	 * Convenience method for selecting a child {@link Option} after the options have already been populated. 
	 * 
	 * @param optionValue The option value.
	 * @return This object (for method chaining).
	 */
	public Select choose(Object optionValue) {
		if (optionValue != null) {
			for (Object o : getChildren()) {
				if (o instanceof Option) {
					Option o2 = (Option)o;
					if (StringUtils.isEquals(optionValue.toString(), o2.getAttr(String.class, "value"))) 
						o2.selected(true);
				}
			}
		}
		return this;
	}

	//--------------------------------------------------------------------------------
	// Overridden methods
	//--------------------------------------------------------------------------------

	@Override /* HtmlElement */
	public final Select _class(String _class) {
		super._class(_class);
		return this;
	}

	@Override /* HtmlElement */
	public final Select id(String id) {
		super.id(id);
		return this;
	}

	@Override /* HtmlElement */
	public final Select style(String style) {
		super.style(style);
		return this;
	}

	@Override /* HtmlElementContainer */
	public final Select children(Object...children) {
		super.children(children);
		return this;
	}

	@Override /* HtmlElementContainer */
	public final Select child(Object child) {
		super.child(child);
		return this;
	}
}
