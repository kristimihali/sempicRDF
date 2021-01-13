## RDF Modelizer

<img src = "https://i.imgur.com/pGutK5k.png"/>

## Fichier d'ontologie
Vous pouvez trouver par ce lien [sempic.ttl](https://github.com/kristimihali/sempicRDF/blob/master/src/main/resources/sempic.ttl) 

## Initialisation des donnés && SPARQL QUERIES

D'abord, il faut utiliser la commande `git clone git@github.com:kristimihali/sempicRDF.git` pour récupérer notre application.

En suit, vous téléchargez [Apache Jena Fuseki](https://jena.apache.org/download/) et lancez le service Fuseki.

Vous allez [http://localhost:3030/manage.html](http://localhost:3030/manage.html) dans votre navigateur et ajoutez un nouveau datasets `sempic`.

Lorsque tout sont prêt, et alors, vous juste tapez la commande suivante: 

```
mvn compile && mvn package && mvn install && mvn exec:java -Dexec.mainClass="fr.uga.miashs.sempic.rdf.ExampleRDFStore"
```

Normalement, il va produit nombre de données avec les classs `Cat`、`Dog`、`Animal`、`Person`、`Place` etc.

Il va aussi faire les requêtes de recherche et afficher les résultats dans le terminale comme ci-dessous.
```
Commencer à initialiser l'exemple du donnée.
<http://miashs.univ-grenoble-alpes.fr/place/0>
        a       <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Place> ;
        <http://www.w3.org/2000/01/rdf-schema#label>
                "Grenoble" .

<http://miashs.univ-grenoble-alpes.fr/depicts/1>
        a       <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Cat> ;
        <http://www.w3.org/2000/01/rdf-schema#label>
                "Miao" .

<http://miashs.univ-grenoble-alpes.fr/photo/1>
        <http://www.w3.org/2000/01/rdf-schema#label>
                "un photo d'exemple 1" ;
        <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#depicts>
                <http://miashs.univ-grenoble-alpes.fr/depicts/1> .
<http://miashs.univ-grenoble-alpes.fr/place/1>
        a       <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Place> ;
        <http://www.w3.org/2000/01/rdf-schema#label>
                "Tirana" .

<http://miashs.univ-grenoble-alpes.fr/depicts/2>
        a       <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Dog> ;
        <http://www.w3.org/2000/01/rdf-schema#label>
                "Toto" .

<http://miashs.univ-grenoble-alpes.fr/photo/2>
        <http://www.w3.org/2000/01/rdf-schema#label>
                "un photo d'exemple 2" ;
        <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#depicts>
                <http://miashs.univ-grenoble-alpes.fr/depicts/2> .
<http://miashs.univ-grenoble-alpes.fr/photo/3>
        <http://www.w3.org/2000/01/rdf-schema#label>
                "un photo d'exemple 3" ;
        <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#depicts>
                <http://miashs.univ-grenoble-alpes.fr/depicts/3> .

<http://miashs.univ-grenoble-alpes.fr/place/2>
        a       <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Place> ;
        <http://www.w3.org/2000/01/rdf-schema#label>
                "Roma" .

<http://miashs.univ-grenoble-alpes.fr/depicts/3>
        a       <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Cat> ;
        <http://www.w3.org/2000/01/rdf-schema#label>
                "XiaoBai" .
<http://miashs.univ-grenoble-alpes.fr/place/3>
        a       <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Place> ;
        <http://www.w3.org/2000/01/rdf-schema#label>
                "Paris" .

<http://miashs.univ-grenoble-alpes.fr/depicts/4>
        a       <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Animal> ;
        <http://www.w3.org/2000/01/rdf-schema#label>
                "XiaoHei" .

<http://miashs.univ-grenoble-alpes.fr/photo/4>
        <http://www.w3.org/2000/01/rdf-schema#label>
                "un photo d'exemple 4" ;
        <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#depicts>
                <http://miashs.univ-grenoble-alpes.fr/depicts/4> .
<http://miashs.univ-grenoble-alpes.fr/photo/15>
        <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#who>
                <http://miashs.univ-grenoble-alpes.fr/depicts/15> .

<http://miashs.univ-grenoble-alpes.fr/depicts/15>
        a       <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Male> , <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Person> .
Terminer à initialiser l'exemple du donnée.
Commencer à faire les exemple de recherches.
Exemple recherche des photo qui depictent des instances d'une classe donnée:
PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>
SELECT ?s
WHERE {
  ?s a sempic:Photo .
  ?s sempic:depicts [ a sempic:Cat]
}
Résultat de recherche:
[ "http://miashs.univ-grenoble-alpes.fr/photo/1" ,
  "http://miashs.univ-grenoble-alpes.fr/photo/3"
]
Jan 13, 2021 9:10:33 PM fr.uga.miashs.sempic.rdf.BasicSempicRDFStore searchPhotos
WARNING: closed
Exemple recherche des selfies:
PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>
SELECT *
WHERE{
	?photo a sempic:Photo;
	         sempic:who [ sempic:owner ?auteur ] .
}

Résultat de recherche:
[ ]
Jan 13, 2021 9:10:33 PM fr.uga.miashs.sempic.rdf.BasicSempicRDFStore searchPhotos
WARNING: closed
Recherche des photos qui depictent une certaine restriction de propriété:

PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>

SELECT ?p  ?o
WHERE {
	?p sempic:who ?o.
}

Résultat de recherche:
[ ]
Terminer les exemple de recherches.
```

## API - Web Sémentique

Sur cette partie, il faut d'abord lancer le service [API du JavaEE](https://gricad-gitlab.univ-grenoble-alpes.fr/davidjer/sempic-zhang-kristi/-/tree/master).

Si vous utilisez `Postman`, vous pouvez import cette collection [Postman Collection](https://github.com/kristimihali/sempicRDF/blob/master/postman_collection.json)
 à fin de simplifier votre opération. 

Vous pouvez aussi manipuler les requêtes par le terminale.

### Ajoutation un depicts
 `curl -i -X POST -H "Content-Type: application/json" -d '["http://miashs.univ-grenoble-alpes.fr/depicts/1"]' http://localhost:8080/data/photos/15/depicts`
 
### Ajoutation un takenIn
 `curl -i -X POST -H "Content-Type: application/json" -d '["http://miashs.univ-grenoble-alpes.fr/places/1"]' http://localhost:8080/data/photos/15/takenIn`

### Ajoutation un takenBy
 `curl -i -X POST -H "Content-Type: application/json" -d '["http://miashs.univ-grenoble-alpes.fr/depicts/2"]' http://localhost:8080/data/photos/2/takenBy`
 
### Recherche un photo par class donnée
 `curl --location --request GET 'http://localhost:8080/data/photos/search?type=depictent&className=Cat'`
 
### Recherche des photos selfies 
 `curl --location --request GET 'http://localhost:8080/data/photos/search?type=selfies'`
 
### Recherche des photos qui depictent une certaine restriction de propriété 
 `curl --location --request GET 'http://localhost:8080/data/photos/search?type=restriction'`

## SPARQL d'exemple

### Select picture with a Male

```
PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>

SELECT DISTINCT ?p  
WHERE {
	?p a sempic:Photo ;
   	   sempic:who ?who .
  	?who a sempic:Male .
}
```

### Select picture of Manuel

```
PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>

SELECT DISTINCT ?p  
WHERE {
	?p a sempic:Photo ;
  		sempic:who sempic:Manuel.
}
```

// alternative solution
```
PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
SELECT ?s
WHERE {
  ?s a sempic:Photo.
  ?s sempic:depicts [ a ?o ] .
  ?o rdfs:label "Manuel Atencia".
}
```

### Select all picturee of Manuel and Jerome
```
PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>
PREFIX  rdfs: <http://www.w3.org/2000/01/rdf-schema#>

SELECT DISTINCT ?p  
WHERE {
     ?p a sempic:Photo ;
     sempic:who ?manuel ;
     sempic:who ?jerome .
     ?manuel rdfs:label "Manuel Atencia".
     ?jerome rdfs:label "Jerome David".
}
```
Alternative solution 
```
SELECT DISTINCT ?p  
WHERE {
	?p a sempic:Photo ;
           sempic:who ?manuel ;
	   sempic:who ?jerome .
          ?manuel rdfs:label "Manuel Atencia".
	?jerome rdfs:label "Jerome David".
}
```
### Select all pictures about House warming parties
```
PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>

SELECT DISTINCT ?p WHERE {
	{
    ?p a sempic:Photo;
		sempic:what ?what. 
		?what a sempic:HouseWarming. 
	} UNION {
		?p a sempic:Photo ;
		sempic:what sempic:HouseWarming .
	}
}
```

### All housewarming parties depicting Manuel Atencia
```
PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>
PREFIX  rdfs: <http://www.w3.org/2000/01/rdf-schema#>

SELECT DISTINCT ?p  
WHERE {
	?p a sempic:Photo ;
  	sempic:who sempic:Manuel;
		sempic:what sempic:HouseWarming .
	  sempic:Manuel rdfs:label "Manuel Atencia".
}
```
Alternative solution

```
PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>

SELECT DISTINCT ?p  
WHERE {
	?p a sempic:Photo ;
  		sempic:who ?manuel;
		sempic:what sempic:HouseWarming .
	?manuel rdfs:label "Manuel Atencia".
}
```

### Select picture with a Person

```
PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>
PREFIX foaf: <http://xmlns.com/foaf/0.1/> 
SELECT DISTINCT ?p  
WHERE {
	?p a	sempic:Photo ;
		sempic:who ?who .
	?who a  sempic:Person .
}
```

### Select all pictures without a person
```
PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>
SELECT DISTINCT ?p  
WHERE {
  ?who a sempic:Person .
  	OPTIONAL {
		?p sempic:who ?who .
		FILTER (?who != sempic:Person)
	} 
}
```

### Select all pictures in Grenoble
```
PREFIX db-owl: <http://dbpedia.org/ontology/>
PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>
PREFIX dbr: <http://dbpedia.org/resource/>

SELECT DISTINCT ?p
WHERE {
  ?p a sempic:Photo ;
     sempic:where ?city .
  BIND(IRI(CONCAT('http://dbpedia.org/resource/',?city)) AS ?cityResource)
  SERVICE <http://dbpedia.org/sparql>
  {
    ?cityResource db-owl:region dbr:Grenoble .
  }
}
```
### User's selfies

```
PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>
Select * where 
{
	?picture a sempic:Photo;
		sempic:who [ sempic:owner ?owner ] .
}
```

### Select all pictures of 2017 (between june and july)
```
PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>
PREFIX  dc:   <http://purl.org/dc/elements/1.1/>
PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#>

SELECT DISTINCT ?p  
WHERE {
	?p a sempic:Photo ;
	dc:date ?d .
	bind(strdt(?d, xsd:date) as ?date)
	Filter(year(?date) = 2017 && (month(?date) = 06 || month(?date) = 07 || month(?date) = 08 || month(?date) = 09 ))}
  ```
