/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.rdf;

import fr.uga.miashs.sempic.model.rdf.SempicOnto;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.vocabulary.RDFS;

import javax.inject.Singleton;
import java.util.List;

/**
 * Classe offrant les services de base de l'appli sempic : créer une photo,
 * lire les annotations d'une photo, et effacer les annotations d'une photo
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */

// mvn generate-sources -> regenerer la classe la classe SempicOnto

@Singleton
public class BasicSempicRDFStore extends RDFStore {
    
    public final PrefixMapping prefixes;

    public BasicSempicRDFStore() {
        prefixes = PrefixMapping.Factory.create();
        prefixes.withDefaultMappings(PrefixMapping.Standard);
        prefixes.setNsPrefix("sempic", SempicOnto.NS);
    }
    
    public Resource createPhoto(long photoId, long albumId, long ownerId) {
        // create an empty RDF graph
        Model m = ModelFactory.createDefaultModel();
        // create an instance of Photo in Model m
        Resource pRes = m.createResource(Namespaces.getPhotoUri(photoId), SempicOnto.Photo);

        pRes.addLiteral(SempicOnto.albumId, albumId);
        pRes.addLiteral(SempicOnto.ownerId, ownerId);

        saveModel(m);

        return pRes;
    }

    public void deletePhoto(long photoId) {
        // create an instance of Photo in Model m
        Resource pRes = ResourceFactory.createResource(Namespaces.getPhotoUri(photoId));
        deleteResource(pRes);
    }

    /**
     * Query a Photo and retrieve all the direct properties of the photo and if
     * the property are depict, takenIn or takenBy, it also retrieves the labels
     * of the object of these properties
     *
     * @param photoId
     * @return
     */
    public Resource readPhoto(long photoId) {
        String pUri = Namespaces.getPhotoUri(photoId);
        ParameterizedSparqlString pss = new ParameterizedSparqlString(prefixes);
        pss.setBaseUri(Namespaces.photoNS);
        
        pss.setCommandText(
                "CONSTRUCT {"
                        + "?photo ?p ?o ."
                        + "?photo ?p1 ?o1 ."
                        + "?o1 rdfs:label ?o2 ."
                + "} WHERE { "
                        + "?photo ?p ?o ."
                        + "OPTIONAL {"
                        + "?photo ?p1 ?o1 ."
                        + "?o1 rdfs:label ?o2 ."
                        + "FILTER (?p1 IN (<" + SempicOnto.depicts + ">,<" + SempicOnto.takenIn + ">,<" + SempicOnto.takenBy + ">)) "
                        +"}"
                 + "}");
        pss.setIri("photo", pUri);
        
        Model m = cnx.queryConstruct(pss.asQuery());
        return m.getResource(pUri);
    }

    /**
     * Delete the triple <picture,p,o> from the triple store and also the
     * resource o if it is a blank node
     *
     * @param picture
     * @param p
     * @param o
     */
    public void deleteAnnotation(Resource picture, Property p, Resource o) {
        /*if (!picture.getURI().startsWith(Namespaces.photoNS)) {
            return;
        }*/
        cnx.begin(ReadWrite.WRITE);
        // can add a clause that check that p is a subproperty of depicts
        if (o.isAnon()) {
            cnx.update("DELETE WHERE { "
                    + "<" + picture.getURI() + "> <" + p.getURI() + "> ?o . "
                    + "?o <" + RDFS.label + "> \"" + o.getProperty(RDFS.label).getString() + "\" ."
                    + "?o ?p ?x}");
        } else {
            cnx.update("DELETE DATA { <" + picture.getURI() + "> <" + p.getURI() + "> <" + o.getURI() + "> } ");
        }
        cnx.commit();
        if (picture.getModel() != null) {
            picture.getModel().removeAll(picture, p, o);
        }
    }

    /**
     * delete all the triples with subject "picture" and property "p"
     *
     * @param picture
     * @param p
     */
    public void deleteAnnotation(Resource picture, Property p) {
        /*if (!picture.getURI().startsWith(Namespaces.photoNS)) {
            return;
        }*/
        cnx.update("DELETE  WHERE { <" + picture.getURI() + "> <" + p.getURI() + "> ?o } ");
        if (picture.getModel() != null) {
            picture.removeAll(p);
        }
    }

    /**
     * Add annotation to the picture i.e. triple <picture,p,o> is added to the
     * triple store. if o has properties, they are also added.
     *
     * @param picture
     * @param p
     * @param o
     */
    public void addAnnotation(Resource picture, Property p, Resource o) {
        if (o == null) {
            return;
        }
        /*if (!picture.getURI().startsWith(Namespaces.photoNS)) {
            return;
        }*/
        Model m = ModelFactory.createDefaultModel();
        if (o.getModel() != null) {
            m.add(o.listProperties());
        }
        m.add(picture, p, o);
        cnx.begin(ReadWrite.WRITE);
        cnx.load(m);
        cnx.commit();
        picture.getModel().add(m);
    }

    /**
     * Replace annotations (picture.getURI(), property.getUri(), x)
     *     par (picture.getURI(), property.getUri(), r.getUri()) et
     *     ajoute éventuellement les propriétés de o
     * @param picture
     * @param property
     * @param r
     */
    public void setAnnotation(Resource picture, Property property, Resource r) {
        deleteAnnotation(picture, property);
        addAnnotation(picture, property, r);
    }

    /**
     * Retrieve the instances that contain the string labelContent in one of
     * their labels
     *
     * @param type the type of instances
     * @param labelContent the content of the instance labels
     * @param instanciatedProperty if not null the instance has to be object of
     * this property for at least one triple
     * @param ownerId if not -1 the subject (photo) of the instanciatedProperty
     * has to be owned by the given user
     * @return
     */
    public List<Resource> lookupInstances(Resource type, String labelContent, Resource instanciatedProperty, long ownerId) {
        String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX text: <http://jena.apache.org/text#> "
                + "CONSTRUCT {?s rdfs:label ?l} "
                + "WHERE { ?s rdfs:label ?l.";
        if (type != null) {
            query += "?s a <" + type.getURI() + "> .";
        }
        if (instanciatedProperty != null) {
            query += "[ <" + instanciatedProperty.getURI() + "> ?s";
            if (ownerId != -1) {
                query += "; <" + SempicOnto.ownerId + "> " + ownerId;
            }
            query += "] .";
        }
        if (labelContent != null && !"".equals(labelContent)) {
            query += "FILTER regex(?l, \"" + labelContent.toLowerCase() + "\", \"i\")";//"FILTER (STRSTARTS(LCASE(?l),\""+labelContent.toLowerCase()+"\")) .";
            query += "(?s) text:query (rdfs:label \"" + labelContent.toLowerCase() + "*\") .";

        }

        query += "FILTER (ISIRI(?s)) .";
        query += "}";
        //System.out.println(query);
        Model m = cnx.queryConstruct(query);
        List<Resource> res = m.listSubjects().toList();
        return res;
    }
}
