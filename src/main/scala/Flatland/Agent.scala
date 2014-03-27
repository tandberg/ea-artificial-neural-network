package Flatland

import ANN._
import EA._
import Helpers.FlatLandHelpers
class ConnectString(s: String, network:NeuralNetwork){
	def connect(label:String, weight:Double) = {
		network addLink(s, label, weight)
	}

}
class Agent(val genotype: Array[Int]) extends Genotype{

	implicit def stringToLinkMap(s: String) = new ConnectString(s, brainNetwork)

	val foodSensors = List(new Neuron("ff"), new Neuron("fl"), new Neuron("fr"))
	val poisonSensors = List(new Neuron("pf"), new Neuron("pl"), new Neuron("pr"))
	val hiddenNeurons = List(new Neuron("h1"), new Neuron("h2"), new Neuron("h3"))
	val outputNeurons = List(new Neuron("of"), new Neuron("ol"), new Neuron("or"))	
	val brainNetwork = new NeuralNetwork
	val bitstringpercision = 4
	val world:World = new World

	brainNetwork addGroup (foodSensors ::: poisonSensors)
	brainNetwork addGroup hiddenNeurons
	brainNetwork addGroup outputNeurons
	
	def this() = this(FlatLandHelpers.generateRandomBitString)

	def wireUp(bitString: Array[Int]) = {
		brainNetwork clear
		val weights = FlatLandHelpers.bitStringToWeights(bitString, bitstringpercision)

		"ff" connect ("h1", 1.0)
		"fl" connect ("h2", 1.0)
		"fr" connect ("h3", 1.0)
		
		"pf" connect ("h2", 0.5)
		"pf" connect ("h3", 0.5)

		"pl" connect ("h1", 0.5)
		"pl" connect ("h3", 0.5)

		"pr" connect ("h1", 0.5)
		"pr" connect ("h2", 0.5)

		"h1" connect ("of", 1.0)
		"h2" connect ("ol", 1.0)
	
		"h3" connect ("or", 1.0)
	}
	
	def this(o1: Agent, o2: Agent) =  {
		this(FlatLandHelpers.cross(o1.genotype, o2.genotype, 1.0))
	}

	override def done(size: Int): Boolean = ???
	override def fitness(): Double = {
		val lol =  world.getEnvironment
		inputToSensorNeurons(lol)
		println(lol.toList)
		world.doMove(FlatLandHelpers.indexToMove(brainNetwork.search))
		world.printToFile
		1.0
	}
	override def getArray(): Array[Int] = ???
	override def mutate(mutationPercent: Double): Unit = wireUp(FlatLandHelpers.mutateBitString(genotype, mutationPercent))

	override def sum(): Double = {
		//set weights from bitStringToWeights
		//simulate
		//return score?
		0.0
	}

	def inputToSensorNeurons(input: Array[Int]) = {
		val sensors:List[Neuron] = foodSensors ::: poisonSensors
		for (i <- 0 to sensors.length - 1) {
			if (input(i) == 1){
				sensors(i).sumOfWeights = 1
			}
		}

	}

	
	override def toPhenotype(): String = {
		//simulate
		"Phoenotype of Agent"
	}

	override def compareTo(other: Genotype):Int = {
        if (fitness() > other.fitness()) -1 else 1

	}

	wireUp(genotype)
}

object Agent {

	def main(args: Array[String]) {
		val ag = new Agent
		val ag2 = new Agent
		val ag3 = new Agent(ag, ag2)
		ag3.fitness
	}
}

