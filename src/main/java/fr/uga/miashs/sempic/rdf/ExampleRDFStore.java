/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.rdf;

import fr.uga.miashs.sempic.model.rdf.SempicOnto;
import org.apache.jena.atlas.json.JsonArray;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDFS;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
public class ExampleRDFStore {

    private static BasicSempicRDFStore RDFStore = new BasicSempicRDFStore();

    public static void main(String[] args) {

           // initialisation les donées.
           initialiseData();

           // test du recherche
           searchTest();
    }

    /**
     * Initialisation les donées d'exmples.
     */
    public static void initialiseData() {

        System.out.println("Commencer à initialiser l'exemple du donnée.");

        String[][] exemples = {
                {
                    "1",
                    "5",
                    "2",
                    "Miao",
                    "un photo d'exemple 1",
                    "Grenoble"
                },

                {
                    "2",
                    "5",
                    "2",
                    "Toto",
                    "un photo d'exemple 2",
                    "Tirana"
                },

                {
                    "3",
                    "5",
                    "2",
                    "XiaoBai",
                    "un photo d'exemple 3",
                    "Roma"
                },

                {
                    "4",
                    "5",
                    "2",
                    "XiaoHei",
                    "un photo d'exemple 4",
                    "Paris"
                }
        };

        Resource classNames[] =
                {
                  SempicOnto.Cat,
                  SempicOnto.Dog,
                  SempicOnto.Cat,
                  SempicOnto.Animal
                };

        for (int i = 0; i < exemples.length; i++) {
            Resource pRes = RDFStore.createPhoto(
                    Long.parseLong(exemples[i][0]),
                    Long.parseLong(exemples[i][1]),
                    Long.parseLong(exemples[i][2]),
                    exemples[i][4]
            );

            Model m = ModelFactory.createDefaultModel();
            Resource photo = m.createResource(Namespaces.getNsUri(Long.parseLong(exemples[i][0]), "photo"));
            photo.addLiteral(RDFS.label, exemples[i][4]);

            Resource depict = m.createResource(Namespaces.getNsUri(Long.parseLong(exemples[i][0]), "depict"), classNames[i]);
            depict.addLiteral(RDFS.label, exemples[i][3]);
            m.add(photo, SempicOnto.depicts, depict);

            Resource place = m.createResource(Namespaces.getNsUri(i, "place"), SempicOnto.Place);
            place.addLiteral(RDFS.label, exemples[i][5]);

            m.write(System.out, "turtle");

            RDFStore.saveModel(m);
        }

        // les autre donnés.
        Model m = ModelFactory.createDefaultModel();
        Resource photo = m.createResource(Namespaces.getNsUri(15, "photo"));
        Resource person = m.createResource(Namespaces.getNsUri(15, "depict"), SempicOnto.Person);
        Resource male = m.createResource(Namespaces.getNsUri(15, "depict"), SempicOnto.Male);
        m.add(photo, SempicOnto.who, person);
        m.write(System.out, "turtle");
        RDFStore.saveModel(m);

        System.out.println("Terminer à initialiser l'exemple du donnée.");
    }

    /**
     * Exemple d'utilisation du recherche
     */
    public static void searchTest() {

        System.out.println("Commencer à faire les exemple de recherches.");

        // exemple recherche des photo qui depictent des instances d'une classe donnée
        JsonArray resultsDepictent = RDFStore.searchPhotos("depictent","Cat");
        System.out.println("Exemple recherche des photo qui depictent des instances d'une classe donnée: ");
        System.out.println("PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>\n" +
                "SELECT ?s \n" +
                "WHERE {\n" +
                "  ?s a sempic:Photo .\n" +
                "  ?s sempic:depicts [ a sempic:Cat] \n" +
                "}");
        System.out.println("Résultat de recherche:");
        System.out.println(resultsDepictent);

        // exemple recherche des selfies
        JsonArray resultsSelfies = RDFStore.searchPhotos("selfies",null);
        System.out.println("Exemple recherche des selfies: ");
        System.out.println("PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>\n" +
                "SELECT * \n" +
                "WHERE{ \n" +
                "\t?photo a sempic:Photo;\n" +
                "\t         sempic:who [ sempic:owner ?auteur ] .\n" +
                "}\n");
        System.out.println("Résultat de recherche:");
        System.out.println(resultsSelfies);

        // recherche des photos qui depictent une certaine restriction de propriété
        JsonArray resultsRestriction = RDFStore.searchPhotos("restriction", null);
        System.out.println("Recherche des photos qui depictent une certaine restriction de propriété: ");
        System.out.println("\n" +
                "PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>\n" +
                "\n" +
                "SELECT ?p  ?o\n" +
                "WHERE {\n" +
                "\t?p sempic:who ?o.\n" +
                "}\n");
        System.out.println("Résultat de recherche:");
        System.out.println(resultsRestriction);

        System.out.println("Terminer les exemple de recherches.");
    }
}
