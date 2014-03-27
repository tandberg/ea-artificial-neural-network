package ANN
class Neuron(val activationFunction: (Double) => Boolean, val label: String) {
	var sumOfWeights:Double = -0.4 

	def this(label:String) = this((x) => 0.5 <= (1/(1 + math.exp(-x))), label)

	def this(activationFunction:(Double) => Boolean) = this(activationFunction, "")
	def increaseSumOfWeights(input: Double) = {
		sumOfWeights += input
	}
	def activate() = {
	//	println("activate called in neuron " + label)
		if (activationFunction(sumOfWeights)) { 
			1
		}
		else 0
	}
	override def toString = label
}


