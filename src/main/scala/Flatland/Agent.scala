import ANN._
import EA._
import javaWorld._
class ConnectString(s: String, network:NeuralNetwork){

	def connect(label:String, weight:Double) = {
		network addLink(s, label, weight)
	}

}
class Agent(genotype: List[Int]) extends Genotype{

	implicit def stringToLinkMap(s: String) = new ConnectString(s, brainNetwork)

	val foodSensors = List(new Neuron("ff"), new Neuron("fl"), new Neuron("fr"))
	val poisonSensors = List(new Neuron("pf"), new Neuron("pl"), new Neuron("pr"))
	val hiddenNeurons = List(new Neuron("h1"), new Neuron("h2"), new Neuron("h3"))
	val outputNeurons = List(new Neuron("of"), new Neuron("ol"), new Neuron("or"))	
	val brainNetwork = new NeuralNetwork
	val bitstringpercision = 4

	val map:World = new World
	
	def this() = this(List(1,2,3,4,5,6,7))

	brainNetwork addNeurons foodSensors
	brainNetwork addNeurons poisonSensors 
	brainNetwork addNeurons hiddenNeurons
	brainNetwork addNeurons outputNeurons

	"ff" connect ("h1", 0.4)
	"fl" connect ("h2", 0.5)
	"fr" connect ("h3", 0.4)
	
	"pf" connect ("h2", 0.2)
	"pf" connect ("h3", 0.1)

	"pl" connect ("h1", 0.7)
	"pl" connect ("h3", 0.1)

	"pr" connect ("h1", 0.2)
	"pr" connect ("h2", 0.3)

	"h1" connect ("of", 0.3)
	"h2" connect ("ol", 0.4)
	"h3" connect ("or", 0.8)

	def bitStringToWeights(array: Array[Int]) = {
		val split = array.grouped(bitstringpercision).toList
		var weights:List[Double] = List()
			println(split.toList)

		for(i <- 0 to split.length - 1) {
			var weight = 0.0
			for(j <- 0 to split(i).length - 1) {

				weight += (split(i)(j) << (split(i).length - j - 1))
			}
			weight /= math.pow(2, bitstringpercision)
			weights = weights :+ weight
		}

		weights
	}

	override def done(size: Int): Boolean = ???
	override def fitness(): Double = ???
	override def getArray(): Array[Int] = ???
	override def mutate(mutationPercent: Double): Unit = ???
	override def sum(): Double = ???
	override def toPhenotype(): String = ???
	override def compareTo(other: Genotype):Int = ???
}

object Agent {

	def main(args: Array[String]) {
		val ag = new Agent


		// println(ag.brainNetwork.neurons)
		// println(ag.brainNetwork.adjacencyList)
	}
}

