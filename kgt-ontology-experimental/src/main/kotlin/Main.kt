import org.apache.jena.graph.Graph
import org.apache.jena.graph.NodeFactory
import org.apache.jena.graph.Triple
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.iterator.ExtendedIterator
import org.apache.jena.vocabulary.RDFS

fun findFirstLevel(graph: Graph, parent: String): ExtendedIterator<Triple> {
    return graph.find(Triple.createMatch(null, null, NodeFactory.createURI(parent)))
}

fun getObjects(graph: Graph, id: String): ExtendedIterator<Triple> {
    return graph.find(Triple.createMatch(NodeFactory.createURI(id), null, null))
}

fun main(args: Array<String>) {
    // TODO: Investigate different approach using "createOntologyModel" since it seems that everything is generated a lot better
    val model = ModelFactory.createDefaultModel()
    model.read(
        "https://raw.githubusercontent.com/foodkg/foodkg.github.io/master/ontologies/WhatToMake_FoodOn.rdf",
        "RDF/XML"
    )
    val graph = model.graph
    val parents = graph.find(Triple.createMatch(null, RDFS.isDefinedBy.asNode(), null))

    val sets = mutableSetOf<String>()
    val map = mutableMapOf<String, Int>()
    while (parents.hasNext()) {
        val uri = parents.next().subject.uri
        sets.add(uri)
        map[uri] = findFirstLevel(graph, uri).toList().size
    }

    val items = findFirstLevel(graph, "http://purl.obolibrary.org/obo/FOODON_03400648")
    while (items.hasNext()) {
        val id = items.next().subject.uri
        val props = getObjects(graph, id)
        println("ID: $id")
        while(props.hasNext()) {
            val obj = props.next()
            if (obj.`object`.isLiteral)
                println("\t${obj.predicate.localName}: ${obj.`object`.literalValue}")
            else if (obj.`object`.isURI)
                println("\t${obj.predicate.localName}: ${obj.`object`.uri}")
        }
        println()
    }
    println(map)
}