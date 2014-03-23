package ANN
class Neuron(val activationFunction: (Double) => Boolean, val label: String) {
	var sumOfWeights:Double = 0

	def this(label:String) = this((x) => x>0.4, label)
	def this(activationFunction:(Double) => Boolean) = this(activationFunction, "")
	def increaseSumOfWeights(input: Double) = {
		sumOfWeights += input
	}
	def activate() = {
		println("Hello")
	}
	override def toString = label
}


