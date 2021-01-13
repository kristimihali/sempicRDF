/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.rdf;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
public class Namespaces {
    
    public final static String photoNS = "http://miashs.univ-grenoble-alpes.fr/photo/";
    public final static String depictsNS = "http://miashs.univ-grenoble-alpes.fr/depicts/";
    public final static String placeNS = "http://miashs.univ-grenoble-alpes.fr/place/";

    public static String getNsUri(long id, String type) {
        String nsUri = "";
        switch(type) {
            case "photo":
                nsUri = photoNS+id;
                break;

            case "depict":
                nsUri = depictsNS+id;
                break;

            case "place":
                nsUri = placeNS+id;
                break;
        }

        return nsUri;
    }
}
