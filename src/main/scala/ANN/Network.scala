import ANN._
class NeuralNetwork(){

	var adjacencyList = new AdjacencyList(List())
	
	var neurons:List[Neuron] = List()

	def addNeurons(neurons1:List[Neuron]) = {
		neurons = neurons ::: neurons1
	}
	
	def addNeuron(neuron:Neuron) = {
		neurons = neurons :+ neuron
	}

	def addLink(label:String, label2:String, weigth:Double) = {
		val neuron1 = neurons.find((x) => x.label == label)
		val neuron2 = neurons.find((x) => x.label == label2)
		(neuron1, neuron2) match {
			case (Some(n1), Some(n2)) => adjacencyList = adjacencyList add ((n1, n2, weigth))
			case _ => throw new ClassCastException
		}
	}
}

object NeuralNetwork {
	
	def dummyfunction (d: Double) = {
		(x: Double) => x > d
	}

	def main(args: Array[String]){ 
		
		val nn = new NeuralNetwork
		nn addNeuron(new Neuron(dummyfunction(4)))
		nn addNeuron(new Neuron(dummyfunction(5)))
		println(nn.neurons)
	}

}	
