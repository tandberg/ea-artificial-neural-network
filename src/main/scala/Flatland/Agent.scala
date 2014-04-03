package Flatland

import ANN._
import scala.util.Random
import EA._
import Helpers.FlatLandHelpers
import scala.collection.mutable.ArrayBuffer

class ConnectString(s: String, network:NeuralNetwork){
	def connect(label:String, weight:Double) = {
		network addLink(s, label, weight)
	}

}
class Agent(val genotype: Array[Int], val runType:Boolean, val mapSeed:Long) extends Genotype{

	implicit def stringToLinkMap(s: String) = new ConnectString(s, brainNetwork)

	val foodSensors = List(new Neuron("ff"), new Neuron("fl"), new Neuron("fr"))
	val poisonSensors = List(new Neuron("pf"), new Neuron("pl"), new Neuron("pr"))
	val hiddenNeurons = List(new Neuron("h1"), new Neuron("h2"), new Neuron("h3"))
	val outputNeurons = List(new Neuron("of"), new Neuron("ol"), new Neuron("or"))	
	val brainNetwork = new NeuralNetwork
	val bitstringpercision = 4
	var worlds:List[World] = _
	var fitness2:Double = _

	val poisonDist = 0.5
	val foodDist = 0.5

	brainNetwork addGroup (foodSensors ::: poisonSensors)
	brainNetwork addGroup hiddenNeurons
	brainNetwork addGroup outputNeurons
	
	def this(runType:Boolean, mapSeed:Long) = this(FlatLandHelpers.generateRandomBitString, runType, mapSeed)

	def this(o1:Genotype, o2:Genotype, crossOverRate:Double, runType:Boolean, mapSeed:Long) = this(FlatLandHelpers.cross(o1.getArray, o2.getArray, crossOverRate), runType,mapSeed)




	if(runType){
		val staticWorldBuffer:ArrayBuffer[World] = new ArrayBuffer()
		for (i <- 0 until 5){
			staticWorldBuffer += new World(i + 1)
		}
		worlds = staticWorldBuffer.toList
	}

	else {
		val randomWorldBuffer:ArrayBuffer[World] = new ArrayBuffer()
		for (i <- 0 until 5){
			randomWorldBuffer += new World(mapSeed + i, foodDist, poisonDist)
		}
		worlds = randomWorldBuffer.toList
	}

	def wireUp(bitString: Array[Int]) = {
		brainNetwork clear
		val weights = FlatLandHelpers.bitStringToWeights(bitString, bitstringpercision)

		"ff" connect ("h1", weights(0))
		"fl" connect ("h2", weights(1))
		"fr" connect ("h3", weights(2))
		
		"pf" connect ("h2", weights(3))
		"pf" connect ("h3", weights(4))

		"pl" connect ("h1", weights(5))
		"pl" connect ("h3", weights(6))

		"pr" connect ("h1", weights(7))
		"pr" connect ("h2", weights(8))

		"h1" connect ("of", weights(9))
		"h2" connect ("ol", weights(10))
	
		"h3" connect ("or", weights(11))
	}
	


	override def done(size: Int): Boolean = ???
	override def fitness(): Double = {
		val random = new Random()
		var tempFitness = 0
		for (world:World <- worlds) {
			while (!world.finished){
				val lol =  world.getEnvironment
				inputToSensorNeurons(lol)
				val out = brainNetwork.search
				var index = out._2
				var listen = out._1
				if (listen == List(-1.0, -1.0, -1.0)){		
					index = random.nextInt(3)
				}
				world.doMove(FlatLandHelpers.indexToMove(index))
				brainNetwork.resetNeurons
			}
			val scores = world.getScores
			tempFitness += (scores(0) - scores(1))
		}	
		tempFitness
	}

	override def getArray(): Array[Int] = {
		genotype
	}
	override def mutate(mutationPercent: Double): Unit = wireUp(FlatLandHelpers.mutateBitString(genotype, mutationPercent))

	override def sum(): Double = {
		//set weights from bitStringToWeights
		//simulate
		//return score?
		return fitness()
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
		val tempFitness:Double = other.fitness()
		val ownFitness:Double = fitness()
       	if (ownFitness > tempFitness) 
        	-1 
       	else if (ownFitness == tempFitness)
        	0
        else
        	1


	}
	
	def printToFile = {
		worlds(0).printToFile(0)
		worlds(1).printToFile(1)
		worlds(2).printToFile(2)
		worlds(3).printToFile(3)
		worlds(4).printToFile(4)
	}

	def weights = {
		FlatLandHelpers.bitStringToWeights(genotype, bitstringpercision)
	}

	wireUp(genotype)
}

object Agent {

	def main(args: Array[String]) {
		val ag = new Agent(true, 12030123020L)
		println(ag.fitness())
	}
}

