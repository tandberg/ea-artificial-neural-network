package ANN
import scala.collection.mutable.Queue

class NeuralNetwork(){

	var adjacencyList = new AdjacencyList(List())
	
	var groups:List[List[Neuron]] = List()
	
	def addGroup(group:List[Neuron]) = {
		groups = groups :+ group 
	}
	
	def neurons = groups.flatten

	def clear = {
		adjacencyList = new AdjacencyList(List())
	}
	def addLink(label:String, label2:String, weight:Double) = {
		val neuron1 = neurons.find((x) => x.label == label)
		val neuron2 = neurons.find((x) => x.label == label2)
		(neuron1, neuron2) match {
			case (Some(n1), Some(n2)) => adjacencyList = adjacencyList add ((n1, n2, weight))
			case _ => throw new ClassCastException
		}
	}

	def findLinks(neuron: Neuron): List[(Neuron, Neuron, Double)] = {
		adjacencyList linksToNeuron neuron
	}
	def fromNeuron(link: (Neuron, Neuron, Double)) = {
		link._1
	}
	def weight(link: (Neuron, Neuron, Double)): Double = {	
		link._3
	}
	def output = {
		groups(2).map((x) => x.activationFunction(x.sumOfWeights))
	}
	def search():(List[Double], Int) = {
		for (neuron <- neurons){
			for (link <- findLinks(neuron)){
				val from = fromNeuron(link) 
				neuron.increaseSumOfWeights((from activate) * weight(link))
			}
		}
		(output, output.indexOf(output.max))
	}

	def resetNeurons = {
		for (neuron <- neurons) {
			neuron.sumOfWeights = -1	
		}
	}
}

object NeuralNetwork {
	

}	
