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

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;

import org.apache.juneau.*;
import org.apache.juneau.http.header.*;
import org.apache.juneau.internal.*;
import org.apache.juneau.jena.annotation.Rdf;
import org.apache.juneau.parser.*;
import org.apache.juneau.xml.*;

/**
 * Builder class for building instances of RDF parsers.
 * {@review}
 */
@FluentSetters
public class RdfParserBuilder extends ReaderParserBuilder {

	private static final Namespace
		DEFAULT_JUNEAU_NS = Namespace.of("j", "http://www.apache.org/juneau/"),
		DEFAULT_JUNEAUBP_NS = Namespace.of("jp", "http://www.apache.org/juneaubp/");

	boolean trimWhitespace, looseCollections;
	String language;
	Namespace juneauNs, juneauBpNs;
	RdfCollectionFormat collectionFormat;
	Map<String,Object> jenaSettings = new TreeMap<String,Object>();

	/**
	 * Constructor, default settings.
	 */
	protected RdfParserBuilder() {
		super();
		type(RdfParser.class);
		trimWhitespace = env("Rdf.trimWhitespace", false);
		looseCollections = env("Rdf.looseCollections", false);
		language = env("Rdf.language", "RDF/XML-ABBREV");
		collectionFormat = env("Rdf.collectionFormat", RdfCollectionFormat.DEFAULT);
		juneauNs = DEFAULT_JUNEAU_NS;
		juneauBpNs = DEFAULT_JUNEAUBP_NS;
		jenaSettings = new TreeMap<>();
	}

	/**
	 * Copy constructor.
	 *
	 * @param copyFrom The bean to copy from.
	 */
	protected RdfParserBuilder(RdfParser copyFrom) {
		super(copyFrom);
		trimWhitespace = copyFrom.trimWhitespace;
		looseCollections = copyFrom.looseCollections;
		language = copyFrom.language;
		collectionFormat = copyFrom.collectionFormat;
		juneauNs = copyFrom.juneauNs;
		juneauBpNs = copyFrom.juneauBpNs;
		jenaSettings = new TreeMap<>(copyFrom.jenaSettings);
	}

	/**
	 * Copy constructor.
	 *
	 * @param copyFrom The builder to copy from.
	 */
	protected RdfParserBuilder(RdfParserBuilder copyFrom) {
		super(copyFrom);
		trimWhitespace = copyFrom.trimWhitespace;
		looseCollections = copyFrom.looseCollections;
		language = copyFrom.language;
		collectionFormat = copyFrom.collectionFormat;
		juneauNs = copyFrom.juneauNs;
		juneauBpNs = copyFrom.juneauBpNs;
		jenaSettings = new TreeMap<>(copyFrom.jenaSettings);
	}

	@Override /* ContextBuilder */
	public RdfParserBuilder copy() {
		return new RdfParserBuilder(this);
	}

	@Override /* ContextBuilder */
	public RdfParser build() {
		return (RdfParser)super.build();
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Properties
	//-----------------------------------------------------------------------------------------------------------------

	@Override
	public RdfParserBuilder consumes(String value) {
		super.consumes(value);
		return this;
	}

	RdfParserBuilder jena(String key, Object value) {
		jenaSettings.put(key, value);
		return this;
	}

	/**
	 * RDF/XML property: <c>iri_rules</c>.
	 *
	 * <p>
	 * Set the engine for checking and resolving.
	 *
	 * <p>
	 * Possible values:
	 * <ul class='spaced-list'>
	 * 	<li>
	 * 		<js>"lax"</js> - The rules for RDF URI references only, which does permit spaces although the use of spaces
	 * 		is not good practice.
	 * 	<li>
	 * 		<js>"strict"</js> - Sets the IRI engine with rules for valid IRIs, XLink and RDF; it does not permit spaces
	 * 		in IRIs.
	 * 	<li>
	 * 		<js>"iri"</js> - Sets the IRI engine to IRI
	 * 		({@doc http://www.ietf.org/rfc/rfc3986.txt RFC 3986},
	 * 		{@doc http://www.ietf.org/rfc/rfc3987.txt RFC 3987}).
	 *
	 * </ul>
	 *
	 * @param value
	 * 	The new value for this property.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder rdfxml_iriRules(String value) {
		return jena("rdfXml.iri-rules", value);
	}

	/**
	 * RDF/XML ARP property: <c>error-mode</c>.
	 *
	 * <p>
	 * This allows a coarse-grained approach to control of error handling.
	 *
	 * <p>
	 * Possible values:
	 * <ul>
	 * 	<li><js>"default"</js>
	 * 	<li><js>"lax"</js>
	 * 	<li><js>"strict"</js>
	 * 	<li><js>"strict-ignore"</js>
	 * 	<li><js>"strict-warning"</js>
	 * 	<li><js>"strict-error"</js>
	 * 	<li><js>"strict-fatal"</js>
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li>
	 * 		{@doc ExtARP/ARPOptions.html#setDefaultErrorMode() ARPOptions.setDefaultErrorMode()}
	 * 	<li>
	 * 		{@doc ExtARP/ARPOptions.html#setLaxErrorMode() ARPOptions.setLaxErrorMode()}
	 * 	<li>
	 * 		{@doc ExtARP/ARPOptions.html#setStrictErrorMode() ARPOptions.setStrictErrorMode()}
	 * 	<li>
	 * 		{@doc ExtARP/ARPOptions.html#setStrictErrorMode(int) ARPOptions.setStrictErrorMode(int)}
	 * </ul>
	 *
	 * @param value
	 * 	The new value for this property.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder rdfxml_errorMode(String value) {
		return jena("rdfXml.error-mode", value);
	}

	/**
	 * RDF/XML ARP property: <c>embedding</c>.
	 *
	 * <p>
	 * Sets ARP to look for RDF embedded within an enclosing XML document.
	 *
	 * <ul class='seealso'>
	 * 	<li>
	 * 		{@doc ExtARP/ARPOptions.html#setEmbedding(boolean) ARPOptions.setEmbedding(boolean)}
	 * </ul>
	 *
	 * @param value
	 * 	The new value for this property.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder rdfxml_embedding() {
		return rdfxml_embedding(true);
	}

	@FluentSetter
	public RdfParserBuilder rdfxml_embedding(boolean value) {
		return jena("rdfXml.embedding", value);
	}

	/**
	 * RDF/XML property: <c>xmlbase</c>.
	 *
	 * <p>
	 * The value to be included for an <xa>xml:base</xa> attribute on the root element in the file.
	 *
	 * @param value
	 * 	The new value for this property.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder rdfxml_xmlbase(String value) {
		return jena("rdfXml.xmlbase", value);
	}

	/**
	 * RDF/XML property: <c>longId</c>.
	 *
	 * <p>
	 * Whether to use long ID's for anon resources.
	 * Short ID's are easier to read, but can run out of memory on very large models.
	 *
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder rdfxml_longId() {
		return rdfxml_longId(true);
	}

	@FluentSetter
	public RdfParserBuilder rdfxml_longId(boolean value) {
		return jena("rdfXml.longId", value);
	}

	/**
	 * RDF/XML property: <c>allowBadURIs</c>.
	 *
	 * <p>
	 * URIs in the graph are, by default, checked prior to serialization.
	 *
	 * @param value
	 * 	The new value for this property.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder rdfxml_allowBadUris() {
		return rdfxml_allowBadUris(true);
	}

	@FluentSetter
	public RdfParserBuilder rdfxml_allowBadUris(boolean value) {
		return jena("rdfXml.allowBadURIs", value);
	}

	/**
	 * RDF/XML property: <c>relativeURIs</c>.
	 *
	 * <p>
	 * What sort of relative URIs should be used.
	 *
	 * <p>
	 * A comma separate list of options:
	 * <ul class='spaced-list'>
	 * 	<li>
	 * 		<js>"same-document"</js> - Same-document references (e.g. <js>""</js> or <js>"#foo"</js>)
	 * 	<li>
	 * 		<js>"network"</js>  - Network paths (e.g. <js>"//example.org/foo"</js> omitting the URI scheme)
	 * 	<li>
	 * 		<js>"absolute"</js> - Absolute paths (e.g. <js>"/foo"</js> omitting the scheme and authority)
	 * 	<li>
	 * 		<js>"relative"</js> - Relative path not beginning in <js>"../"</js>
	 * 	<li>
	 * 		<js>"parent"</js> - Relative path beginning in <js>"../"</js>
	 * 	<li>
	 * 		<js>"grandparent"</js> - Relative path beginning in <js>"../../"</js>
	 * </ul>
	 *
	 * <p>
	 * The default value is <js>"same-document, absolute, relative, parent"</js>.
	 * To switch off relative URIs use the value <js>""</js>.
	 * Relative URIs of any of these types are output where possible if and only if the option has been specified.
	 *
	 * @param value
	 * 	The new value for this property.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder rdfxml_relativeUris(String value) {
		return jena("rdfXml.relativeURIs", value);
	}

	/**
	 * RDF/XML property: <c>showXmlDeclaration</c>.
	 *
	 * <p>
	 * Possible values:
	 * <ul class='spaced-list'>
	 * 	<li>
	 * 		<js>"true"</js> - Add XML Declaration to the output.
	 * 	<li>
	 * 		<js>"false"</js> - Don't add XML Declaration to the output.
	 * 	<li>
	 * 		<js>"default"</js> - Only add an XML Declaration when asked to write to an <c>OutputStreamWriter</c>
	 * 		that uses some encoding other than <c>UTF-8</c> or <c>UTF-16</c>.
	 * 		In this case the encoding is shown in the XML declaration.
	 * </ul>
	 *
	 * @param value
	 * 	The new value for this property.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder rdfxml_showXmlDeclaration(String value) {
		return jena("rdfXml.showXmlDeclaration", value);
	}

	/**
	 * RDF/XML property: <c>disableShowDoctypeDeclaration</c>.
	 *
	 * <p>
	 * If disabled, an XML doctype declaration isn't included in the output.
	 * This declaration includes a <c>!ENTITY</c> declaration for each prefix mapping in the model, and any
	 * attribute value that starts with the URI of that mapping is written as starting with the corresponding entity
	 * invocation.
	 *
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder rdfxml_disableShowDoctypeDeclaration() {
		return rdfxml_disableShowDoctypeDeclaration(true);
	}

	@FluentSetter
	public RdfParserBuilder rdfxml_disableShowDoctypeDeclaration(boolean value) {
		return jena("rdfXml.disableShowDoctypeDeclaration", value);
	}

	/**
	 * RDF/XML property: <c>tab</c>.
	 *
	 * <p>
	 * The number of spaces with which to indent XML child elements.
	 *
	 * @param value
	 * 	The new value for this property.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder rdfxml_tab(int value) {
		return jena("rdfXml.tab", value);
	}

	/**
	 * RDF/XML property: <c>attributeQuoteChar</c>.
	 *
	 * <p>
	 * The XML attribute quote character.
	 *
	 * @param value
	 * 	The new value for this property.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder rdfxml_attributeQuoteChar(char value) {
		return jena("rdfXml.attributeQuoteChar", value);
	}

	/**
	 * RDF/XML property: <c>blockRules</c>.
	 *
	 * <p>
	 * A list of <c>Resource</c> or a <c>String</c> being a comma separated list of fragment IDs from
	 * {@doc http://www.w3.org/TR/rdf-syntax-grammar RDF Syntax Grammar} indicating grammar
	 * rules that will not be used.
	 *
	 * @param value
	 * 	The new value for this property.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder rdfxml_blockRules(String value) {
		return jena("rdfXml.blockRules", value);
	}

	/**
	 * N3/Turtle property: <c>minGap</c>.
	 *
	 * <p>
	 * Minimum gap between items on a line.
	 *
	 * @param value
	 * 	The new value for this property.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder n3_minGap(int value) {
		return jena("n3.minGap", value);
	}

	/**
	 * N3/Turtle property: <c>disableObjectLists</c>.
	 *
	 * <p>
	 * Don't print object lists as comma separated lists.
	 *
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder n3_disableObjectLists() {
		return n3_disableObjectLists(true);
	}

	@FluentSetter
	public RdfParserBuilder n3_disableObjectLists(boolean value) {
		return jena("n3.disableObjectLists", value);
	}

	/**
	 * N3/Turtle property: <c>subjectColumn</c>.
	 *
	 * <p>
	 * If the subject is shorter than this value, the first property may go on the same line.
	 *
	 * @param value
	 * 	The new value for this property.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder n3_subjectColumn(int value) {
		return jena("n3.subjectColumn", value);
	}

	/**
	 * N3/Turtle property: <c>propertyColumn</c>.
	 *
	 * <p>
	 * Width of the property column.
	 *
	 * @param value
	 * 	The new value for this property.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder n3_propertyColumn(int value) {
		return jena("n3.propertyColumn", value);
	}

	/**
	 * N3/Turtle property: <c>indentProperty</c>.
	 *
	 * <p>
	 * Width to indent properties.
	 *
	 * @param value
	 * 	The new value for this property.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder n3_indentProperty(int value) {
		return jena("n3.indentProperty", value);
	}

	/**
	 * N3/Turtle property: <c>widePropertyLen</c>.
	 *
	 * <p>
	 * Width of the property column.
	 * Must be longer than <c>propertyColumn</c>.
	 *
	 * @param value
	 * 	The new value for this property.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder n3_widePropertyLen(int value) {
		return jena("n3.widePropertyLen", value);
	}

	/**
	 * N3/Turtle property: <c>disableAbbrevBaseURI</c>.
	 *
	 * <p>
	 * Controls whether to use abbreviations <c>&lt;&gt;</c> or <c>&lt;#&gt;</c>.
	 *
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder n3_disableAbbrevBaseUri() {
		return n3_disableAbbrevBaseUri(true);
	}

	@FluentSetter
	public RdfParserBuilder n3_disableAbbrevBaseUri(boolean value) {
		return jena("n3.disableAbbrevBaseUri", value);
	}

	/**
	 * N3/Turtle property: <c>disableUsePropertySymbols</c>.
	 *
	 * <p>
	 * Controls whether to use <c>a</c>, <c>=</c> and <c>=&gt;</c> in output
	 *
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder n3_disableUsePropertySymbols() {
		return n3_disableUsePropertySymbols(true);
	}

	@FluentSetter
	public RdfParserBuilder n3_disableUsePropertySymbols(boolean value) {
		return jena("n3.disableUsePropertySymbols", value);
	}

	/**
	 * N3/Turtle property: <c>disableUseTripleQuotedStrings</c>.
	 *
	 * <p>
	 * Disallow the use of <c>"""</c> to delimit long strings.
	 *
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder n3_disableUseTripleQuotedStrings() {
		return n3_disableUseTripleQuotedStrings(true);
	}

	@FluentSetter
	public RdfParserBuilder n3_disableUseTripleQuotedStrings(boolean value) {
		return jena("n3.disableUseTripleQuotedStrings", value);
	}

	/**
	 * N3/Turtle property: <c>disableUseDoubles</c>.
	 *
	 * <p>
	 * Disallow the use of doubles as <c>123.456</c>.
	 *
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder n3_disableUseDoubles() {
		return n3_disableUseDoubles(true);
	}

	@FluentSetter
	public RdfParserBuilder n3_disableUseDoubles(boolean value) {
		return jena("n3.disableUseDoubles", value);
	}

	/**
	 * RDF format for representing collections and arrays.
	 *
	 * <p>
	 * Possible values:
	 * <ul class='spaced-list'>
	 * 	<li>
	 * 		<js>"DEFAULT"</js> - Default format.  The default is an RDF Sequence container.
	 * 	<li>
	 * 		<js>"SEQ"</js> - RDF Sequence container.
	 * 	<li>
	 * 		<js>"BAG"</js> - RDF Bag container.
	 * 	<li>
	 * 		<js>"LIST"</js> - RDF List container.
	 * 	<li>
	 * 		<js>"MULTI_VALUED"</js> - Multi-valued properties.
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link RdfParser#RDF_collectionFormat}
	 * </ul>
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder collectionFormat(RdfCollectionFormat value) {
		collectionFormat = value;
		return this;
	}

	/**
	 * Default XML namespace for bean properties.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link RdfParser#RDF_juneauBpNs}
	 * </ul>
	 *
	 * @param value
	 * 	The new value for this property.
	 * 	<br>The default is <code>{j:<js>'http://www.apache.org/juneaubp/'</js>}</code>.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder juneauBpNs(Namespace value) {
		juneauBpNs = value;
		return this;
	}

	/**
	 * XML namespace for Juneau properties.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link RdfParser#RDF_juneauNs}
	 * </ul>
	 *
	 * @param value
	 * 	The new value for this property.
	 * 	<br>The default is <code>{j:<js>'http://www.apache.org/juneau/'</js>}</code>.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder juneauNs(Namespace value) {
		juneauNs = value;
		return this;
	}

	/**
	 * RDF language.
	 *
	 * <p>
	 * Can be any of the following:
	 * <ul class='spaced-list'>
	 * 	<li>
	 * 		<js>"RDF/XML"</js>
	 * 	<li>
	 * 		<js>"RDF/XML-ABBREV"</js> (default)
	 * 	<li>
	 * 		<js>"N-TRIPLE"</js>
	 * 	<li>
	 * 		<js>"N3"</js> - General name for the N3 writer.
	 * 		Will make a decision on exactly which writer to use (pretty writer, plain writer or simple writer) when
	 * 		created.
	 * 		Default is the pretty writer but can be overridden with system property
	 * 		<c>org.apache.jena.n3.N3JenaWriter.writer</c>.
	 * 	<li>
	 * 		<js>"N3-PP"</js> - Name of the N3 pretty writer.
	 * 		The pretty writer uses a frame-like layout, with prefixing, clustering like properties and embedding
	 * 		one-referenced bNodes.
	 * 	<li>
	 * 		<js>"N3-PLAIN"</js> - Name of the N3 plain writer.
	 * 		The plain writer writes records by subject.
	 * 	<li>
	 * 		<js>"N3-TRIPLES"</js> - Name of the N3 triples writer.
	 * 		This writer writes one line per statement, like N-Triples, but does N3-style prefixing.
	 * 	<li>
	 * 		<js>"TURTLE"</js> -  Turtle writer.
	 * 		http://www.dajobe.org/2004/01/turtle/
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link RdfParser#RDF_language}
	 * 	<li class='jm'>{@link org.apache.juneau.jena.RdfParserBuilder#n3()}
	 * 	<li class='jm'>{@link org.apache.juneau.jena.RdfParserBuilder#ntriple()}
	 * 	<li class='jm'>{@link org.apache.juneau.jena.RdfParserBuilder#turtle()}
	 * 	<li class='jm'>{@link org.apache.juneau.jena.RdfParserBuilder#xml()}
	 * </ul>
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder language(String value) {
		language = value;
		return this;
	}

	/**
	 * Collections should be serialized and parsed as loose collections.
	 *
	 * <p>
	 * When specified, collections of resources are handled as loose collections of resources in RDF instead of
	 * resources that are children of an RDF collection (e.g. Sequence, Bag).
	 *
	 * <p>
	 * Note that this setting is specialized for RDF syntax, and is incompatible with the concept of
	 * losslessly representing POJO models, since the tree structure of these POJO models are lost
	 * when serialized as loose collections.
	 *
	 * <p>
	 * This setting is typically only useful if the beans being parsed into do not have a bean property
	 * annotated with {@link Rdf#beanUri @Rdf(beanUri=true)}.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	WriterSerializer <jv>serializer</jv> = RdfSerializer
	 * 		.<jsm>create</jsm>()
	 * 		.xmlabbrev()
	 * 		.looseCollections()
	 * 		.build();
	 *
	 * 	ReaderParser <jv>parser</jv> = RdfParser
	 * 		.<jsm>create</jsm>()
	 * 		.xml()
	 * 		.looseCollections()
	 * 		.build();
	 *
	 * 	List&lt;MyBean&gt; <jv>myList</jv> = createListOfMyBeans();
	 *
	 * 	<jc>// Serialize to RDF/XML as loose resources</jc>
	 * 	String <jv>rdfXml</jv> = <jv>serializer</jv>.serialize(<jv>myList</jv>);
	 *
	 * 	<jc>// Parse back into a Java collection</jc>
	 * 	<jv>myList</jv> = <jv>parser</jv>.parse(<jv>rdfXml</jv>, LinkedList.<jk>class</jk>, MyBean.<jk>class</jk>);
	 *
	 * 	MyBean[] <jv>myBeans</jv> = createArrayOfMyBeans();
	 *
	 * 	<jc>// Serialize to RDF/XML as loose resources</jc>
	 * 	<jv>rdfXml</jv> = <jv>serializer</jv>.serialize(<jv>myBeans</jv>);
	 *
	 * 	<jc>// Parse back into a bean array</jc>
	 * 	<jv>myBeans</jv> = <jv>parser</jv>.parse(<jv>rdfXml</jv>, MyBean[].<jk>class</jk>);
	 * </p>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link RdfParser#RDF_looseCollections}
	 * </ul>
	 *
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder looseCollections() {
		return looseCollections(true);
	}

	@FluentSetter
	public RdfParserBuilder looseCollections(boolean value) {
		looseCollections = value;
		return this;
	}

	/**
	 * RDF language.
	 *
	 * <p>
	 * Shortcut for calling <code>language(<jsf>LANG_N3</jsf>)</code>.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link RdfParser#RDF_language}
	 * </ul>
	 *
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder n3() {
		return language(Constants.LANG_N3);
	}

	/**
	 * RDF language.
	 *
	 * <p>
	 * Shortcut for calling <code>language(<jsf>LANG_NTRIPLE</jsf>)</code>.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link RdfParser#RDF_language}
	 * </ul>
	 *
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder ntriple() {
		return language(Constants.LANG_NTRIPLE);
	}

	/**
	 * Trim whitespace from text elements.
	 *
	 * <p>
	 * When enabled, whitespace in text elements will be automatically trimmed.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Create an RDF parser that trims whitespace.</jc>
	 * 	ReaderParser <jv>parser</jv> = RdfParser
	 * 		.<jsm>create</jsm>()
	 * 		.xml()
	 * 		.trimWhitespace()
	 * 		.build();
	 * </p>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link RdfParser#RDF_trimWhitespace}
	 * </ul>
	 *
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder trimWhitespace() {
		return trimWhitespace(true);
	}

	@FluentSetter
	public RdfParserBuilder trimWhitespace(boolean value) {
		trimWhitespace = value;
		return this;
	}

	/**
	 * Shortcut for calling <code>language(<jsf>LANG_TURTLE</jsf>)</code>.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link RdfParser#RDF_language}
	 * </ul>
	 *
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder turtle() {
		return language(Constants.LANG_TURTLE);
	}

	/**
	 * Shortcut for calling <code>language(<jsf>LANG_RDF_XML</jsf>)</code>.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link RdfParser#RDF_language}
	 * </ul>
	 *
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder xml() {
		return language(Constants.LANG_RDF_XML);
	}

	/**
	 * Shortcut for calling <code>language(<jsf>LANG_RDF_XML_ABBREV</jsf>)</code>.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link RdfParser#RDF_language}
	 * </ul>
	 *
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public RdfParserBuilder xmlabbrev() {
		return language(Constants.LANG_RDF_XML_ABBREV);
	}

	// <FluentSetters>

	@Override /* GENERATED - ContextBuilder */
	public RdfParserBuilder applyAnnotations(java.lang.Class<?>...fromClasses) {
		super.applyAnnotations(fromClasses);
		return this;
	}

	@Override /* GENERATED - ContextBuilder */
	public RdfParserBuilder applyAnnotations(Method...fromMethods) {
		super.applyAnnotations(fromMethods);
		return this;
	}

	@Override /* GENERATED - ContextBuilder */
	public RdfParserBuilder apply(AnnotationWorkList work) {
		super.apply(work);
		return this;
	}

	@Override /* GENERATED - ContextBuilder */
	public RdfParserBuilder debug() {
		super.debug();
		return this;
	}

	@Override /* GENERATED - ContextBuilder */
	public RdfParserBuilder locale(Locale value) {
		super.locale(value);
		return this;
	}

	@Override /* GENERATED - ContextBuilder */
	public RdfParserBuilder mediaType(MediaType value) {
		super.mediaType(value);
		return this;
	}

	@Override /* GENERATED - ContextBuilder */
	public RdfParserBuilder timeZone(TimeZone value) {
		super.timeZone(value);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder annotations(Annotation...values) {
		super.annotations(values);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanClassVisibility(Visibility value) {
		super.beanClassVisibility(value);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanConstructorVisibility(Visibility value) {
		super.beanConstructorVisibility(value);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanFieldVisibility(Visibility value) {
		super.beanFieldVisibility(value);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanInterceptor(Class<?> on, Class<? extends org.apache.juneau.transform.BeanInterceptor<?>> value) {
		super.beanInterceptor(on, value);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanMapPutReturnsOldValue() {
		super.beanMapPutReturnsOldValue();
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanMethodVisibility(Visibility value) {
		super.beanMethodVisibility(value);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanProperties(Map<String,Object> values) {
		super.beanProperties(values);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanProperties(Class<?> beanClass, String properties) {
		super.beanProperties(beanClass, properties);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanProperties(String beanClassName, String properties) {
		super.beanProperties(beanClassName, properties);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanPropertiesExcludes(Map<String,Object> values) {
		super.beanPropertiesExcludes(values);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanPropertiesExcludes(Class<?> beanClass, String properties) {
		super.beanPropertiesExcludes(beanClass, properties);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanPropertiesExcludes(String beanClassName, String properties) {
		super.beanPropertiesExcludes(beanClassName, properties);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanPropertiesReadOnly(Map<String,Object> values) {
		super.beanPropertiesReadOnly(values);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanPropertiesReadOnly(Class<?> beanClass, String properties) {
		super.beanPropertiesReadOnly(beanClass, properties);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanPropertiesReadOnly(String beanClassName, String properties) {
		super.beanPropertiesReadOnly(beanClassName, properties);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanPropertiesWriteOnly(Map<String,Object> values) {
		super.beanPropertiesWriteOnly(values);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanPropertiesWriteOnly(Class<?> beanClass, String properties) {
		super.beanPropertiesWriteOnly(beanClass, properties);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanPropertiesWriteOnly(String beanClassName, String properties) {
		super.beanPropertiesWriteOnly(beanClassName, properties);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beansRequireDefaultConstructor() {
		super.beansRequireDefaultConstructor();
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beansRequireSerializable() {
		super.beansRequireSerializable();
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beansRequireSettersForGetters() {
		super.beansRequireSettersForGetters();
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder beanDictionary(Class<?>...values) {
		super.beanDictionary(values);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder dictionaryOn(Class<?> on, java.lang.Class<?>...values) {
		super.dictionaryOn(on, values);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder disableBeansRequireSomeProperties() {
		super.disableBeansRequireSomeProperties();
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder disableIgnoreMissingSetters() {
		super.disableIgnoreMissingSetters();
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder disableIgnoreTransientFields() {
		super.disableIgnoreTransientFields();
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder disableIgnoreUnknownNullBeanProperties() {
		super.disableIgnoreUnknownNullBeanProperties();
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder disableInterfaceProxies() {
		super.disableInterfaceProxies();
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public <T> RdfParserBuilder example(Class<T> pojoClass, T o) {
		super.example(pojoClass, o);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public <T> RdfParserBuilder example(Class<T> pojoClass, String json) {
		super.example(pojoClass, json);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder findFluentSetters() {
		super.findFluentSetters();
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder findFluentSetters(Class<?> on) {
		super.findFluentSetters(on);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder ignoreInvocationExceptionsOnGetters() {
		super.ignoreInvocationExceptionsOnGetters();
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder ignoreInvocationExceptionsOnSetters() {
		super.ignoreInvocationExceptionsOnSetters();
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder ignoreUnknownBeanProperties() {
		super.ignoreUnknownBeanProperties();
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder implClass(Class<?> interfaceClass, Class<?> implClass) {
		super.implClass(interfaceClass, implClass);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder implClasses(Map<Class<?>,Class<?>> values) {
		super.implClasses(values);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder interfaceClass(Class<?> on, Class<?> value) {
		super.interfaceClass(on, value);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder interfaces(java.lang.Class<?>...value) {
		super.interfaces(value);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder notBeanClasses(Class<?>...values) {
		super.notBeanClasses(values);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder notBeanPackages(String...values) {
		super.notBeanPackages(values);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder propertyNamer(Class<? extends org.apache.juneau.PropertyNamer> value) {
		super.propertyNamer(value);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder propertyNamer(Class<?> on, Class<? extends org.apache.juneau.PropertyNamer> value) {
		super.propertyNamer(on, value);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder sortProperties() {
		super.sortProperties();
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder sortProperties(java.lang.Class<?>...on) {
		super.sortProperties(on);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder stopClass(Class<?> on, Class<?> value) {
		super.stopClass(on, value);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder swaps(Class<?>...values) {
		super.swaps(values);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder typeName(Class<?> on, String value) {
		super.typeName(on, value);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder typePropertyName(String value) {
		super.typePropertyName(value);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder typePropertyName(Class<?> on, String value) {
		super.typePropertyName(on, value);
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder useEnumNames() {
		super.useEnumNames();
		return this;
	}

	@Override /* GENERATED - BeanContextBuilder */
	public RdfParserBuilder useJavaBeanIntrospector() {
		super.useJavaBeanIntrospector();
		return this;
	}

	@Override /* GENERATED - ParserBuilder */
	public RdfParserBuilder autoCloseStreams() {
		super.autoCloseStreams();
		return this;
	}

	@Override /* GENERATED - ParserBuilder */
	public RdfParserBuilder debugOutputLines(int value) {
		super.debugOutputLines(value);
		return this;
	}

	@Override /* GENERATED - ParserBuilder */
	public RdfParserBuilder listener(Class<? extends org.apache.juneau.parser.ParserListener> value) {
		super.listener(value);
		return this;
	}

	@Override /* GENERATED - ParserBuilder */
	public RdfParserBuilder strict() {
		super.strict();
		return this;
	}

	@Override /* GENERATED - ParserBuilder */
	public RdfParserBuilder trimStrings() {
		super.trimStrings();
		return this;
	}

	@Override /* GENERATED - ParserBuilder */
	public RdfParserBuilder unbuffered() {
		super.unbuffered();
		return this;
	}

	@Override /* GENERATED - ReaderParserBuilder */
	public RdfParserBuilder fileCharset(Charset value) {
		super.fileCharset(value);
		return this;
	}

	@Override /* GENERATED - ReaderParserBuilder */
	public RdfParserBuilder streamCharset(Charset value) {
		super.streamCharset(value);
		return this;
	}

	// </FluentSetters>
}
