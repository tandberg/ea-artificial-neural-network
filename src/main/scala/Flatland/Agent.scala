import ANN._
class ConnectString(s: String, network:NeuralNetwork){

	def connect(label:String, weight:Double) = {
		network addLink(s, label, weight)
	}

}
class Agent {

	implicit def stringToLinkMap(s: String) = new ConnectString(s, brainNetwork)

	val foodSensors = List(new Neuron((x:Double) => x>0.4, "ff"), new Neuron((x:Double) => x>0.4, "fl"), new Neuron((x:Double) => x>0.4, "fr"))
	val poisonSensors = List(new Neuron((x:Double) => x>0.4, "pf"), new Neuron((x:Double) => x>0.4, "pl"), new Neuron((x:Double) => x>0.4, "pr"))
	val hiddenNeurons = List(new Neuron((x:Double) => x> 0.4, "h1"), new Neuron((x: Double) => x>0.4, "h2"), new Neuron((x:Double) => x>0.4, "h3"))	
	val brainNetwork = new NeuralNetwork

	brainNetwork addNeurons foodSensors
	brainNetwork addNeurons poisonSensors 
	brainNetwork addNeurons hiddenNeurons

	"ff" connect ("h1", 0.4)
	"fl" connect ("h2", 0.5)
	"fr" connect ("h3", 0.4)
	
	"pf" connect ("h2", 0.2)
	"pf" connect ("h3", 0.1)

	"pl" connect ("h1", 0.7)
	"pl" connect ("h3", 0.1)

	"pr" connect ("h1", 0.2)
	"pr" connect ("h2", 0.3)
}

object Agent {

	def main(args: Array[String]) {
		val ag = new Agent
		println(ag.brainNetwork.neurons)
		println(ag.brainNetwork.adjacencyList)
	}
}
