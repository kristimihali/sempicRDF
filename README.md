# RDF Modelizer

<img src = "https://i.imgur.com/pGutK5k.png"/>

# SPARQL QUERIES


## WEB SEMANTICS

 `curl -i -X POST -H "Content-Type: application/json" -d '["http://miashs.univ-grenoble-alpes.fr/depicts/1"]' http://localhost:8080/data/users/1/photos/5/depicts`


# JENA

```
PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>

INSERT DATA {<http://miashs.univ-grenoble-alpes.fr/depicts/1> a sempic:Dog .
  <http://miashs.univ-grenoble-alpes.fr/depicts/2> a sempic:Person .
} 
```

### Insert Data

```
PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>

INSERT DATA {
    <http://miashs.univ-grenoble-alpes.fr/depicts/15> a sempic:Male .

  <http://miashs.univ-grenoble-alpes.fr/photos/15> a sempic:Photo ;
    sempic:who <http://miashs.univ-grenoble-alpes.fr/depicts/15>
  .
} 
```
### Insert Data
```
prefix sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>
INSERT DATA {<http://miashs.univ-grenoble-alpes.fr/depicts/1> sempic:proprio <http://miashs.univ-grenoble-alpes.fr/depicts/2> .}


## Add places 
PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
Insert data{
  <http://mias/Users/user/Downloads/SempicRDF/src/main/resourceshs.univ-grenoble-alpes.fr/places/1> a sempic:Place .
  <http://miashs.univ-grenoble-alpes.fr/places/1> rdfs:label "Tirana" .
  
<http://miashs.univ-grenoble-alpes.fr/places/2> a sempic:Place .
 <http://miashs.univ-grenoble-alpes.fr/places/2> rdfs:label "Roma" .
}
```

## taken in
 `curl -i -X POST -H "Content-Type: application/json" -d '["http://miashs.univ-grenoble-alpes.fr/places/1"]' http://localhost:8080/data/users/1/photos/5/takenIn `

## taken by
`curl -i -X POST -H "Content-Type: application/json" -d '["http://miashs.univ-grenoble-alpes.fr/depicts/2"]' http://localhost:8080/data/users/1/photos/5/takenBy`

# SPARQL

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
  ?s sempic:depicts [ ?o ] .
  ?o rdfs:label "Manuel Atencia".
}
```

### Select all picturee of Manuel and Jerome
```
PREFIX sempic: <http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>

SELECT DISTINCT ?p  
WHERE {
	?p a sempic:Photo ;
           sempic:who sempic:Manuel ;
	   sempic:who sempic:Jerome .
	sempic:Manuel rdfs:label "Manuel Atencia".
	sempic:Jerome rdfs:label "Jerome David".
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
	{?p a sempic:Photo;
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

SELECT DISTINCT ?p  
WHERE {
	?p a sempic:Photo ;
	dc:date ?d .
	bind(strdt(?d, xsd:date) as ?date)
	Filter(year(?date) = 2017 && (month(?date) = 06 || month(?date) = 07 || month(?date) = 08 || month(?date) = 09 ))}
  ```
