package ANN
import scala.collection.mutable.Queue
class CTRNetwork {
	var adjacencyList = new CTRAdjacencyList(List())
	
	var groups:List[List[CTRNeuron]] = List()

	def addGroup(group:List[CTRNeuron]) = {
		groups = groups :+ group
	}

	def neurons = groups.flatten

	def clear = {
		adjacencyList = new CTRAdjacencyList(List())
	}

	def addLink(label:String, label2:String, weight:Double) = {
		val neuron = neurons.find((x) => x.label == label)
		val neuron2 = neurons.find((x) => x.label == label2)

		(neuron, neuron2) match {
			case (Some(n1), Some(n2)) => adjacencyList = adjacencyList add ((n1, n2, weight))
			case _ => throw new ClassCastException  
		}
	}
	def findLinks(neuron:CTRNeuron):List[(CTRNeuron, CTRNeuron, Double)] = {
		adjacencyList linksToCTRNeuron neuron	
	}
	def fromNeuron(link:(CTRNeuron, CTRNeuron, Double)):CTRNeuron = {
		link._1
	}
	def weight(link: (CTRNeuron, CTRNeuron, Double)): Double = {	
		link._3
	}

	def search() = {
		for (neuron <- neurons){
			for (link <- findLinks(neuron)){
				val from = fromNeuron(link) 
				neuron.∑((from output) * weight(link))
			}
		}
		val out = output1
		(out, out.indexOf(out.max))
	}
	
	def output1:List[Double] = {
		groups(3).map((x) => x.activationFunction(x.output))
	}

	def resetCTRNeurons = {
		for (neuron <- neurons){
			neuron reset
		}

	}

}
