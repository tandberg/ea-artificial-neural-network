package Tracker
import ANN._
import EA._
import Helpers._
class ConnectString(s: String, network:CTRNetwork) {
	def connect(label:String, weight:Double) = {
		network addLink(s, label, weight)	
	}
	def connect(label: String) = {	
		network addLink(s, label, 0.5)
	}
}

class Agent(var genotype: Array[Int]) extends Genotype {
	
	val seed = 1 
	
	
	implicit def stringToLinkMap(s: String) = new ConnectString(s, brainNetwork)

	var input:List[CTRNeuron] = _ 
	var bias:List[CTRNeuron] = _
	var hidden:List[CTRNeuron] = _ 
	var output:List[CTRNeuron] = _
	var fitness2:Int = _	

	def this() = this(TrackerHelpers.generateRandomBitString)
	def this(parent1: Genotype, parent2: Genotype, crossoverRate:Double) = this(TrackerHelpers.cross(parent1.getArray, parent2.getArray, crossoverRate))
	val brainNetwork = new CTRNetwork	


	val world = new World(seed)	
 	def done(x$1: Int): Boolean = ???
	def fitness(): Double = {
		var i = 0
		while(!world.finished){
			val env = world.getEnvironment
			envToSensors(env)
			println(env.toList)
			val out = brainNetwork.search
			world.doMove(out)
			brainNetwork.resetCTRNeurons
			i += 1
		}
		fitness2 = world.getScore
		fitness2
	}
	def getArray(): Array[Int] = genotype 
	def mutate(mutationRate: Double): Unit = {
		val tempGen = genotype
		genotype = TrackerHelpers.mutateBitString(tempGen, mutationRate)
		wireUp(genotype) 
	}
	def sum(): Double = ???
	def toPhenotype(): String = ???

	def envToSensors(sensorInput: Array[Boolean]) = {	
		val sensors:List[CTRNeuron] = input
		for (i <- 0 to sensors.length - 1) {
			if (sensorInput(i)){
				sensors(i).sum = 1.0
			}else {
				sensors(i).sum = -1.0
			}
		}
	}

	def printToFile = {
		world.printToFile
	}

	def weights = {
		Array(10,2)
	}

	override def compareTo(other:Genotype):Int = {
		var tempFitness = other.fitness()
		var fitness2 = fitness()
		if (fitness2 > tempFitness)
			-1
		else if (fitness2 == tempFitness)
			0
		else 
			1
	}


	def wireUp (bitString: Array[Int]) = {
		brainNetwork clear
		val phenotype:Map[String, List[Double]] = TrackerHelpers.bitStringConverter(bitString, 8)
		val weights:List[Double] = phenotype("weights")
		val biases: List[Double] = phenotype("biases")
		val gains: List[Double] = phenotype("gains")
		val timeconstants: List[Double] = phenotype("timeconstants")
		

	 	input = List(new CTRNeuron("i1"), new CTRNeuron("i2"), new CTRNeuron("i3"), new CTRNeuron("i4"), new CTRNeuron("i5"))
		bias = List(new CTRNeuron("B",(x:Double) => 1.0))
		hidden = List(new CTRNeuron("h1", gains(0) , timeconstants(0)), new CTRNeuron("h2", gains(1), timeconstants(1)))
		output = List(new CTRNeuron("o1", gains(2), timeconstants(2)), new CTRNeuron("o2", gains(3), timeconstants(3)))

		brainNetwork addGroup input
		brainNetwork addGroup bias
		brainNetwork addGroup hidden
		brainNetwork addGroup output
		
		"i1" connect ("h1", weights(0))
		"i1" connect ("h2", weights(1))

		"i2" connect ("h1", weights(2))
		"i2" connect ("h2", weights(3))

		"i3" connect ("h1", weights(4))
		"i3" connect ("h2", weights(5))
		
		"i4" connect ("h1", weights(6))
		"i4" connect ("h2", weights(7))

		"i5" connect ("h1", weights(8))
		"i5" connect ("h2", weights(9))
		
		"h1" connect ("h1", weights(10))
		"h1" connect ("h2", weights(11))
		"h1" connect ("o1", weights(12))
		"h1" connect ("o2", weights(13))

		"h2" connect ("h1", weights(14))
		"h2" connect ("h2", weights(15))
		"h2" connect ("o1", weights(16))
		"h2" connect ("o2", weights(17))

		"o1" connect ("o1", weights(18))
		"o1" connect ("o2", weights(19))

		"o2" connect ("o1", weights(20))
		"o2" connect ("o2", weights(21))

		"B" connect ("h1", biases(0))
		"B" connect ("h2", biases(1))
		"B" connect ("o1", biases(2))
		"B" connect ("o2", biases(3))
	}
	
	wireUp(genotype)
}


object Agent {
	def main(args: Array[String]) {
		val a = new Agent()
		a.fitness()
		a.printToFile
	}
	
}
