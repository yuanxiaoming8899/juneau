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
package org.apache.juneau.jena;

import static org.apache.juneau.jena.Constants.*;
import static org.apache.juneau.internal.ThrowableUtils.*;
import static org.apache.juneau.internal.IOUtils.*;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Consumer;

import org.apache.jena.rdf.model.*;
import org.apache.juneau.*;
import org.apache.juneau.collections.*;
import org.apache.juneau.http.header.MediaType;
import org.apache.juneau.httppart.HttpPartSchema;
import org.apache.juneau.internal.*;
import org.apache.juneau.jena.annotation.*;
import org.apache.juneau.serializer.*;
import org.apache.juneau.svl.VarResolverSession;
import org.apache.juneau.swap.*;
import org.apache.juneau.xml.*;
import org.apache.juneau.xml.annotation.*;

/**
 * Session object that lives for the duration of a single use of {@link RdfSerializer}.
 *
 * <p>
 * This class is NOT thread safe.
 * It is typically discarded after one-time use although it can be reused within the same thread.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class RdfSerializerSession extends WriterSerializerSession {

	/**
	 * Maps RDF writer names to property prefixes that apply to them.
	 */
	static final Map<String,String> LANG_PROP_MAP = AMap.of("RDF/XML","rdfXml.","RDF/XML-ABBREV","rdfXml.","N3","n3.","N3-PP","n3.","N3-PLAIN","n3.","N3-TRIPLES","n3.","TURTLE","n3.","N-TRIPLE","ntriple.");

	//-----------------------------------------------------------------------------------------------------------------
	// Static
	//-----------------------------------------------------------------------------------------------------------------

	/**
	 * Creates a new builder for this object.
	 *
	 * @param ctx The context creating this session.
	 * @return A new builder.
	 */
	public static Builder create(RdfSerializer ctx) {
		return new Builder(ctx);
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Builder
	//-----------------------------------------------------------------------------------------------------------------

	/**
	 * Builder class.
	 */
	@FluentSetters
	public static class Builder extends WriterSerializerSession.Builder {

		RdfSerializer ctx;

		/**
		 * Constructor
		 *
		 * @param ctx The context creating this session.
		 */
		protected Builder(RdfSerializer ctx) {
			super(ctx);
			this.ctx = ctx;
		}

		@Override
		public RdfSerializerSession build() {
			return new RdfSerializerSession(this);
		}

		// <FluentSetters>

		@Override /* GENERATED - org.apache.juneau.ContextSession.Builder */
		public <T> Builder apply(Class<T> type, Consumer<T> apply) {
			super.apply(type, apply);
			return this;
		}

		@Override /* GENERATED - org.apache.juneau.ContextSession.Builder */
		public Builder debug(Boolean value) {
			super.debug(value);
			return this;
		}

		@Override /* GENERATED - org.apache.juneau.ContextSession.Builder */
		public Builder properties(Map<String,Object> value) {
			super.properties(value);
			return this;
		}

		@Override /* GENERATED - org.apache.juneau.ContextSession.Builder */
		public Builder property(String key, Object value) {
			super.property(key, value);
			return this;
		}

		@Override /* GENERATED - org.apache.juneau.ContextSession.Builder */
		public Builder unmodifiable() {
			super.unmodifiable();
			return this;
		}

		@Override /* GENERATED - org.apache.juneau.BeanSession.Builder */
		public Builder locale(Locale value) {
			super.locale(value);
			return this;
		}

		@Override /* GENERATED - org.apache.juneau.BeanSession.Builder */
		public Builder localeDefault(Locale value) {
			super.localeDefault(value);
			return this;
		}

		@Override /* GENERATED - org.apache.juneau.BeanSession.Builder */
		public Builder mediaType(MediaType value) {
			super.mediaType(value);
			return this;
		}

		@Override /* GENERATED - org.apache.juneau.BeanSession.Builder */
		public Builder mediaTypeDefault(MediaType value) {
			super.mediaTypeDefault(value);
			return this;
		}

		@Override /* GENERATED - org.apache.juneau.BeanSession.Builder */
		public Builder timeZone(TimeZone value) {
			super.timeZone(value);
			return this;
		}

		@Override /* GENERATED - org.apache.juneau.BeanSession.Builder */
		public Builder timeZoneDefault(TimeZone value) {
			super.timeZoneDefault(value);
			return this;
		}

		@Override /* GENERATED - org.apache.juneau.serializer.SerializerSession.Builder */
		public Builder javaMethod(Method value) {
			super.javaMethod(value);
			return this;
		}

		@Override /* GENERATED - org.apache.juneau.serializer.SerializerSession.Builder */
		public Builder resolver(VarResolverSession value) {
			super.resolver(value);
			return this;
		}

		@Override /* GENERATED - org.apache.juneau.serializer.SerializerSession.Builder */
		public Builder schema(HttpPartSchema value) {
			super.schema(value);
			return this;
		}

		@Override /* GENERATED - org.apache.juneau.serializer.SerializerSession.Builder */
		public Builder schemaDefault(HttpPartSchema value) {
			super.schemaDefault(value);
			return this;
		}

		@Override /* GENERATED - org.apache.juneau.serializer.SerializerSession.Builder */
		public Builder uriContext(UriContext value) {
			super.uriContext(value);
			return this;
		}

		@Override /* GENERATED - org.apache.juneau.serializer.WriterSerializerSession.Builder */
		public Builder fileCharset(Charset value) {
			super.fileCharset(value);
			return this;
		}

		@Override /* GENERATED - org.apache.juneau.serializer.WriterSerializerSession.Builder */
		public Builder streamCharset(Charset value) {
			super.streamCharset(value);
			return this;
		}

		@Override /* GENERATED - org.apache.juneau.serializer.WriterSerializerSession.Builder */
		public Builder useWhitespace(Boolean value) {
			super.useWhitespace(value);
			return this;
		}

		// </FluentSetters>
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Instance
	//-----------------------------------------------------------------------------------------------------------------

	private final RdfSerializer ctx;
	private final Property pRoot, pValue;
	private final Model model;
	private final RDFWriter writer;
	private final Namespace[] namespaces;

	/**
	 * Constructor.
	 *
	 * @param builder The builder for this object.
	 */
	protected RdfSerializerSession(Builder builder) {
		super(builder);
		ctx = builder.ctx;

		namespaces = ctx.namespaces;
		model = ModelFactory.createDefaultModel();
		addModelPrefix(ctx.getJuneauNs());
		addModelPrefix(ctx.getJuneauBpNs());
		for (Namespace ns : this.namespaces)
			addModelPrefix(ns);
		pRoot = model.createProperty(ctx.getJuneauNs().getUri(), RDF_juneauNs_ROOT);
		pValue = model.createProperty(ctx.getJuneauNs().getUri(), RDF_juneauNs_VALUE);
		writer = model.getWriter(ctx.getLanguage());

		// Only apply properties with this prefix!
		String propPrefix = LANG_PROP_MAP.get(ctx.getLanguage());
		if (propPrefix == null)
			throw runtimeException("Unknown RDF language encountered: ''{0}''", ctx.getLanguage());

		// RDF/XML specific properties.
		if (propPrefix.equals("rdfXml.")) {
			writer.setProperty("tab", isUseWhitespace() ? 2 : 0);
			writer.setProperty("attributeQuoteChar", Character.toString(getQuoteChar()));
		}

		for (Map.Entry<String,Object> e : ctx.getJenaSettings().entrySet())
			if (e.getKey().startsWith(propPrefix, 5))
				writer.setProperty(e.getKey().substring(5 + propPrefix.length()), e.getValue());
	}

	/*
	 * Adds the specified namespace as a model prefix.
	 */
	private void addModelPrefix(Namespace ns) {
		model.setNsPrefix(ns.getName(), ns.getUri());
	}

	/*
	 * XML-encodes the specified string using the {@link XmlUtils#escapeText(Object)} method.
	 */
	private String encodeTextInvalidChars(Object o) {
		if (o == null)
			return null;
		String s = toString(o);
		return XmlUtils.escapeText(s);
	}

	/*
	 * XML-encoded the specified element name using the {@link XmlUtils#encodeElementName(Object)} method.
	 */
	private String encodeElementName(Object o) {
		return XmlUtils.encodeElementName(toString(o));
	}

	@Override /* Serializer */
	protected void doSerialize(SerializerPipe out, Object o) throws IOException, SerializeException {

		Resource r = null;

		ClassMeta<?> cm = getClassMetaForObject(o);
		if (isLooseCollections() && cm != null && cm.isCollectionOrArray()) {
			Collection c = sort(cm.isCollection() ? (Collection)o : toList(cm.getInnerClass(), o));
			for (Object o2 : c)
				serializeAnything(o2, false, object(), "root", null, null);
		} else {
			RDFNode n = serializeAnything(o, false, getExpectedRootType(o), "root", null, null);
			if (n.isLiteral()) {
				r = model.createResource();
				r.addProperty(pValue, n);
			} else {
				r = n.asResource();
			}

			if (isAddRootProp())
				r.addProperty(pRoot, "true");
		}

		writer.write(model, out.getWriter(), "http://unknown/");
	}

	private RDFNode serializeAnything(Object o, boolean isURI, ClassMeta<?> eType,
			String attrName, BeanPropertyMeta bpm, Resource parentResource) throws IOException, SerializeException {
		Model m = model;

		ClassMeta<?> aType = null;       // The actual type
		ClassMeta<?> wType = null;       // The wrapped type
		ClassMeta<?> sType = object();   // The serialized type

		aType = push2(attrName, o, eType);

		if (eType == null)
			eType = object();

		// Handle recursion
		if (aType == null) {
			o = null;
			aType = object();
		}

		// Handle Optional<X>
		if (isOptional(aType)) {
			o = getOptionalValue(o);
			eType = getOptionalType(eType);
			aType = getClassMetaForObject(o, object());
		}

		if (o != null) {

			if (aType.isDelegate()) {
				wType = aType;
				aType = ((Delegate)o).getClassMeta();
			}

			sType = aType;

			// Swap if necessary
			ObjectSwap swap = aType.getSwap(this);
			if (swap != null) {
				o = swap(swap, o);
				sType = swap.getSwapClassMeta(this);

				// If the getSwapClass() method returns Object, we need to figure out
				// the actual type now.
				if (sType.isObject())
					sType = getClassMetaForObject(o);
			}
		} else {
			sType = eType.getSerializedClassMeta(this);
		}

		String typeName = getBeanTypeName(this, eType, aType, bpm);

		RDFNode n = null;

		if (o == null || sType.isChar() && ((Character)o).charValue() == 0) {
			if (bpm != null) {
				if (isKeepNullProperties()) {
					n = m.createResource(RDF_NIL);
				}
			} else {
				n = m.createResource(RDF_NIL);
			}

		} else if (sType.isUri() || isURI) {
			// Note that RDF URIs must be absolute to be valid!
			String uri = getUri(o, null);
			if (StringUtils.isAbsoluteUri(uri))
				n = m.createResource(uri);
			else
				n = m.createLiteral(encodeTextInvalidChars(uri));

		} else if (sType.isCharSequence() || sType.isChar()) {
			n = m.createLiteral(encodeTextInvalidChars(o));

		} else if (sType.isNumber() || sType.isBoolean()) {
			if (! isAddLiteralTypes())
				n = m.createLiteral(o.toString());
			else
				n = m.createTypedLiteral(o);

		} else if (sType.isMap() || (wType != null && wType.isMap())) {
			if (o instanceof BeanMap) {
				BeanMap bm = (BeanMap)o;
				Object uri = null;
				RdfBeanMeta rbm = getRdfBeanMeta(bm.getMeta());
				if (rbm.hasBeanUri())
					uri = rbm.getBeanUriProperty().get(bm, null);
				String uri2 = getUri(uri, null);
				n = m.createResource(uri2);
				serializeBeanMap(bm, (Resource)n, typeName);
			} else {
				Map m2 = (Map)o;
				n = m.createResource();
				serializeMap(m2, (Resource)n, sType);
			}

		} else if (sType.isBean()) {
			BeanMap bm = toBeanMap(o);
			Object uri = null;
			RdfBeanMeta rbm = getRdfBeanMeta(bm.getMeta());
			if (rbm.hasBeanUri())
				uri = rbm.getBeanUriProperty().get(bm, null);
			String uri2 = getUri(uri, null);
			n = m.createResource(uri2);
			serializeBeanMap(bm, (Resource)n, typeName);

		} else if (sType.isCollectionOrArray() || (wType != null && wType.isCollection())) {

			Collection c = sort(sType.isCollection() ? (Collection)o : toList(sType.getInnerClass(), o));
			RdfCollectionFormat f = getCollectionFormat();
			RdfClassMeta cRdf = getRdfClassMeta(sType);
			RdfBeanPropertyMeta bpRdf = getRdfBeanPropertyMeta(bpm);

			if (cRdf.getCollectionFormat() != RdfCollectionFormat.DEFAULT)
				f = cRdf.getCollectionFormat();
			if (bpRdf.getCollectionFormat() != RdfCollectionFormat.DEFAULT)
				f = bpRdf.getCollectionFormat();

			switch (f) {
				case BAG: n = serializeToContainer(c, eType, m.createBag()); break;
				case LIST: n = serializeToList(c, eType); break;
				case MULTI_VALUED: serializeToMultiProperties(c, eType, bpm, attrName, parentResource); break;
				default: n = serializeToContainer(c, eType, m.createSeq());
			}

		} else if (sType.isReader()) {
			n = m.createLiteral(encodeTextInvalidChars(read((Reader)o)));
		} else if (sType.isInputStream()) {
			n = m.createLiteral(encodeTextInvalidChars(read((InputStream)o)));

		} else {
			n = m.createLiteral(encodeTextInvalidChars(toString(o)));
		}

		pop();

		return n;
	}

	private String getUri(Object uri, Object uri2) {
		String s = null;
		if (uri != null)
			s = uri.toString();
		if ((s == null || s.isEmpty()) && uri2 != null)
			s = uri2.toString();
		if (s == null)
			return null;
		return getUriResolver().resolve(s);
	}

	private void serializeMap(Map m, Resource r, ClassMeta<?> type) throws IOException, SerializeException {

		m = sort(m);

		ClassMeta<?> keyType = type.getKeyType(), valueType = type.getValueType();

		ArrayList<Map.Entry<Object,Object>> l = new ArrayList<>(m.entrySet());
		Collections.reverse(l);
		for (Map.Entry<Object,Object> me : l) {
			Object value = me.getValue();

			Object key = generalize(me.getKey(), keyType);

			Namespace ns = getJuneauBpNs();
			Property p = model.createProperty(ns.getUri(), encodeElementName(toString(key)));
			RDFNode n = serializeAnything(value, false, valueType, toString(key), null, r);
			if (n != null)
				r.addProperty(p, n);
		}
	}

	private void serializeBeanMap(BeanMap<?> m, Resource r, String typeName) throws IOException, SerializeException {
		List<BeanPropertyValue> l = m.getValues(isKeepNullProperties(), typeName != null ? createBeanTypeNameProperty(m, typeName) : null);
		Collections.reverse(l);
		for (BeanPropertyValue bpv : l) {

			BeanPropertyMeta bpMeta = bpv.getMeta();
			ClassMeta<?> cMeta = bpMeta.getClassMeta();
			RdfBeanPropertyMeta bpRdf = getRdfBeanPropertyMeta(bpMeta);
			XmlBeanPropertyMeta bpXml = getXmlBeanPropertyMeta(bpMeta);

			if (bpRdf.isBeanUri())
				continue;

			String key = bpv.getName();
			Object value = bpv.getValue();
			Throwable t = bpv.getThrown();
			if (t != null)
				onBeanGetterException(bpMeta, t);

			if (canIgnoreValue(cMeta, key, value))
				continue;

			Namespace ns = bpRdf.getNamespace();
			if (ns == null && isUseXmlNamespaces())
				ns = bpXml.getNamespace();
			if (ns == null)
				ns = getJuneauBpNs();
			else if (isAutoDetectNamespaces())
				addModelPrefix(ns);

			Property p = model.createProperty(ns.getUri(), encodeElementName(key));
			RDFNode n = serializeAnything(value, bpMeta.isUri(), cMeta, key, bpMeta, r);
			if (n != null)
				r.addProperty(p, n);
		}
	}


	private Container serializeToContainer(Collection c, ClassMeta<?> type, Container list) throws IOException, SerializeException {

		ClassMeta<?> elementType = type.getElementType();
		for (Object e : c) {
			RDFNode n = serializeAnything(e, false, elementType, null, null, null);
			list = list.add(n);
		}
		return list;
	}

	private RDFList serializeToList(Collection c, ClassMeta<?> type) throws IOException, SerializeException {
		ClassMeta<?> elementType = type.getElementType();
		List<RDFNode> l = new ArrayList<>(c.size());
		for (Object e : c) {
			l.add(serializeAnything(e, false, elementType, null, null, null));
		}
		return model.createList(l.iterator());
	}

	private void serializeToMultiProperties(Collection c, ClassMeta<?> sType,
			BeanPropertyMeta bpm, String attrName, Resource parentResource) throws IOException, SerializeException {

		ClassMeta<?> elementType = sType.getElementType();
		RdfBeanPropertyMeta bpRdf = getRdfBeanPropertyMeta(bpm);
		XmlBeanPropertyMeta bpXml = getXmlBeanPropertyMeta(bpm);

		for (Object e : c) {
			Namespace ns = bpRdf.getNamespace();
			if (ns == null && isUseXmlNamespaces())
				ns = bpXml.getNamespace();
			if (ns == null)
				ns = getJuneauBpNs();
			else if (isAutoDetectNamespaces())
				addModelPrefix(ns);
			RDFNode n2 = serializeAnything(e, false, elementType, null, null, null);
			Property p = model.createProperty(ns.getUri(), encodeElementName(attrName));
			parentResource.addProperty(p, n2);
		}
	}


	//-----------------------------------------------------------------------------------------------------------------
	// Common properties
	//-----------------------------------------------------------------------------------------------------------------

	/**
	 * Configuration property:  RDF format for representing collections and arrays.
	 *
	 * @see RdfSerializer.Builder#collectionFormat(RdfCollectionFormat)
	 * @return
	 * 	RDF format for representing collections and arrays.
	 */
	protected final RdfCollectionFormat getCollectionFormat() {
		return ctx.getCollectionFormat();
	}

	/**
	 * Configuration property:  Default XML namespace for bean properties.
	 *
	 * @see RdfSerializer.Builder#juneauBpNs(Namespace)
	 * @return
	 * 	The XML namespace to use for bean properties.
	 */
	protected final Namespace getJuneauBpNs() {
		return ctx.getJuneauBpNs();
	}

	/**
	 * Configuration property:  XML namespace for Juneau properties.
	 *
	 * @see RdfSerializer.Builder#juneauNs(Namespace)
	 * @return
	 * 	The XML namespace to use for Juneau properties.
	 */
	protected final Namespace getJuneauNs() {
		return ctx.getJuneauNs();
	}

	/**
	 * Configuration property:  RDF language.
	 *
	 * @see RdfSerializer.Builder#language(String)
	 * @return
	 * 	The RDF language to use.
	 */
	protected final String getLanguage() {
		return ctx.getLanguage();
	}

	/**
	 * Configuration property:  Collections should be serialized and parsed as loose collections.
	 *
	 * @see RdfSerializer.Builder#looseCollections()
	 * @return
	 * 	<jk>true</jk> if collections of resources are handled as loose collections of resources in RDF instead of
	 * 	resources that are children of an RDF collection (e.g. Sequence, Bag).
	 */
	protected final boolean isLooseCollections() {
		return ctx.isLooseCollections();
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Jena properties
	//-----------------------------------------------------------------------------------------------------------------

	/**
	 * Configuration property:  All Jena-related configuration properties.
	 *
	 * @return
	 * 	A map of all Jena-related configuration properties.
	 */
	protected final Map<String,Object> getJenaSettings() {
		return ctx.getJenaSettings();
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Properties
	//-----------------------------------------------------------------------------------------------------------------

	/**
	 * Configuration property:  Add <js>"_type"</js> properties when needed.
	 *
	 * @see RdfSerializer.Builder#addBeanTypes()
	 * @return
	 * 	<jk>true</jk> if <js>"_type"</js> properties will be added to beans if their type cannot be inferred
	 * 	through reflection.
	 */
	@Override
	protected final boolean isAddBeanTypes() {
		return ctx.isAddBeanTypes();
	}

	/**
	 * Configuration property:  Add XSI data types to non-<c>String</c> literals.
	 *
	 * @see RdfSerializer.Builder#addLiteralTypes()
	 * @return
	 * 	<jk>true</jk> if XSI data types should be added to string literals.
	 */
	protected final boolean isAddLiteralTypes() {
		return ctx.isAddLiteralTypes();
	}

	/**
	 * Configuration property:  Add RDF root identifier property to root node.
	 *
	 * @see RdfSerializer.Builder#addRootProperty()
	 * @return
	 * 	<jk>true</jk> if RDF property <c>http://www.apache.org/juneau/root</c> is added with a value of <js>"true"</js>
	 * 	to identify the root node in the graph.
	 */
	protected final boolean isAddRootProp() {
		return ctx.isAddRootProp();
	}

	/**
	 * Configuration property:  Auto-detect namespace usage.
	 *
	 * @see RdfSerializer.Builder#disableAutoDetectNamespaces()
	 * @return
	 * 	<jk>true</jk> if namespaces usage should be detected before serialization.
	 */
	protected final boolean isAutoDetectNamespaces() {
		return ctx.isAutoDetectNamespaces();
	}

	/**
	 * Configuration property:  Default namespaces.
	 *
	 * @see RdfSerializer.Builder#namespaces(Namespace...)
	 * @return
	 * 	The default list of namespaces associated with this serializer.
	 */
	protected final Namespace[] getNamespaces() {
		return ctx.getNamespaces();
	}

	/**
	 * Configuration property:  Reuse XML namespaces when RDF namespaces not specified.
	 *
	 * @see RdfSerializer.Builder#disableUseXmlNamespaces()
	 * @return
	 * 	<jk>true</jk> if namespaces defined using {@link XmlNs @XmlNs} and {@link org.apache.juneau.xml.annotation.Xml @Xml} will be inherited by the RDF serializers.
	 * 	<br>Otherwise, namespaces will be defined using {@link RdfNs @RdfNs} and {@link Rdf @Rdf}.
	 */
	protected final boolean isUseXmlNamespaces() {
		return ctx.isUseXmlNamespaces();
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Extended metadata
	//-----------------------------------------------------------------------------------------------------------------

	/**
	 * Returns the language-specific metadata on the specified class.
	 *
	 * @param cm The class to return the metadata on.
	 * @return The metadata.
	 */
	protected RdfClassMeta getRdfClassMeta(ClassMeta<?> cm) {
		return ctx.getRdfClassMeta(cm);
	}

	/**
	 * Returns the language-specific metadata on the specified bean.
	 *
	 * @param bm The bean to return the metadata on.
	 * @return The metadata.
	 */
	protected RdfBeanMeta getRdfBeanMeta(BeanMeta<?> bm) {
		return ctx.getRdfBeanMeta(bm);
	}

	/**
	 * Returns the language-specific metadata on the specified bean property.
	 *
	 * @param bpm The bean property to return the metadata on.
	 * @return The metadata.
	 */
	protected RdfBeanPropertyMeta getRdfBeanPropertyMeta(BeanPropertyMeta bpm) {
		return bpm == null ? RdfBeanPropertyMeta.DEFAULT : ctx.getRdfBeanPropertyMeta(bpm);
	}

	/**
	 * Returns the language-specific metadata on the specified bean property.
	 *
	 * @param bpm The bean property to return the metadata on.
	 * @return The metadata.
	 */
	protected XmlBeanPropertyMeta getXmlBeanPropertyMeta(BeanPropertyMeta bpm) {
		return bpm == null ? XmlBeanPropertyMeta.DEFAULT : ctx.getXmlBeanPropertyMeta(bpm);
	}
}
